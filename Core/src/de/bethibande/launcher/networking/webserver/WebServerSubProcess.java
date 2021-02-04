package de.bethibande.launcher.networking.webserver;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.networking.logging.IServerLogSession;
import de.bethibande.launcher.networking.logging.NetworkPackageSource;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.TimeUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class WebServerSubProcess extends Thread {

    private final Socket client;
    private final IWebServer owner;
    private final int buffer_size;

    private final IServerLogSession logSession;

    public WebServerSubProcess(Socket client, IWebServer owner, int buffer_size, IServerLogSession logSession) {
        this.client = client;
        this.owner = owner;
        this.buffer_size = buffer_size;
        this.logSession = logSession;
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
            if(this.logSession != null) this.logSession.log(NetworkPackageSource.IN, buffer);

            if(ArrayUtils.contains(this.owner.getAllowedConnectionMethods(), header[0].split(" ")[0])) {
                String requestedFile = header[0].split(" ")[1];
                File f = new File(this.owner.getWebServerRoot() + "/" + requestedFile);
                if(f.exists()) {
                    if(f.isFile()) {
                        try {
                            PrintWriter writer = new PrintWriter(out);
                            StringBuilder sb = new StringBuilder();
                            sb.append("HTTP/1.1 200 OK\n");
                            sb.append("Connection: close\n");
                            sb.append("Date: ").append(new Date()).append("\n");
                            sb.append("Content-Length: ").append(f.length()).append("\n");
                            sb.append("Content-Type:").append(getContentType(f)).append("\n");
                            writer.println(sb.toString());
                            if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, sb.toString().getBytes());
                            writer.flush();
                            byte[] copyBuffer = new byte[this.buffer_size];
                            InputStream fileIn = new FileInputStream(f);
                            while ((read = fileIn.read(copyBuffer)) > 0) {
                                out.write(copyBuffer, 0, read);
                                out.flush();
                                copyBuffer = new byte[this.buffer_size];
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            sendErrorHeader(500, out);
                        }
                    } else if(new File(f + "/index.html").exists()) {
                        try {
                            f = new File(f + "/index.html");
                            PrintWriter writer = new PrintWriter(out);
                            StringBuilder sb = new StringBuilder();
                            sb.append("HTTP/1.1 200 OK\n");
                            sb.append("Connection: close\n");
                            sb.append("Date: ").append(new Date()).append("\n");
                            sb.append("Content-Length: ").append(f.length()).append("\n");
                            sb.append("Content-Type:").append(getContentType(f)).append("\n");
                            writer.println(sb.toString());
                            if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, sb.toString().getBytes(StandardCharsets.UTF_8));
                            writer.flush();
                            byte[] copyBuffer = new byte[this.buffer_size];
                            InputStream fileIn = new FileInputStream(f);
                            while ((read = fileIn.read(copyBuffer)) > 0) {
                                out.write(copyBuffer, 0, read);
                                out.flush();
                                copyBuffer = new byte[this.buffer_size];
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
        if(this.logSession != null) this.logSession.endSession();
        this.owner.connectionClosed(this.client, (int)(end-start));
    }

    private String getContentType(File f) {
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
        return type;
    }

    public void sendErrorHeader(int code, OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        StringBuilder sb = new StringBuilder();
        switch (code) {
            case 400:
                sb.append("HTTP/1.1 400 Bad Request\n");
                break;
            case 403:
                sb.append("HTTP/1.1 403 FORBIDDEN\n");
                break;
            case 404:
                sb.append("HTTP/1.1 404 Not Found\n");
                break;
            case 405:
                sb.append("HTTP/1.1 405 Method Not Allowed\n");
                break;
            case 500:
                sb.append("HTTP/1.1 500 Internal Server Error\n");
                break;
        }
        sb.append("Connection: close\n");
        sb.append("Date: ").append(new Date()).append("\n");

        WebServer ws = (WebServer)this.owner;
        File errorFile = ws.getConfig().getErrorPage(code);
        if(errorFile != null && errorFile.exists()) {
            try {
                sb.append("Content-Length: ").append(errorFile.length()).append("\n");
                writer.println(sb.toString());
                writer.flush();
                if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, sb.toString().getBytes(StandardCharsets.UTF_8));

                byte[] copyBuffer = new byte[this.buffer_size];
                int read;
                InputStream in = new FileInputStream(errorFile);
                while((read = in.read(copyBuffer)) > 0) {
                    out.write(copyBuffer, 0, read);
                    out.flush();
                    if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, copyBuffer);
                    copyBuffer = new byte[this.buffer_size];
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            sb.append("Content-Length: 0\n");
            writer.println(sb.toString());
            writer.flush();
            if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, sb.toString().getBytes());
        }
    }

}
