package pubgm.loader.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import pubgm.loader.Component.Utils;
import pubgm.loader.R;
import pubgm.loader.utils.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class SplashActivity extends ActivityCompat {

    public static boolean mahyong = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mahyong = true;
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
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