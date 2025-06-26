package pubgm.loader.utils;

import android.util.Log;
import io.michaelrocks.paranoid.Obfuscate;
import pubgm.loader.BuildConfig;

@Obfuscate
public class FLog {
    public static final String TAG = FLog.class.getSimpleName();
    
    public static void debug(String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.d(TAG, msg);
    }
    
    public static void info(String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.i(TAG, msg);
    }
    
    public static void warning(String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.w(TAG, msg);
    }
    
    public static void error(String msg) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.e(TAG, msg);
    }
}
