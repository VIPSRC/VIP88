package pubgm.loader.utils;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
//import com.airbnb.lottie.LottieAnimationView;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import pubgm.loader.BuildConfig;
import pubgm.loader.activity.CrashHandler;
import pubgm.loader.activity.MainActivity;
import pubgm.loader.libhelper.FileHelper;
import pubgm.loader.libhelper.Loader;
//import com.craiyon.loader.overlay.MenuFloatingView;
import android.app.ProgressDialog;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import pubgm.loader.R;

import org.jdeferred.android.AndroidDeferredManager;

import pubgm.loader.libhelper.ApkEnv;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class ActivityCompat extends AppCompatActivity {
    private static ActivityCompat activityCompat;
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    public static int PERMISSION_REQUEST_STORAGE = 100;
    public static int REQUEST_MANAGE_UNKNOWN_APP_SOURCES = 200;
    public boolean isLogin = false;
    public FPrefs prefs;
    private BottomSheetDialog bottomSheetDialog;
    public static String gamename;
    public static String name;
    public static int version;
    public static String url;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
   
    public static ActivityCompat getActivityCompat() {
        return activityCompat;
    }

    protected static void requestPermissions(MainActivity mainActivity, String[] strings, int requestPermissions) {
    }

    public FPrefs getPref() {
        return FPrefs.with(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityCompat = this;
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        setNavBar(R.color.background);

        prefs = getPref();

        ManageFiles();
    }
    
    public void setNavBar(int color){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,color));
    }
    
    public void restartApp(String clazz) {
        Intent lauchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        lauchIntent.addFlags(335577088);
        lauchIntent.putExtra("restartApp", clazz);
        startActivity(lauchIntent);
        Runtime.getRuntime().exit(0);
    }
    
    public void toast(CharSequence msg) {
        ToastUtils _toast = ToastUtils.make();
        _toast.setBgColor(android.R.color.white);
        _toast.setLeftIcon(R.drawable.icon);
        //_toast.setGravity(Gravity.BOTTOM, Gravity.CENTER, Gravity.CENTER);
        _toast.setTextColor(android.R.color.black);
        _toast.setNotUseSystemToast();
        //_toast.setBgResource(R.drawable.button_coming);
        _toast.show(msg);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    
    public static void toastImage(int id, CharSequence msg) {
        ToastUtils _toast = ToastUtils.make();
        _toast.setBgColor(android.R.color.white);
        _toast.setLeftIcon(id);
        _toast.setTextColor(android.R.color.black);
        _toast.setNotUseSystemToast();
        _toast.show(msg);
    }
    
    public void RestartAppp() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void ShowRestartApp() {
        showBottomSheetDialog(
                getResources().getDrawable(R.drawable.ic_check),
                "Download Success: Restart Loader",
                "The loader has been downloaded successfully. Please restart the loader now.",
                false,
                v -> {
                    MainActivity.get().doShowProgress(true);
                    RestartAppp();
                    dismissBottomSheetDialog();
                },
                null);
    }
    
    public void takeFilePermissions() {
        new MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.file_access_title)
            .setMessage(R.string.file_access_message)
            .setPositiveButton(
                R.string.grant_permission,
                (d, w) -> {
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                  } else {
                    androidx.core.app.ActivityCompat.requestPermissions(
                        this,
                        new String[] {
                          Manifest.permission.READ_EXTERNAL_STORAGE,
                          Manifest.permission.MANAGE_EXTERNAL_STORAGE
                        },
                        1);
                  }
                })
            .setNegativeButton(
                R.string.exit,
                (d, w) -> {
                  finish();
                  System.exit(0);
                })
            .show();
    }
    
    public boolean isPermissionGaranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          return Environment.isExternalStorageManager();
        } else {
          return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    
    public void InstllUnknownApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setMessage("Please allow Install Unknown App Source");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_MANAGE_UNKNOWN_APP_SOURCES);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else {
                if (!isPermissionGaranted()) {
                    takeFilePermissions();
                }
            }
        }
    }
    
    public void OverlayPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setMessage("Please allow permision floating");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else {
                InstllUnknownApp();
            }
        }
    }
    
    public void ManageFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_STORAGE);
            } else {
                OverlayPermision();
            }
        }
    }
    
    protected AndroidDeferredManager defer() {
        return UiKit.defer();
    }
    
    private long backPressedTime = 0; 
    
    @Override
    public void onBackPressed() {
        if (isLogin) {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {    // 2 secs
                backPressedTime = t;
                toast("Press back again to exit");
            } else {
                super.onBackPressed();
            }
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }
    
    private void hideSystemUI() {
      
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    
    
    
    public void launch(AlertDialog dialog, String pkg) {
        UiKit.defer().when(() -> {
            long startTime = System.currentTimeMillis();
            dialog.dismiss();
            long elapsedTime = System.currentTimeMillis() - startTime;
            long delta = 500L - elapsedTime;
            if (delta > 0) {
                UiKit.sleep(delta);
            }
        }).done((ree) -> {
            ApkEnv.getInstance().launchApk(pkg);
        });
    }

    public void launchSplash(String pkg) {
        try {
            View view = getLayoutInflater().inflate(R.layout.launcher, null);
            CardView cv = view.findViewById(R.id.cv_lauch);
            TextView txt = view.findViewById(R.id.start_client);
            

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setCancelable(false)
                   .setView(view)
                   .setBackground(this.getResources().getDrawable(R.drawable.background_trans));

            AlertDialog dialog = builder.create();
            dialog.show();

            defer().when(() -> {
                long startTime = System.currentTimeMillis();
              //  doActionAnimation(txt, pkg);
                long elapsedTime = System.currentTimeMillis() - startTime;
                long delta = 5000L - elapsedTime;
                if (delta > 0) {
                    UiKit.sleep(delta);
                }
            }).done((ree) -> launch(dialog, pkg)).fail(fa -> dialog.dismiss());

        } catch(Exception err) {
            FLog.error(err.getCause().getMessage());
        }
    }



    public void showBottomSheetDialog(Drawable icon, String title, String msg, boolean cancelable, View.OnClickListener listener, View.OnClickListener listenerCancle) {
        if (BuildConfig.VERSION_CODE == 200) {
            return;
        }
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(cancelable);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        
        ImageView img = bottomSheetDialog.findViewById(R.id.icon);
        if (icon != null) {
            img.setImageDrawable(icon);
        }
        TextView title_tv = bottomSheetDialog.findViewById(R.id.title);
        title_tv.setText(title);
        TextView msg_tv = bottomSheetDialog.findViewById(R.id.msg);
        msg_tv.setText(msg);
        
        MaterialButton download = bottomSheetDialog.findViewById(R.id.btn);
        if (listener != null) {
            download.setOnClickListener(listener);
        }
        
        MaterialButton cancle = bottomSheetDialog.findViewById(R.id.btn_cancle);
        if (listenerCancle != null) {
            cancle.setOnClickListener(listenerCancle);
        } else {
            cancle.setVisibility(View.GONE);
        }
        
        bottomSheetDialog.show();
    }

    public void showBottomSheetDialog2(Drawable icon, String title, String msg, boolean cancelable, View.OnClickListener listener,View.OnClickListener updates, View.OnClickListener listenerCancle) {
        if (BuildConfig.VERSION_CODE == 200) {
            return;
        }
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(cancelable);
        bottomSheetDialog.setContentView(R.layout.bottom_update);

        ImageView img = bottomSheetDialog.findViewById(R.id.icon);
        if (icon != null) {
            img.setImageDrawable(icon);
        }
        TextView title_tv = bottomSheetDialog.findViewById(R.id.title);
        title_tv.setText(title);
        TextView msg_tv = bottomSheetDialog.findViewById(R.id.msg);
        msg_tv.setText(msg);

        MaterialButton download = bottomSheetDialog.findViewById(R.id.btn);
        if (listener != null) {
            download.setOnClickListener(listener);
        }

        MaterialButton updatess = bottomSheetDialog.findViewById(R.id.updateee);
        if (updates != null) {
            updatess.setOnClickListener(updates);
        }


        MaterialButton cancle = bottomSheetDialog.findViewById(R.id.btn_cancle);
        if (listenerCancle != null) {
            cancle.setOnClickListener(listenerCancle);
        } else {
            cancle.setVisibility(View.GONE);
        }

        bottomSheetDialog.show();
    }

    public void showBottomSheetDialog3(Drawable icon, String title, String msg, boolean cancelable, View.OnClickListener listener,View.OnClickListener updates, View.OnClickListener listenerCancle) {
        if (BuildConfig.VERSION_CODE == 200) {
            return;
        }
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(cancelable);
        bottomSheetDialog.setContentView(R.layout.bottom_logout);

        ImageView img = bottomSheetDialog.findViewById(R.id.icon);
        if (icon != null) {
            img.setImageDrawable(icon);
        }
        TextView title_tv = bottomSheetDialog.findViewById(R.id.title);
        title_tv.setText(title);
        TextView msg_tv = bottomSheetDialog.findViewById(R.id.msg);
        msg_tv.setText(msg);


        MaterialButton updatess = bottomSheetDialog.findViewById(R.id.updateee);
        if (updates != null) {
            updatess.setOnClickListener(updates);
        }


        MaterialButton cancle = bottomSheetDialog.findViewById(R.id.btn_cancle);
        if (listenerCancle != null) {
            cancle.setOnClickListener(listenerCancle);
        } else {
            cancle.setVisibility(View.GONE);
        }

        bottomSheetDialog.show();
    }

    public void dismissBottomSheetDialog() {
        try {
        	if (bottomSheetDialog.isShowing()) {
                bottomSheetDialog.dismiss();
                bottomSheetDialog = null;
            }
        } catch(Exception err) {
        	FLog.error(err.getMessage());
        }
    }
    
}
