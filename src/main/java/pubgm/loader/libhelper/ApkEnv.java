package pubgm.loader.libhelper;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.blankj.molihuan.utilcode.util.FileUtils;
import net_62v.external.MetaApplicationInstaller;
import net_62v.external.MetaActivityManager;
import net_62v.external.MetaPackageManager;
import net_62v.external.MetaStorageManager;

import pubgm.loader.BoxApplication;
import pubgm.loader.R;
import pubgm.loader.utils.FLog;

import java.io.File;

public class ApkEnv {
    File obbContaine;

    private static ApkEnv singleton;

    public static ApkEnv getInstance() {
        if (singleton == null) {
            singleton = new ApkEnv();
        }
        return singleton;
    }

    public ApplicationInfo getApplicationInfo(String packageName) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = BoxApplication.get().getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException err) {
            FLog.error(err.getMessage());
            BoxApplication.get().showToastWithImage(R.drawable.ic_error, err.getMessage());
            return null;
        }

        return applicationInfo;
    }

    public ApplicationInfo getApplicationInfoContainer(String packageName) {
        if (!isInstalled(packageName)) {
            BoxApplication.get().showToastWithImage(R.drawable.ic_error, "App not install, install first");
            return null;
        }

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = MetaPackageManager.getApplicationInfo(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (applicationInfo == null) {
            return null;
        }
        return applicationInfo;
    }

    public boolean isInstalled(String packageName) {
        try {
            return MetaPackageManager.isInnerAppInstalled(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning(String packageName) {
        try {
            return MetaActivityManager.isAppRunning(packageName, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean installByFile(String packageName) {
        ApplicationInfo applicationInfo = getApplicationInfo(packageName);
        if (applicationInfo == null) {
            return false;
        }
        try {
            MetaApplicationInstaller.installAppByPath(applicationInfo.sourceDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean installByPackage(String packageName) {
        try {
            MetaApplicationInstaller.cloneApp(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void unInstallApp(String packageName) {
        try {
            MetaPackageManager.uninstallAppFully(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopRunningApp(String packageName) {
        try {
            MetaActivityManager.killAppByPkg(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getObbContainerPath(String packageName) {
        try {
            return new File(MetaStorageManager.obtainAppExternalStorageDir(packageName) + "/Android/obb", packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean tryAddLoader(String packageName) {
        boolean is_online = BoxApplication.STATUS_BY.equals("online");

        ApplicationInfo applicationInfo = getApplicationInfoContainer(packageName);
        if (applicationInfo == null) {
            FLog.error("Error, Application Info");
            return false;
        }

        String target = "libpubgm.so";
        if (packageName.equals("com.pubg.imobile")) {
            target = "libbgmi.so";
        } else {
            target = "libpubgm.so";
        }

        File loader = new File(is_online ? BoxApplication.get().getFilesDir().toString() : BoxApplication.get().getApplicationInfo().nativeLibraryDir, target);
        File loaderDest = new File(applicationInfo.nativeLibraryDir, "libAkAudioVisiual.so");

        if (loaderDest.exists()) loaderDest.delete();
        try {
            if (FileUtils.copy(loader.toString(), loaderDest.toString())) {
                return true;
            }
        } catch(Exception err) {
            FLog.error(err.getMessage());
            return false;
        }
        return false;
    }
    public void launchApk(String packageName) {
        if (!isInstalled(packageName)) {
            BoxApplication.get().showToastWithImage(R.drawable.icon, "Client not installed");
            return;
        }
        try {
            MetaActivityManager.launchApp(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
