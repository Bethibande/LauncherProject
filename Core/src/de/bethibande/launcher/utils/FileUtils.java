package de.bethibande.launcher.utils;

import de.bethibande.launcher.Core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FileUtils {

    public static void createFile(File f) {
        try {
            f.createNewFile();
        } catch(IOException e) {
            Core.loggerInstance.logError("Error while creating file: " + f);
            e.printStackTrace();
        }
    }

    public static List<String> readFile(File f) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            List<String> content = new ArrayList<>();
            String buffer;
            while((buffer = reader.readLine()) != null) {
                content.add(buffer);
            }
            return content;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(File f, String[] content) {
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(f));
            for(String s : content) {
                writer.println(s);
            }
            writer.flush();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
