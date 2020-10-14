package de.bethibande.launcher.networking.connector;

import de.bethibande.launcher.networking.RestAction;

public interface ISimpleServerConnector {

    // ip address of the host server
    String getHostIP();
    // port of the host server
    int getHostPort();
    // returns true if the host server forces encryption
    boolean isEncryptionEnabled();
    // rest action to handle received buffers
    ISimpleServerConnector payloadReceived(RestAction rest);
    // disconnect from host
    void disconnect();
    // returns true if the connector is connected to the host
    boolean isConnected();
    // returns true if the connection was established (header packets exchanged)
    boolean isConnectionEstablished();
    // get the status code send by the host
    int getStatusCode();
    // get the header packet received by the host
    String getReceivedHeader();
    // send a buffer to the host
    void sendBufferToServer(byte[] buffer);

}
