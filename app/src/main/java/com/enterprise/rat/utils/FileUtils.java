package com.enterprise.rat.utils;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtils {
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) destFile.createNewFile();
        try (FileChannel source = new FileInputStream(sourceFile).getChannel();
             FileChannel destination = new FileOutputStream(destFile).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
