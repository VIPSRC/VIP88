package pubgm.loader.server;

import pubgm.loader.utils.FLog;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class ApiServer {

    public static ApiServer AppChecker;

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    public static native String mainURL();
    public static native String getOwner();
    public static native String getTelegram();
   public static native String activity();
    public static native String ApiKeyBox();
    public static native String EXP();
    public static native String URLJSON();
    private static native boolean nativeVerifyAppLabel(String appLabel);




    public static boolean verifyAppLabel(Activity activity) {
        try {
            ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), 0);
            String appLabel = (String) activity.getPackageManager().getApplicationLabel(appInfo);
            return nativeVerifyAppLabel(appLabel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}

