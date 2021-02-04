package de.bethibande.launcher.networking.logging;

public interface IServerLogSession {

    IServerLogger getParent();
    // return the ipv4 of the connected user this session belongs to
    String getIpv4();
    // return the port of the connected user this session belongs to
    int getPort();
    // log a message
    void log(String msg);
    // log an incoming or outgoing package/buffer
    void log(NetworkPackageSource source, byte[] buffer);
    // start the session
    void startSession();
    // end the session
    void endSession();

}
