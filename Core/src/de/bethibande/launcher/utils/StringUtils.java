package de.bethibande.launcher.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {

    public static String toMD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; ++i) {
                sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
