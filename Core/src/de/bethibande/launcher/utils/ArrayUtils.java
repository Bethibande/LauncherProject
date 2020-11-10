package de.bethibande.launcher.utils;

import java.util.Arrays;

public class ArrayUtils {

    public static byte[] trim(byte[] bytes, int i, int i2) {
        return Arrays.copyOfRange(bytes, i , i2);
    }

    public static byte[] trim(byte[] bytes, int i) {
        return Arrays.copyOf(bytes, i );
    }

    public static <T> boolean contains(T[] array, T object) {
        for(T obj : array) {
            if(obj.toString().equalsIgnoreCase(object.toString())) return true;
        }
        return false;
    }

    public static byte[] join(final byte[] array1, byte[] array2) {
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
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
