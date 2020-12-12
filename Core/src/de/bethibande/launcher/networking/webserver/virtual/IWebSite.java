package de.bethibande.launcher.networking.webserver.virtual;

public interface IWebSite {

    void linkStyleSheet(String url);
    void linkScript(String url);
    void setHeader(String header);

    void handleRequest(WebRequest request);

}
