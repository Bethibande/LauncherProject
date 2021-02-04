package de.bethibande.launcher.networking.logging;

import de.bethibande.launcher.networking.server.INetworkServer;

// use one logger instance per server
public interface IServerLogger {

    // get the server this logger belongs to
    INetworkServer getServer();
    // set the server this logger belongs to
    void setServer(INetworkServer server);
    // create a new session
    IServerLogSession createSession(String ipv4, int port);
    // end a logging session
    void endSession(String ipv4, int port);
    // end a logging session
    void endSession(IServerLogSession session);
}
