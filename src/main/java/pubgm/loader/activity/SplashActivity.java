package pubgm.loader.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import pubgm.loader.server.ApiServer;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

import pubgm.loader.R;
import pubgm.loader.utils.ActivityCompat;

import io.michaelrocks.paranoid.Obfuscate;
import pubgm.loader.utils.myTools;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : JungliCheats
 * *************************/


@Obfuscate
public class SplashActivity extends ActivityCompat {
    myTools m = new myTools(this);
    public static boolean mahyong = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        seaawdawdw();
        if (!ApiServer.AppChecker.verifyAppLabel(this)) {
            finish();
        }

        String currentLang = m.getSt("myKey", "mapLang", "en");
        setLocale(this, currentLang);
        TextView kowadk = findViewById(R.id.tv_description);
        kowadk.setSelected(true);
        LottieAnimationView lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView.setAnimation(R.raw.anim_splash);
        lottieAnimationView.animate().setStartDelay(10000);
        lottieAnimationView.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mahyong = true;
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
    private void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Create new configuration with the updated locale
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        // Create a new configuration context and apply it
        Context localizedContext = context.createConfigurationContext(config);

        // Update the resources with the new configuration context
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Optionally store the language setting if you are persisting it
        m.setSt("myKey", "mapLang", languageCode);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void seaawdawdw() {
        String[] packageNamesToCheck = {"com.guoshi.httpcanary.premium", "com.guoshi.httpcanary", "com.sniffer", "com.httpcanary.pro","com.httpcanary.*","com.*.httpcanary"};
        for (String packageName : packageNamesToCheck) {
            boolean isInstalled = isAppInstalled(this, packageName);
            if (isInstalled) {
                mahyong = false;
                System.out.println("Aplikasi " + packageName + " terdeteksi!");
                finish();
                finishActivity(1);
                toastImage(R.drawable.ic_error,getString(R.string.please_delete_your_vpn_cannary));
            } else {
                System.out.println("Aplikasi " + packageName + " tidak terdeteksi.");
            }
        }
    }


}