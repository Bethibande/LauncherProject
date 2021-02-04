package de.bethibande.launcher.networking.server;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.networking.RestAction;
import de.bethibande.launcher.networking.encryption.PublicKey;
import de.bethibande.launcher.networking.encryption.RSA;
import de.bethibande.launcher.networking.events.SubServerConnectedEvent;
import de.bethibande.launcher.networking.logging.IServerLogSession;
import de.bethibande.launcher.networking.logging.NetworkPackageSource;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.DataSerializer;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class NetworkSubServer extends Thread {

    @Getter
    private final Socket client;
    @Getter
    private final int buffer_size;
    @Getter
    private final boolean useEncryption;
    @Getter
    private String[] header;
    @Getter
    private final SimpleNetworkServer parent;

    private RestAction action;
    @Getter
    private final IServerLogSession loggerSession;

    // rsa instance used for encryption
    private RSA rsa = null;
    // public key received from client/connector
    private PublicKey client_key = null;

    public NetworkSubServer(Socket client, int buffer_size, boolean useEncryption, SimpleNetworkServer parent, IServerLogSession loggerSession) {
        this.client = client;
        this.buffer_size = buffer_size;
        this.useEncryption = useEncryption;
        this.parent = parent;
        this.loggerSession = loggerSession;
        if(useEncryption) {
            this.rsa = new RSA(SimpleNetworkServer.rsa_key_size);
        }
    }

    @Override
    public final void run() {
        try {
            try {
                byte[] buffer = new byte[this.buffer_size];
                InputStream in = this.client.getInputStream();
                OutputStream out = this.client.getOutputStream();

                int read = in.read(buffer);
                if (read <= 0) {
                    sendStatusHeader(out, 400);
                    this.client.close();
                    return;
                }

                String[] header = new String(buffer, StandardCharsets.UTF_8).split("\\\n");
                if(this.loggerSession != null) this.loggerSession.log(NetworkPackageSource.IN, buffer);
                String request = header[0];

                if (request.toLowerCase().startsWith("connect")) {
                    String protocol = request.split(" ")[2];
                    if (protocol.startsWith("HTTP/1.1")) {
                        this.header = header;

                        BigInteger e = null, N = null;
                        for(String headerLine : header) {
                            if(headerLine.startsWith("Public-Key-e: ")) {
                                e = new BigInteger(headerLine.substring(14, headerLine.length()-1));
                            }
                            if(headerLine.startsWith("Public-Key-N: ")) {
                                N = new BigInteger(headerLine.substring(14, headerLine.length()-1));
                            }
                        }

                        if(this.useEncryption && e != null) {
                            this.client_key = new PublicKey(e, N);
                        }

                        sendStatusHeader(out, 200);
                        this.client.setSoTimeout(this.parent.getPingTimeOut()*2);

                        //Core.loggerInstance.logMessage("Client connected!");
                        Core.eventManager.runEvent(new SubServerConnectedEvent(this));

                        buffer = new byte[this.buffer_size];
                        while(this.client.isConnected() && (read = in.read(buffer)) > 0) {

                            if(this.client_key != null && this.useEncryption) {
                                buffer = ArrayUtils.trim(buffer, read);
                                buffer = this.rsa.decrypt(buffer);
                            }

                            // check if the incoming buffer is a ping and if not run the rest action
                            if(buffer[0] == SimpleNetworkServer.ping_packet_byte) {
                                parent.getPinged().remove(this);
                                if(this.loggerSession != null) this.loggerSession.log(NetworkPackageSource.IN, buffer);

                            } else {
                                if(this.loggerSession != null) this.loggerSession.log(NetworkPackageSource.IN, buffer);
                                this.action.run(buffer);
                            }

                            // clear the buffer
                            buffer = new byte[this.buffer_size];
                        }
                    } else {
                        sendStatusHeader(out, 400);
                        this.client.close();
                    }
                } else {
                    sendStatusHeader(out, 405);
                    this.client.close();
                }
            } catch(IndexOutOfBoundsException e) {
                sendStatusHeader(client.getOutputStream(), 500);
            }

        } catch(IOException e) {
            e.printStackTrace();
            Core.loggerInstance.logError("Error while executing server task.");
            if(!this.client.isClosed() && this.client.isConnected()) {
                try {
                    sendStatusHeader(this.client.getOutputStream(), 500);
                    this.client.close();
                } catch(IOException ex) {
                    Core.loggerInstance.logError("Error while trying to close connection.");
                }
            }
        }
        if(this.loggerSession != null) this.loggerSession.endSession();
        this.parent.subServerClosed(this);
    }

    private void sendStatusHeader(OutputStream out, int code) {
        PrintWriter writer = new PrintWriter(out);
        StringBuilder sb = new StringBuilder();
        switch(code) {
            case 400:
                sb.append("HTTP/1.1 400 Bad Request\n");
                sb.append("Connection: close\n");
                break;
            case 405:
                sb.append("HTTP/1.1 405 Method Not Allowed\n");
                sb.append("Connection: close\n");
                break;
            case 500:
                sb.append("HTTP/1.1 500 Internal Server Error\n");
                sb.append("Connection: close\n");
                break;
            case 200:
                sb.append("HTTP/1.1 200 OK\n");
                sb.append("Connection: keep-alive\n");
                if(useEncryption) {
                    sb.append("Encryption: rsa\n");
                    sb.append("Public-Key-e: ").append(this.rsa.getPublicKey().getE()).append("\n");
                    sb.append("Public-Key-N: ").append(this.rsa.getPublicKey().getN()).append("\n");
                }
        }
        sb.append("Date: ").append(new Date()).append("\n");
        sb.append("Content-Length: 0\n");
        writer.println(sb.toString());
        writer.flush();
        if(this.loggerSession != null) this.loggerSession.log(NetworkPackageSource.OUT, sb.toString().getBytes());
    }

    public NetworkSubServer action(RestAction action) {
        this.action = action;
        return this;
    }

    public void closeConnection() {
        try {
            this.client.close();
            this.parent.subServerClosed(this);
            if(this.loggerSession != null) this.loggerSession.endSession();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return (!this.client.isClosed() && this.client.isConnected());
    }

    public void sendBufferToClient(byte[] buffer) {
        try {
            OutputStream out = this.client.getOutputStream();
            if(this.useEncryption) {
                byte[] encrypted = this.rsa.encrypt(buffer, this.client_key);
                out.write(encrypted, 0, encrypted.length);
            } else out.write(buffer, 0, buffer.length);
            out.flush();
            if(this.loggerSession != null) this.loggerSession.log(NetworkPackageSource.OUT, buffer);
        } catch(IOException e) {
            Core.loggerInstance.logError("An error occurred while writing buffer to client/connector");
            e.printStackTrace();
        }
    }

}
