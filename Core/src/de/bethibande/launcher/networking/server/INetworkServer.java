package de.bethibande.launcher.networking.server;

import java.net.Socket;

public interface INetworkServer {

    // get the port the server is running on
    int getPort();
    // returns amount of connected users/clients
    int getConnectedClients();
    // returns whether the server uses/forces encryption or not
    boolean useEncryption();
    // returns whether new users are allowed to connect or not
    boolean canConnect();
    // stops/closes the server
    void close();


    void handleConnection(Socket s);

}
