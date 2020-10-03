package de.bethibande.launcher.utils;

import java.util.Calendar;

public class TimeUtils {

    // 1000 nanoseconds = 1 microsecond
    // 1000 microseconds = 1 millisecond
    // 1000 milliseconds = 1 second
    // 1 second = 1000000000 nanoseconds

    public static long getTimeInMillis() { return Calendar.getInstance().getTimeInMillis(); }

    public static long getTimeInNano() { return System.nanoTime(); }

}
