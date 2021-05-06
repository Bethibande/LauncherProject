package de.bethibande.launcher.networking.webserver.virtual;

public interface INetworkResourceProvider {

    INetworkResourceProvider setBufferSize(int bufferSize);

    int getBufferSize();

    long getTotalLength();

    boolean hasNext();

    void reset();

    byte[] getNext();

}
