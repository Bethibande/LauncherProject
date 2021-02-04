package de.bethibande.launcher.networking.webserver.virtual;

import java.nio.charset.StandardCharsets;

public class StringProvider implements INetworkResourceProvider {

    private int _bufferSize = 0;

    private boolean _next = true;

    private byte[] _string;

    private int _index = 0;

    public StringProvider(String s) {
        _string = s.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public INetworkResourceProvider setBufferSize(int bufferSize) {
        _bufferSize = bufferSize;
        return this;
    }

    @Override
    public int getBufferSize() {
        return _bufferSize;
    }

    @Override
    public long getTotalLength() {
        return _string.length;
    }

    @Override
    public boolean hasNext() {
        return _next;
    }

    @Override
    public void rest() {
        _index = 0;
        _next = true;
    }

    @Override
    public byte[] getNext() {
        int toRead = _string.length - _index;
        if(toRead > _bufferSize) {
            toRead = _bufferSize;
        } else _next = false;

        byte[] read = new byte[toRead];
        System.arraycopy(_string, _index, read, 0, toRead);

        _index += toRead;
        return read;
    }
}
