import os
import struct
import zlib

def buat_png_merah(path, ukuran):
    w, h = ukuran, ukuran
    def png_chunk(name, data):
        c = zlib.crc32(name + data) & 0xffffffff
        return struct.pack('>I', len(data)) + name + data + struct.pack('>I', c)
    
    raw = b''
    for y in range(h):
        raw += b'\x00'
        for x in range(w):
            raw += b'\x4CAF50\xff'[:4]  
            raw = raw[:-4] + bytes([76, 175, 80, 255])
    
    compressed = zlib.compress(raw)
    png = b'\x89PNG\r\n\x1a\n'
    png += png_chunk(b'IHDR', struct.pack('>IIBBBBB', w, h, 8, 2, 0, 0, 0))
    png += png_chunk(b'IDAT', compressed)
    png += png_chunk(b'IEND', b'')
    
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'wb') as f:
        f.write(png)
    print(f"Dibuat: {path}")

sizes = {
    'mipmap-mdpi': 48,
    'mipmap-hdpi': 72,
    'mipmap-xhdpi': 96,
    'mipmap-xxhdpi': 144,
    'mipmap-xxxhdpi': 192,
}

for folder, size in sizes.items():
    base = f'app/src/main/res/{folder}'
    buat_png_merah(f'{base}/ic_launcher.png', size)
    buat_png_merah(f'{base}/ic_launcher_round.png', size)

print("Semua icon selesai dibuat!")
