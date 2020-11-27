package de.bethibande.launcher.ui;

import de.bethibande.launcher.Core;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;

public class UiManager {

    @Getter
    @Setter
    public UiResourceContainer scheme;

    public UiManager() {

    }

    public UiResource getResource(String res) {
        if(res.toLowerCase().startsWith("@color/")) {
            res = res.substring(7);
            if(this.scheme != null) {
                return this.scheme.getResource(res);
            }
        }
        return null;
    }

    public UiResourceContainer loadScheme(InputStream in) {
        try {
            UiResourceContainer scheme = new UiResourceContainer();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            Element rootElement = doc.getDocumentElement();
            NodeList colors = rootElement.getElementsByTagName("color");

            if(rootElement.getTagName().equals("scheme")) {
                int i = 0;
                while(i < colors.getLength()) {
                    Node n = colors.item(i);
                    Element e = (Element)n;
                    if(e.hasAttribute("name")) {
                        String name = e.getAttribute("name");
                        String color = e.getTextContent();

                        if(!color.startsWith("#")) {
                            Core.loggerInstance.logError("Encountered an invalid color value, while loading a scheme: " + color);
                        } else scheme.addResource(name, Color.decode(color));
                    } else Core.loggerInstance.logError("Encountered an element without the 'name' attribute, while loading a scheme, skipping element.");
                    i++;
                }
                return scheme;
            } else {
                Core.loggerInstance.logError("An error occurred while loading a scheme: the root element tag is not 'scheme'!");
            }

        } catch(IOException | ParserConfigurationException | SAXException e) {
            Core.loggerInstance.logError("An error occurred while loading a scheme");
            e.printStackTrace();
        }
        return null;
    }

    public UiResourceContainer loadScheme(String path) {
        File f = new File(path);
        if(f.exists()) {
            try {
                return loadScheme(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else Core.loggerInstance.logError("An error occurred, the specified file does not exist!");
        return null;
    }

}
