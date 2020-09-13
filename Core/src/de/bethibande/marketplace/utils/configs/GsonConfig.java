package de.bethibande.marketplace.utils.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bethibande.marketplace.Core;

import java.io.*;
import java.util.HashMap;

public class GsonConfig implements ISimpleConfig, Serializable {

    private HashMap<String, Object> values = new HashMap<>();
    private File configFile;

    @Override
    public void set(String key, Object value) {
        if(values == null) values = new HashMap<>();
        values.remove(key);
        values.put(key, value);
    }

    @Override
    public Object get(String key) {
        return values.get(key);
    }

    @Override
    public void load(File f) {
        if(configFile == null) {
            configFile = f;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                StringBuilder sb = new StringBuilder();
                String buffer;
                while((buffer = reader.readLine()) != null) {
                    sb.append(buffer.replaceAll("\t", ""));
                }
                Gson g = new Gson();
                this.values = g.fromJson(sb.toString(), HashMap.class);
            } catch(IOException e) {
                Core.loggerInstance.logError("Error while loading config.");
                e.printStackTrace();
            }
        } else Core.loggerInstance.logError("This config has already been loaded from a file: " + configFile);
    }

    @Override
    public void save() {
        GsonBuilder gb = new GsonBuilder();
        gb.setPrettyPrinting();
        Gson g = gb.create();
        String json = g.toJson(values);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(this.configFile));
            writer.println(json);
            writer.flush();
        } catch(IOException e) {
            Core.loggerInstance.logError("Error while saving config.");
            e.printStackTrace();
        }
    }

    @Override
    public File getLoadedFile() {
        return this.configFile;
    }
}
