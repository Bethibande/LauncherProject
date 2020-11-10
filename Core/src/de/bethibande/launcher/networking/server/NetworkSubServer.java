package de.bethibande.launcher.networking.server;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.networking.RestAction;
import de.bethibande.launcher.networking.encryption.PublicKey;
import de.bethibande.launcher.networking.encryption.RSA;
import de.bethibande.launcher.networking.events.SubServerConnectedEvent;
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

    // rsa instance used for encryption
    private RSA rsa = null;
    // public key received from client/connector
    private PublicKey client_key = null;

    public NetworkSubServer(Socket client, int buffer_size, boolean useEncryption, SimpleNetworkServer parent) {
        this.client = client;
        this.buffer_size = buffer_size;
        this.useEncryption = useEncryption;
        this.parent = parent;
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

                            }/* else if(buffer[0] == SimpleNetworkServer.rsa_public_key_byte) {
                                ByteBuffer buf = ByteBuffer.allocate(buffer.length);
                                buf.put(buffer);
                                buf.flip();
                                int e_length = buf.getShort(1);

                                BigInteger e;
                                BigInteger N;
                                byte[] e_buf = new byte[e_length+3];
                                byte[] N_buf = new byte[buffer.length-3-e_length];
                                buf.get(e_buf, 0, e_length+3);
                                buf.get(N_buf, 0, N_buf.length-3);
                                String e_str = new String(e_buf).substring(3).trim();
                                String N_str = new String(N_buf).trim();
                                e = new BigInteger(e_str);
                                N = new BigInteger(N_str);
                                this.client_key = new PublicKey(e, N);
                            }*/ else this.action.run(buffer);

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
        this.parent.subServerClosed(this);
    }

    private void sendStatusHeader(OutputStream out, int code) {
        PrintWriter writer = new PrintWriter(out);
        switch(code) {
            case 400:
                writer.println("HTTP/1.1 400 Bad Request");
                writer.println("Connection: close");
                break;
            case 405:
                writer.println("HTTP/1.1 405 Method Not Allowed");
                writer.println("Connection: close");
                break;
            case 500:
                writer.println("HTTP/1.1 500 Internal Server Error");
                writer.println("Connection: close");
                break;
            case 200:
                writer.println("HTTP/1.1 200 OK");
                writer.println("Connection: keep-alive");
                if(useEncryption) {
                    writer.println("Encryption: rsa");
                    writer.println("Public-Key-e: " + this.rsa.getPublicKey().getE());
                    writer.println("Public-Key-N: " + this.rsa.getPublicKey().getN());
                }
        }
        writer.println("Date: " + new Date());
        writer.println("Content-Length: 0");
        writer.flush();
    }

    public NetworkSubServer action(RestAction action) {
        this.action = action;
        return this;
    }

    public void closeConnection() {
        try {
            this.client.close();
            this.parent.subServerClosed(this);
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
        } catch(IOException e) {
            Core.loggerInstance.logError("An error occurred while writing buffer to client/connector");
            e.printStackTrace();
        }
    }

}
