package de.bethibande.launcher.networking.webserver;

import java.io.File;
import java.net.Socket;

public interface IWebServer {

    int maxResponseTimeTracking = 20;

    // get the server port
    int getPort();
    // get the amount of open connections
    int getConnections();
    // set the root directory
    void setWebServerRoot(File root);
    // get the root directory of the webserver
    File getWebServerRoot();
    // set allowed http methods
    void setAllowedMethods(HTTPConnectionMethod... methods);
    // get all allowed http methods
    HTTPConnectionMethod[] getAllowedConnectionMethods();
    // start the webserver
    void start();
    // average response time in ms
    int getAverageResponseTime();

    void connectionClosed(Socket s, int responseTime);

}
