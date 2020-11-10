package de.bethibande.launcher.networking.webserver;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.utils.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class WebServerSubProcess extends Thread {

    private final Socket client;
    private final IWebServer owner;
    private final int buffer_size;

    public WebServerSubProcess(Socket client, IWebServer owner, int buffer_size) {
        this.client = client;
        this.owner = owner;
        this.buffer_size = buffer_size;
    }

    @Override
    public void run() {
        try {
            InputStream in = this.client.getInputStream();
            OutputStream out = this.client.getOutputStream();
            byte[] buffer = new byte[this.buffer_size];

            int read = in.read(buffer);
            String[] header = new String(buffer).split("\\n");

            if(ArrayUtils.contains(this.owner.getAllowedConnectionMethods(), header[0].split(" ")[0])) {

            } else sendErrorHeader(405, out);

            this.client.close();
            this.owner.connectionClosed(this.client, 0);
        } catch(IOException e) {
            e.printStackTrace();
            Core.loggerInstance.logError("An error occurred while executing a webserver subprocess.");
        }
    }

    public void sendErrorHeader(int code, OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        switch (code) {
            case 400:
                writer.println("HTTP/1.1 400 Bad Request");
                writer.println("Connection: close");
                writer.println("Date: " + new Date());
                break;
            case 405:
                writer.println("HTTP/1.1 405 Method Not Allowed");
                writer.println("Connection: close");
                writer.println("Date: " + new Date());
                break;
            case 500:
                writer.println("HTTP/1.1 500 Internal Server Error");
                writer.println("Connection: close");
                writer.println("Date: " + new Date());
                break;
        }
    }

}
