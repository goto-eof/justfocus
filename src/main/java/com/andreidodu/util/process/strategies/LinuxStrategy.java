package com.andreidodu.util.process.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxStrategy implements Strategy {

    private static final boolean IS_GNOME_DESKTOP = System.getProperty("os.name").equalsIgnoreCase("linux") && isGnomeInstalled();

    @Override
    public boolean accept() {
        return IS_GNOME_DESKTOP;
    }

    @Override
    public void execute(boolean isEnabled) {
        try {
            System.out.println("Focus mode: " + isEnabled);
            ProcessBuilder pb = new ProcessBuilder("gsettings", "set", "org.gnome.desktop.notifications", "show-banners", !isEnabled ? "true" : "false");
            pb.redirectErrorStream(true);

            Process p = pb.start();
            System.out.println("Executing....");

            StringBuilder resultString = new StringBuilder();

            String line;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));) {
                while ((line = reader.readLine()) != null) {
                    resultString.append(line);
                }
            }

            int exitCode = p.waitFor();
            System.out.println("ExitCode: " + exitCode);


            if (exitCode == 0) {
                System.out.println("enableFocusMode executed successfully: " + resultString);
                return;
            }

            System.err.println("Unable to enableFocusMode (Codice: " + exitCode + "): " + resultString);
        } catch (IOException | InterruptedException e) {
            System.err.println("Unable to enableFocusMode (Code: " + e.getMessage());
        }
    }

    private static boolean isGnomeInstalled() {
        try {
            ProcessBuilder pb = new ProcessBuilder("gsettings", "list-schemas");
            pb.redirectErrorStream(true);

            Process p = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));) {
                while (reader.readLine() != null) { /*do nothing, we are just consuming the stream to preved process deadlock*/ }
            }
            int exitCode = p.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }


}
