package pubgm.loader.activity;


import static pubgm.loader.activity.LoginActivity.Kooontoool;
import static pubgm.loader.activity.SplashActivity.mahyong;
import static pubgm.loader.server.ApiServer.FixCrash;
import static pubgm.loader.server.ApiServer.activity;
import static pubgm.loader.server.ApiServer.getOwner;
import static pubgm.loader.server.ApiServer.mainURL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import pubgm.loader.BuildConfig;
import pubgm.loader.Component.DownloadZip;
import pubgm.loader.Component.Downtwo;
import pubgm.loader.Component.Prefs;
import pubgm.loader.R;
import pubgm.loader.utils.ActivityCompat;
import pubgm.loader.utils.FLog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.michaelrocks.paranoid.Obfuscate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

@Obfuscate
public class LoginActivity extends ActivityCompat {

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    private static final String UPDATE_URL = URLJSON();
    private boolean isDownloadComplete = false;
    private boolean updateCheckComplete = false;
    private static final String QUESTION = "Q: %s";
    private static final String ANSWER = "A: %s";
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    public static boolean Kooontoool = false;
    private ImageView showpassword;
    private static String ModeSelect;
    public static String USERKEY, PASSKEY;
    CardView btnSignIn;
    private Prefs prefs;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar(this);
        setContentView(R.layout.activity_login);
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        loadbahasa();
        initDesign();
        DwnSocket(this);
        checkForNewVersion();
        OverlayPermision();
    }

    public static void DwnSocket(LoginActivity activity) {
   
    new DownloadZip(activity).execute("1", mainURL());
    new Downtwo(activity).execute("1", FixCrash());
    

}

    public void initDesign(){
        prefs = Prefs.with(this);
        final Context m_Context = this;
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textPassword = findViewById(R.id.textPassword);
        // Get references to the TextInputLayout and TextInputEditTexts
TextInputLayout emailLayout = findViewById(R.id.email_layout);  // Assuming this is where endIconDrawable is used
MaterialButton btnSignIn = findViewById(R.id.loginBtn);
       // LinearLayout timg = findViewById(R.id.timg);
        //showpassword = findViewById(R.id.viewpw);
        textUsername.setText(prefs.read(USER, ""));
        textPassword.setText(prefs.read(PASS, ""));
        Intent intent = getIntent();
        
        btnSignIn.setEnabled(false);
        
        btnSignIn.setOnClickListener(
    new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textUsername = findViewById(R.id.textUsername);
            TextView textPassword = findViewById(R.id.textPassword);
            
            if (!textUsername.getText().toString().isEmpty()
                    && !textPassword.getText().toString().isEmpty()) {
                
                prefs.write(USER, textUsername.getText().toString());
                prefs.write(PASS, textPassword.getText().toString());
                
                String userKey = textUsername.getText().toString().trim();
                String passKey = textPassword.getText().toString().trim();
                String combinedKey = passKey+userKey; 
                
                Login(LoginActivity.this, userKey, ModeSelect);
                
                USERKEY = userKey;
                PASSKEY = passKey;
            }

            TextView errorUsername = findViewById(R.id.error_username);
            TextView errorPassword = findViewById(R.id.error_password);

            if (textUsername.getText().toString().isEmpty()) {
                errorUsername.setText(getString(R.string.please_enter_username));
                errorUsername.setVisibility(View.VISIBLE);
            } else {
                errorUsername.setVisibility(View.GONE);
            }

            if (textPassword.getText().toString().isEmpty()) {
                errorPassword.setText(getString(R.string.please_enter_password));
                errorPassword.setVisibility(View.VISIBLE);
            } else {
                errorPassword.setVisibility(View.GONE);
            }
        }
    });


       
        

// Set the endIcon click listener for the TextInputLayout (paste functionality)
emailLayout.setEndIconOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String ed = clipboardManager.getText().toString();
        String[] parts = ed.split(":");
        
        if (parts.length == 2) {
            textUsername.setText(parts[1].trim());
            textPassword.setText(parts[0].trim());
        } else {
            Toast.makeText(m_Context, String.format(QUESTION, R.string.please_copy_licence_and_paste), Toast.LENGTH_SHORT).show();
        }
    }
});



        TextView getKey = findViewById(R.id.registerText);
        getKey.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getOwner()));
                        startActivity(intent);
                    }
                });

        

        

        PowerSpinnerView powerSpinnerView = findViewById(R.id.bahasa);
        powerSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                switch (newIndex) {

                    case 0:
                        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("bahasa", "zh");
                        editor.apply();
                        recreate();
                        break;

                    case 1:
                        SharedPreferences sharedPreferences1 = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("bahasa", "ar");
                        editor1.apply();
                        recreate();
                        break;

                    case 2:
                        SharedPreferences sharedPreferences2 = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString("bahasa", "en");
                        editor2.apply();
                        recreate();
                        break;

                }

                Toast.makeText(getApplicationContext(), newItem, Toast.LENGTH_SHORT).show();

            }
        });
        
        PowerSpinnerView modeSpinner = findViewById(R.id.mode_spinner);
modeSpinner.selectItemByIndex(0); 

// Execute the action for the default selection ("PREMIUM")
LoginActivity.setModeSelect("PREMIUM");
Kooontoool = true;

modeSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
    @Override
    public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
        switch (newIndex) {
            case 0:
                LoginActivity.setModeSelect("PREMIUM");
                Kooontoool = true;
                break;

            case 1:
                LoginActivity.setModeSelect("FREE");
                Kooontoool = false;
                break;
        }

        Toast.makeText(getApplicationContext(), newItem, Toast.LENGTH_SHORT).show();
    }
});



        
        LinearLayout timg = findViewById(R.id.timg);
        timg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://t.me/ "));
                    startActivity(intent);
                }
            });
			
		LinearLayout website = findViewById(R.id.website);
		website.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("https://t.me/ "));
					startActivity(intent);
				}
			});


    }


    private void setLightStatusBar(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        activity.getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
    }

    public static void goLogin(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    public void OverlayPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setMessage(R.string.please_allow_permision_floating);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }
    
    private void checkForNewVersion() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, UPDATE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("UpdateCheck", "Raw JSON Response: " + response.toString());
                            String apkUrl = response.getString("apk_url");
                            String updateTitle = response.getString("update_title");
                            String updateMessage = response.getString("update_message");
                            String updateSize = response.getString("update_size");
                            String serverVersionName = response.getString("version_name");
                            String currentVersionName = BuildConfig.VERSION_NAME;

                            Log.d("UpdateCheck", "Current version name: " + currentVersionName);
                            Log.d("UpdateCheck", "Fetched version name: " + serverVersionName);

                            if (!serverVersionName.equals(currentVersionName)) {
                                Log.d("UpdateCheck", "New version available, showing update dialog.");
                                showUpdateDialog(apkUrl, updateTitle, updateMessage, updateSize);
                            } else {
                                Log.d("UpdateCheck", "No new version available.");
                                Toast.makeText(LoginActivity.this, "App is up to date", Toast.LENGTH_SHORT).show();
                                updateCheckComplete = true;
                                enableNavigationButtons();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("UpdateCheck", "Error parsing update information: " + e.getMessage());
                            Toast.makeText(LoginActivity.this, "App is up to date", Toast.LENGTH_SHORT).show();
                            updateCheckComplete = true;
                            enableNavigationButtons();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateCheck", "Failed to fetch update info: " + error.getMessage());
                        Toast.makeText(LoginActivity.this, "Failed to check for updates", Toast.LENGTH_SHORT).show();
                        updateCheckComplete = true;
                        enableNavigationButtons();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
    
    private void showUpdateDialog(String apkUrl, String updateTitle, String updateMessage, String updateSize) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View updateLayout = getLayoutInflater().inflate(R.layout.apk_update_version, null);
        builder.setView(updateLayout);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView titleView = updateLayout.findViewById(R.id.update_title);
        TextView messageView = updateLayout.findViewById(R.id.update_message);
        TextView sizeView = updateLayout.findViewById(R.id.update_size);
        TextView btnNoThanks = updateLayout.findViewById(R.id.btn_no_thanks);
        TextView btnUpdate = updateLayout.findViewById(R.id.btn_update);

        titleView.setText(updateTitle);
        messageView.setText(updateMessage);
        sizeView.setText("Download size: " + updateSize);

        btnNoThanks.setOnClickListener(v -> finishAffinity());

        btnUpdate.setOnClickListener(v -> {
            dialog.dismiss();
            startDownload(apkUrl);
        });
    }
    
    private void startDownload(String apkUrl) {
        KProgressHUD dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Downloading")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(apkUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Connection", "close");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    throw new IOException("Request Code Not 200");
                }

                File outputPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "pak.apk");

                InputStream inputStream = connection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    long finalTotal = total;
                    runOnUiThread(() -> {
                        float curr = (float) (finalTotal / 1024) / 1024;
                        String txt = String.format(Locale.getDefault(), "Downloading ( %.2fMB )", curr);
                        dialog.setLabel(txt);
                    });
                    fileOutputStream.write(data, 0, count);
                }

                inputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                connection.disconnect();

                runOnUiThread(() -> {
                    dialog.dismiss();
                    isDownloadComplete = true;
                    installApk(outputPath);
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Failed to download, please check your internet connection.", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", apkFile);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    private void enableNavigationButtons() {
        MaterialButton btnSignIn = findViewById(R.id.loginBtn);
        

        if (updateCheckComplete) {
            btnSignIn.setEnabled(true);
            
        }
    }

    public static native String URLJSON();




    public static void setModeSelect(String mode) {
        ModeSelect = mode;
    }

    public static String getModeSelect() {
        return ModeSelect;
    }

    private static void Login(final LoginActivity m_Context, final String userKey, final String modeSelect) {
        LayoutInflater inflater = LayoutInflater.from(m_Context);
        View viewloading = inflater.inflate(R.layout.animation_login, null);
        AlertDialog dialogloading =
                new AlertDialog.Builder(m_Context, 5)
                        .setView(viewloading)
                        .setCancelable(false)
                        .create();
        dialogloading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogloading.show();

        final Handler loginHandler =
                new Handler() {
                    @SuppressLint("HandlerLeak")
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {
                            
                               
                                try {
                                    Class DeviceInfo = Class.forName(activity());
                                    m_Context.startActivity(new Intent(m_Context.getApplicationContext(), DeviceInfo));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            
                            Toast.makeText(m_Context, "Login Success", Toast.LENGTH_SHORT).show();
                            
                        } else if (msg.what == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(m_Context, 5);
                            builder.setTitle(m_Context.getString(R.string.erorserver));
                            builder.setMessage(msg.obj.toString());
                            builder.setCancelable(false);
                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {}
                                    });
                            builder.show();
                        }
                        dialogloading.dismiss();
                    }
                };

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String result = native_Check(m_Context, userKey, modeSelect);
                        if (result.equals("OK")) {
                            loginHandler.sendEmptyMessage(0);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            loginHandler.sendMessage(msg);
                        }
                    }
                })
                .start();
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            OverlayPermision();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            InstllUnknownApp();
        } else if (requestCode == REQUEST_MANAGE_UNKNOWN_APP_SOURCES) {
            if (!isPermissionGaranted()) {
                takeFilePermissions();
            }
        }
    }

    private void setLokasi(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bahasa", lang);
        editor.apply();
    }

    private void loadbahasa() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String bahasa = sharedPreferences.getString("bahasa", "");
        setLokasi(bahasa);
    }

    private static native String native_Check(Context context, String userKey, String modeSelect);

   // private static native String Check(Context mContext, String userKey, String level);
}
