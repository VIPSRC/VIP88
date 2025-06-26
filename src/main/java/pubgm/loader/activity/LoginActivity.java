package pubgm.loader.activity;


import static com.topjohnwu.superuser.internal.Utils.context;
import static pubgm.loader.activity.SplashActivity.mahyong;
import static pubgm.loader.server.ApiServer.URLJSON;
import static pubgm.loader.server.ApiServer.getOwner;
import static pubgm.loader.server.ApiServer.getTelegram;
import static pubgm.loader.server.ApiServer.mainURL;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import net_62v.external.MetaActivationManager;
import pubgm.loader.Component.DownC;
import pubgm.loader.Component.Prefs;
import pubgm.loader.R;
import pubgm.loader.utils.ActivityCompat;
import pubgm.loader.utils.FLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.topjohnwu.superuser.Shell;

import java.util.ArrayList;
import java.util.Locale;

import io.michaelrocks.paranoid.Obfuscate;
import pubgm.loader.utils.LanguageItem;
import pubgm.loader.utils.LanguageSpinnerAdapter;
import pubgm.loader.utils.myTools;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : JungliCheats
 * *************************/


@Obfuscate
public class LoginActivity extends ActivityCompat {

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }
    private myTools m;
    private static final String QUESTION = "Q: %s";
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    private static final String USER = "USER";
    private static final String PASS = "PASS";
    public static String USERKEY, PASSKEY;
    LinearLayoutCompat btnSignIn;
    private Prefs prefs;
    private boolean isCardExpanded = false;

    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLightStatusBar(this);
        m = new myTools(this);
        setContentView(R.layout.activity_login);
        if (!mahyong){
            finish();
            finishActivity(1);
        }
        initializeLanguageSpinner();

        initDesign();
        OverlayPermision();

    }



    public void initDesign(){

        prefs = Prefs.with(this);
        final Context m_Context = this;
        final EditText textUsername = findViewById(R.id.textUsername);
        final EditText textPassword = findViewById(R.id.textPassword);

        ImageView pasteOrCut = findViewById(R.id.paste);
        textUsername.setText(prefs.read(USER, ""));
        textPassword.setText(prefs.read(PASS, ""));
        // Set the correct icon during initialization
        if (!textUsername.getText().toString().isEmpty() || !textPassword.getText().toString().isEmpty()) {
            pasteOrCut.setImageResource(R.drawable.ic_close); // Show cut icon if text is filled
        } else {
            pasteOrCut.setImageResource(R.drawable.ic_paste); // Show paste icon if text is empty
        }
        btnSignIn = findViewById(R.id.loginBtn);
        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!textUsername.getText().toString().isEmpty()
                                && !textPassword.getText().toString().isEmpty()) {
                            prefs.write(USER, textUsername.getText().toString());
                            prefs.write(PASS, textPassword.getText().toString());
                            String userKey = textUsername.getText().toString().trim();
                            String passKey = textPassword.getText().toString().trim();
                            Login(LoginActivity.this, userKey);
                            USERKEY = userKey;
                            PASSKEY = passKey;
                        }

                        if (textUsername.getText().toString().isEmpty()
                                && textPassword.getText().toString().isEmpty()) {
                            textUsername.setError(getString(R.string.please_enter_username));
                            textPassword.setError(getString(R.string.please_enter_password));
                        }
                        if (textUsername.getText().toString().isEmpty()) {
                            textUsername.setError(getString(R.string.please_enter_username));
                        }
                        if (textPassword.getText().toString().isEmpty()) {
                            textPassword.setError(getString(R.string.please_enter_password));
                        }
                    }
                });


        pasteOrCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if text is already present in the EditTexts
                if (!textUsername.getText().toString().isEmpty() || !textPassword.getText().toString().isEmpty()) {
                    // Text is already pasted, perform "cut" action
                    textUsername.setText("");
                    textPassword.setText("");

                    // Switch to paste icon
                    pasteOrCut.setImageResource(R.drawable.ic_paste);
                } else {
                    // Perform "paste" action
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                    if (clipboardManager != null && clipboardManager.getPrimaryClip() != null) {
                        ClipData clipData = clipboardManager.getPrimaryClip();
                        if (clipData.getItemCount() > 0) {
                            CharSequence pastedText = clipData.getItemAt(0).getText();
                            if (pastedText != null && pastedText.length() > 5) {
                                textUsername.setText(pastedText.toString());
                                textPassword.setText(pastedText.toString());

                                // Switch to cut icon
                                pasteOrCut.setImageResource(R.drawable.ic_close);
                            } else {
                                Toast.makeText(m_Context, getString(R.string.please_copy_licence_and_paste), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(m_Context, "Clipboard is empty or unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        LinearLayoutCompat getKey = findViewById(R.id.telegram);
        getKey.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getOwner()));
                        startActivity(intent);
                    }
                });
        LinearLayoutCompat store = findViewById(R.id.store);
        store.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getTelegram()));
                        startActivity(intent);
                    }
                });

    }

    private void initializeLanguageSpinner() {
        languageSpinner = findViewById(R.id.splang);

        // Create language options with display names
        ArrayList<LanguageItem> languageList = new ArrayList<>();
        languageList.add(new LanguageItem("🇮🇳 English", "en"));
        languageList.add(new LanguageItem("🇨🇳 中文", "zh"));
        languageList.add(new LanguageItem("🇸🇦 العربية", "ar"));
        languageList.add(new LanguageItem("🇷🇺 Русский", "ru"));

        // Create adapter with a nice looking dropdown
        LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(this,
                android.R.layout.simple_spinner_item,
                languageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Get the saved language or system default
        String currentLang = m.getSt("myKey", "mapLang", null);
        if (currentLang == null) {
            // If no saved language, use system's default language
            currentLang = Locale.getDefault().getLanguage();
        }

        // Set initial selection based on saved or system default language
        for (int i = 0; i < languageList.size(); i++) {
            if (languageList.get(i).getCode().equals(currentLang)) {
                languageSpinner.setSelection(i); // Set the default selection
                break;
            }
        }

        // Set selection listener for language change
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isInitialSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Skip the first automatic selection
                if (isInitialSelection) {
                    isInitialSelection = false;
                    return;
                }

                LanguageItem selectedLanguage = (LanguageItem) parent.getItemAtPosition(position);
                String langCode = selectedLanguage.getCode();

                // If the selected language is different, change the language
                if (!langCode.equals(m.getSt("myKey", "mapLang", "en"))) {
                    m.setLocale(LoginActivity.this, langCode); // Update locale based on selection
                    m.setSt("myKey", "mapLang", langCode); // Save the selected language
                    recreate(); // Recreate activity to apply language change
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        languageSpinner.setBackground(getDrawable(R.drawable.custom_spinner_background));
    }


    private void setLightStatusBar(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        activity.getWindow().setNavigationBarColor(Color.parseColor("#FFFFFF"));
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




    private static void Login(final LoginActivity m_Context, final String userKey) {
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
                            new DownC(m_Context).execute(URLJSON(), mainURL());
                            if(!Shell.rootAccess()) {
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
                                    e.printStackTrace();
                                }
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
                                        public void onClick(DialogInterface dialog, int which) {
                                           /// System.exit(0);
                                        }
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
                        String result = native_Check(m_Context, userKey);
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

    private static native String native_Check(Context context, String userKey);
}
