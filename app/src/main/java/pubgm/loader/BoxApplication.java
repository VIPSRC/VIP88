package pubgm.loader;

import static pubgm.loader.server.ApiServer.ApiKeyBox;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.molihuan.utilcode.util.ToastUtils;
import pubgm.loader.activity.CrashHandler;
import pubgm.loader.utils.BuildCompat;
import pubgm.loader.utils.FLog;
import pubgm.loader.utils.FPrefs;
import pubgm.loader.utils.NetworkConnection;
import com.google.android.material.color.DynamicColors;
import com.topjohnwu.superuser.Shell;

import java.io.IOException;

import io.michaelrocks.paranoid.Obfuscate;

import net_62v.external.MetaActivationManager;

@Obfuscate
public class BoxApplication extends MultiDexApplication {
    public static final String STATUS_BY = "online";
    public static BoxApplication gApp;
    private boolean isNetworkConnected = false;

    public static BoxApplication get() {
        return gApp;
    }

    public boolean isInternetAvailable() {
        return isNetworkConnected;
    }

    public void setInternetAvailable(boolean b) {
        isNetworkConnected = b;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        FPrefs prefs = FPrefs.with(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gApp = this;
        try {
            MetaActivationManager.activateSdk((new Object() {
                int t;
   public String toString() {
      byte[] buf = new byte[20];
      t = 1366971217;
      buf[0] = (byte) (t >>> 22);
      t = 1441466506;
      buf[1] = (byte) (t >>> 22);
      t = 1106537234;
      buf[2] = (byte) (t >>> 5);
      t = -752295884;
      buf[3] = (byte) (t >>> 15);
      t = 648250771;
      buf[4] = (byte) (t >>> 4);
      t = 488727135;
      buf[5] = (byte) (t >>> 15);
      t = -1320147818;
      buf[6] = (byte) (t >>> 16);
      t = 1731244870;
      buf[7] = (byte) (t >>> 9);
      t = 1973243044;
      buf[8] = (byte) (t >>> 8);
      t = -1130191048;
      buf[9] = (byte) (t >>> 15);
      t = -257349039;
      buf[10] = (byte) (t >>> 7);
      t = 1383467863;
      buf[11] = (byte) (t >>> 22);
      t = -1140761827;
      buf[12] = (byte) (t >>> 10);
      t = 392121244;
      buf[13] = (byte) (t >>> 8);
      t = -383399748;
      buf[14] = (byte) (t >>> 15);
      t = -197360099;
      buf[15] = (byte) (t >>> 5);
      t = 1472521977;
      buf[16] = (byte) (t >>> 24);
      t = 1804571026;
      buf[17] = (byte) (t >>> 6);
      t = -848975162;
      buf[18] = (byte) (t >>> 3);
      t = 43514133;
      buf[19] = (byte) (t >>> 17);
      return new String(buf);  
                    }
                }.toString()));
        } catch (Exception e) {


            // Handle the exception, e.g., log an error message or take appropriate action
            e.printStackTrace(); // Print the stack trace for debugging
        }
        DynamicColors.applyToActivitiesIfAvailable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setCrashHandler();

        if (BuildCompat.isA11below()) {
            FLog.info("Android 11 below");
        } else {
            FLog.info("Android 12 above");
        }
        FLog.info("SDK INT: " + Build.VERSION.SDK_INT);
        FLog.info("SDK RELEASE: " + Build.VERSION.RELEASE);

        NetworkConnection.CheckInternet network = new NetworkConnection.CheckInternet(this);
        network.registerNetworkCallback();
    }


    /**
     * Setting the default uncaught exception handler that will handle all the uncaught exceptions.
     */
    public void setCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    public boolean checkRootAccess() {
        if (Shell.rootAccess()) {
            FLog.info("Root granted");
            return true;
        } else {
            FLog.info("Root not granted");
            return false;
        }
    }

    public void doExe(String shell) {
        if (checkRootAccess()) {
            Shell.su(shell).exec();
        } else {
            try {
                Runtime.getRuntime().exec(shell);
                FLog.info("Shell: " + shell);
            } catch (IOException e) {
                FLog.error(e.getMessage());
            }
        }
    }

    public void doExecute(String shell) {
        doChmod(shell, 777);
        doExe(shell);
    }

    public void doChmod(String shell, int mask) {
        doExe("chmod " + mask + " " + shell);
    }

    public void toast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastWithImage(int id, CharSequence msg) {
        ToastUtils _toast = ToastUtils.make();
        _toast.setBgColor(android.R.color.white);
        _toast.setLeftIcon(id);
        _toast.setTextColor(android.R.color.black);
        _toast.setNotUseSystemToast();
        _toast.show(msg);
    }
}
