package de.bethibande.launcher.networking.webserver.virtual;

import de.bethibande.launcher.networking.webserver.HTTPConnectionMethod;
import de.bethibande.launcher.networking.webserver.IWebServer;
import de.bethibande.launcher.networking.webserver.ServerConfig;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class ApiWebServer extends Thread implements IWebServer {

    @Getter
    private final int port;
    @Getter
    private final int buffer_size;
    @Getter
    private HTTPConnectionMethod[] allowedConnectionMethods;
    @Getter
    private final ServerConfig config = new ServerConfig();

    private final HashMap<Socket, ApiSubProcess> connections = new HashMap<>();

    private final LinkedList<Integer> responseTimes = new LinkedList<>();
    @Getter
    private int averageResponseTime = 0;
    @Getter
    private final LinkedList<RequestHandler> handlers = new LinkedList<>();

    public ApiWebServer(int port, int buffer_size) {
        this.port = port;
        this.buffer_size = buffer_size;
        setAllowedMethods(HTTPConnectionMethod.GET);
    }

    public void registerHandler(RequestHandler handler) {
        this.handlers.add(handler);
    }

    public void unregisterHandler(RequestHandler handler) {
        this.handlers.remove(handler);
    }

    @Override
    public int getConnections() {
        return this.connections.size();
    }

    @Override
    public void setWebServerRoot(File root) { }

    @Override
    public File getWebServerRoot() {
        return new File(".");
    }

    @Override
    public void setAllowedMethods(HTTPConnectionMethod... methods) {
        this.allowedConnectionMethods = methods;
    }

    @Override
    public void connectionClosed(Socket s, int responseTime) {
        this.connections.remove(s);
        if(responseTime >= 0) {
            this.responseTimes.add(responseTime);
            if(this.responseTimes.size() > IWebServer.maxResponseTimeTracking) this.responseTimes.remove(0);
            long total = 0;
            for(int i : this.responseTimes) {
                total += i;
            }
            this.averageResponseTime = (int)(total/this.responseTimes.size());
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(this.port);
            while(true) {
                Socket client = server.accept();
                ApiSubProcess wssp = new ApiSubProcess(client, this, this.buffer_size);
                this.connections.put(client, wssp);
                wssp.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}