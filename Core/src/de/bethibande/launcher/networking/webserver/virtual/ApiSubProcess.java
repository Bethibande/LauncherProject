package de.bethibande.launcher.networking.webserver.virtual;

import de.bethibande.launcher.networking.logging.IServerLogSession;
import de.bethibande.launcher.networking.logging.NetworkPackageSource;
import de.bethibande.launcher.networking.webserver.IWebServer;
import de.bethibande.launcher.networking.webserver.WebServer;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.TimeUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

public class ApiSubProcess extends Thread {

    private final Socket socket;

    private final ApiWebServer parent;

    private final int buffer_size;

    private final IServerLogSession logSession;

    public ApiSubProcess(Socket s, ApiWebServer parent, int buffer_size, IServerLogSession logSession) {
        this.socket = s;
        this.parent = parent;
        this.buffer_size = buffer_size;
        this.logSession = logSession;
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
            String method = header[0].split(" ")[0];
            int content_length = 0;
            if(this.logSession != null) this.logSession.log(NetworkPackageSource.IN, buffer);

            for(String s : header) {
                if(s.toLowerCase().startsWith("content-length: ")) {
                    content_length = Integer.parseInt(s.substring(16, s.length()-1));
                }
            }

            if(ArrayUtils.contains(this.parent.getAllowedConnectionMethods(), method)) {
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
                WebRequest request = new WebRequest(method, uri, queryArguments, content_length, this.socket);
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

                //--------------------------------------------------------------------------------------------
                // write header
                //--------------------------------------------------------------------------------------------

                PrintWriter writer = new PrintWriter(out);
                StringBuilder sb = new StringBuilder();
                sb.append("HTTP/1.1 " + request.getResponse_code() + " " + request.getResponse_message() + "\n");
                sb.append("Connection: close\n");
                sb.append("Date: " + new Date() + "\n");
                sb.append("Access-Control-Allow-Origin: *\n");
                if(request.getRedirect() != null) {
                    sb.append("Location: " + request.getRedirect() + "\n");
                    request.setSend_payload(false);
                }
                if(request.isSend_payload() && request.getResponse_payload() != null) {
                    sb.append("Content-Length: " + request.getResponse_payload().getTotalLength() + "\n");
                } else sb.append("Content-Length: 0\n");
                if(request.getContent_type() != null) {
                    sb.append("Content-Type: " + request.getContent_type() + "\n");
                }

                // process custom header arguments
                for(String s : request.getCustomResponseHeader().keySet()) {
                    String v = request.getCustomResponseHeader().get(s);
                    sb.append(s).append(": ").append(v).append("\n");
                }

                writer.println(sb.toString());
                writer.flush();
                if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, sb.toString().getBytes(StandardCharsets.UTF_8));

                //--------------------------------------------------------------------------------------------
                // read and process payload/content
                //--------------------------------------------------------------------------------------------
                if(content_length > 0) {
                    buffer = new byte[this.buffer_size];
                    int __read = 0;
                    while ((read = in.read(buffer)) > 0) {
                        try {
                            for (IDataHandler handler : this.parent.getDataHandlers()) {
                                handler.run(request, uri, buffer, read);
                            }
                        } catch (Exception e) {
                            sendErrorHeader(500, out);
                            end = TimeUtils.getTimeInMillis();
                            this.parent.connectionClosed(this.socket, (int) (end - start));
                            return;
                        }
                        if (this.logSession != null) this.logSession.log(NetworkPackageSource.IN, buffer);
                        __read += read;
                        if (__read <= content_length) break;
                    }
                }

                //--------------------------------------------------------------------------------------------
                // write payload/content
                //--------------------------------------------------------------------------------------------
                INetworkResourceProvider nrp = request.getResponse_payload().setBufferSize(buffer_size);
                if(nrp.getTotalLength() > 0) {
                    nrp.rest();
                    while(nrp.hasNext()) {
                        buffer = nrp.getNext();
                        if(buffer != null) {
                            out.write(buffer, 0, buffer.length);
                            out.flush();
                            if (this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, buffer);
                        } else break;
                    }
                }

                //--------------------------------------------------------------------------------------------
                // stop timer and close connection
                //--------------------------------------------------------------------------------------------
                end = TimeUtils.getTimeInMillis();
            } else sendErrorHeader(405, out);

            this.socket.close();
        } catch(IOException e) { }
        this.parent.connectionClosed(this.socket, (int)(end-start));
        if(this.logSession != null) this.logSession.endSession();
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
        sb.append("Date: " + new Date() + "\n");
        sb.append("Access-Control-Allow-Origin: *\n");

        String error_content = "{\"Error\":\"" + code + "\"}";

        sb.append("Content-Length: "+  error_content.length() + "\n");
        sb.append("Content-Type: text/json\n");
        writer.println(sb.toString());
        writer.flush();
        if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, sb.toString().getBytes(StandardCharsets.UTF_8));
        writer.println(error_content);
        writer.flush();
        if(this.logSession != null) this.logSession.log(NetworkPackageSource.OUT, error_content.getBytes(StandardCharsets.UTF_8));
    }

}
