# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.itsarisu.erroldec.** { *; }
-keep class com.fvbox.lib.** { *; }
-keep class com.fvbox.mirror.** { *; }
-keep class  net_62v.external.** { *; }

-optimizations

-keepattributes Signature

-keep class com.pubgm.floating.FloatService {
    public <methods>;
}

-keep class com.itsarisu.erroldec.service.** {*;}
-keep public class com.pubgm.floating.FloatService


-keep class io.grpc.** {*;}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn javax.annotation.Nullable
-dontwarn com.squareup.okhttp.CipherSuite
-dontwarn com.squareup.okhttp.ConnectionSpec
-dontwarn com.squareup.okhttp.TlsVersion
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.lang.model.element.Modifier
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder

-dontwarn com.hjq.permissions.**
-dontwarn top.niunaijun.android_mirror.**
-dontwarn top.canyie.pine.**
-dontwarn top.canyie.pine.xposed.**
-dontwarn top.canyie.dreamland.**
-dontwarn top.niunaijun.blackbox.**

-keep class com.hjq.permissions.** {
    public <methods>;
}

-keep class top.niunaijun.android_mirror.** {
    public <methods>;
}

-keep class top.canyie.pine.** {
    public <methods>;
}

-keep class top.canyie.pine.xposed.** {
    public <methods>;
}

-keep class top.canyie.dreamland.** {
    public <methods>;
}

-keep class top.niunaijun.blackbox.** {
    public <methods>;
}

-keep class ** {
    public <methods>;
}

-keep class * {
    public <methods>;
}

-keep class com.hjq.permissions.** {*;}
-keep class top.niunaijun.android_mirror.** {*;}
-keep class top.canyie.pine.** {*;}
-keep class top.canyie.pine.xposed.** {*;}
-keep class top.canyie.dreamland.** {*;}
-keep class top.niunaijun.blackbox.** {*;}

