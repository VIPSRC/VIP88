package pubgm.loader.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import pubgm.loader.BoxApplication;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import android.graphics.Color;
import static pubgm.loader.activity.LoginActivity.PASSKEY;
import static pubgm.loader.activity.LoginActivity.USERKEY;
import static pubgm.loader.activity.LoginActivity.Kooontoool;
import static pubgm.loader.activity.SplashActivity.mahyong;
import static pubgm.loader.server.ApiServer.EXP;
import static pubgm.loader.server.ApiServer.FixCrash;
import static pubgm.loader.server.ApiServer.activity;
import static pubgm.loader.server.ApiServer.mainURL;
import pubgm.loader.utils.UiKit;
import java.util.List;
import java.util.Locale;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.google.android.material.button.MaterialButton;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import pubgm.loader.utils.PermissionUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import pubgm.loader.libhelper.FileHelper;
import android.widget.Toast;
import com.blankj.molihuan.utilcode.util.FileUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pubgm.loader.Component.DownloadZip;
import pubgm.loader.Component.Downtwo;
import pubgm.loader.Component.SharedPreferencesManager;
import pubgm.loader.R;
//import pubgm.loader.adapter.RecyclerViewAdapter;
import pubgm.loader.floating.FloatRei;
import pubgm.loader.floating.FloatService;
import pubgm.loader.floating.Overlay;
import pubgm.loader.floating.ToggleAim;
import pubgm.loader.floating.ToggleBullet;
import pubgm.loader.floating.ToggleSimulation;
import pubgm.loader.floating.FightMode;
import pubgm.loader.libhelper.ApkEnv;
import pubgm.loader.utils.ActivityCompat;
import pubgm.loader.utils.FLog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.topjohnwu.superuser.Shell;
import android.view.LayoutInflater;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import net_62v.external.MetaActivityManager;
import net_62v.external.MetaPackageManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import io.michaelrocks.paranoid.Obfuscate;
import net_62v.external.MetaActivationManager;

@Obfuscate
public class MainActivity extends ActivityCompat {

    public native String ONOFFBGMI();
    public String BGMI_INSTALL_OFF = "";
    RelativeLayout BGMIONOFF;
    public static String socket;
    public static String daemonPath;
    public static boolean fixinstallint = false;
    public static boolean check = false;
    public static int hiderecord = 0;
    static MainActivity instance;

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    private static final int REQUEST_PERMISSIONS = 1;
    private static final String PREF_NAME = "espValue";
    private SharedPreferences sharedPreferences;
    String[] packageapp = {"com.tencent.ig", "com.pubg.krmobile", "com.vng.pubgmobile", "com.rekoo.pubgm","com.pubg.imobile"};
    public String nameGame = "PROTECTION GLOBAL";
    public String CURRENT_PACKAGE = "";
    public LinearProgressIndicator progres;
    public CardView enable, disable;
    public static int gameint = 1;
    public static String bypassmode = "manual";
    public static int bitversi = 64;
    public static boolean noroot = false;
    public static int device = 1;
    public static String game = "com.tencent.ig";
    TextView root;
    public static boolean kernel = false;
    public static boolean Ischeck = false;
    public static boolean modestatus = false;
    public LinearLayout container;
    
    public static String modeselect;
    public static String typelogin;
    Context ctx;
    
    public static MainActivity get() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        
        setContentView(R.layout.activity_navigation);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
        initializeViews();
        updateInstallStates();
        init();
        VER();
        
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
       
    if(!Shell.rootAccess()) {
    boolean isActivated = MetaActivationManager.getActivationStatus();

    if (isActivated) {
        Toast.makeText(getApplicationContext(), "Server Contact✓✓", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(getApplicationContext(), "Server error XX", Toast.LENGTH_SHORT).show();
    }
}
     
        
        initMenu1();
        initMenu2();
        Loadssets();
        devicecheck();
        SettingESP();
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        instance = this;
        isLogin = true;
        Checking();
        
        
        
    }
    
    

    public void devicecheck(){
        root = findViewById(R.id.textroot);
        
       // protection = findviewById(R.id.protection);
        if (Shell.rootAccess()){
            FLog.info("Root granted");
            modeselect =   "ROOT MODE " + "ANDROID " + Build.VERSION.RELEASE;
          //  protectection.setVisibility(View.VISIBLE);
            root.setText(getString(R.string.root) );
            Ischeck = true;
            noroot = true;
            device = 1;
        } else {
            FLog.info("Root not granted");
            modeselect =   "CONTAINER MODE " + "ANDROID " + Build.VERSION.RELEASE;
           // protectection.setVisibility(View.GONE);
            root.setText(getString(R.string.notooroot));
          //  doInitRecycler();
          //  container.setVisibility(View.VISIBLE);
            Ischeck = false;
            device = 2;
        }
    }


    public void Checking(){
        File newFile = new File(getFilesDir().toString() + "/TW");
        if (newFile.exists()) {
            } else {
                new DownloadZip(this).execute("1", mainURL());
        }
    }
    public void VER() {
        try {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/emu.dat";
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("Verify".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressLint("SetTextI18n")
    public void initMenu1() {
    // Initialize all UI components
    
    TextView textroot = findViewById(R.id.texttag);
    
    
    LinearLayout layhome3 = findViewById(R.id.hackkkk);

    MaterialButton esp64Button = findViewById(R.id.esp64);
    MaterialButton esp32Button = findViewById(R.id.esp32);
    MaterialButton systemModeBtn = findViewById(R.id.system);
    MaterialButton kernelModeBtn = findViewById(R.id.kernel);
    MaterialButton espsafe = findViewById(R.id.espsafe);
    MaterialButton espunsafe = findViewById(R.id.espunsafe);
   
    
    
   
   // LinearLayout container = findViewById(R.id.container);   
    MaterialButton InstallBgmiBtn = findViewById(R.id.InstallBgmi);
    MaterialButton InstallGlobalBtn = findViewById(R.id.InstallGlobal);
    MaterialButton InstallKRBtn = findViewById(R.id.InstallKR);
    MaterialButton InstallVNGBtn = findViewById(R.id.InstallVNG);
    MaterialButton InstallTWBtn = findViewById(R.id.InstallTW);
     //   showprotect.setEnabled(false); 

    // showprotect click listener

    // Check if resources have been updated
    
        
       

        if (Shell.rootAccess()) {
            InstallBgmiBtn.setText("START");
            InstallGlobalBtn.setText("START");
            InstallKRBtn.setText("START");
            InstallVNGBtn.setText("START");
            InstallTWBtn.setText("START");
         
        } else {
           
        }
       
    

    

 // SharedPreferences initialization
SharedPreferences sharedPreferences = getSharedPreferences("espValue", MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPreferences.edit();

// Load saved preferences on activity start
boolean isEsp64Selected = sharedPreferences.getBoolean("esp64Selected", true);
boolean isKernelMode = sharedPreferences.getBoolean("kernelSelected", false);
boolean isEspSafe = sharedPreferences.getBoolean("espSafe", true);

// Initial UI based on saved preferences
if (isEsp64Selected) {
    esp64Button.setTextColor(getResources().getColor(R.color.blazered));
    esp64Button.setIconTintResource(R.color.blazered);
    esp32Button.setTextColor(getResources().getColor(R.color.black));
    esp32Button.setIconTintResource(R.color.black);
} else {
    esp32Button.setTextColor(getResources().getColor(R.color.blazered));
    esp32Button.setIconTintResource(R.color.blazered);
    esp64Button.setTextColor(getResources().getColor(R.color.black));
    esp64Button.setIconTintResource(R.color.black);
}

if (!isKernelMode) {
    systemModeBtn.setTextColor(getResources().getColor(R.color.blazered));
    systemModeBtn.setIconTintResource(R.color.blazered);
    kernelModeBtn.setTextColor(getResources().getColor(R.color.black));
    kernelModeBtn.setIconTintResource(R.color.black);
} else {
    kernelModeBtn.setTextColor(getResources().getColor(R.color.blazered));
    kernelModeBtn.setIconTintResource(R.color.blazered);
    systemModeBtn.setTextColor(getResources().getColor(R.color.black));
    systemModeBtn.setIconTintResource(R.color.black);
}

if (isEspSafe) {
    espsafe.setTextColor(getResources().getColor(R.color.blazered));
    espsafe.setIconTintResource(R.color.blazered);
    espunsafe.setTextColor(getResources().getColor(R.color.black));
    espunsafe.setIconTintResource(R.color.black);
    textroot.setText(R.string.SafeMode);
} else {
    espunsafe.setTextColor(getResources().getColor(R.color.blazered));
    espunsafe.setIconTintResource(R.color.blazered);
    espsafe.setTextColor(getResources().getColor(R.color.black));
    espsafe.setIconTintResource(R.color.black);
    textroot.setText(R.string.BrutalMode);
}

// Save the selection in SharedPreferences when button is clicked
esp64Button.setOnClickListener(v -> {
    bitversi = 64;
    esp64Button.setTextColor(getResources().getColor(R.color.blazered));
    esp64Button.setIconTintResource(R.color.blazered);
    esp32Button.setTextColor(getResources().getColor(R.color.black));
    esp32Button.setIconTintResource(R.color.black);
    editor.putBoolean("esp64Selected", true).apply();  // Save esp64 selected
});

esp32Button.setOnClickListener(v -> {
    bitversi = 32;
    esp32Button.setTextColor(getResources().getColor(R.color.blazered));
    esp32Button.setIconTintResource(R.color.blazered);
    esp64Button.setTextColor(getResources().getColor(R.color.black));
    esp64Button.setIconTintResource(R.color.black);
    editor.putBoolean("esp64Selected", false).apply();  // Save esp32 selected
});

systemModeBtn.setOnClickListener(view -> {
    kernel = false;
    systemModeBtn.setTextColor(getResources().getColor(R.color.blazered));
    systemModeBtn.setIconTintResource(R.color.blazered);
    kernelModeBtn.setTextColor(getResources().getColor(R.color.black));
    kernelModeBtn.setIconTintResource(R.color.black);
    editor.putBoolean("kernelSelected", false).apply();  // Save system mode selected
});

kernelModeBtn.setOnClickListener(view -> {
    kernel = true;
    kernelModeBtn.setTextColor(getResources().getColor(R.color.blazered));
    kernelModeBtn.setIconTintResource(R.color.blazered);
    systemModeBtn.setTextColor(getResources().getColor(R.color.black));
    systemModeBtn.setIconTintResource(R.color.black);
    editor.putBoolean("kernelSelected", true).apply();  // Save kernel mode selected
});

espsafe.setOnClickListener(v -> {
    espsafe.setTextColor(getResources().getColor(R.color.blazered));
    espsafe.setIconTintResource(R.color.blazered);
    espunsafe.setTextColor(getResources().getColor(R.color.black));
    espunsafe.setIconTintResource(R.color.black);
    textroot.setText(R.string.SafeMode);
    modestatus = false;
    editor.putBoolean("espSafe", true).apply();  // Save esp safe selected
});

espunsafe.setOnClickListener(v -> {
    espunsafe.setTextColor(getResources().getColor(R.color.blazered));
    espunsafe.setIconTintResource(R.color.blazered);
    espsafe.setTextColor(getResources().getColor(R.color.black));
    espsafe.setIconTintResource(R.color.black);
    textroot.setText(R.string.BrutalMode);
    modestatus = true;
    editor.putBoolean("espSafe", false).apply();  // Save esp unsafe selected
});
        
        bypassmode = sharedPreferences.getString("bypassmode", "manual");

        MaterialButton bypassButton = findViewById(R.id.bypexecute);

        // Update the button state based on saved preferences
        updateBypassButton(bypassButton);

        // Set the button click listener to toggle the mode
        bypassButton.setOnClickListener(v -> {
            // Toggle the bypass mode
            if (bypassmode.equals("manual")) {
                bypassmode = "automatic";
            } else {
                bypassmode = "manual";
            }

            // Update the button state and save the new mode in SharedPreferences
            updateBypassButton(bypassButton);
            editor.putString("bypassmode", bypassmode);
            editor.apply();
        });
    

    
    
}





    
    

    @SuppressLint("ResourceAsColor")
    void initMenu2(){
   
        
        TextView username = findViewById(R.id.username);
        TextView leveluser = findViewById(R.id.leveluser);
        
        
        
        
      
        
        TextView deviceInfoTextView = findViewById(R.id.deviceInfoTextView);

// Get manufacturer, model, and Android version
String manufacturer = Build.MANUFACTURER;
String model = Build.MODEL;
String androidVersion = Build.VERSION.RELEASE;

// Combine the information into a single string
String deviceInfo = manufacturer + " " + model + " - Android " + androidVersion;

// Set the text to the TextView
deviceInfoTextView.setText(deviceInfo);


        username.setText(PASSKEY+":"+USERKEY);

        if (Kooontoool){
            leveluser.setText("PREMIUM");
        }else{
            leveluser.setText("FREE");
            findViewById(R.id.fixinstall).setAlpha(0.5f);
            findViewById(R.id.hiderecord).setAlpha(0.5f);
            

        }



        ImageView play = findViewById(R.id.playPauseButton);
        boolean[] isPlaying = {false};

        play.setOnClickListener(v -> {
            if (!isPlaying[0]) {
                play.setImageResource(R.drawable.icon);

                startPatcher();
                isPlaying[0] = true;
            } else {
                play.setImageResource(R.drawable.icon);

                stopPatcher();
                isPlaying[0] = false;
            }
        });


       
        
        

        

        MaterialButton InstallTwitterBtn = findViewById(R.id.InstallTwitter);

        InstallTwitterBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        LayoutInflater inflater = getLayoutInflater();
                        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
                        TextView titleText = titleView.findViewById(R.id.dialogTitle);
                        titleText.setText("Select an option");
                        titleText.setTextColor(getResources().getColor(R.color.blazered));

                        builder.setCustomTitle(titleView);

                        String[] options = {"Launch", "Install", "Uninstall"};

                        builder.setAdapter(
                                new android.widget.ArrayAdapter<String>(
                                        MainActivity.this, R.layout.dialog_item, options) {
                                    @Override
                                    public View getView(
                                            int position,
                                            View convertView,
                                            android.view.ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView optionText =
                                                view.findViewById(R.id.dialog_option_text);
                                        optionText.setText(options[position]);
                                        optionText.setTextColor(
                                                getResources().getColor(R.color.black));
                                        return view;
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                // doShowProgress(true);
                                                ApkEnv.getInstance().launchApk("com.twitter.android");
                                                break;
                                            case 1:
                                                doShowProgress(true);
                                                addAdditionalApp(false, "com.twitter.android");
                                                break;
                                            case 2:
                                                doShowProgress(true);
                                                unInstallWithDellay("com.twitter.android");

                                                break;
                                        }
                                    }
                                });

                        AlertDialog dialog = builder.create();

                        ShapeDrawable background = new ShapeDrawable();
                        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
                        background.setShape(
                                new RoundRectShape(
                                        new float[] {
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius
                                        },
                                        null,
                                        null));
                        background.getPaint().setColor(Color.WHITE);

                        dialog.setOnShowListener(
                                dialogInterface -> {
                                    if (dialog.getWindow() != null) {
                                        dialog.getWindow().setBackgroundDrawable(background);
                                    }
                                });

                        dialog.show();
                    }
                });
        
        MaterialButton installFbButton = findViewById(R.id.InstallFb);

        installFbButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        LayoutInflater inflater = getLayoutInflater();
                        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
                        TextView titleText = titleView.findViewById(R.id.dialogTitle);
                        titleText.setText("Select an option");
                        titleText.setTextColor(getResources().getColor(R.color.blazered));

                        builder.setCustomTitle(titleView);

                        String[] options = {"Launch", "Install", "Uninstall"};

                        builder.setAdapter(
                                new android.widget.ArrayAdapter<String>(
                                        MainActivity.this, R.layout.dialog_item, options) {
                                    @Override
                                    public View getView(
                                            int position,
                                            View convertView,
                                            android.view.ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView optionText =
                                                view.findViewById(R.id.dialog_option_text);
                                        optionText.setText(options[position]);
                                        optionText.setTextColor(
                                                getResources().getColor(R.color.black));
                                        return view;
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                // doShowProgress(true);
                                                ApkEnv.getInstance().launchApk("com.mogg");
                                                break;
                                            case 1:
                                                doShowProgress(true);
                                                addAdditionalApp(false, "com.mogg");
                                                break;
                                            case 2:
                                                doShowProgress(true);
                                                unInstallWithDellay("com.mogg");

                                                break;
                                        }
                                    }
                                });

                        AlertDialog dialog = builder.create();

                        ShapeDrawable background = new ShapeDrawable();
                        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
                        background.setShape(
                                new RoundRectShape(
                                        new float[] {
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius,
                                            cornerRadius
                                        },
                                        null,
                                        null));
                        background.getPaint().setColor(Color.WHITE);

                        dialog.setOnShowListener(
                                dialogInterface -> {
                                    if (dialog.getWindow() != null) {
                                        dialog.getWindow().setBackgroundDrawable(background);
                                    }
                                });

                        dialog.show();
                    }
                });
        
        

        MaterialButton InstallBgmiBtn = findViewById(R.id.InstallBgmi);

String packageName = "com.pubg.imobile"; // Define the package name once

InstallBgmiBtn.setOnClickListener(v -> {
    gameint = 5;  
    game = packageName;       
    if (Shell.rootAccess()) {
        if (InstallBgmiBtn.getText().toString().equals("START")) {
            InstallBgmiBtn.setText("STOP");

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                toastImage(R.drawable.ic_error,packageName + getString(R.string.not_installed_please_check));
                
            }

            launchbypass();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                
            }, 3000);

        } else if (InstallBgmiBtn.getText().toString().equals("STOP")) {
            InstallBgmiBtn.setText("START");

            stopPatcher();
            // stopRunningApp(packageName);
        }
    } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleText = titleView.findViewById(R.id.dialogTitle);
        titleText.setText("Select an option");
        titleText.setTextColor(getResources().getColor(R.color.blazered));

        builder.setCustomTitle(titleView);

        boolean isAppRunning = isRunning(packageName);
        String[] options;

        if (isAppRunning) {
            options = new String[]{"Stop App", "Install", "Uninstall"};
        } else {
            options = new String[]{"Launch", "Install", "Uninstall"};
        }

        builder.setAdapter(
                new android.widget.ArrayAdapter<String>(
                        MainActivity.this, R.layout.dialog_item, options) {
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView optionText = view.findViewById(R.id.dialog_option_text);
                        optionText.setText(options[position]);
                        optionText.setTextColor(getResources().getColor(R.color.black));
                        return view;
                    }
                },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (isAppRunning) {
                                
                                stopRunningApp(packageName);
                            } else {
                                launchApk(packageName);
                                tryAddLoader(packageName);

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
    if (isRunning(packageName)) {
        

       
        if (bypassmode.equals("automatic")) {
            launchbypassNoRoot();
        }
    } else {
        stopPatcher();
    }
}, 3000);

                            }
                            break;

                        case 1:
                            doShowProgress(true);
                            if (!fixinstallint) {
                                FileHelper.tryInstallWithCopyObb(MainActivity.this, getProgresBar(), packageName);
                            } else {
                                PermissionUtils.openobb(MainActivity.this, 1, packageName);
                            }
                            break;

                        case 2:
                            doShowProgress(true);
                            unInstallWithDellay(packageName);
                            break;
                    }
                });

        AlertDialog dialog = builder.create();

        ShapeDrawable background = new ShapeDrawable();
        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
        background.setShape(
                new RoundRectShape(
                        new float[] {
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius
                        },
                        null, null));
        background.getPaint().setColor(Color.WHITE);

        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(background);
            }
        });

        dialog.show();
    }
});
        
        
        MaterialButton InstallGlobalBtn = findViewById(R.id.InstallGlobal);

String packageNameGl = "com.tencent.ig"; // Define the package name once

InstallGlobalBtn.setOnClickListener(v -> {
    gameint = 1;
    game = packageNameGl;     
    if (Shell.rootAccess()) {
        if (InstallGlobalBtn.getText().toString().equals("START")) {
            InstallGlobalBtn.setText("STOP");

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageNameGl);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                toastImage(R.drawable.ic_error,packageNameGl + getString(R.string.not_installed_please_check));
                stopPatcher();
            }

            launchbypass();

         

        } else if (InstallGlobalBtn.getText().toString().equals("STOP")) {
            InstallGlobalBtn.setText("START");

            stopPatcher();
         
        }
    } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleText = titleView.findViewById(R.id.dialogTitle);
        titleText.setText("Select an option");
        titleText.setTextColor(getResources().getColor(R.color.blazered));

        builder.setCustomTitle(titleView);

        boolean isAppRunning = isRunning(packageNameGl);
        String[] options;

        if (isAppRunning) {
            options = new String[]{"Stop App", "Install", "Uninstall"};
        } else {
            options = new String[]{"Launch", "Install", "Uninstall"};
        }

        builder.setAdapter(
                new android.widget.ArrayAdapter<String>(
                        MainActivity.this, R.layout.dialog_item, options) {
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView optionText = view.findViewById(R.id.dialog_option_text);
                        optionText.setText(options[position]);
                        optionText.setTextColor(getResources().getColor(R.color.black));
                        return view;
                    }
                },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (isAppRunning) {
                                
                                stopRunningApp(packageNameGl);
                            } else {
                                launchApk(packageNameGl);
                                tryAddLoader(packageNameGl);
                                   

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if (isRunning(packageNameGl)) {
                                        
                                        if (bypassmode.equals("automatic")) {
                                        launchbypassNoRoot();
                                        }
                                    } else {
                                        
                                    }
                                }, 1300);
                            }
                            break;

                        case 1:
                            doShowProgress(true);
                            if (!fixinstallint) {
                                FileHelper.tryInstallWithCopyObb(MainActivity.this, getProgresBar(), packageNameGl);
                            } else {
                                PermissionUtils.openobb(MainActivity.this, 1, packageNameGl);
                            }
                            break;

                        case 2:
                            doShowProgress(true);
                            unInstallWithDellay(packageNameGl);
                            break;
                    }
                });

        AlertDialog dialog = builder.create();

        ShapeDrawable background = new ShapeDrawable();
        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
        background.setShape(
                new RoundRectShape(
                        new float[] {
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius
                        },
                        null, null));
        background.getPaint().setColor(Color.WHITE);

        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(background);
            }
        });

        dialog.show();
    }
});

        
        MaterialButton InstallKRBtn = findViewById(R.id.InstallKR);

String packageNameKr = "com.pubg.krmobile"; // Define the package name once

InstallKRBtn.setOnClickListener(v -> {
    gameint = 2;
    game = packageNameKr;         
    if (Shell.rootAccess()) {
        if (InstallKRBtn.getText().toString().equals("START")) {
            InstallKRBtn.setText("STOP");

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageNameKr);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                toastImage(R.drawable.ic_error,packageNameKr + getString(R.string.not_installed_please_check));
                
            }

            launchbypass();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                
            }, 1300);

        } else if (InstallKRBtn.getText().toString().equals("STOP")) {
            InstallKRBtn.setText("START");

          
        }
    } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleText = titleView.findViewById(R.id.dialogTitle);
        titleText.setText("Select an option");
        titleText.setTextColor(getResources().getColor(R.color.blazered));

        builder.setCustomTitle(titleView);

        boolean isAppRunning = isRunning(packageNameKr);
        String[] options;

        if (isAppRunning) {
            options = new String[]{"Stop App", "Install", "Uninstall"};
        } else {
            options = new String[]{"Launch", "Install", "Uninstall"};
        }

        builder.setAdapter(
                new android.widget.ArrayAdapter<String>(
                        MainActivity.this, R.layout.dialog_item, options) {
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView optionText = view.findViewById(R.id.dialog_option_text);
                        optionText.setText(options[position]);
                        optionText.setTextColor(getResources().getColor(R.color.black));
                        return view;
                    }
                },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (isAppRunning) {
                                
                                stopRunningApp(packageNameKr);
                            } else {
                                launchApk(packageNameKr);
                                tryAddLoader(packageNameKr);

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if (isRunning(packageNameKr)) {
                                        
                                        if (bypassmode.equals("automatic")) {
                                        launchbypassNoRoot();
                                        }
                                    } else {
                                        
                                    }
                                }, 1300);
                            }
                            break;

                        case 1:
                            doShowProgress(true);
                            if (!fixinstallint) {
                                FileHelper.tryInstallWithCopyObb(MainActivity.this, getProgresBar(), packageNameKr);
                            } else {
                                PermissionUtils.openobb(MainActivity.this, 1, packageNameKr);
                            }
                            break;

                        case 2:
                            doShowProgress(true);
                            unInstallWithDellay(packageNameKr);
                            break;
                    }
                });

        AlertDialog dialog = builder.create();

        ShapeDrawable background = new ShapeDrawable();
        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
        background.setShape(
                new RoundRectShape(
                        new float[] {
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius
                        },
                        null, null));
        background.getPaint().setColor(Color.WHITE);

        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(background);
            }
        });

        dialog.show();
    }
});
        
        
        MaterialButton InstallVNGBtn = findViewById(R.id.InstallVNG);

String packageNameVng = "com.vng.pubgmobile"; // Define the package name once

InstallVNGBtn.setOnClickListener(v -> {
    gameint = 3;
    game = packageNameVng;                 
    if (Shell.rootAccess()) {
        if (InstallVNGBtn.getText().toString().equals("START")) {
            InstallVNGBtn.setText("STOP");

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageNameVng);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                toastImage(R.drawable.ic_error,packageNameVng + getString(R.string.not_installed_please_check));
                
            }

            launchbypass();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startPatcher();
            }, 3000);

        } else if (InstallVNGBtn.getText().toString().equals("STOP")) {
            InstallVNGBtn.setText("START");

            stopPatcher();
           
        }
    } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleText = titleView.findViewById(R.id.dialogTitle);
        titleText.setText("Select an option");
        titleText.setTextColor(getResources().getColor(R.color.blazered));

        builder.setCustomTitle(titleView);

        boolean isAppRunning = isRunning(packageNameVng);
        String[] options;

        if (isAppRunning) {
            options = new String[]{"Stop App", "Install", "Uninstall"};
        } else {
            options = new String[]{"Launch", "Install", "Uninstall"};
        }

        builder.setAdapter(
                new android.widget.ArrayAdapter<String>(
                        MainActivity.this, R.layout.dialog_item, options) {
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView optionText = view.findViewById(R.id.dialog_option_text);
                        optionText.setText(options[position]);
                        optionText.setTextColor(getResources().getColor(R.color.black));
                        return view;
                    }
                },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (isAppRunning) {
                                
                                stopRunningApp(packageNameVng);
                            } else {
                                launchApk(packageNameVng);
                                tryAddLoader(packageNameVng);

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if (isRunning(packageNameVng)) {
                                        
                                        if (bypassmode.equals("automatic")) {
                                        launchbypassNoRoot();
                                        }
                                    } else {
                                        
                                    }
                                }, 3000);
                            }
                            break;

                        case 1:
                            doShowProgress(true);
                            if (!fixinstallint) {
                                FileHelper.tryInstallWithCopyObb(MainActivity.this, getProgresBar(), packageNameVng);
                            } else {
                                PermissionUtils.openobb(MainActivity.this, 1, packageNameVng);
                            }
                            break;

                        case 2:
                            doShowProgress(true);
                            unInstallWithDellay(packageNameVng);
                            break;
                    }
                });

        AlertDialog dialog = builder.create();

        ShapeDrawable background = new ShapeDrawable();
        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
        background.setShape(
                new RoundRectShape(
                        new float[] {
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius
                        },
                        null, null));
        background.getPaint().setColor(Color.WHITE);

        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(background);
            }
        });

        dialog.show();
    }
});
        
        MaterialButton InstallTWBtn = findViewById(R.id.InstallTW);

String packageNameTw = "com.rekoo.pubgm"; // Define the package name once

InstallTWBtn.setOnClickListener(v -> {
    gameint = 4;   
    game = packageNameTw;            
    if (Shell.rootAccess()) {
        if (InstallTWBtn.getText().toString().equals("START")) {
            InstallTWBtn.setText("STOP");

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageNameTw);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                toastImage(R.drawable.ic_error,packageNameTw + getString(R.string.not_installed_please_check));
                
            }

            launchbypass();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startPatcher();
            }, 3000);

        } else if (InstallTWBtn.getText().toString().equals("STOP")) {
            InstallTWBtn.setText("START");

            
            // stopRunningApp(packageNameTw);
        }
    } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);
        TextView titleText = titleView.findViewById(R.id.dialogTitle);
        titleText.setText("Select an option");
        titleText.setTextColor(getResources().getColor(R.color.blazered));

        builder.setCustomTitle(titleView);

        boolean isAppRunning = isRunning(packageNameTw);
        String[] options;

        if (isAppRunning) {
            options = new String[]{"Stop App", "Install", "Uninstall"};
        } else {
            options = new String[]{"Launch", "Install", "Uninstall"};
        }

        builder.setAdapter(
                new android.widget.ArrayAdapter<String>(
                        MainActivity.this, R.layout.dialog_item, options) {
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView optionText = view.findViewById(R.id.dialog_option_text);
                        optionText.setText(options[position]);
                        optionText.setTextColor(getResources().getColor(R.color.black));
                        return view;
                    }
                },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (isAppRunning) {
                                
                                stopRunningApp(packageNameTw);
                            } else {
                                launchApk(packageNameTw);
                                tryAddLoader(packageNameTw);

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if (isRunning(packageNameTw)) {
                                        
                                        if (bypassmode.equals("automatic")) {
                                        launchbypassNoRoot();
                                        }
                                    } else {
                                        
                                    }
                                }, 3000);
                            }
                            break;

                        case 1:
                            doShowProgress(true);
                            if (!fixinstallint) {
                                FileHelper.tryInstallWithCopyObb(MainActivity.this, getProgresBar(), packageNameTw);
                            } else {
                                PermissionUtils.openobb(MainActivity.this, 1, packageNameTw);
                            }
                            break;

                        case 2:
                            doShowProgress(true);
                            unInstallWithDellay(packageNameTw);
                            break;
                    }
                });

        AlertDialog dialog = builder.create();

        ShapeDrawable background = new ShapeDrawable();
        float cornerRadius = 4f * getResources().getDisplayMetrics().density;
        background.setShape(
                new RoundRectShape(
                        new float[] {
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius
                        },
                        null, null));
        background.getPaint().setColor(Color.WHITE);

        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(background);
            }
        });

        dialog.show();
    }
});
        
        SharedPreferences sharedPreferences = getSharedPreferences("espValue", MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPreferences.edit();

        int savedHideRecord = sharedPreferences.getInt("hiderecord", 0);
MaterialButton hideRecordButton = findViewById(R.id.hiderecord);

// Set initial UI based on saved preference
if (savedHideRecord == 1) {
    hideRecordButton.setTextColor(getResources().getColor(R.color.green));
    hideRecordButton.setIconTintResource(R.color.green);
} else {
    hideRecordButton.setTextColor(getResources().getColor(R.color.blazered));
    hideRecordButton.setIconTintResource(R.color.blazered);
}

findViewById(R.id.hiderecord).setOnClickListener(v -> {
    if (Kooontoool) {
        showBottomSheetDialog(
            getResources().getDrawable(R.drawable.icon_toast_alert),
            getString(R.string.confirm),
            getString(R.string.want_remove_it),
            false,
            sv -> {
                hiderecord = 1;
                hideRecordButton.setTextColor(getResources().getColor(R.color.green));
                hideRecordButton.setIconTintResource(R.color.green);
                editor.putInt("hiderecord", 1).apply(); // Save hiderecord as 1
                dismissBottomSheetDialog();
            },
            v1 -> {
                hiderecord = 0;
                hideRecordButton.setTextColor(getResources().getColor(R.color.blazered));
                hideRecordButton.setIconTintResource(R.color.blazered);
                editor.putInt("hiderecord", 0).apply(); // Save hiderecord as 0
                dismissBottomSheetDialog();
            }
        );
    } else {
        toastImage(R.drawable.notife, "Please Upgrade to PREMIUM");
    }
});
        

        findViewById(R.id.fixinstall).setOnClickListener(v -> {
            if (Kooontoool){
                showBottomSheetDialog(getResources().getDrawable(R.drawable.icon_toast_alert), getString(R.string.confirm), getString(R.string.this_for_fix_obb_not_found_need_actived_this), false, sv -> {
                    fixinstallint = true;
                    dismissBottomSheetDialog();
                }, v1 -> {
                    fixinstallint = false;
                    dismissBottomSheetDialog();
                });
            }else{
                toastImage(R.drawable.notife,"Please Upgrade to PREMIUM");
            }
        });



    }


    public void SettingESP(){
        // Meminta izin jika belum diberikan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        findViewById(R.id.savesetting).setOnClickListener(v -> {
            try {
                importSharedPreferences();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to import", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.exportsetting).setOnClickListener(v -> {
            try {
                exportSharedPreferences();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to export", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.resetsetting).setOnClickListener(v -> {
            resetSharedPreferences();
            Toast.makeText(MainActivity.this, "Success Reset", Toast.LENGTH_SHORT).show();
        });
    }
    
    // Method to update the button UI based on the bypass mode
    private void updateBypassButton(MaterialButton bypassButton) {
        if (bypassmode.equals("automatic")) {
            bypassButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            bypassButton.setText("AUTOMATIC");
            bypassButton.setIcon(getDrawable(R.drawable.bypon));
        } else {
            bypassButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blazered)));
            bypassButton.setText("MANUAL");
            bypassButton.setIcon(getDrawable(R.drawable.bypoff));
        }
    }

    private void exportSharedPreferences() throws IOException {
        File srcFile = new File(getApplication().getDataDir().toString() + "/shared_prefs/"+ PREF_NAME + ".xml");
        File dstFile = new File(Environment.getExternalStorageDirectory(), PREF_NAME + ".xml");

        if (srcFile.exists()) {
            FileChannel src = null;
            FileChannel dst = null;
            try {
                src = new FileInputStream(srcFile).getChannel();
                dst = new FileOutputStream(dstFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                Toast.makeText(MainActivity.this, "Exported to " + dstFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } finally {
                if (src != null) {
                    src.close();
                }
                if (dst != null) {
                    dst.close();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Setting esp files file not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void importSharedPreferences() throws IOException {
        File srcFile = new File(Environment.getExternalStorageDirectory(), PREF_NAME + ".xml");
        File dstFile = new File(getApplication().getDataDir().toString() + "/shared_prefs/" + PREF_NAME + ".xml");

        if (srcFile.exists()) {
            FileChannel src = null;
            FileChannel dst = null;
            try {
                src = new FileInputStream(srcFile).getChannel();
                dst = new FileOutputStream(dstFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                Toast.makeText(MainActivity.this, "Imported from " + srcFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } finally {
                if (src != null) {
                    src.close();
                }
                if (dst != null) {
                    dst.close();
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "Setting esp file not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    void gameversion(LinearLayout a, LinearLayout b, LinearLayout c, LinearLayout d, LinearLayout e){
        a.setBackgroundResource(R.drawable.bgfituron);
        b.setBackgroundResource(R.drawable.bgfituroff);
        c.setBackgroundResource(R.drawable.bgfituroff);
        d.setBackgroundResource(R.drawable.bgfituroff);
        e.setBackgroundResource(R.drawable.bgfituroff);
    }


    void init() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        LinearLayout navhome = findViewById(R.id.navhome);
        LinearLayout navsetting = findViewById(R.id.navsetting);
        LinearLayout profiles = findViewById(R.id.profiles);


        LinearLayout effecthome = findViewById(R.id.effecthome);
        LinearLayout effectsetting = findViewById(R.id.effectsetting);
        LinearLayout menu1 = findViewById(R.id.imenu1);
        LinearLayout menu2 = findViewById(R.id.imenu2);
        LinearLayout menu3 = findViewById(R.id.imenu3);
        ImageView home = findViewById(R.id.imghome);
        ImageView sett = findViewById(R.id.imgsett);
        TextView txtsett = findViewById(R.id.txtsett);
        TextView txthome = findViewById(R.id.txthome);


        navhome.setOnClickListener(v -> {
            menu1.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
        
            txthome.setTextColor(getResources().getColor(R.color.white));
            txtsett.setTextColor(getResources().getColor(R.color.gray));
            home.setBackgroundResource(R.drawable.ic_home);
            sett.setBackgroundResource(R.drawable.outline_settings_24);
        });

        navsetting.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
            menu3.setVisibility(View.GONE);
         

            txthome.setTextColor(getResources().getColor(R.color.gray));
            txtsett.setTextColor(getResources().getColor(R.color.white));
            home.setBackgroundResource(R.drawable.ic_home_outline);
            sett.setBackgroundResource(R.drawable.ic_setting);
        });
        profiles.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.VISIBLE);

        });
    }


   

    public void addAdditionalApp(boolean system, String packageName) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ApkEnv.getInstance().isInstalled(packageName)) {
                    doShowProgress(true);
                   // ApkEnv.getInstance().launchApk(packageName);
                } else {
                    try {
                        if (ApkEnv.getInstance().installByPackage(packageName)) {
                            doShowProgress(true);
                           // ApkEnv.getInstance().launchApk(packageName);
                            toastImage(R.drawable.notife, "Apk Installed ✓");
                        }
                    } catch (Exception err) {
                        FLog.error(err.getMessage());
                        doHideProgress();
                    }
                }
            }
        });
    }
    
    public void launchApk(String packageName) {
        if (!isInstalled(packageName)) {
            BoxApplication.get().showToastWithImage(R.drawable.icon, "Game not installed");
            return;
        }
        try {
            MetaActivityManager.launchApp(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void unInstallApp(String packageName) {
    	try {
            MetaPackageManager.uninstallAppFully(packageName);
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
    
    public void stopRunningApp(String packageName) {
    	try {
            MetaActivityManager.killAppByPkg(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    
    public boolean tryAddLoader(String packageName) {
        boolean is_online = BoxApplication.STATUS_BY.equals("online");

        ApplicationInfo applicationInfo = getApplicationInfoContainer(packageName);
        if (applicationInfo == null) {
            FLog.error("Error, Application Info");
            return false;
        }

        String target = "libpubgm.so";

        if (packageName.equals("com.miraclegames.farlight84")) {
            target = "libfarlight.so";
        } else if (packageName.equals("com.pubg.imobile")) {
            target = "libSdk.so";
        } else {
            target = "libpubgm.so";
        }

        File loader = new File(is_online ? new File(BoxApplication.get().getFilesDir(), "loader").toString() : BoxApplication.get().getApplicationInfo().nativeLibraryDir, target);
        File loaderDest = new File(applicationInfo.nativeLibraryDir, packageName.equals("com.miraclegames.farlight84") ? "libfarlight.so" : "libAkAudioVisiual.so");

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
    
    private void unInstallWithDellay(String packageName) {
        UiKit.defer().when(() -> {
            long time = System.currentTimeMillis();
            unInstallApp(packageName);
            time = System.currentTimeMillis() - time;
            long delta = 500L - time;
            if (delta > 0) {
                UiKit.sleep(delta);
            }
        }).done((res) -> {
           // doInitRecycler();
            doHideProgress();
            toastImage(R.drawable.ic_check, packageName + " was successfully uninstalled.");
        });
    }
    
    private void restartApp() {
    
    startPatcher();
   
}


    ////////////////////////// Panel Enc ////////////////////////////////////////

    private void initializeViews() {
        BGMIONOFF = findViewById(R.id.ANTIBAN);
    }

    private void updateInstallStates() {
        BGMI_INSTALL_OFF = ONOFFBGMI();
        toggleOnOff();
    }

    public void toggleOnOff() {
        toggleOnOffHelper(BGMI_INSTALL_OFF, BGMIONOFF);
    }
    private void toggleOnOffHelper(String installOff, RelativeLayout layout) {
        if (installOff != null && installOff.equals("on")) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
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
        
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bitversi == 64 ){
                        if (gameint >= 1 && gameint <= 4) {
                            Exec("/TW " + game + " 001", getString(R.string.bypass_launch_success));
                        } else if (gameint == 5) {
                            Exec("/TW " + game + " 005", getString(R.string.bypass_launch_success));
                        }
                    }else if (bitversi == 32){
                        if (gameint >= 1 && gameint <= 4) {
                            Exec("/TW " + game + " 001", getString(R.string.bypass_launch_success));
                        } else if (gameint == 5) {
                            Exec("/TW " + game + " 005", getString(R.string.bypass_launch_success));
                        }
                    }

                }
            }, 13000);
        
            
        
    }
    
    public void launchbypassNoRoot(){
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            if (bitversi == 64) {
                if (gameint >= 1 && gameint <= 4) {
                    Exec("/TW " + game + " 002", getString(R.string.BYPASS_64_ENABLE));
                } else if (gameint == 5) {
                    Exec("/TW " + game + " 005", getString(R.string.BYPASS_64_ENABLE));
                }
            } else if (bitversi == 32) {
                if (gameint >= 1 && gameint <= 4) {
                    Exec("/TW " + game + " 002", getString(R.string.BYPASS_32_ENABLE));
                } else if (gameint == 5) {
                    Exec("/TW " + game + " 005", getString(R.string.BYPASS_32_ENABLE));
                }
            }
        }
    }, 10000);
}



    public void Exec(String path, String toast) {
        try {
            ExecuteElf("su -c chmod 777 " + getFilesDir() + path);
            ExecuteElf("su -c " + getFilesDir() + path);
            ExecuteElf("chmod 777 " + getFilesDir() + path);
            ExecuteElf("" +  getFilesDir() + path);
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
        stopService(new Intent(MainActivity.get(), FloatService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), FloatRei.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));
        stopService(new Intent(MainActivity.get(), FightMode.class));

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
                if (FloatService.class.getName().equals(service.service.getClassName())) {
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
        String filename = !Shell.rootAccess() ? "soc" : kernel ? "kernel" : "soc";
        String status = kernel ? "s" : modestatus ? "u" : "u";
        String bit = kernel ? "64" : bitversi == 64 ? "64" : "32";
        String rootaccess = kernel ? "" : Shell.rootAccess() ? "" : "";

        if (!isServiceRunning()) {
            loadAssets(filename + status + bit + rootaccess);
            loadAssets("socu64");
            startService(new Intent(MainActivity.get(), FloatService.class));
            startService(new Intent(MainActivity.get(), FightMode.class));
        } else {
            toastImage(R.drawable.ic_error, getString(R.string.service_is_already_running));
        }
    }

    private void stopPatcher() {
        stopService(new Intent(MainActivity.get(), FloatService.class));
        stopService(new Intent(MainActivity.get(), Overlay.class));
        stopService(new Intent(MainActivity.get(), FloatRei.class));
        stopService(new Intent(MainActivity.get(), ToggleAim.class));
        stopService(new Intent(MainActivity.get(), ToggleBullet.class));
        stopService(new Intent(MainActivity.get(), ToggleSimulation.class));
        stopService(new Intent(MainActivity.get(), FightMode.class));
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
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date expiryDate = dateFormat.parse(EXP());
                long now = System.currentTimeMillis();
                long distance = expiryDate.getTime() - now;

                // Set the expiry date in the TextView
                TextView Datetimer = findViewById(R.id.keyexpiry);
                if (expiryDate != null) {
                    SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Datetimer.setText(std.format(expiryDate));
                }

                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    handler.postDelayed(runnable, 0);
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

}
