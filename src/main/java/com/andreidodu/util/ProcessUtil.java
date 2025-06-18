package com.andreidodu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ProcessUtil {
    public static void enableFocusMode(boolean isEnabled) {
        try {
            System.out.println("Focus mode: " + isEnabled);
            ProcessBuilder pb = new ProcessBuilder("gsettings", "set", "org.gnome.desktop.notifications", "show-banners", !isEnabled ? "true" : "false");
            Process p = pb.start();
            System.out.println("Executing....");
            int exitCode = p.waitFor();
            System.out.println("ExitCode: " + exitCode);
            if (exitCode == 0) {
                System.out.println("enableFocusMode executed successfully");
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                StringBuilder errorOutput = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                System.err.println("Unable to enableFocusMode (Codice: " + exitCode + "): " + errorOutput.toString().trim());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
