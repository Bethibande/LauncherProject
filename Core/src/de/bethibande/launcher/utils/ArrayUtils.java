package de.bethibande.launcher.utils;

import java.util.Arrays;

public class ArrayUtils {

    public static byte[] trim(byte[] bytes, int i) {
        return Arrays.copyOf(bytes, i );
    }

    public static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }

    public static void printByteArray(String pre, byte[] array) {
        StringBuilder sb = new StringBuilder();
        for(byte b : array) {
            sb.append(" " + b);
        }
        System.out.println(pre + sb);
    }

}
