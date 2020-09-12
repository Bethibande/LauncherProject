package de.bethibande.marketplace.moduleloader;

import java.net.URL;
import java.net.URLClassLoader;

public class StaticClassloader extends URLClassLoader {

    public StaticClassloader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
