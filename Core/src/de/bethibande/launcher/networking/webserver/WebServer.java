package de.bethibande.launcher.networking.webserver;

import com.sun.security.ntlm.Server;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WebServer extends Thread implements IWebServer {

    @Getter
    private final int port;
    @Getter
    private final int buffer_size;
    @Getter
    @Setter
    private File webServerRoot;
    @Getter
    private HTTPConnectionMethod[] allowedConnectionMethods;
    @Getter
    private final ServerConfig config = new ServerConfig();

    private final HashMap<Socket, WebServerSubProcess> connections = new HashMap<>();

    private final LinkedList<Integer> responseTimes = new LinkedList<>();
    @Getter
    private int averageResponseTime = 0;

    public WebServer(int port, int buffer_size, File root) {
        this.port = port;
        this.buffer_size = buffer_size;
        this.webServerRoot = root;
        setAllowedMethods(HTTPConnectionMethod.GET);
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
                WebServerSubProcess wssp = new WebServerSubProcess(client, this, this.buffer_size);
                this.connections.put(client, wssp);
                wssp.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
