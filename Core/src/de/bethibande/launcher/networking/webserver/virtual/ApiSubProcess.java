package de.bethibande.launcher.networking.webserver.virtual;

import de.bethibande.launcher.networking.webserver.IWebServer;
import de.bethibande.launcher.networking.webserver.WebServer;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.TimeUtils;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class ApiSubProcess extends Thread {

    private final Socket socket;

    private final ApiWebServer parent;

    private final int buffer_size;

    public ApiSubProcess(Socket s, ApiWebServer parent, int buffer_size) {
        this.socket = s;
        this.parent = parent;
        this.buffer_size = buffer_size;
    }

    @Override
    public void run() {
        long start = 0;
        long end = -1;
        try {
            InputStream in = this.socket.getInputStream();
            OutputStream out = this.socket.getOutputStream();

            byte[] buffer = new byte[this.buffer_size];
            int read = in.read(buffer);
            start = TimeUtils.getTimeInMillis();

            String[] header = new String(buffer).split("\\n");

            if(ArrayUtils.contains(this.parent.getAllowedConnectionMethods(), header[0].split(" ")[0])) {
                String uri = header[0].split(" ")[1];
                HashMap<String, String> queryArguments = new HashMap<>();
                if(uri.contains("?")) {
                    String[] query = uri.split("\\?")[1].split("&");
                    uri = uri.split("\\?")[0];
                    for(String q : query) {
                        String k = q, v = null;
                        if(q.contains("=")) {
                            k = q.split("=")[0];
                            v= q.substring(k.length()+1);
                        }
                        queryArguments.put(k, v);
                    }
                }
                WebRequest request = new WebRequest(uri, queryArguments, this.socket);
                try {
                    for (RequestHandler handler : this.parent.getHandlers()) {
                        handler.handleRequest(request);
                    }
                } catch(Exception e) {
                    sendErrorHeader(500, out);
                    end = TimeUtils.getTimeInMillis();
                    this.parent.connectionClosed(this.socket, (int)(end-start));
                    return;
                }

                PrintWriter writer = new PrintWriter(out);
                writer.println("HTTP/1.1 " + request.getResponse_code() + " " + request.getResponse_message());
                writer.println("Connection: close");
                writer.println("Date: " + new Date());
                if(request.getRedirect() != null) {
                    writer.println("Location: " + request.getRedirect());
                    request.setSend_payload(false);
                }
                if(request.isSend_payload() && request.getResponse_payload() != null) {
                    writer.println("Content-Length: " + request.getResponse_payload().length());
                } else writer.println("Content-Length: 0");
                if(request.getContent_type() != null) {
                    writer.println("Content-Type: " + request.getContent_type());
                }
                writer.println();
                writer.flush();

                String payload = request.getResponse_payload();
                for(int i = 0; i < (payload.length()/this.buffer_size)+1; i++) {
                    out.write(payload.substring(this.buffer_size*i, Math.min(this.buffer_size * (i + 1), payload.length())).getBytes());
                    out.flush();
                }
                end = TimeUtils.getTimeInMillis();
            } else sendErrorHeader(405, out);

            this.socket.close();
        } catch(IOException e) { }
        this.parent.connectionClosed(this.socket, (int)(end-start));
    }

    public void sendErrorHeader(int code, OutputStream out) {
        PrintWriter writer = new PrintWriter(out);
        switch (code) {
            case 400:
                writer.println("HTTP/1.1 400 Bad Request");
                break;
            case 403:
                writer.println("HTTP/1.1 403 FORBIDDEN");
                break;
            case 404:
                writer.println("HTTP/1.1 404 Not Found");
                break;
            case 405:
                writer.println("HTTP/1.1 405 Method Not Allowed");
                break;
            case 500:
                writer.println("HTTP/1.1 500 Internal Server Error");
                break;
        }
        writer.println("Connection: close");
        writer.println("Date: " + new Date());

        String error_content = "{\"Error\":\"" + code + "\"}";

        writer.println("Content-Length: "+  error_content.length());
        writer.println("Content-Type: text/json");
        writer.println();
        writer.flush();
        writer.println(error_content);
        writer.flush();
    }

}
