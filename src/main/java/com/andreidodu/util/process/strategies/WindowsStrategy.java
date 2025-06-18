package com.andreidodu.util.process.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsStrategy implements Strategy {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").equalsIgnoreCase("windows");


    @Override
    public boolean accept() {
        return IS_WINDOWS;
    }

    @Override
    public void execute(boolean isEnabled) {
        String value = isEnabled ? "0" : "1";
        // Reg key valid for Windows 10 and 11 -> TODO try to understand how to manage other Windows versions
        String[] command = {"cmd.exe", "/c", "reg", "add", "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Notifications\\Settings", "/v", "NOC_GLOBAL_SETTING_TOASTS_ENABLED", "/t", "REG_DWORD", "/d", value, "/f"};

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);

            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = p.waitFor();

            if (exitCode == 0) {
                System.out.println("Execution completed successfully:\n" + output);
            } else {
                System.err.println("Execution failed. Exit code: " + exitCode + ". Output:\n" + output);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
