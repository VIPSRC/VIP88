package pubgm.loader.utils;

import android.os.Build;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class BuildCompat {
    
    public static boolean isA11below() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            return true;
        }
        return false;
    }
    
    public static boolean isA12above() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return true;
        }
        return false;
    }
    
    public static boolean atLeastTiramisu() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    public static boolean atLeastR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }
}
