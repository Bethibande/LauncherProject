package de.bethibande.launcher.ui;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.ui.components.UiComponent;
import de.bethibande.launcher.ui.components.UiLabel;
import de.bethibande.launcher.ui.components.UiLayout;
import de.bethibande.launcher.ui.drawable.UiDrawOrder;
import de.bethibande.launcher.ui.drawable.UiDrawable;
import de.bethibande.launcher.ui.drawable.UiGradient;
import de.bethibande.launcher.ui.drawable.UiShape;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UiManager {

    public static final UiManagerConfig defaultConfig = new UiManagerConfig();

    public static final String xmlFieldPrefix = "ui";

    private static Field drawableIdField;

    public static Field componentIdField;
    public static Field componentChildrenField;

    static {
        defaultConfig.addXmlIntegerReplacementValue("align_left", 1);
        defaultConfig.addXmlIntegerReplacementValue("align_right", 2);
        defaultConfig.addXmlIntegerReplacementValue("align_center", 3);
        defaultConfig.addXmlIntegerReplacementValue("align_top", 4);
        defaultConfig.addXmlIntegerReplacementValue("align_bottom", 5);

        defaultConfig.addXmlComponentAssociation("layout", UiLayout.class);
        defaultConfig.addXmlComponentAssociation("label", UiLabel.class);

        defaultConfig.addXmlDrawableAssociation("gradient", UiGradient.class);
        defaultConfig.addXmlDrawableAssociation("shape", UiShape.class);
        defaultConfig.addXmlDrawableAssociation("draworder", UiDrawOrder.class);

        try {
            drawableIdField = UiResource.class.getDeclaredField("id");
            drawableIdField.setAccessible(true);

            componentIdField = UiComponent.class.getDeclaredField("id");
            componentIdField.setAccessible(true);
            componentChildrenField = UiComponent.class.getDeclaredField("children");
            componentChildrenField.setAccessible(true);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private final HashMap<String, UiComponent> layouts = new HashMap<>();
    private final HashMap<String, UiDrawable> drawableCache = new HashMap<>();

    @Getter
    @Setter
    public UiResourceContainer scheme;

    @Getter
    @Setter
    private UiManagerConfig config = defaultConfig;

    public UiManager() {

    }

    public UiResource getResource(String res) {
        if(res.toLowerCase().startsWith("@color/")) {
            res = res.substring(7);
            if(this.scheme != null) {
                return this.scheme.getResource(res);
            }
        }
        if(res.toLowerCase().startsWith("@drawable/")) {
            res = res.substring(10);
            if(drawableCache.containsKey(res)) {
                return drawableCache.get(res);
            }
        }
        return null;
    }

    public UiComponent getLayoutById(String id) {
        return this.layouts.getOrDefault(id, null);
    }

    public void removeDrawableFromCache(String id) {
        this.drawableCache.remove(id);
    }

    public void cacheDrawable(UiDrawable drawable) {
        if(!drawable.getId().isEmpty()) {
            this.drawableCache.put(drawable.getId(), drawable);
        } else {
            Core.loggerInstance.logError("Error while caching drawable, the drawable id is null/empty");
        }
    }

    public void removeComponentFromCache(String id) {
        this.layouts.remove(id);
    }

    public void cacheComponent(UiComponent component) {
        if(!component.getId().isEmpty()) {
            this.layouts.put(component.getId(), component);
        } else {
            Core.loggerInstance.logError("Error while caching component, the component id is null/empty");
        }
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

    public UiDrawable loadDrawable(InputStream in) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            Element rootElement = doc.getDocumentElement();
            String rootType = rootElement.getTagName().toLowerCase();
            Class<? extends UiDrawable> drawableClass;
            if(rootType.contains(".")) {
                drawableClass = (Class<? extends UiDrawable>)Class.forName(rootType);
            } else drawableClass = config.getXmlDrawableAssociations().get(rootType);
            UiDrawable drawable = drawableClass.newInstance();

            NamedNodeMap attributes = rootElement.getAttributes();
            int i = 0;
            while(i < attributes.getLength()) {
                Node n = attributes.item(i);
                if(n.getNodeType() == Node.ATTRIBUTE_NODE) {
                    if(n.getNodeName().equals("id")) {
                        drawableIdField.set(drawable, n.getNodeValue());
                    }
                    if(n.getNodeName().startsWith(xmlFieldPrefix)) {
                        String key = n.getNodeName().substring(xmlFieldPrefix.length()+1);
                        try {
                            Field f = drawableClass.getDeclaredField(key);
                            f.setAccessible(true);
                            setFieldValue(f, drawable, n.getNodeValue());
                        } catch(NoSuchFieldException e) {
                            Core.loggerInstance.logError("Encountered an error while loading a drawable, there is no field with the name '" + n.getNodeName() + "'");
                            e.printStackTrace();
                        }
                    }
                }
                i++;
            }
            if(rootElement.hasChildNodes()) {
                try {
                    if (drawableClass.getDeclaredField("children") != null) {
                        Field f = drawableClass.getDeclaredField("children");
                        f.setAccessible(true);
                        Object o = f.get(drawable);

                        if(o instanceof List) {
                            List<UiResource> children = (List<UiResource>)f.get(drawable);
                            NodeList childNodes = rootElement.getChildNodes();

                            i = 0;
                            while(i < childNodes.getLength()) {
                                Node n = childNodes.item(i);
                                if(n.getNodeType() == Node.ELEMENT_NODE) {
                                    Element e = (Element)n;
                                    if(n.getNodeName().equalsIgnoreCase("item")) {
                                        if(e.hasAttribute("res")) {
                                            children.add(getResource(e.getAttribute("res")));
                                        }
                                    }
                                }
                                i++;
                            }
                        }
                        if(o instanceof Map) {
                            Map<String, UiResource> children = (Map<String, UiResource>)f.get(drawable);
                            NodeList childNodes = rootElement.getChildNodes();

                            i = 0;
                            while(i < childNodes.getLength()) {
                                Node n = childNodes.item(i);
                                if(n.getNodeType() == Node.ELEMENT_NODE) {
                                    Element e = (Element)n;
                                    if(n.getNodeName().equalsIgnoreCase("item")) {
                                        if(e.hasAttribute("res") && e.hasAttribute("id")) {
                                            children.put(e.getAttribute("id"), getResource(e.getAttribute("res")));
                                        }
                                    }
                                }
                                i++;
                            }
                        }
                    }
                } catch(NoSuchFieldException e) {
                    Core.loggerInstance.logError("Encountered an error while loading a drawable, found unexpected child nodes");
                }
            }
            if(drawable.getId() != null) {
                cacheDrawable(drawable);
            }
            return drawable;
        } catch(IOException | ParserConfigurationException | SAXException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UiComponent loadUiComponent(InputStream in) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            Element rootElement = doc.getDocumentElement();
            UiComponent rootComponent = parseUiComponentElement(rootElement);

            cacheComponent(rootComponent);
            return rootComponent;
        } catch(IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UiComponent parseUiComponentElement(Element rootElement) {
        try {
            String rootType = rootElement.getTagName().toLowerCase();
            Class<? extends UiComponent> componentClass;
            if (rootType.contains(".")) {
                componentClass = (Class<? extends UiComponent>) Class.forName(rootType);
            } else componentClass = config.getXmlComponentAssociations().get(rootType);
            UiComponent root = componentClass.newInstance();
            List<UiComponent> childrenList = (List<UiComponent>)componentChildrenField.get(root);

            if (rootElement.hasAttribute("id")) componentIdField.set(root, rootElement.getAttribute("id"));

            NamedNodeMap attributes = rootElement.getAttributes();
            int i = 0;
            while(i < attributes.getLength()) {
                Node n = attributes.item(i);

                if(n.getNodeType() == Node.ATTRIBUTE_NODE) {
                    String key = n.getNodeName();
                    String value = n.getNodeValue();
                    if(key.startsWith(xmlFieldPrefix)) {
                        key = key.substring(xmlFieldPrefix.length()+1);
                        try {
                            Field f = componentClass.getDeclaredField(key);
                            f.setAccessible(true);
                            setFieldValue(f, root, value);
                            f.setAccessible(false);
                        } catch(NoSuchFieldException e) {
                            try {
                                Field f = UiComponent.class.getDeclaredField(key);
                                f.setAccessible(true);
                                setFieldValue(f, root, value);
                                f.setAccessible(false);
                            } catch (NoSuchFieldException e2) {
                                Core.loggerInstance.logError("Encountered an error while loading an ui component, there is no such field with the name '" + key + "' in the class '" + rootElement.getTagName() + "'");
                            }
                        }
                    }
                }
                i++;
            }

            NodeList children = rootElement.getChildNodes();
            i = 0;
            while(i < children.getLength()) {
                Node n = children.item(i);
                if(n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)n;
                    childrenList.add(parseUiComponentElement(e));
                }
                i++;
            }

            return root;
        } catch(IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setFieldValue(Field f, Object instance, String value) throws IllegalAccessException {
        if(value.isEmpty()) return;
        if(f.getType() == int.class || f.getType() == Integer.class) {
            value = checkForIntegerReplacement(value);
            f.set(instance, new Integer(value));
        }
        if(f.getType() == double.class || f.getType() == Double.class) {
            f.set(instance, new Double(value));
        }
        if(f.getType() == float.class || f.getType() == Float.class) {
            f.set(instance, new Float(value));
        }
        if(f.getType() == long.class || f.getType() == Long.class) {
            f.set(instance, new Long(value));
        }
        if(f.getType() == short.class || f.getType() == Short.class) {
            f.set(instance, new Short(value));
        }
        if(f.getType() == boolean.class || f.getType() == Boolean.class) {
            boolean b = new Boolean(value);
            if(value.equalsIgnoreCase("1")) b = true;
            if(value.equalsIgnoreCase("0")) b = false;
            f.set(instance, b);
        }
        if(f.getType() == char.class || f.getType() == Character.class) {
            f.set(instance, value.charAt(0));
        }
        if(f.getType() == String.class) {
            f.set(instance, value);
        }
        if(f.getType() == UiResource.class) {
            f.set(instance, getResource(value));
        }
        if(f.getType() == File.class) {
            f.set(instance, new File(value));
        }
    }

    private String checkForIntegerReplacement(String value) {
        value = value.toLowerCase();
        for(String key : this.config.getXmlIntegerReplacementValues().keySet()) {
            value = value.replaceAll(key, this.config.getXmlIntegerReplacementValues().get(key) + "");
        }
        return value;
    }

}