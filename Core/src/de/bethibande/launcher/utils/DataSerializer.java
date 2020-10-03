package de.bethibande.launcher.utils;

import java.io.*;
import java.util.Base64;

public class DataSerializer {

    public static Object deserialize(String serialized) {
        try {
            byte[] buffer = Base64.getDecoder().decode(serialized);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
            //return new String(baos.toByteArray());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
