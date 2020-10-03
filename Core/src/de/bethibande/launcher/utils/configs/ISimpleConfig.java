package de.bethibande.launcher.utils.configs;

import java.io.File;

public interface ISimpleConfig {

    // a simple config, may be replaced with a more advanced config approach later

    // key/path may not contain an ':' only the value is allowed to in case it is a String, the ':' is used to split the key from the value -> message.shutdown: test:test
    void set(String key, Object value);
    Object get(String key);

    void load(File f);
    void save();
    // returns null if no file has been loaded using load(File f);
    File getLoadedFile();

}
