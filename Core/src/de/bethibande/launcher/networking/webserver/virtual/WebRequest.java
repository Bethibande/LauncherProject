package de.bethibande.launcher.networking.webserver.virtual;

import lombok.Getter;
import lombok.Setter;

import java.net.Socket;
import java.util.HashMap;

public class WebRequest {

    @Getter
    private final String uri;
    @Getter
    private final HashMap<String, String> queryArguments;
    @Getter
    private final Socket client;

    @Getter
    @Setter
    private int response_code = 200;
    @Getter
    @Setter
    private String response_message = "OK";
    @Getter
    @Setter
    private String content_type = "text/json";
    @Getter
    @Setter
    private String response_payload = "{\"Status\":\"Success!\"}";
    @Getter
    @Setter
    // this will be used for the Location: tag in the response header
    private String redirect = null;
    @Getter
    @Setter
    private boolean send_payload = true;

    public WebRequest(String uri, HashMap<String, String> queryArguments, Socket socket) {
        this.uri = uri;
        this.queryArguments = queryArguments;
        this.client = socket;
    }

}
