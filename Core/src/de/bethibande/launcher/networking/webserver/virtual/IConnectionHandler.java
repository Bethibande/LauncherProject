package de.bethibande.launcher.networking.webserver.virtual;

import java.net.Socket;

public interface IConnectionHandler {

    void handle(Socket client, ApiWebServer webServer, ApiSubProcess process);

}
