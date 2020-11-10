package de.bethibande.launcher.networking.webserver.virtual;

import java.util.List;

public interface IWebElement {

    // change element tag from for example "div" to "button"
    void setHtmlTag(String tag);

    void addChildElement(IWebElement element);

    void removeChildElement(IWebElement element);
    // add a style tag like width example -> <div style="width=100%;">[...]</div>
    void setStyleTag(String tag, String value);
    // for example -> key = test, value = true result -> <div test="true">[...]</div>
    void setCustomTag(String key, String value);
    // compile element to plain text/html
    List<String> compile();

}
