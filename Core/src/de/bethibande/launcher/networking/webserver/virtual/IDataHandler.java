package de.bethibande.launcher.networking.webserver.virtual;

import java.io.IOException;
import java.io.InputStream;

// used to handle data send with post request
public interface IDataHandler {

    void run(WebRequest request, InputStream stream) throws IOException;

}
