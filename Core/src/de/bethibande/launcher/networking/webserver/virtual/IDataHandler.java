package de.bethibande.launcher.networking.webserver.virtual;

// used to handle data send with post request
public interface IDataHandler {

    void run(WebRequest request, String uri, byte[] received_data, int read);

}
