package de.bethibande.launcher.networking.webserver.virtual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileProvider implements INetworkResourceProvider {

    private final File _file;

    private boolean _hasNext = true;

    private long _index = 0;

    private int _bufferSize = 0;

    private final FileInputStream _in;

    public FileProvider(File f) throws FileNotFoundException {
        _file = f;
        _in = new FileInputStream(f);
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
        return _file.length();
    }

    @Override
    public boolean hasNext() {
        return _hasNext;
    }

    @Override
    public void rest() {
        _hasNext = true;
        _index = 0;
        try {
            _in.reset();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public byte[] getNext() {
        long toRead = _file.length() - _index;
        if(toRead > _bufferSize) {
            toRead = _bufferSize;
        } else _hasNext = false;

        byte[] buffer = new byte[(int)toRead];

        try {
            _in.read(buffer);

            _index += toRead;
            return buffer;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
