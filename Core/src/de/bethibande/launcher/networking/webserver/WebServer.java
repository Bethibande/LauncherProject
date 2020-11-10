package de.bethibande.launcher.networking.webserver;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WebServer extends Thread implements IWebServer {

    @Getter
    private final int port;
    @Getter
    @Setter
    private File webServerRoot;
    @Getter
    private HTTPConnectionMethod[] allowedConnectionMethods;

    private final List<Socket> connections = new ArrayList<>();

    private final List<Integer> responseTimes = new ArrayList<>();

    public WebServer(int port, File root) {
        this.port = port;
        this.webServerRoot = root;
    }

    @Override
    public int getConnections() {
        return this.connections.size();
    }

    @Override
    public void setAllowedMethods(HTTPConnectionMethod... methods) {
        this.allowedConnectionMethods = methods;
    }

    @Override
    public void connectionClosed(Socket s, int responseTime) {
        this.connections.remove(s);
        this.responseTimes.add(responseTime);
        if(this.responseTimes.size() >= IWebServer.maxResponseTimeTracking) this.responseTimes.remove(0);
    }

    @Override
    public int getAverageResponseTime() {
        return 0;
    }
}
