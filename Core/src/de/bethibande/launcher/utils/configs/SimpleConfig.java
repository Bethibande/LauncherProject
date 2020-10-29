package de.bethibande.launcher.utils.configs;

import de.bethibande.launcher.Core;
import lombok.Getter;

import java.io.*;
import java.util.HashMap;

public class SimpleConfig implements ISimpleConfig, Serializable {

    // this implementation is only capable of saving and returning Strings
    HashMap<String, String> values = new HashMap<>();
    @Getter
    private File configFile = null;

    @Override
    public void set(String key, Object value) {
        if(values == null) values = new HashMap<>();
        values.remove(key);
        values.put(key, value.toString());
    }

    @Override
    public String get(String key) {
        if(values == null) values = new HashMap<>();
        return values.get(key);
    }

    @Override
    public void load(File f) {
        if(this.configFile == null) {
            try {
                this.configFile = f;
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String buffer;
                while ((buffer = reader.readLine()) != null) {
                    String key = buffer.split(":")[0];
                    String value = buffer.substring(key.length() + 2);
                    values.put(key, value);
                }
            } catch (IOException e) {
                Core.loggerInstance.logError("Error while loading config.");
                e.printStackTrace();
            }
        } else Core.loggerInstance.logError("This config has already been loaded from a file: " + configFile);
    }

    @Override
    public void save() {
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(this.configFile));
            for(String key : values.keySet()) {
                String value = values.get(key);
                writer.println(key + ": " + value);
            }
            writer.flush();
        } catch(IOException e) {
            Core.loggerInstance.logError("Error while saving config.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSet(String key) {
        if(this.values == null) return false;
        return values.containsKey(key);
    }

    @Override
    public File getLoadedFile() {
        return this.configFile;
    }
}
