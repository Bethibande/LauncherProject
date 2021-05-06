package de.bethibande.launcher.networking.webserver.virtual;

import de.bethibande.launcher.networking.encryption.PrivateKey;
import de.bethibande.launcher.networking.encryption.PublicKey;
import de.bethibande.launcher.networking.encryption.RSA;
import de.bethibande.launcher.networking.logging.IServerLogSession;
import de.bethibande.launcher.networking.logging.NetworkPackageSource;
import de.bethibande.launcher.networking.webserver.IWebServer;
import de.bethibande.launcher.networking.webserver.WebServer;
import de.bethibande.launcher.utils.ArrayUtils;
import de.bethibande.launcher.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

public class ApiSubProcess extends Thread {

    private final Socket socket;

    private final ApiWebServer parent;

    private final int buffer_size;

    @Getter
    @Setter
    private PublicKey encryptionKey;
    @Getter
    @Setter
    private PrivateKey decryptionKey;

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

            for(IConnectionHandler handler : this.parent.getConnectionHandlers()) {
                handler.handle(this.socket, this.parent, this);
            }

            int read = in.read(buffer);
            buffer = ArrayUtils.trim(buffer, 0, read);
            if(decryptionKey != null) buffer = RSA.decryptData(buffer, decryptionKey);

            start = TimeUtils.getTimeInMillis();

            String[] headerArr = new String(buffer, StandardCharsets.UTF_8).split("\n");
            String header = headerArr[0];
            String method = header.split(" ")[0];
            int content_length = 0;
            String receiveContentType = "text/json";

            for(String s : headerArr) {
                if(s.toLowerCase().startsWith("content-length: ")) {
                    content_length = Integer.parseInt(s.trim().substring(16));
                }
                if(s.toLowerCase().startsWith("content-type: ")) {
                    receiveContentType = s.trim().substring(14);
                }
            }

            if(ArrayUtils.contains(this.parent.getAllowedConnectionMethods(), method)) {
                String uri = header.split(" ")[1];
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
                WebRequest request = new WebRequest(method, uri, queryArguments, content_length, receiveContentType, this.socket);
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

                StringBuilder sb = new StringBuilder();
                PrintWriter writer = new PrintWriter(out);
                sb.append("HTTP/1.1 " + request.getResponse_code() + " " + request.getResponse_message() + "\n");
                sb.append("Connection: Close\n");
                sb.append("Date " + new Date() + "\n");
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
                    sb.append(s + ": " + v + "\n");
                }
                sb.append("\n");

                if(encryptionKey != null) {
                    byte[] encryptedHeader = RSA.encryptString(sb.toString(), encryptionKey);
                    out.write(encryptedHeader);
                    out.flush();
                } else {
                    writer.print(sb.toString());
                    writer.flush();
                }

                //--------------------------------------------------------------------------------------------
                // read and process payload/content
                //--------------------------------------------------------------------------------------------
                if(content_length > 0) {
                    if(this.parent.getDataHandler() != null) {
                        IDataHandler handler = this.parent.getDataHandler();
                        try {
                            handler.run(request, in);
                        } catch(IOException e) {
                            sendErrorHeader(500, out);
                            end = TimeUtils.getTimeInMillis();
                            this.parent.connectionClosed(this.socket, (int) (end - start));
                            return;
                        }
                    }
                }

                //--------------------------------------------------------------------------------------------
                // write payload/content
                //--------------------------------------------------------------------------------------------
                INetworkResourceProvider nrp = request.getResponse_payload().setBufferSize(buffer_size);
                if(nrp.getTotalLength() > 0) {
                    nrp.reset();
                    while(nrp.hasNext()) {
                        buffer = new byte[buffer_size];
                        buffer = nrp.getNext();
                        if(buffer != null) {
                            if(encryptionKey != null) {
                                buffer = RSA.encryptData(buffer, encryptionKey);
                                out.write(buffer, 0, buffer.length);
                                out.flush();
                            } else {
                                out.write(buffer, 0, buffer.length);
                                out.flush();
                            }
                        } else break;
                    }
                }

                //--------------------------------------------------------------------------------------------
                // stop timer and close connection
                //--------------------------------------------------------------------------------------------
                end = TimeUtils.getTimeInMillis();
            } else sendErrorHeader(405, out);

            while(true) {
                if(!socket.isConnected() || socket.isClosed()) break;
                Thread.sleep(100);
            }
            socket.close();
        } catch(IOException | InterruptedException e) { }
        this.parent.connectionClosed(this.socket, (int)(end-start));
    }

    private byte[] prepareStringForOutput(String s, boolean newLine) {
        if(encryptionKey == null) return !newLine ? s.getBytes(StandardCharsets.UTF_8): (s + "\n").getBytes(StandardCharsets.UTF_8);
        return !newLine ? RSA.encryptString(s, encryptionKey): ArrayUtils.join(RSA.encryptString(s, encryptionKey), "\n".getBytes(StandardCharsets.UTF_8));
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
        writer.println(error_content);
        writer.flush();
    }

}
