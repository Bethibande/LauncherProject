package de.bethibande.launcher.networking.connector;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.networking.RestAction;
import de.bethibande.launcher.networking.encryption.PublicKey;
import de.bethibande.launcher.networking.encryption.RSA;
import de.bethibande.launcher.networking.events.ConnectorConnectedEvent;
import de.bethibande.launcher.networking.server.SimpleNetworkServer;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.TimeUtils;
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

// TODO: server and connector -> content-type
// TODO: server and connector -> redirect
public class SimpleServerConnector extends Thread implements ISimpleServerConnector {

    @Getter
    private final String hostIP;
    @Getter
    private final int hostPort;

    private final int buffer_size;

    private final int timeout;

    @Getter
    private int statusCode = 0;
    @Getter
    private String receivedHeader = null;

    private Socket socket;

    @Getter
    // TODO: implement encryption later
    private boolean encryptionEnabled = false;

    // rsa instance used for encryption
    private RSA rsa = null;
    // public key sent by the server
    private PublicKey server_public_key = null;

    private RestAction action;

    private final ByteBuffer pingBuffer = ByteBuffer.allocate(Long.BYTES+1);

    private final boolean initEncryption;

    public SimpleServerConnector(String host, int port, int buffer_size, int timeout, boolean initializeEncryption) {
        this.hostIP = host;
        this.hostPort = port;
        this.buffer_size = buffer_size;
        this.timeout = timeout;
        this.initEncryption = initializeEncryption;
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.hostIP, this.hostPort);
            this.socket.setSoTimeout(this.timeout*2);
            InputStream in = this.socket.getInputStream();
            OutputStream out = this.socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out);

            byte[] buffer = new byte[this.buffer_size];
            if(this.socket.isConnected()) {
                writer.println("Connect " + this.hostIP + ":" + this.hostPort + " HTTP/1.1");
                writer.println("Connection: keep-alive");
                writer.println("Date: " + new Date());
                if(this.initEncryption) {
                    this.rsa = new RSA(SimpleNetworkServer.rsa_key_size);
                    writer.println("Public-Key-e: " + this.rsa.getPublicKey().getE());
                    writer.println("Public-Key-N: " + this.rsa.getPublicKey().getN());
                }
                writer.println();
                writer.flush();

                int read = in.read(buffer);
                if(read <= 0) {
                    if(this.socket != null && this.socket.isConnected()) this.socket.close();
                    Core.loggerInstance.logError("An error occurred while executing connector/client task, the received header has a total size of 0 bytes (or less).");
                    return;
                }
                this.receivedHeader = new String(buffer, StandardCharsets.UTF_8);
                String[] incomingHeader = this.receivedHeader.split("\\\n");
                String protocol = incomingHeader[0].split(" ")[0];
                this.statusCode = new Integer(incomingHeader[0].split(" ")[1]);

                switch (this.statusCode) {
                    case 200: // OK

                        BigInteger e = null;
                        BigInteger N = null;
                        for(String s : incomingHeader) {
                            if(s.startsWith("Encryption: rsa")) {
                                this.encryptionEnabled = true;
                            }
                            if(s.startsWith("Public-Key-e: ")) {
                                e = new BigInteger(s.substring(14, s.length()-1));
                            }
                            if(s.startsWith("Public-Key-N: ")) {
                                N = new BigInteger(s.substring(14, s.length()-1));
                            }
                        }

                        if(encryptionEnabled && e != null && N != null) {
                            //this.rsa = new RSA(SimpleNetworkServer.rsa_key_size);
                            this.server_public_key = new PublicKey(e, N);

                            /*byte[] e_buf = e.toString().getBytes();
                            byte[] N_buf = N.toString().getBytes();

                            ByteBuffer key_packet = ByteBuffer.allocate((e_buf.length + N_buf.length + 1 + 2));
                            key_packet.put(SimpleNetworkServer.rsa_public_key_byte);
                            key_packet.putShort((short)e_buf.length);
                            key_packet.put(e_buf);
                            key_packet.put(N_buf);
                            sendBufferToServerUnEncrypted(key_packet.array());*/
                        }

                        Core.eventManager.runEvent(new ConnectorConnectedEvent(this));

                        synchronized (this) {
                            this.notify();
                        }

                        buffer = new byte[this.buffer_size];
                        while(this.socket.isConnected() && !this.socket.isClosed() && (read = in.read(buffer)) >= 0) {
                            if(this.encryptionEnabled) {
                                buffer = ArrayUtils.trim(buffer, read);
                                buffer = this.rsa.decrypt(buffer);
                            }
                            if(buffer[0] == SimpleNetworkServer.ping_packet_byte) {
                                pingBuffer.clear();
                                pingBuffer.put(SimpleNetworkServer.ping_packet_byte);
                                pingBuffer.putLong(TimeUtils.getTimeInMillis());
                                sendBufferToServer(pingBuffer.array());
                                //System.out.println("ping");
                            } else this.action.run(buffer);
                            buffer = new byte[this.buffer_size];
                        }
                        this.socket.close();
                        break;
                    case 405: // Internal Server Error
                    case 400: // Bad Request
                    case 500: // Method Not Allowed

                        if(this.socket != null && this.socket.isConnected()) this.socket.close();
                        Core.loggerInstance.logError("An error occurred while executing connector/client task, status code: " + incomingHeader[0].substring(protocol.length()));

                        break;
                }

            } else Core.loggerInstance.logError("An error occurred while executing connector/client task.");
        } catch(IOException e) {
            //Core.loggerInstance.logError("An error occurred while executing connector/client task.");
            //e.printStackTrace();
        }
    }

    private void sendBufferToServerUnEncrypted(byte[] buffer) {
        try {
            OutputStream out = this.socket.getOutputStream();
            out.write(buffer, 0, buffer.length);
            out.flush();
        } catch(IOException e) {
            Core.loggerInstance.logError("An error occurred while writing buffer to server.");
            e.printStackTrace();
        }
    }

    @Override
    public void sendBufferToServer(byte[] buffer) {
        try {
            OutputStream out = this.socket.getOutputStream();
            if(encryptionEnabled) {
                byte[] encrypted = this.rsa.encrypt(buffer, this.server_public_key);
                out.write(encrypted, 0, encrypted.length);
            } else out.write(buffer, 0, buffer.length);
            out.flush();
        } catch(IOException e) {
            Core.loggerInstance.logError("An error occurred while writing buffer to server.");
            e.printStackTrace();
        }
    }

    @Override
    public ISimpleServerConnector payloadReceived(RestAction rest) {
        this.action = rest;
        return this;
    }

    @Override
    public void disconnect() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    @Override
    public boolean isConnectionEstablished() {
        return this.receivedHeader != null && this.statusCode != 0;
    }
}