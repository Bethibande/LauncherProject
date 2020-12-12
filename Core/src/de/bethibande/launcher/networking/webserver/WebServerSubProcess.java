package de.bethibande.launcher.networking.webserver;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.TimeUtils;

import java.io.*;
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
        long start = 0;
        long end = -1;
        try {
            InputStream in = this.client.getInputStream();
            OutputStream out = this.client.getOutputStream();
            byte[] buffer = new byte[this.buffer_size];

            int read = in.read(buffer);
            start = TimeUtils.getTimeInMillis();
            String[] header = new String(buffer).split("\\n");

            if(ArrayUtils.contains(this.owner.getAllowedConnectionMethods(), header[0].split(" ")[0])) {
                String requestedFile = header[0].split(" ")[1];
                File f = new File(((WebServer)this.owner).getWebServerRoot() + "/" + requestedFile);
                if(f.exists()) {
                    if(f.isFile()) {
                        try {
                            PrintWriter writer = new PrintWriter(out);
                            writer.println("HTTP/1.1 200 OK");
                            writer.println("Connection: close");
                            writer.println("Date: " + new Date());
                            writer.println("Content-Length: " + f.length());
                            printContentType(f, out);
                            writer.println();
                            writer.flush();
                            byte[] copyBuffer = new byte[this.buffer_size];
                            InputStream fileIn = new FileInputStream(f);
                            while ((read = fileIn.read(copyBuffer)) > 0) {
                                out.write(copyBuffer, 0, read);
                                out.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            sendErrorHeader(500, out);
                        }
                    } else if(new File(f + "/index.html").exists()) {
                        try {
                            f = new File(f + "/index.html");
                            PrintWriter writer = new PrintWriter(out);
                            writer.println("HTTP/1.1 200 OK");
                            writer.println("Connection: close");
                            writer.println("Date: " + new Date());
                            writer.println("Content-Length: " + f.length());
                            printContentType(f, out);
                            writer.println();
                            writer.flush();
                            byte[] copyBuffer = new byte[this.buffer_size];
                            InputStream fileIn = new FileInputStream(f);
                            while ((read = fileIn.read(copyBuffer)) > 0) {
                                out.write(copyBuffer, 0, read);
                                out.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            sendErrorHeader(500, out);
                        }
                    } else sendErrorHeader(403, out);
                } else sendErrorHeader(404, out);
            } else sendErrorHeader(405, out);
            end = TimeUtils.getTimeInMillis();
            this.client.close();
        } catch(IOException e) {
            e.printStackTrace();
            Core.loggerInstance.logError("An error occurred while executing a webserver subprocess.");
        }
        this.owner.connectionClosed(this.client, (int)(end-start));
    }

    private void printContentType(File f, OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        String[] s = f.getAbsolutePath().split("\\.");
        String extension = s[s.length-1];
        String type = "text/plain";
        switch (extension) {
            case "html": case "htm":
                type = "text/html";
                break;
            case "json":
                type = "text/json";
                break;
            case "css":
                type = "text/css";
                break;
            case "js":
                type = "text/javascript";
                break;
            case "jar":
                type = "application/java-archive";
                break;
            case "png":
                type = "image/png";
                break;
        }
        writer.println("Content-Type: " + type);
    }

    public void sendErrorHeader(int code, OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        switch (code) {
            case 400:
                writer.println("HTTP/1.1 400 Bad Request");
                writer.println("Connection: close");
                writer.println("Date: " + new Date());
                break;
            case 403:
                writer.println("HTTP/1.1 403 FORBIDDEN");
                writer.println("Connection: close");
                writer.println("Date: " + new Date());
                break;
            case 404:
                writer.println("HTTP/1.1 404 Not Found");
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
        WebServer ws = (WebServer)this.owner;
        File errorFile = ws.getConfig().getErrorPage(code);
        if(errorFile != null && errorFile.exists()) {
            try {
                writer.println("Content-Length: " + errorFile.length());
                writer.println("\n");
                byte[] copyBuffer = new byte[this.buffer_size];
                int read;
                InputStream in = new FileInputStream(errorFile);
                while((read = in.read(copyBuffer)) > 0) {
                    out.write(copyBuffer, 0, read);
                    out.flush();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else writer.println("Content-Length: 0");
        writer.flush();
    }

}
