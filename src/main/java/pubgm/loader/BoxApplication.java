package pubgm.loader;

import static pubgm.loader.server.ApiServer.ApiKeyBox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.molihuan.utilcode.util.ToastUtils;
import pubgm.loader.activity.CrashHandler;
import pubgm.loader.utils.FLog;
import pubgm.loader.utils.FPrefs;
import pubgm.loader.utils.NetworkConnection;
import com.google.android.material.color.DynamicColors;
import com.topjohnwu.superuser.Shell;

import java.io.IOException;

import io.michaelrocks.paranoid.Obfuscate;

import net_62v.external.MetaActivationManager;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : JungliCheats
 * *************************/

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



        DynamicColors.applyToActivitiesIfAvailable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setCrashHandler();

        NetworkConnection.CheckInternet network = new NetworkConnection.CheckInternet(this);
        network.registerNetworkCallback();
    }

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

    @SuppressLint("ResourceAsColor")
    public void showToastWithImage(int id, CharSequence msg) {
        ToastUtils _toast = ToastUtils.make();
        _toast.setBgColor(android.R.color.white);
        _toast.setLeftIcon(id);
        _toast.setTextColor(android.R.color.black);
        _toast.setNotUseSystemToast();
        _toast.show(msg);
    }
}
