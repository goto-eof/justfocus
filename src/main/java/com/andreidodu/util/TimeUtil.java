package com.andreidodu.util;

public class TimeUtil {

    public static String toMinuteSeconds(long time) {

        long totalSeconds = time / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

}
