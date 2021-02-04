package de.bethibande.launcher.networking.server;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.networking.events.SubServerBufferReceivedEvent;
import de.bethibande.launcher.networking.logging.IServerLogSession;
import de.bethibande.launcher.networking.logging.IServerLogger;
import de.bethibande.launcher.utils.TimeUtils;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SimpleNetworkServer extends NetworkServer implements INetworkServer {

    public static final byte ping_packet_byte = (byte)0x11;

    public static final byte rsa_public_key_byte = (byte)0x21;

    public static final int rsa_key_size = 1024;

    @Getter
    private final List<NetworkSubServer> subServers = new ArrayList<>();
    @Getter
    private final List<NetworkSubServer> pinged = new ArrayList<>();
    @Getter
    private final int buffer_size;
    @Getter
    private PingThread pt;
    @Getter
    private IServerLogger logger;

    @Getter
    private int pingTimeOut = 5000;

    public SimpleNetworkServer(int port, final int buffer_size, boolean useEncryption, int pingTimeOut) {
        super(port, useEncryption);
        this.buffer_size = buffer_size;
        if(pingTimeOut > 0) {
            this.pingTimeOut = pingTimeOut;
            this.pt = new PingThread(this);
            this.pt.start();
        }
    }

    public void setPingTimeOut(int timeOut) {
        try {
            this.pingTimeOut = timeOut;
            for (NetworkSubServer sub : subServers) {
                if(sub.getClient() != null && sub.getClient().isConnected()) sub.getClient().setSoTimeout(timeOut*2);
            }
        } catch(SocketException e) {
            Core.loggerInstance.logError("An error occurred while changing the time out for all running sockets");
            e.printStackTrace();
        }
    }

    public void subServerClosed(NetworkSubServer subServer) {
        this.subServers.remove(subServer);
        if(this.logger != null) this.logger.endSession(subServer.getLoggerSession());
    }

    @Override
    public void handleConnection(Socket s) {
        IServerLogSession sls = this.logger == null ? null: this.logger.createSession(s.getInetAddress().getHostAddress(), s.getPort());
        NetworkSubServer subServer = new NetworkSubServer(s, this.buffer_size, this.useEncryption(), this, sls);
        subServer.action(buffer -> { Core.eventManager.runEvent(new SubServerBufferReceivedEvent(buffer, subServer)); });
        subServer.start();
        this.subServers.add(subServer);
    }

    public static void sendBufferToClient(byte[] buffer, NetworkSubServer client) {
        client.sendBufferToClient(buffer);
    }

    @Override
    public INetworkServer log(IServerLogger logger) {
        this.logger = logger;
        this.logger.setServer(this);
        return this;
    }

    public static class PingThread extends Thread {

        private final SimpleNetworkServer server;

        public PingThread(SimpleNetworkServer server) {
            this.server = server;
        }

        @Override
        public void run() {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES+1);
                while(true) {

                    for(NetworkSubServer p : this.server.getPinged()) {
                        if(p.getClient() != null && p.getClient().isConnected()) {
                            p.getClient().close();
                        }
                        this.server.getSubServers().remove(p);
                    }
                    this.server.pinged.clear();

                    buffer.clear();
                    buffer.put(ping_packet_byte);
                    buffer.putLong(TimeUtils.getTimeInMillis());
                    byte[] packet = buffer.array();
                    for(NetworkSubServer sub : this.server.getSubServers()) {
                        if(sub.getClient() != null) {
                            Socket client = sub.getClient();
                            if(client.isConnected()) {
                                sub.sendBufferToClient(packet);
                                server.getPinged().add(sub);
                            }
                        }
                    }
                    Thread.sleep(this.server.getPingTimeOut());
                }
            } catch(InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
