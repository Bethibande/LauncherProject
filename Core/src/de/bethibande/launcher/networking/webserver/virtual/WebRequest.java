package de.bethibande.launcher.networking.webserver.virtual;

import lombok.Getter;
import lombok.Setter;

import java.net.Socket;
import java.util.HashMap;

public class WebRequest {

    @Getter
    private final String method;
    @Getter
    private final String uri;
    @Getter
    private final HashMap<String, String> queryArguments;
    @Getter
    private final int content_length;
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
    private INetworkResourceProvider response_payload = new StringProvider("{\"Status\":\"Success!\"}");
    @Getter
    @Setter
    // this will be used for the Location: tag in the response header
    private String redirect = null;
    @Getter
    @Setter
    private boolean send_payload = true;

    @Getter
    @Setter
    // add custom arguments/keys to response header
    private HashMap<String, String> customResponseHeader = new HashMap<>();

    public WebRequest(String method, String uri, HashMap<String, String> queryArguments, int content_length, Socket socket) {
        this.method = method;
        this.uri = uri;
        this.queryArguments = queryArguments;
        this.content_length = content_length;
        this.client = socket;
    }

}
