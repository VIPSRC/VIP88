package pubgm.loader.activity;

import static pubgm.loader.Config.GAME_LIST_ICON;
import static pubgm.loader.R.id.remaining_time;
import static pubgm.loader.activity.LoginActivity.USERKEY;
import static pubgm.loader.activity.SplashActivity.mahyong;
import static pubgm.loader.server.ApiServer.EXP;
import static pubgm.loader.server.ApiServer.URLJSON;
import static pubgm.loader.server.ApiServer.mainURL;
import static pubgm.loader.utils.FLog.TAG;
import static com.topjohnwu.superuser.internal.UiThreadHandler.handler;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.molihuan.utilcode.util.SnackbarUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import pubgm.loader.Component.DownC;
import pubgm.loader.R;
import pubgm.loader.adapter.AppFilePathHelper;
import pubgm.loader.adapter.RecyclerViewAdapter;
import pubgm.loader.floating.FloatingService;
import pubgm.loader.floating.Overlay;
import pubgm.loader.floating.ToggleAim;
import pubgm.loader.floating.ToggleBullet;
import pubgm.loader.floating.ToggleSimulation;
import pubgm.loader.libhelper.ApkEnv;
import pubgm.loader.server.ApiServer;
import pubgm.loader.utils.ActivityCompat;
import pubgm.loader.utils.FLog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.topjohnwu.superuser.Shell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.michaelrocks.paranoid.Obfuscate;
import net_62v.external.MetaActivationManager;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : JungliCheats
 * *************************/

@Obfuscate
public class MainActivity extends ActivityCompat {

    private static final int BUFFER_SIZE = 0;
    public static String socket;
    public static String daemonPath;
    public static boolean fixinstallint = false;
    public static boolean check = false;
    public static int hiderecord = 0;
    public static int skin = 0;
    static MainActivity instance;

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    private PowerSpinnerView powerSpinnerView;
    private MaterialButton installKernelButton;
    private static final int REQUEST_PERMISSIONS = 1;
    String[] packageapp = {"com.tencent.ig", "com.pubg.krmobile", "com.vng.pubgmobile", "com.rekoo.pubgm","com.pubg.imobile"};
    public String nameGame = "PROTECTION GLOBAL";
    public String CURRENT_PACKAGE = "";
    public LinearProgressIndicator progres;
    public CardView enable, disable;
    public static int gameint = 1;
    public static int bitversi = 64;
    public static boolean noroot = false;
    public static int device = 1;
    public static String game = "com.tencent.ig";
    TextView root;
    public static int checkesp;
    public static boolean kernel = false;
    public static boolean Ischeck = false;
    public LinearLayout container;
    public static String modeselect;
    Context ctx;
    public static MainActivity get() {
        return instance;
    }
    private AppCompatSpinner spinnerLanguage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        init();
        initMenu1();


if(!Shell.rootAccess()) {
    boolean isActivated = MetaActivationManager.getActivationStatus();

    if (isActivated) {
        Toast.makeText(getApplicationContext(), "SDK Activation Successful", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(getApplicationContext(), "SDK Activation Failed !", Toast.LENGTH_SHORT).show();
    }
}
        initMenu2();
        Loadssets();
        devicecheck();
        loadAssets("socu64 UNSAFE");
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        if (!ApiServer.AppChecker.verifyAppLabel(this)) {
            finish();
        }

        instance = this;
        isLogin = true;

    }
    public void devicecheck(){
        root = findViewById(R.id.textroot);
        container = findViewById(R.id.container);
        LinearLayout menuroot = findViewById(R.id.menuantiban);

        if (Shell.rootAccess()){
            FLog.info("Root granted");
            modeselect = "ROOT -" + "ANDROID " + Build.VERSION.RELEASE;
            root.setText(getString(R.string.root) );
            container.setVisibility(View.GONE);
            menuroot.setVisibility(View.VISIBLE);
            Ischeck = true;
            noroot = true;
            device = 1;
        } else {
            FLog.info("Root not granted");
            modeselect = "CONTAINER -" + "ANDROID " + Build.VERSION.RELEASE;
            root.setText(getString(R.string.notooroot));
            doInitRecycler();
            container.setVisibility(View.VISIBLE);
            menuroot.setVisibility(View.GONE);
            Ischeck = false;
            device = 2;
        }
    }

    @SuppressLint("SetTextI18n")
    void initMenu1(){
        ImageView start = findViewById(R.id.starthack);
        ImageView stop =  findViewById(R.id.stophack);
        LinearLayout global =  findViewById(R.id.global);
        LinearLayout korea =  findViewById(R.id.korea);
        LinearLayout vietnam =  findViewById(R.id.vietnam);
        LinearLayout taiwan =  findViewById(R.id.taiwan);
        LinearLayout india =  findViewById(R.id.india);
        LinearLayout layoutprtc =  findViewById(R.id.layoutprtc);
        LinearLayout menuselectesp =  findViewById(R.id.menuselectesp);
        LinearLayout protection =  findViewById(R.id.protection);
        TextView textversions =  findViewById(R.id.textversions1);
        ImageView imgs1 =  findViewById(R.id.imgs1);
        RadioGroup modesp = findViewById(R.id.groupmode);
        RadioGroup versionesp = findViewById(R.id.groupesp);


        if (!Shell.rootAccess()){
            menuselectesp.setVisibility(View.GONE);
        }else{
            menuselectesp.setVisibility(View.VISIBLE);
        }
        protection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String packageName : packageapp) {
                    boolean isInstalled = isAppInstalled(MainActivity.get(), packageName);
                    if (!isInstalled) {
                    } else {
                        launchbypass();
//                        try {
//                            AppFilePathHelper.replaceLibWithRoot(getApplicationContext(), packageName, "libopenplatform.so", new Runnable() {
//                                @Override
//                                public void run() {
//                                    launchbypass();
//                                }
//                            });
//                        } catch (Exception e) {
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
            }
        });


        modesp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.system:
                        kernel = false;
                        checkesp = 1;
                        break;
                    case R.id.kernel:
                        kernel = true;
                        checkesp = 2;
                        break;
                }
            }
        });

        versionesp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.esp64:
                        bitversi = 64;
                        checkesp = 1;
                        break;
                    case R.id.esp32:
                        bitversi = 32;
                        checkesp = 2;
                        break;
                }
            }
        });




        start.setOnClickListener(v -> {
            toastImage(R.drawable.success_250px, getString(R.string.start_floating_success));
            startPatcher();

            start.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
        });

        stop.setOnClickListener(v -> {
            stopPatcher();
            toastImage(R.drawable.ic_error, getString(R.string.stop_floating_success));

            start.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
        });


        global.setOnClickListener(v -> {
            gameversion(global,korea,vietnam,taiwan,india);
            gameint = 1;
            game = packageapp[0];
            nameGame = getString(R.string.protection_global);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circlegl);
            toastImage(R.drawable.circlegl,getString(R.string.global_selected));
        });

        korea.setOnClickListener(v -> {
            gameversion(korea,global,vietnam,taiwan,india);
            gameint = 2;
            game = packageapp[1];
            nameGame = getString(R.string.protection_korea);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.krcircle);
            toastImage(R.drawable.krcircle,getString(R.string.korea_selected));
        });

        vietnam.setOnClickListener(v -> {
            gameversion(vietnam,korea,global,taiwan,india);
            gameint = 3;
            game = packageapp[2];
            nameGame = getString(R.string.protection_vietnam);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circlevn);
            toastImage(R.drawable.circlevn,getString(R.string.vietnam_selected));
        });

        taiwan.setOnClickListener(v -> {
            gameversion(taiwan,korea,vietnam,global,india);
            gameint = 4;
            game = packageapp[3];
            nameGame = getString(R.string.protection_taiwan);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circletw);
            toastImage(R.drawable.circletw,getString(R.string.taiwan_selected));
        });

        india.setOnClickListener(v -> {
            gameversion(india,korea,vietnam,taiwan,global);
            gameint = 5;
            game = packageapp[4];
            nameGame = getString(R.string.protection_india);
            textversions.setText(nameGame);
            imgs1.setBackgroundResource(R.drawable.circlebgmi);
            toastImage(R.drawable.circlebgmi,getString(R.string.india_selected));
        });


    }

    @SuppressLint("ResourceAsColor")
    void initMenu2(){

        // LinearLayout updatesresource = findViewById(R.id.updatesresource);
        LinearLayout layoutother = findViewById(R.id.layoutother);
        TextView key = (TextView) findViewById(R.id.user_key);
        TextView time = (TextView) findViewById(remaining_time);
        TextView brand = (TextView) findViewById(R.id.devices_brand);
        TextView os = (TextView) findViewById(R.id.os_version);
        TextView kernel = (TextView) findViewById(R.id.kernelversiontxt);

        String user_keys = USERKEY;
        if (user_keys.length() > 2) {
            user_keys = user_keys.substring(0, user_keys.length() - 4);
            user_keys += "****";
        }

        key.setText(user_keys);
        brand.setText(Build.BRAND);
        os.setText(Build.VERSION.RELEASE);
        String kernelVersion = System.getProperty("os.version");
        kernel.setText(kernelVersion);
        StartCountDown(time);


/*
        updatesresource.setOnClickListener(v -> {
            showBottomSheetDialog2(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.you_want_update_resource_to_latest_version), false, sv -> {
             //   new DownloadZip(this).execute("1", mainURL());
                dismissBottomSheetDialog();
            }, v1 -> {
               // checkAndDeleteFile(MainActivity.get());
                }, v2 ->{
                dismissBottomSheetDialog();
            });
        });*/

        //TODO : TWITTER
        findViewById(R.id.twitter).setOnClickListener(v -> {
                doShowProgress(true);
                addAdditionalApp(false, "com.twitter.android");


        });

        findViewById(R.id.twitter).setOnLongClickListener(v -> {
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                    ApkEnv.getInstance().unInstallApp("com.twitter.android");
                    dismissBottomSheetDialog();
                }, v1 -> {
                    dismissBottomSheetDialog();
                });

            return true;
        });

        //TODO : FACEBOOK
        findViewById(R.id.facebook).setOnClickListener(v -> {
                doShowProgress(true);
                addAdditionalApp(false, "mark.via.gp");

        });

        findViewById(R.id.facebook).setOnLongClickListener(v -> {
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                    ApkEnv.getInstance().unInstallApp("mark.via.gp");
                    dismissBottomSheetDialog();
                }, v1 -> {
                    dismissBottomSheetDialog();
                });

            return true;
        });

        findViewById(R.id.injector).setOnClickListener(v -> {
                doShowProgress(true);
                addAdditionalApp(false, "com.narcos.hax");

        });

        findViewById(R.id.injector).setOnLongClickListener(v -> {
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.want_remove_it), false, sv -> {
                    ApkEnv.getInstance().unInstallApp("com.narcos.hax");
                    dismissBottomSheetDialog();
                }, v1 -> {
                    dismissBottomSheetDialog();
                });

            return true;
        });

        final Switch hide_recorder = findViewById(R.id.hide_recorder);
        hide_recorder.setChecked(getPref().readBoolean("anti_recorder"));
        hide_recorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getPref().writeBoolean("anti_recorder", isChecked);
                SnackbarUtils.with(buttonView).setBgColor(R.color.background).setMessage("Restart App for the changes to take effect.").setMessageColor(Color.WHITE).setAction("Ok", v-> {
                    restartApp(MainActivity.class.getSimpleName());
                }).show();
            }
        });

//        findViewById(R.id.obbfix).setOnClickListener(v -> {
//            try {
//                Intent intent = new Intent(Intent.ACTION_DELETE);
//                intent.setData(Uri.parse("package:com.google.android.documentsui"));
//                this.startActivity(intent);
//            } catch (Exception e) {
//                FLog.error("Error uninstalling package: " + e.getMessage());
//            }
//        });
    }


    private Runnable runnable;
    Handler handler2 = new Handler();

    void StartCountDown(TextView time) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    long duration = Long.parseLong(EXP());
                    long now = Calendar.getInstance().getTimeInMillis();
                    long distance = duration - now;
                    long days = distance / (24 * 60 * 60 * 1000);
                    long hours = distance / (60 * 60 * 1000) % 24;
                    long minutes = distance / (60 * 1000) % 60;
                    long seconds = distance / 1000 % 60;

                    if (distance < 0) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.get(), "Upss error distance...", Toast.LENGTH_LONG).show();
                            handler2.removeCallbacks(runnable);
                            System.exit(0);
                        });
                    } else {
                        int textColor = Color.WHITE;
                        if (days == 0 && hours == 0 && minutes < 60) {
                            textColor = Color.YELLOW;
                        }

                        if (days == 0 && hours == 0 && minutes < 30) {
                            textColor = Color.RED;
                        }
                        //expires = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
                        runOnUiThread(() -> {
                            time.setText(String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds));
                        });
                    }
                } catch(Exception err) {

                }
            }
        }, 0);
        handler2.postAtTime(runnable, 6000);
    }



    void gameversion(LinearLayout a, LinearLayout b, LinearLayout c, LinearLayout d, LinearLayout e){
        a.setBackgroundResource(R.drawable.button_coming);
        b.setBackgroundResource(R.drawable.button_normal);
        c.setBackgroundResource(R.drawable.button_normal);
        d.setBackgroundResource(R.drawable.button_normal);
        e.setBackgroundResource(R.drawable.button_normal);
    }



    void init() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        LinearLayout menu1 = findViewById(R.id.imenu1);
        LinearLayout menu2 = findViewById(R.id.imenu2);

        // Set the initial state
        menu1.setVisibility(View.VISIBLE);
        menu2.setVisibility(View.GONE);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Get the item view to animate just the icon and text
            View itemView = bottomNavigationView.findViewById(item.getItemId());
            if (itemView != null) {
                animateNavItem(itemView);

            }

            int itemId = item.getItemId();

            if (itemId == R.id.navhome) {
                menu1.setVisibility(View.VISIBLE);
                menu2.setVisibility(View.GONE);
                return true;
            }
            else if (itemId == R.id.navsetting) {
                menu1.setVisibility(View.GONE);
                menu2.setVisibility(View.VISIBLE);
                return true;
            }


            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navhome);
    }

    // Alternative animation approach using AnimatorSet
    private void animateNavItem(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f);
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);

        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);
        scaleUpX.setDuration(100);
        scaleUpY.setDuration(100);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.play(scaleUpX).with(scaleUpY);

        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.play(scaleDown).before(scaleUp);
        fullAnimation.start();
    }



    ////////////////////////// Load Json ////////////////////////////////////////
    public void doInitRecycler() {
        doShowProgress(true);
        ArrayList<Integer> imageValues = new ArrayList<Integer>();
        ArrayList<String> titleValues = new ArrayList<String>();
        ArrayList<String> versionValues = new ArrayList<String>();
        ArrayList<String> statusValues = new ArrayList<String>();
        ArrayList<String> packageValues = new ArrayList<String>();
        try {
            String jsonLocation = loadJSONFromAsset("games.json");
            JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = jsonobject.getJSONArray("gamesList");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jb = (JSONObject) jarray.get(i);
                String title = jb.getString("title");
                String packageName = jb.getString("package");
                String version = jb.getString("version");
                String status = jb.getString("status");
                imageValues.add(GAME_LIST_ICON[i]);
                titleValues.add(title);
                versionValues.add(version);
                statusValues.add(status);
                packageValues.add(packageName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, imageValues, titleValues, versionValues, statusValues, packageValues);
        RecyclerView myView = (RecyclerView) findViewById(R.id.recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        myView.setLayoutManager(llm);
    }

    public void addAdditionalApp(boolean system, String packageName) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ApkEnv.getInstance().isInstalled(packageName)) {
                    doHideProgress();
                    ApkEnv.getInstance().launchApk(packageName);
                } else {
                    try {
                        if (ApkEnv.getInstance().installByPackage(packageName)) {
                            doHideProgress();
                            ApkEnv.getInstance().launchApk(packageName);
                        }
                    } catch (Exception err) {
                        FLog.error(err.getMessage());
                        doHideProgress();
                    }
                }
            }
        });
    }

    ////////////////////////// Other ////////////////////////////////////////
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void launchbypass(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(game);
        if (launchIntent != null) {
            startActivity(launchIntent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bitversi == 64 ){
                        if (gameint >= 1 && gameint <= 4) {
                            Exec("/ParrotCheat 9898", getString(R.string.bypass_launch_success));
                        } else if (gameint == 5) {
                            Exec("/ParrotCheat 9898", getString(R.string.bypass_launch_success));
                        }
                    }else if (bitversi == 32){
                        if (gameint >= 1 && gameint <= 4) {
                            Exec("/ParrotCheat 9832", getString(R.string.bypass_launch_success));
                        } else if (gameint == 5) {
                            Exec("/ParrotCheat 9832", getString(R.string.bypass_launch_success));
                        }
                    }

                }
            }, 3800);
        }else{
            toastImage(R.drawable.ic_error,game + getString(R.string.not_installed_please_check));
        }
    }

    public void Exec(String path, String toast) {
        try {
            ExecuteElf("su -c chmod 777 " + getFilesDir() + path);
            ExecuteElf("su -c " + getFilesDir() + path);
            ExecuteElf("chmod 777 " + getFilesDir() + path);
            ExecuteElf(  getFilesDir() + path);
            toastImage(R.drawable.ic_check, toast);
        } catch (Exception e) {
        }
    }

    private void ExecuteElf(String shell) {
        try {
            Runtime.getRuntime().exec(shell, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Loadssets() {
        MoveAssets(getFilesDir() + "/", "socs64");
        MoveAssets(getFilesDir() + "/", "socu64");
        MoveAssets(getFilesDir() + "/", "socs32");
        MoveAssets(getFilesDir() + "/", "socu32");
        MoveAssets(getFilesDir() + "/", "TW");
        MoveAssets(getFilesDir() + "/", "VNG");
        MoveAssets(getFilesDir() + "/", "ParrotCheat");
        MoveAssets(getFilesDir() + "/", "kernels64");
    }

    private boolean MoveAssets(String outPath, String fileName) {
        File file = new File(outPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("--Method--", "copyAssetsSingleFile: cannot create directory.");
                return false;
            }
        }
        try {
            InputStream inputStream = getAssets().open(fileName);
            File outFile = new File(file, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.get(), FloatingService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, getString(R.string.please_click_icon_logout_for_exit), Toast.LENGTH_SHORT).show();
    }

    public LinearProgressIndicator getProgresBar() {
        if (progres == null) {
            progres = findViewById(R.id.progress);
        }
        return progres;
    }

    public void doShowProgress(boolean indeterminate) {
        if (progres == null) {
            return;
        }
        progres.setVisibility(View.VISIBLE);
        progres.setIndeterminate(indeterminate);

        if (!indeterminate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                progres.setMin(0);
            }
            progres.setMax(100);
        }
    }

    public void doHideProgress() {
        if (progres == null) {
            return;
        }
        progres.setIndeterminate(true);
        progres.setVisibility(View.GONE);
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
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (FloatingService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startPatcher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.get())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 123);
            } else {
                startFloater();
            }
        }
    }

    private void startFloater() {
        if (!isServiceRunning()) {
            if (kernel){
                if(bitversi == 64) {
                    loadAssets("kernels64");
                }else if(bitversi == 32){
                    loadAssets("kernels32");
                }
            }else{
                if(bitversi == 64) {
                    loadAssets("socu64 UNSAFE");
                }else if(bitversi == 32){
                    loadAssets("socu32");
                }
            }
            String CMD =    "rm -rf  /data/data/" + game + "/files;\n" +
                    "touch  /data/data/" + game + "/files;\n";
            Shell.su(CMD).submit();
            startService(new Intent(MainActivity.get(), FloatingService.class));
        } else {
            toastImage(R.drawable.ic_error, getString(R.string.service_is_already_running));
        }
    }

    private void stopPatcher() {
        stopService(new Intent(MainActivity.get(), FloatingService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new

                Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));
    }

    public void loadAssets(String sockver) {
        daemonPath = MainActivity.this.getFilesDir().toString() + "/" + sockver;
        socket = daemonPath;
        try {
            Runtime.getRuntime().exec("chmod 777 " + daemonPath);
        } catch (IOException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CountTimerAccout();
        boolean needsRecreate = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("needs_recreate", false);
        if (needsRecreate) {
            getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("needs_recreate", false)
                    .apply();
        }
    }

    private void CountTimerAccout() {
        if (EXP().isEmpty()) {
            if (instance != null) {
                instance.finishAffinity();
            }
            return;
        }
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000); // Update every second

                    String xprValue = EXP();

                    // Parse the xpr() string into a Date object
                    long duration = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    try {
                        Date date = sdf.parse(xprValue);
                        if (date != null) {
                            duration = date.getTime(); // Convert to milliseconds
                        } else {
                            Log.e(TAG, "Failed to parse xpr value: " + xprValue);
                            return;
                        }
                    } catch (ParseException e) {
                        Log.e(TAG, "Invalid date format from xpr: " + xprValue, e);
                        return;
                    }

                    long now = android.icu.util.Calendar.getInstance().getTimeInMillis();
                    long remainingTime = duration - now;

                    if (remainingTime < 0) {
                        if (instance != null) {
                            instance.finishAffinity();
                        }
                        return;
                    }

                    long days = remainingTime / (24 * 60 * 60 * 1000);
                    long hours = (remainingTime / (60 * 60 * 1000)) % 24;
                    long minutes = (remainingTime / (60 * 1000)) % 60;
                    long seconds = (remainingTime / 1000) % 60;

                    updateCountdownUI(days, hours, minutes, seconds);
                } catch (Exception e) {
                    Log.e(TAG, "Error in countdown timer: ", e);
                }
            }
        });
    }

    private void updateCountdownUI(long days, long hours, long minutes, long seconds) {
        // Ensure you're working on the main thread to update the UI
        if (instance != null && !instance.isFinishing() && !instance.isDestroyed()) {
            // Get the TextViews for displaying the countdown
            TextView daysView = findViewById(R.id.days);
            TextView hoursView = findViewById(R.id.hours);
            TextView minutesView = findViewById(R.id.minutes);
            TextView secondsView = findViewById(R.id.second);

            // Update the days part
            if (daysView != null) {
                daysView.setText(String.format("%02d", days)); // Format to two digits (e.g., 02)
            }

            // Update the hours part
            if (hoursView != null) {
                hoursView.setText(String.format("%02d", hours)); // Format to two digits (e.g., 01)
            }

            // Update the minutes part
            if (minutesView != null) {
                minutesView.setText(String.format("%02d", minutes)); // Format to two digits (e.g., 01)
            }

            // Update the seconds part
            if (secondsView != null) {
                secondsView.setText(String.format("%02d", seconds)); // Format to two digits (e.g., 01)
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        }
    }
    private int getKernelIndex(String kernelVersion) {
        if (kernelVersion.contains("4.9.186")) return 0;
        if (kernelVersion.contains("4.14.117")) return 1;
        if (kernelVersion.contains("4.14.180")) return 2;
        if (kernelVersion.contains("4.14.186")) return 3;
        if (kernelVersion.contains("4.14.186b")) return 4;
        if (kernelVersion.contains("4.14.186c")) return 5;
        if (kernelVersion.contains("4.19.81")) return 6;
        if (kernelVersion.contains("4.19.113")) return 7;
        if (kernelVersion.contains("4.19.113c")) return 8;
        if (kernelVersion.contains("4.19.157")) return 9;
        if (kernelVersion.contains("4.19.157b")) return 10;
        if (kernelVersion.contains("4.19.157-安卓13")) return 11;
        if (kernelVersion.contains("4.19.191-安卓13")) return 12;
        if (kernelVersion.contains("5.4.210-安卓13")) return 13;
        if (kernelVersion.contains("5.15")) return 14;
        if (kernelVersion.contains("5.15b")) return 15;
        if (kernelVersion.contains("5.10")) return 16;
        if (kernelVersion.contains("5.10b")) return 17;
        if (kernelVersion.contains("5.10-安卓13-GooglePixel")) return 18;
        if (kernelVersion.contains("5.4.61~250")) return 19;
        if (kernelVersion.contains("5.4.86~250")) return 20;
        if (kernelVersion.contains("5.4.147~250")) return 21;
        return -1;
    }


    private void installKernel(int index) {
        String kernelFileName = "";
        switch (index) {
            case 0:
                kernelFileName = "4.9.186_fix.ko.sh";
                break;
            case 1:
                kernelFileName = "4.14.117.ko.sh";
                break;
            case 2:
                kernelFileName = "4.14.180.ko.sh";
                break;
            case 3:
                kernelFileName = "4.14.186.ko.sh";
                break;
            case 4:
                kernelFileName = "4.14.186b.ko.sh";
                break;
            case 5:
                kernelFileName = "4.14.186c.ko.sh";
                break;
            case 6:
                kernelFileName = "4.19.81.ko.sh";
                break;
            case 7:
                kernelFileName = "4.19.113.ko.sh";
                break;
            case 8:
                kernelFileName = "4.19.113c.ko.sh";
                break;
            case 9:
                kernelFileName = "4.19.157.ko.sh";
                break;
            case 10:
                kernelFileName = "4.19.157b.ko.sh";
                break;
            case 11:
                kernelFileName = "4.19.157-安卓13.ko.sh";
                break;
            case 12:
                kernelFileName = "4.19.191-安卓13.ko.sh";
                break;
            case 13:
                kernelFileName = "5.4.210-安卓13.ko.sh";
                break;
            case 14:
                kernelFileName = "5.15.ko.sh";
                break;
            case 15:
                kernelFileName = "5.15b.ko.sh";
                break;
            case 16:
                kernelFileName = "5.10.ko.sh";
                break;
            case 17:
                kernelFileName = "5.10b.ko.sh";
                break;
            case 18:
                kernelFileName = "5.10-安卓13-GooglePixel.ko.sh";
                break;
            case 19:
                kernelFileName = "5.4.61~250.ko.sh";
                break;
            case 20:
                kernelFileName = "5.4.86~250.ko.sh";
                break;
            case 21:
                kernelFileName = "5.4.147~250.ko.sh";
                break;
        }

        MoveAssets(getFilesDir() + "/", kernelFileName);
        Exec("/"+ kernelFileName, "Kernel Driver Success");
    }


}
