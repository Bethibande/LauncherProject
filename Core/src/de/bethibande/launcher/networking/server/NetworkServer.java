package de.bethibande.launcher.networking.server;

import de.bethibande.launcher.Core;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServer extends Thread implements INetworkServer {

    @Getter
    private final int port;
    private final boolean useEncryption;
    @Setter
    private boolean canConnect = true;

    private ServerSocket server;

    private final List<Socket> connectedClients = new ArrayList<>();

    public NetworkServer(int port, boolean useEncryption) {
        this.port = port;
        this.useEncryption = useEncryption;
    }

    public int getConnectedClients() { return this.connectedClients.size(); }
    public boolean useEncryption() { return this.useEncryption; }
    public boolean canConnect() { return this.canConnect; }
    public void close() {
        try {
            if (server != null && !server.isClosed()) {
                interrupt();
                server.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
            Core.loggerInstance.logError("Error while closing server task.");
        }
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(this.port);
            Core.loggerInstance.logMessage("Network server started and listening for incoming connections on port: " + this.port);
            while(!server.isClosed() && this.canConnect) {
                Socket client = server.accept();
                handleConnection(client);
            }
        } catch(IOException e) {
            e.printStackTrace();
            Core.loggerInstance.logError("An error occurred while executing a server task.");
        }
    }

    @Override
    public void handleConnection(Socket s) { }
}
