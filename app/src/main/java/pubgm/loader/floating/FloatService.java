package pubgm.loader.floating;

import static pubgm.loader.activity.LoginActivity.USERKEY;
import static pubgm.loader.activity.MainActivity.Ischeck;
import static pubgm.loader.activity.MainActivity.bitversi;
import static pubgm.loader.activity.MainActivity.gameint;
import static pubgm.loader.activity.MainActivity.game;
import static pubgm.loader.activity.MainActivity.modestatus;
import static pubgm.loader.floating.FloatRei.toastImage;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import pubgm.loader.R;
import pubgm.loader.activity.MainActivity;
import pubgm.loader.utils.FLog;
import com.topjohnwu.superuser.Shell;

import static pubgm.loader.activity.LoginActivity.Kooontoool;

import java.util.Locale;

import io.michaelrocks.paranoid.Obfuscate;


@Obfuscate
public class FloatService extends Service {

    static {
        try {
            System.loadLibrary("client");
        } catch (UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    Context ctx;
    private View mainView;
    private PowerManager.WakeLock mWakeLock;
    private WindowManager windowManagerMainView;
    private WindowManager.LayoutParams paramsMainView;
    private LinearLayout layout_main_view;
    private RelativeLayout layout_icon_control_view;
    public static String typelogin;

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

    private static int getLayoutType() {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        return LAYOUT_FLAG;
    }

    private void StartAimTouch() {
        startService(new Intent(getApplicationContext(), ToggleSimulation.class));
    }

    private void StopAimTouch() {
        stopService(new Intent(getApplicationContext(), ToggleSimulation.class));
    }

    private void StartAimFloat() {
        startService(new Intent(getApplicationContext(), ToggleAim.class));
    }

    private void StopAimFloat() {
        stopService(new Intent(getApplicationContext(), ToggleAim.class));
    }

    private void StartAimBulletFloat() {
        startService(new Intent(getApplicationContext(), ToggleBullet.class));
    }

    private void StopAimBulletFloat() {
        stopService(new Intent(getApplicationContext(), ToggleBullet.class));
    }

    public native void SettingValue(int setting_code, boolean value);

    public native void SettingMemory(int setting_code, boolean value);

    public native void SettingAim(int setting_code, boolean value);

    public native void SkinHack(int setting_code);

    public native void RadarSize(int size);

    public native void Range(int range);

    public native void recoil(int recoil);

    public native void recoil2(int recoil);

    public native void recoil3(int recoil);

    public native void Target(int target);

    public native void AimBy(int aimby);

    public native void AimWhen(int aimwhen);

    public native void distances(int distances);

    public native void Bulletspeed(int bulletspeed);

    public native void WideView(int wideview);

    public native void AimingSpeed(int aimingspeed);

    public native void Smoothness(int smoothness);

    public native void TouchSize(int touchsize);

    public native void TouchPosX(int touchposx);

    public native void TouchPosY(int touchposy);


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        InitShowMainView();
        loadbahasa();
    }


    private void InitShowMainView() {
        mainView = LayoutInflater.from(this).inflate(R.layout.float_service, null);
        paramsMainView = getparams();
        windowManagerMainView = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerMainView.addView(mainView, paramsMainView);
        layout_icon_control_view = mainView.findViewById(R.id.layout_icon_control_view);
        layout_main_view = mainView.findViewById(R.id.layout_main_view);

        View layout_close_main_view = mainView.findViewById(R.id.layout_close_main_view);
        layout_close_main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                layout_main_view.setVisibility(View.GONE);
                layout_icon_control_view.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout layout_view = mainView.findViewById(R.id.layout_view);
        layout_view.setOnTouchListener(onTouchListener());

        initDesign();
        visual(mainView);
        aimbot(mainView);
        items(mainView);
        memory(mainView);
        // car(mainView);
    }

    public void initDesign() {
        LinearLayout visual = mainView.findViewById(R.id.navf1);
        LinearLayout items = mainView.findViewById(R.id.navf2);
        LinearLayout aimbot = mainView.findViewById(R.id.navf3);
        LinearLayout memory = mainView.findViewById(R.id.navf4);
        LinearLayout car = mainView.findViewById(R.id.navf5);
        LinearLayout skin = mainView.findViewById(R.id.navf6);

        LinearLayout menu1 = mainView.findViewById(R.id.menuf1);
        LinearLayout menu2 = mainView.findViewById(R.id.menuf2);
        LinearLayout menu3 = mainView.findViewById(R.id.menuf3);
        LinearLayout menu4 = mainView.findViewById(R.id.menuf4);
        LinearLayout menu5 = mainView.findViewById(R.id.menuf5);
        //  LinearLayout menu5 = mainView.findViewById(R.id.menuf5);

       /* View bottom1 = mainView.findViewById(R.id.bottom1);
        View bottom2 = mainView.findViewById(R.id.bottom2);
        View bottom3 = mainView.findViewById(R.id.bottom3);
        View bottom4 = mainView.findViewById(R.id.bottom4);*/

        LinearLayout navi1 = mainView.findViewById(R.id.navitems);
        LinearLayout navi2 = mainView.findViewById(R.id.navvehicle);
        LinearLayout menui1 = mainView.findViewById(R.id.items1);
        LinearLayout menui2 = mainView.findViewById(R.id.lyvehicle);
        View bottomi1 = mainView.findViewById(R.id.bottomi1);
        View bottomi2 = mainView.findViewById(R.id.bottomi2);
        View head1 = mainView.findViewById(R.id.head1);
        View head2 = mainView.findViewById(R.id.head2);
        View head3 = mainView.findViewById(R.id.head3);
        View head4 = mainView.findViewById(R.id.head4);
        View head5 = mainView.findViewById(R.id.head5);
        View head6 = mainView.findViewById(R.id.head6);


        visual.setOnClickListener(v -> {
            menu1.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);

            menu5.setVisibility(View.GONE);
            head6.setVisibility(View.GONE);

            head1.setVisibility(View.VISIBLE);
            head2.setVisibility(View.GONE);
            head3.setVisibility(View.GONE);
            head4.setVisibility(View.GONE);
            head5.setVisibility(View.GONE);
        });

        items.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);

            menu5.setVisibility(View.GONE);
            head6.setVisibility(View.GONE);

            menui1.setVisibility(View.VISIBLE);
            menui2.setVisibility(View.GONE);

            bottomi1.setVisibility(View.VISIBLE);
            bottomi2.setVisibility(View.GONE);
                
          /*  visual.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            items.setBackgroundDrawable(getDrawable(R.drawable.bgfituronfl));
            aimbot.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            memory.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            car.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl)); */

            head1.setVisibility(View.GONE);
            head2.setVisibility(View.VISIBLE);
            head3.setVisibility(View.GONE);
            head4.setVisibility(View.GONE);
            head5.setVisibility(View.GONE);
        });

        aimbot.setOnClickListener(v -> {

            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.VISIBLE);
            menu4.setVisibility(View.GONE);

            menu5.setVisibility(View.GONE);
            head6.setVisibility(View.GONE);
                
          /*  visual.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            items.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            aimbot.setBackgroundDrawable(getDrawable(R.drawable.bgfituronfl));
            memory.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));   
            car.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl)); */

            head1.setVisibility(View.GONE);
            head2.setVisibility(View.GONE);
            head3.setVisibility(View.VISIBLE);
            head4.setVisibility(View.GONE);
            head5.setVisibility(View.GONE);
        });

        memory.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.VISIBLE);


            menu5.setVisibility(View.GONE);
            head6.setVisibility(View.GONE);

           /* visual.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            items.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            aimbot.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            memory.setBackgroundDrawable(getDrawable(R.drawable.bgfituronfl));   
            car.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl)); */

            head1.setVisibility(View.GONE);
            head2.setVisibility(View.GONE);
            head3.setVisibility(View.GONE);
            head4.setVisibility(View.VISIBLE);
            head5.setVisibility(View.GONE);
        });

        car.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);

            menu5.setVisibility(View.GONE);
            head6.setVisibility(View.GONE);

            menui1.setVisibility(View.GONE);
            menui2.setVisibility(View.VISIBLE);

            bottomi1.setVisibility(View.GONE);
            bottomi2.setVisibility(View.VISIBLE);
                
       /*     visual.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            items.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            aimbot.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));
            memory.setBackgroundDrawable(getDrawable(R.drawable.bgfiturofffl));   
            car.setBackgroundDrawable(getDrawable(R.drawable.bgfituronfl)); */


            head1.setVisibility(View.GONE);
            head2.setVisibility(View.GONE);
            head3.setVisibility(View.GONE);
            head4.setVisibility(View.GONE);
            head5.setVisibility(View.VISIBLE);
        });

        skin.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);

            menu5.setVisibility(View.VISIBLE);
            head6.setVisibility(View.VISIBLE);

            head1.setVisibility(View.GONE);
            head2.setVisibility(View.GONE);
            head3.setVisibility(View.GONE);
            head4.setVisibility(View.GONE);
            head5.setVisibility(View.GONE);
        });



     /*   navi1.setOnClickListener(v -> {
            menui1.setVisibility(View.VISIBLE);
            menui2.setVisibility(View.GONE);

            bottomi1.setVisibility(View.VISIBLE);
            bottomi2.setVisibility(View.GONE);
        });
        
        /*
LinearLayout navi2 = items.findViewById(R.id.menuf4);
        navi2.setOnClickListener(v -> {
            menui1.setVisibility(View.GONE);
            menui2.setVisibility(View.VISIBLE);

            bottomi1.setVisibility(View.GONE);
            bottomi2.setVisibility(View.VISIBLE);
        });
*/

    }



/*
    private void AdapterNavigation() {
        Dapter vpadapter = new Dapter(ctx);
        ViewPager viewPager = mainView.findViewById(R.id.viewPager);
        viewPager.setAdapter(vpadapter);
        viewPager.setOffscreenPageLimit(4);

        View bottom1 = mainView.findViewById(R.id.bottom1);
        View bottom2 = mainView.findViewById(R.id.bottom2);
        View bottom3 = mainView.findViewById(R.id.bottom3);
        View bottom4 = mainView.findViewById(R.id.bottom4);
        LinearLayout visual = mainView.findViewById(R.id.navf1);
        LinearLayout items = mainView.findViewById(R.id.navf2);
        LinearLayout aimbot = mainView.findViewById(R.id.navf3);
        LinearLayout memory = mainView.findViewById(R.id.navf4);

        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int aa = v.getId();
                viewPager.setCurrentItem(aa == R.id.navf1 ? 0 : aa == R.id.navf2 ? 1 : aa == R.id.navf3 ? 2 : 3);

            }
        };

        visual.setOnClickListener(oc);
        items.setOnClickListener(oc);
        aimbot.setOnClickListener(oc);
        memory.setOnClickListener(oc);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottom1.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                bottom2.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
                bottom3.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                bottom4.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
*/


    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            final View collapsedView = layout_icon_control_view;
            final View expandedView = layout_main_view;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsMainView.x;
                        initialY = paramsMainView.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        paramsMainView.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsMainView.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManagerMainView.updateViewLayout(mainView, paramsMainView);
                        return true;

                }
                return false;
            }
        };
    }

    private boolean isViewCollapsed() {
        return mainView == null || layout_icon_control_view.getVisibility() == View.VISIBLE;
    }

    private WindowManager.LayoutParams getparams() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getLayoutType(),
                getFlagsType(),
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        return params;
    }

    private int getFlagsType() {
        int LAYOUT_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        return LAYOUT_FLAG;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }

        if (mainView != null) {
            windowManagerMainView.removeView(mainView);
        }
    }

    boolean getConfig(String key) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    private int getFps() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("fps", 100);
    }

    private void setFps(int fps) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("fps", fps);
        ed.apply();
    }

    private void setValue(String key, boolean b) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key, b);
        ed.apply();

    }

    private void setradarSize(int radarSize) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("radarSize", radarSize);
        ed.apply();
    }

    private int getradarSize() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("radarSize", 0);
    }

    private int getrangeAim() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getrangeAim", 0);
    }

    private void getrangeAim(int getrangeAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrangeAim", getrangeAim);
        ed.apply();
    }

    private int getDistances() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("Distances", 0);
    }

    private void setDistances(int Distances) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("Distances", Distances);
        ed.apply();
    }

    private int getrecoilAim() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getrecoilAim", 0);
    }

    private void getrecoilAim(int getrecoilAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrecoilAim", getrecoilAim);
        ed.apply();
    }

    private int getrecoilAim2() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getrecoilAim2", 0);
    }

    private void getrecoilAim2(int getrecoilAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrecoilAim2", getrecoilAim);
        ed.apply();
    }

    private int getrecoilAim3() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getrecoilAim2", 0);
    }

    private void getrecoilAim3(int getrecoilAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrecoilAim2", getrecoilAim);
        ed.apply();
    }

    private int getbulletspeedAim() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getbulletspeedAim", 0);
    }

    private void getbulletspeedAim(int getbulletspeedAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getbulletspeedAim", getbulletspeedAim);
        ed.apply();
    }

    private int getwideview() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getwideview", 0);
    }

    private void getwideview(int getwideview) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getwideview", getwideview);
        ed.apply();
    }

    void setTouchSize(int touchsize) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("touchsize", touchsize);
        ed.apply();
    }

    int getTouchSize() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("touchsize", 600);
    }

    void setTouchPosX(int posX) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("posX", posX);
        ed.apply();
    }

    int getTouchPosX() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("posX", 650);
    }

    void setTouchPosY(int posY) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("posY", posY);
        ed.apply();
    }

    int getTouchPosY() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("posY", 1400);
    }

    private boolean getConfigitem(String key, boolean a) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getBoolean(key, a);
    }

    private void setConfigitem(String a, boolean b) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(a, b);
        ed.apply();
    }

    private int getEspValue(String a, int b) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt(a, b);
    }

    private void setEspValue(String a, int b) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(a, b);
        ed.apply();
    }

    private int getAimSpeed() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("AimingSpeed", 18);
    }

    private void setAimSpeed(int AimingSpeed) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("AimingSpeed", AimingSpeed);
        ed.apply();
    }

    private int getSmoothness() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("smoothness", 20);
    }

    private void setSmoothness(int smoothness) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("smoothness", smoothness);
        ed.apply();
    }

    public void skinvisual(final CheckBox a, final int b) {
        a.setChecked(getConfig((String) a.getText()));
        SkinHack(b);
        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                setValue(String.valueOf(a.getText()), a.isChecked());
                SkinHack(b);
            }
        });
    }

    public void espvisual(final CheckBox a, final int b) {
        a.setChecked(getConfig((String) a.getText()));
        SettingValue(b, getConfig((String) a.getText()));
        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                setValue(String.valueOf(a.getText()), a.isChecked());
                SettingValue(b, a.isChecked());
            }
        });
    }

    public void setaim(final Switch a, final int b) {
        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean isChecked) {
                setValue(String.valueOf(a.getText()), a.isChecked());
                SettingAim(b, a.isChecked());
            }
        });
    }

    public void vehicless(final CheckBox checkBox) {
        checkBox.setChecked(getConfig((String) checkBox.getText()));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(checkBox.getText()), checkBox.isChecked());
            }
        });
    }

    public void itemss(final CheckBox checkBox) {
        checkBox.setChecked(getConfig((String) checkBox.getText()));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setValue(String.valueOf(checkBox.getText()), checkBox.isChecked());
            }
        });
    }

    public void memory(final Switch a, final int b) {
        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean isChecked) {
                setValue(String.valueOf(a.getText()), a.isChecked());
                SettingMemory(b, a.isChecked());
            }
        });
    }

    void setupSeekBar(final SeekBar seekBar, final TextView textView, final int initialValue, final Runnable onChangeFunction) {
        seekBar.setProgress(initialValue);
        textView.setText(String.valueOf(initialValue));
        onChangeFunction.run();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(String.valueOf(progress));
                onChangeFunction.run();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void DrawESP() {
        if (Shell.rootAccess()) {
            FLog.info("Root granted");
            MainActivity.socket = "su -c " + MainActivity.daemonPath;
            startService(new Intent(this, Overlay.class));
        } else {
            FLog.info("Root not granted");
            MainActivity.socket = MainActivity.daemonPath;
            startService(new Intent(MainActivity.get(), Overlay.class));
        }
    }

    public void Exec(String path, String toast) {
        try {
            ExecuteElf("su -c chmod 777 " + getFilesDir() + path);
            ExecuteElf("su -c " + getFilesDir() + path);
            ExecuteElf("chmod 777 " + getFilesDir() + path);
            ExecuteElf(getFilesDir() + path);
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


    private void StopESP() {
        stopService(new Intent(this, Overlay.class));
    }

    public void Skin (View view){
        final CheckBox pharaoh = view.findViewById(R.id.pharaohskin);
        final CheckBox bloodreven = view.findViewById(R.id.bloodreven);
        final CheckBox posreidon = view.findViewById(R.id.posreidon);
        final CheckBox avalache = view.findViewById(R.id.avalache);
        final CheckBox silvanus = view.findViewById(R.id.silvanus);
        final CheckBox iridescense = view.findViewById(R.id.iridescense);
        final CheckBox aracane = view.findViewById(R.id.aracane);

        skinvisual(pharaoh,1);
        skinvisual(bloodreven,2);
        skinvisual(posreidon,3);
        skinvisual(avalache,4);
        skinvisual(silvanus,5);
        skinvisual(iridescense,6);
        skinvisual(aracane,7);
    }

    private void visual(View visual) {
        final TextView protectiontext = visual.findViewById(R.id.protectiontext);
        final Switch iscrash = visual.findViewById(R.id.iscrash);
        final Switch isisland = visual.findViewById(R.id.isisland);
        final Switch isfixed = visual.findViewById(R.id.isfixed);
        final Switch drawesp = visual.findViewById(R.id.isenableesp);
        final LinearLayout menuisland = visual.findViewById(R.id.menuisland);
        final LinearLayout menuloho = visual.findViewById(R.id.menuloho);
        final LinearLayout menucrash = visual.findViewById(R.id.menucrash);
        final ImageView imgfixed = visual.findViewById(R.id.imgfixed);
        final ImageView imgisland = visual.findViewById(R.id.imgisland);


        drawesp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    DrawESP();
                } else {
                    StopESP();
                    StopAimFloat();
                    StopAimBulletFloat();
                    StopAimTouch();
                }
            }
        });
        //  NONROOT BYPSS

            isfixed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (bitversi == 64) {
                            if (gameint == 5) {     // BGMI NONROOT BYPSS
                                Exec("/TW " + game + "", getString(R.string.BYPASS_64_ENABLE));


                            } else {                // GLOBAL NONROOT BYPSS
                                Exec("/TW " + game + "", getString(R.string.BYPASS_64_ENABLE));

                            }
                        } else if (bitversi == 32) {
                            if (gameint == 5) {
                                //  Exec("/TW " + game + " 006", getString(R.string.BYPASS_32_ENABLE));
                            } else {
                                //  Exec("/TW " + game + " 33", getString(R.string.BYPASS_32_ENABLE));
                            }
                        }
                    }
                }
            });

            iscrash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (bitversi == 64) {
                            if (gameint == 5) {     // BGMI NONROOT BYPSS
                                Exec("/TW " + game + "", getString(R.string.BYPASS_64_ENABLE));


                            } else {                // GLOBAL NONROOT BYPSS
                                Exec("/TW " + game + "", getString(R.string.BYPASS_64_ENABLE));

                            }
                        } else if (bitversi == 32) {
                            if (gameint == 5) {
                                Exec("/TW " + game + "", getString(R.string.BYPASS_32_ENABLE));
                            } else {
                                Exec("/TW " + game + "", getString(R.string.BYPASS_32_ENABLE));
                            }
                        }
                    }
                }
            });
        


        // ISLAND BYPSS


            isisland.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    switch (gameint) {
                        case 1:
                        case 2:
                        case 3:            //   GL KR VT TW
                        case 4:
                            if (isChecked) {
                                Exec("/VN " + game + "8", getString(R.string.REPORT_64_BLOCK_ENABLE));
                            } else {
                                Exec("/VN " + game + "9", getString(R.string.REPORT_64_BLOCK_DISIBLE));
                            }
                            break;

                        case 5:           // BGMI
                            if (isChecked) {
                                Exec("/VN " + game + "8", getString(R.string.REPORT_BGMI_BLOCK_ENABLE));
                            } else {
                                Exec("/VN " + game + "9", getString(R.string.REPORT_BGMI_BLOCK_DISIBLE));
                            }
                            break;
                    }
                }
            });
        
        final SeekBar radarSizeSeekBar = visual.findViewById(R.id.strokeradar);
        final TextView radarSizeText = visual.findViewById(R.id.radartext);

        setupSeekBar(radarSizeSeekBar, radarSizeText, getradarSize(), new Runnable() {
            @Override
            public void run() {
                int pos = radarSizeSeekBar.getProgress();
                setradarSize(pos);
                RadarSize(pos);
                String a = String.valueOf(pos);
                radarSizeText.setText(a);
            }
        });


        final RadioButton fps3 = visual.findViewById(R.id.fps60);
        final RadioButton fps4 = visual.findViewById(R.id.fps120);
        final RadioButton fps5 = visual.findViewById(R.id.fps130);

        int CheckFps = getFps();
        if (CheckFps == 60) {
            fps3.setChecked(true);
            ESPView.sleepTime = 1000 / 60;
        } else if (CheckFps == 90) {
            fps4.setChecked(true);
            ESPView.sleepTime = 1000 / 90;
        } else if (CheckFps == 120) {
            fps5.setChecked(true);
            ESPView.sleepTime = 1000 / 120;
        } else {
            fps3.setChecked(true);
            ESPView.sleepTime = 1000 / 60;
        }


        fps3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps4.setChecked(false);
                    fps5.setChecked(false);
                    setFps(60);
                    ESPView.ChangeFps(60);
                }
            }
        });

        fps4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps3.setChecked(false);
                    fps5.setChecked(false);
                    setFps(90);
                    ESPView.ChangeFps(90);
                }
            }
        });

        fps5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps3.setChecked(false);
                    fps4.setChecked(false);
                    setFps(120);
                    ESPView.ChangeFps(120);
                }
            }
        });

        SharedPreferences sharedPreferences = visual.getContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        String bypassmode = sharedPreferences.getString("bypassmode", "manual");

        if (Shell.rootAccess()) {
            visual.findViewById(R.id.protectiontext).setVisibility(View.GONE);
            visual.findViewById(R.id.menucrash).setVisibility(View.GONE);
            visual.findViewById(R.id.menuloho).setVisibility(View.GONE);
            // visual.findViewById(R.id.menuisland).setVisibility(View.GONE);
        } else {
            if (bypassmode.equals("automatic")) {
                visual.findViewById(R.id.protectiontext).setVisibility(View.GONE);
                visual.findViewById(R.id.menuloho).setVisibility(View.GONE);
                //visual.findViewById(R.id.menuisland).setVisibility(View.GONE);
            } else {
                visual.findViewById(R.id.protectiontext).setVisibility(View.VISIBLE);
                visual.findViewById(R.id.menuloho).setVisibility(View.VISIBLE);
                // visual.findViewById(R.id.menuisland).setVisibility(View.GONE);
            }
            visual.findViewById(R.id.menucrash).setVisibility(View.GONE);
            //visual.findViewById(R.id.menuisland).setVisibility(View.GONE);
        }


        final CheckBox isLine = visual.findViewById(R.id.isline);
        espvisual(isLine, 2);
        final CheckBox isbox = visual.findViewById(R.id.isBox);
        espvisual(isbox, 3);
        final CheckBox isskeleton = visual.findViewById(R.id.isskeleton);
        espvisual(isskeleton, 4);
        final CheckBox isdistance = visual.findViewById(R.id.isdistance);
        espvisual(isdistance, 5);
        final CheckBox ishealth = visual.findViewById(R.id.ishealth);
        espvisual(ishealth, 6);
        final CheckBox isname = visual.findViewById(R.id.isName);
        espvisual(isname, 7);
        final CheckBox ishead = visual.findViewById(R.id.ishead);
        espvisual(ishead, 8);
        final CheckBox isalert = visual.findViewById(R.id.isalert);
        espvisual(isalert, 9);
        final CheckBox isweapon = visual.findViewById(R.id.isweapon);
        espvisual(isweapon, 10);
        final CheckBox isthrowables = visual.findViewById(R.id.isthrowables);
        espvisual(isthrowables, 11);
        
        final CheckBox isnobot = visual.findViewById(R.id.isnobot);
        espvisual(isnobot, 15);
        final CheckBox isweaponicon = visual.findViewById(R.id.isweaponicon);
        espvisual(isweaponicon, 16);
        final CheckBox islootbox = visual.findViewById(R.id.islootbox);
        espvisual(islootbox, 14);
        final CheckBox isNation = visual.findViewById(R.id.isNation);
        espvisual(isNation, 222);
        final CheckBox isPlayerUID = visual.findViewById(R.id.isPlayerUID);
        espvisual(isPlayerUID, 111);

    }

    private void items(View items) {
        LinearLayout menui1 = items.findViewById(R.id.items1);
        LinearLayout menui2 = items.findViewById(R.id.lyvehicle);
        View bottomi1 = items.findViewById(R.id.bottomi1);
        View bottomi2 = items.findViewById(R.id.bottomi2);
        // LinearLayout navi1 = items.findViewById(R.id.navitems);
        

     /*   navi1.setOnClickListener(v -> {
            menui1.setVisibility(View.VISIBLE);
            menui2.setVisibility(View.GONE);
            bottomi1.setVisibility(View.VISIBLE);
            bottomi2.setVisibility(View.GONE);
        });

        navi2.setOnClickListener(v -> {
            menui1.setVisibility(View.GONE);
            menui2.setVisibility(View.VISIBLE);
            bottomi1.setVisibility(View.GONE);
            bottomi2.setVisibility(View.VISIBLE);
        });*/

        final CheckBox lootbox = items.findViewById(R.id.lootbox);
        espvisual(lootbox, 14);

        final CheckBox Desert = items.findViewById(R.id.Desert);
        itemss(Desert);

        final CheckBox M416 = items.findViewById(R.id.m416);
        itemss(M416);

        final CheckBox QBZ = items.findViewById(R.id.QBZ);
        itemss(QBZ);

        final CheckBox SCARL = items.findViewById(R.id.SCARL);
        itemss(SCARL);

        final CheckBox AKM = items.findViewById(R.id.AKM);
        itemss(AKM);

        final CheckBox M16A4 = items.findViewById(R.id.M16A4);
        itemss(M16A4);

        final CheckBox AUG = items.findViewById(R.id.AUG);
        itemss(AUG);

        final CheckBox M249 = items.findViewById(R.id.M249);
        itemss(M249);

        final CheckBox Groza = items.findViewById(R.id.Groza);
        itemss(Groza);

        final CheckBox MK47 = items.findViewById(R.id.MK47);
        itemss(MK47);

        final CheckBox M762 = items.findViewById(R.id.M762);
        itemss(M762);

        final CheckBox G36C = items.findViewById(R.id.G36C);
        itemss(G36C);

        final CheckBox DP28 = items.findViewById(R.id.DP28);
        itemss(DP28);

        final CheckBox MG3 = items.findViewById(R.id.MG3);
        itemss(MG3);

        final CheckBox FAMAS = items.findViewById(R.id.FAMAS);
        itemss(FAMAS);


        final CheckBox HoneyBadger = items.findViewById(R.id.HoneyBadger);
        itemss(HoneyBadger);


        final CheckBox AC32 = items.findViewById(R.id.AC32);
        itemss(AC32);


        //SMG

        final CheckBox UMP = items.findViewById(R.id.UMP);
        itemss(UMP);

        final CheckBox bizon = items.findViewById(R.id.bizon);
        itemss(bizon);

        final CheckBox MP5K = items.findViewById(R.id.MP5K);
        itemss(MP5K);

        final CheckBox TommyGun = items.findViewById(R.id.TommyGun);
        itemss(TommyGun);

        final CheckBox vector = items.findViewById(R.id.vector);
        itemss(vector);

        final CheckBox P90 = items.findViewById(R.id.P90);
        itemss(P90);

        final CheckBox UZI = items.findViewById(R.id.UZI);
        itemss(UZI);


        //Snipers

        final CheckBox AWM = items.findViewById(R.id.AWM);
        itemss(AWM);

        final CheckBox QBU = items.findViewById(R.id.QBU);
        itemss(QBU);

        final CheckBox Kar98k = items.findViewById(R.id.Kar98k);
        itemss(Kar98k);

        final CheckBox M24 = items.findViewById(R.id.M24);
        itemss(M24);

        final CheckBox SLR = items.findViewById(R.id.SLR);
        itemss(SLR);

        final CheckBox SKS = items.findViewById(R.id.SKS);
        itemss(SKS);

        final CheckBox MK14 = items.findViewById(R.id.MK14);
        itemss(MK14);

        final CheckBox Mini14 = items.findViewById(R.id.Mini14);
        itemss(Mini14);

        final CheckBox Mosin = items.findViewById(R.id.Mosin);
        itemss(Mosin);

        final CheckBox VSS = items.findViewById(R.id.VSS);
        itemss(VSS);

        final CheckBox AMR = items.findViewById(R.id.AMR);
        itemss(AMR);

        final CheckBox Win94 = items.findViewById(R.id.Win94);
        itemss(Win94);

        final CheckBox MK12 = items.findViewById(R.id.MK12);
        itemss(MK12);

        //Scopes

        final CheckBox x2 = items.findViewById(R.id.x2);
        itemss(x2);

        final CheckBox x3 = items.findViewById(R.id.x3);
        itemss(x3);

        final CheckBox x4 = items.findViewById(R.id.x4);
        itemss(x4);

        final CheckBox x6 = items.findViewById(R.id.x6);
        itemss(x6);

        final CheckBox x8 = items.findViewById(R.id.x8);
        itemss(x8);

        final CheckBox canted = items.findViewById(R.id.canted);
        itemss(canted);

        final CheckBox hollow = items.findViewById(R.id.hollow);
        itemss(hollow);

        final CheckBox reddot = items.findViewById(R.id.reddot);
        itemss(reddot);

        //Armor

        final CheckBox bag1 = items.findViewById(R.id.bag1);
        itemss(bag1);

        final CheckBox bag2 = items.findViewById(R.id.bag2);
        itemss(bag2);

        final CheckBox bag3 = items.findViewById(R.id.bag3);
        itemss(bag3);

        final CheckBox helmet1 = items.findViewById(R.id.helmet1);
        itemss(helmet1);

        final CheckBox helmet2 = items.findViewById(R.id.helmet2);
        itemss(helmet2);

        final CheckBox helmet3 = items.findViewById(R.id.helmet3);
        itemss(helmet3);

        final CheckBox vest1 = items.findViewById(R.id.vest1);
        itemss(vest1);

        final CheckBox vest2 = items.findViewById(R.id.vest2);
        itemss(vest2);

        final CheckBox vest3 = items.findViewById(R.id.vest3);
        itemss(vest3);

        //Ammo
        final CheckBox a9 = items.findViewById(R.id.a9);
        itemss(a9);

        final CheckBox a7 = items.findViewById(R.id.a7);
        itemss(a7);

        final CheckBox a5 = items.findViewById(R.id.a5);
        itemss(a5);

        final CheckBox a300 = items.findViewById(R.id.a300);
        itemss(a300);

        final CheckBox a45 = items.findViewById(R.id.a45);
        itemss(a45);

        final CheckBox Arrow = items.findViewById(R.id.arrow);
        itemss(Arrow);

        final CheckBox BMG50 = items.findViewById(R.id.BMG50);
        itemss(BMG50);

        final CheckBox a12 = items.findViewById(R.id.a12);
        itemss(a12);

        //Shotgun
        final CheckBox DBS = items.findViewById(R.id.DBS);
        itemss(DBS);

        final CheckBox NS2000 = items.findViewById(R.id.NS2000);
        itemss(NS2000);

        final CheckBox S686 = items.findViewById(R.id.S686);
        itemss(S686);

        final CheckBox sawed = items.findViewById(R.id.sawed);
        itemss(sawed);

        final CheckBox M1014 = items.findViewById(R.id.M1014);
        itemss(M1014);

        final CheckBox S1897 = items.findViewById(R.id.S1897);
        itemss(S1897);

        final CheckBox S12K = items.findViewById(R.id.S12K);
        itemss(S12K);

        //Throwables
        final CheckBox grenade = items.findViewById(R.id.grenade);
        itemss(grenade);

        final CheckBox molotov = items.findViewById(R.id.molotov);
        itemss(molotov);

        final CheckBox stun = items.findViewById(R.id.stun);
        itemss(stun);

        final CheckBox smoke = items.findViewById(R.id.smoke);
        itemss(smoke);

        //Medics

        final CheckBox painkiller = items.findViewById(R.id.painkiller);
        itemss(painkiller);

        final CheckBox medkit = items.findViewById(R.id.medkit);
        itemss(medkit);

        final CheckBox firstaid = items.findViewById(R.id.firstaid);
        itemss(firstaid);

        final CheckBox bandage = items.findViewById(R.id.bandage);
        itemss(bandage);

        final CheckBox injection = items.findViewById(R.id.injection);
        itemss(injection);

        final CheckBox energydrink = items.findViewById(R.id.energydrink);
        itemss(energydrink);

        //Handy
        final CheckBox Pan = items.findViewById(R.id.Pan);
        itemss(Pan);

        final CheckBox Crowbar = items.findViewById(R.id.Crowbar);
        itemss(Crowbar);

        final CheckBox Sickle = items.findViewById(R.id.Sickle);
        itemss(Sickle);

        final CheckBox Machete = items.findViewById(R.id.Machete);
        itemss(Machete);

        final CheckBox Crossbow = items.findViewById(R.id.Crossbow);
        itemss(Crossbow);

        final CheckBox Explosive = items.findViewById(R.id.Explosive);
        itemss(Explosive);

        //Pistols
        final CheckBox P92 = items.findViewById(R.id.P92);
        itemss(P92);

        final CheckBox R45 = items.findViewById(R.id.R45);
        itemss(R45);

        final CheckBox P18C = items.findViewById(R.id.P18C);
        itemss(P18C);

        final CheckBox P1911 = items.findViewById(R.id.P1911);
        itemss(P1911);

        final CheckBox R1895 = items.findViewById(R.id.R1895);
        itemss(R1895);

        final CheckBox Scorpion = items.findViewById(R.id.Scorpion);
        itemss(Scorpion);

        //Other
        final CheckBox CheekPad = items.findViewById(R.id.CheekPad);
        itemss(CheekPad);

        final CheckBox Choke = items.findViewById(R.id.Choke);
        itemss(Choke);

        final CheckBox CompensatorSMG = items.findViewById(R.id.CompensatorSMG);
        itemss(CompensatorSMG);


        final CheckBox FlashHiderSMG = items.findViewById(R.id.FlashHiderSMG);
        itemss(FlashHiderSMG);


        final CheckBox FlashHiderAr = items.findViewById(R.id.FlashHiderAr);
        itemss(FlashHiderAr);

        final CheckBox ArCompensator = items.findViewById(R.id.ArCompensator);
        itemss(ArCompensator);

        final CheckBox TacticalStock = items.findViewById(R.id.TacticalStock);
        itemss(TacticalStock);

        final CheckBox Duckbill = items.findViewById(R.id.Duckbill);
        itemss(Duckbill);

        final CheckBox FlashHiderSniper = items.findViewById(R.id.FlashHiderSniper);
        itemss(FlashHiderSniper);

        final CheckBox SuppressorSMG = items.findViewById(R.id.SuppressorSMG);
        itemss(SuppressorSMG);

        final CheckBox HalfGrip = items.findViewById(R.id.HalfGrip);
        itemss(HalfGrip);

        final CheckBox StockMicroUZI = items.findViewById(R.id.StockMicroUZI);
        itemss(StockMicroUZI);

        final CheckBox SuppressorSniper = items.findViewById(R.id.SuppressorSniper);
        itemss(SuppressorSniper);

        final CheckBox SuppressorAr = items.findViewById(R.id.SuppressorAr);
        itemss(SuppressorAr);

        final CheckBox SniperCompensator = items.findViewById(R.id.SniperCompensator);
        itemss(SniperCompensator);

        final CheckBox ExQdSniper = items.findViewById(R.id.ExQdSniper);
        itemss(ExQdSniper);

        final CheckBox QdSMG = items.findViewById(R.id.QdSMG);
        itemss(QdSMG);

        final CheckBox ExSMG = items.findViewById(R.id.ExSMG);
        itemss(ExSMG);

        final CheckBox QdSniper = items.findViewById(R.id.QdSniper);
        itemss(QdSniper);

        final CheckBox ExSniper = items.findViewById(R.id.ExSniper);
        itemss(ExSniper);

        final CheckBox ExAr = items.findViewById(R.id.ExAr);
        itemss(ExAr);

        final CheckBox ExQdAr = items.findViewById(R.id.ExQdAr);
        itemss(ExQdAr);

        final CheckBox QdAr = items.findViewById(R.id.QdAr);
        itemss(QdAr);

        final CheckBox ExQdSMG = items.findViewById(R.id.ExQdSMG);
        itemss(ExQdSMG);

        final CheckBox QuiverCrossBow = items.findViewById(R.id.QuiverCrossBow);
        itemss(QuiverCrossBow);

        final CheckBox BulletLoop = items.findViewById(R.id.BulletLoop);
        itemss(BulletLoop);

        final CheckBox ThumbGrip = items.findViewById(R.id.ThumbGrip);
        itemss(ThumbGrip);

        final CheckBox LaserSight = items.findViewById(R.id.LaserSight);
        itemss(LaserSight);

        final CheckBox AngledGrip = items.findViewById(R.id.AngledGrip);
        itemss(AngledGrip);

        final CheckBox LightGrip = items.findViewById(R.id.LightGrip);
        itemss(LightGrip);

        final CheckBox VerticalGrip = items.findViewById(R.id.VerticalGrip);
        itemss(VerticalGrip);

        final CheckBox GasCan = items.findViewById(R.id.GasCan);
        itemss(GasCan);

        //Vehicle
        final CheckBox UTV = items.findViewById(R.id.UTV);
        vehicless(UTV);

        final CheckBox Buggy = items.findViewById(R.id.Buggy);
        vehicless(Buggy);

        final CheckBox UAZ = items.findViewById(R.id.UAZ);
        vehicless(UAZ);

        final CheckBox Trike = items.findViewById(R.id.Trike);
        vehicless(Trike);

        final CheckBox Bike = items.findViewById(R.id.Bike);
        vehicless(Bike);

        final CheckBox Dacia = items.findViewById(R.id.Dacia);
        vehicless(Dacia);

        final CheckBox Jet = items.findViewById(R.id.Jet);
        vehicless(Jet);

        final CheckBox Boat = items.findViewById(R.id.Boat);
        vehicless(Boat);

        final CheckBox Scooter = items.findViewById(R.id.Scooter);
        vehicless(Scooter);

        final CheckBox Bus = items.findViewById(R.id.Bus);
        vehicless(Bus);

        final CheckBox Mirado = items.findViewById(R.id.Mirado);
        vehicless(Mirado);

        final CheckBox Rony = items.findViewById(R.id.Rony);
        vehicless(Rony);

        final CheckBox Snowbike = items.findViewById(R.id.Snowbike);
        vehicless(Snowbike);

        final CheckBox Snowmobile = items.findViewById(R.id.Snowmobile);
        vehicless(Snowmobile);

        final CheckBox Tempo = items.findViewById(R.id.Tempo);
        vehicless(Tempo);

        final CheckBox Truck = items.findViewById(R.id.Truck);
        vehicless(Truck);

        final CheckBox MonsterTruck = items.findViewById(R.id.MonsterTruck);
        vehicless(MonsterTruck);

        final CheckBox BRDM = items.findViewById(R.id.BRDM);
        vehicless(BRDM);

        final CheckBox ATV = items.findViewById(R.id.ATV);
        vehicless(ATV);

        final CheckBox LadaNiva = items.findViewById(R.id.LadaNiva);
        vehicless(LadaNiva);

        final CheckBox Motorglider = items.findViewById(R.id.Motorglider);
        vehicless(Motorglider);

        final CheckBox CoupeRB = items.findViewById(R.id.CoupeRB);
        vehicless(CoupeRB);

        //Special
        final CheckBox Crate = items.findViewById(R.id.Crate);
        itemss(Crate);

        final CheckBox Airdrop = items.findViewById(R.id.Airdrop);
        itemss(Airdrop);

        final CheckBox DropPlane = items.findViewById(R.id.DropPlane);
        itemss(DropPlane);

        final CheckBox FlareGun = items.findViewById(R.id.FlareGun);
        itemss(FlareGun);

        final LinearLayout checkall = mainView.findViewById(R.id.itemscheckall);
        final LinearLayout noneall = mainView.findViewById(R.id.itemsblockall);
        final LinearLayout checkallv = mainView.findViewById(R.id.mobilscheckall);
        final LinearLayout noneallv = mainView.findViewById(R.id.mobilsblockall);

        checkallv.setOnClickListener(v -> {
            Buggy.setChecked(true);
            UAZ.setChecked(true);
            Trike.setChecked(true);
            Bike.setChecked(true);
            Dacia.setChecked(true);
            Jet.setChecked(true);
            Boat.setChecked(true);
            Scooter.setChecked(true);
            Bus.setChecked(true);
            Mirado.setChecked(true);
            Rony.setChecked(true);
            Snowbike.setChecked(true);
            Snowmobile.setChecked(true);
            Tempo.setChecked(true);
            Truck.setChecked(true);
            MonsterTruck.setChecked(true);
            BRDM.setChecked(true);
            LadaNiva.setChecked(true);
            ATV.setChecked(true);
            UTV.setChecked(true);
            CoupeRB.setChecked(true);
            Motorglider.setChecked(true);
        });

        noneallv.setOnClickListener(v -> {
            Buggy.setChecked(false);
            UAZ.setChecked(false);
            Trike.setChecked(false);
            Bike.setChecked(false);
            Dacia.setChecked(false);
            Jet.setChecked(false);
            Boat.setChecked(false);
            Scooter.setChecked(false);
            Bus.setChecked(false);
            Mirado.setChecked(false);
            Rony.setChecked(false);
            Snowbike.setChecked(false);
            Snowmobile.setChecked(false);
            Tempo.setChecked(false);
            Truck.setChecked(false);
            MonsterTruck.setChecked(false);
            BRDM.setChecked(false);
            LadaNiva.setChecked(false);
            ATV.setChecked(false);
            UTV.setChecked(false);
            CoupeRB.setChecked(false);
            Motorglider.setChecked(false);
        });

        checkall.setOnClickListener(v -> {

            /* Other */
            Crate.setChecked(true);
            Airdrop.setChecked(true);
            DropPlane.setChecked(true);
            CheekPad.setChecked(true);
            lootbox.setChecked(true);
            Choke.setChecked(true);


            /* Scope */
            canted.setChecked(true);
            reddot.setChecked(true);
            hollow.setChecked(true);
            x2.setChecked(true);
            x3.setChecked(true);
            x4.setChecked(true);
            x6.setChecked(true);
            x8.setChecked(true);

            /* Weapon */
            AWM.setChecked(true);
            QBU.setChecked(true);
            SLR.setChecked(true);
            SKS.setChecked(true);
            Mini14.setChecked(true);
            M24.setChecked(true);
            Kar98k.setChecked(true);
            VSS.setChecked(true);
            Win94.setChecked(true);
            AUG.setChecked(true);
            M762.setChecked(true);
            SCARL.setChecked(true);
            M416.setChecked(true);
            M16A4.setChecked(true);
            MK47.setChecked(true);
            G36C.setChecked(true);
            QBZ.setChecked(true);
            AKM.setChecked(true);
            Groza.setChecked(true);
            S12K.setChecked(true);
            DBS.setChecked(true);
            S686.setChecked(true);
            S1897.setChecked(true);
            sawed.setChecked(true);
            TommyGun.setChecked(true);
            MP5K.setChecked(true);
            vector.setChecked(true);
            UZI.setChecked(true);
            R1895.setChecked(true);
            Explosive.setChecked(true);
            P92.setChecked(true);
            P18C.setChecked(true);
            R45.setChecked(true);
            P1911.setChecked(true);
            Desert.setChecked(true);
            Sickle.setChecked(true);
            Machete.setChecked(true);
            Pan.setChecked(true);
            MK14.setChecked(true);
            Scorpion.setChecked(true);

            Mosin.setChecked(true);
            MK12.setChecked(true);
            AMR.setChecked(true);

            M1014.setChecked(true);
            NS2000.setChecked(true);
            P90.setChecked(true);
            MG3.setChecked(true);
            AC32.setChecked(true);
            HoneyBadger.setChecked(true);
            FAMAS.setChecked(true);

            /* Ammo */
            a45.setChecked(true);
            a9.setChecked(true);
            a7.setChecked(true);
            a300.setChecked(true);
            a5.setChecked(true);
            BMG50.setChecked(true);
            a12.setChecked(true);

            SniperCompensator.setChecked(true);
            DP28.setChecked(true);
            M249.setChecked(true);
            grenade.setChecked(true);
            smoke.setChecked(true);
            molotov.setChecked(true);
            painkiller.setChecked(true);
            injection.setChecked(true);
            energydrink.setChecked(true);
            firstaid.setChecked(true);
            bandage.setChecked(true);
            medkit.setChecked(true);
            FlareGun.setChecked(true);
            UMP.setChecked(true);
            bizon.setChecked(true);
            CompensatorSMG.setChecked(true);
            FlashHiderSMG.setChecked(true);
            FlashHiderAr.setChecked(true);
            ArCompensator.setChecked(true);
            TacticalStock.setChecked(true);
            Duckbill.setChecked(true);
            FlashHiderSniper.setChecked(true);
            SuppressorSMG.setChecked(true);
            HalfGrip.setChecked(true);
            StockMicroUZI.setChecked(true);
            SuppressorSniper.setChecked(true);
            SuppressorAr.setChecked(true);
            ExQdSniper.setChecked(true);
            QdSMG.setChecked(true);
            ExSMG.setChecked(true);
            QdSniper.setChecked(true);
            ExSniper.setChecked(true);
            ExAr.setChecked(true);
            ExQdAr.setChecked(true);
            QdAr.setChecked(true);
            ExQdSMG.setChecked(true);
            QuiverCrossBow.setChecked(true);
            BulletLoop.setChecked(true);
            ThumbGrip.setChecked(true);
            LaserSight.setChecked(true);
            AngledGrip.setChecked(true);
            LightGrip.setChecked(true);
            VerticalGrip.setChecked(true);
            GasCan.setChecked(true);
            Arrow.setChecked(true);
            Crossbow.setChecked(true);
            bag1.setChecked(true);
            bag2.setChecked(true);
            bag3.setChecked(true);
            helmet1.setChecked(true);
            helmet2.setChecked(true);
            helmet3.setChecked(true);
            vest1.setChecked(true);
            vest2.setChecked(true);
            vest3.setChecked(true);
            stun.setChecked(true);
            Crowbar.setChecked(true);
        });

        noneall.setOnClickListener(v -> {
            /* Other */
            Crate.setChecked(false);
            Airdrop.setChecked(false);
            DropPlane.setChecked(false);
            CheekPad.setChecked(false);
            lootbox.setChecked(false);
            Choke.setChecked(false);


            /* Scope */
            canted.setChecked(false);
            reddot.setChecked(false);
            hollow.setChecked(false);
            x2.setChecked(false);
            x3.setChecked(false);
            x4.setChecked(false);
            x6.setChecked(false);
            x8.setChecked(false);

            /* Weapon */
            AWM.setChecked(false);
            QBU.setChecked(false);
            SLR.setChecked(false);
            SKS.setChecked(false);
            Mini14.setChecked(false);
            M24.setChecked(false);
            Kar98k.setChecked(false);
            VSS.setChecked(false);
            Win94.setChecked(false);
            AUG.setChecked(false);
            M762.setChecked(false);
            SCARL.setChecked(false);
            M416.setChecked(false);
            M16A4.setChecked(false);
            MK47.setChecked(false);
            G36C.setChecked(false);
            QBZ.setChecked(false);
            AKM.setChecked(false);
            Groza.setChecked(false);
            S12K.setChecked(false);
            DBS.setChecked(false);
            S686.setChecked(false);
            S1897.setChecked(false);
            sawed.setChecked(false);
            TommyGun.setChecked(false);
            MP5K.setChecked(false);
            vector.setChecked(false);
            UZI.setChecked(false);
            R1895.setChecked(false);
            Explosive.setChecked(false);
            P92.setChecked(false);
            P18C.setChecked(false);
            R45.setChecked(false);
            P1911.setChecked(false);
            Desert.setChecked(false);
            Sickle.setChecked(false);
            Machete.setChecked(false);
            Pan.setChecked(false);
            MK14.setChecked(false);
            Scorpion.setChecked(false);

            Mosin.setChecked(false);
            MK12.setChecked(false);
            AMR.setChecked(false);

            M1014.setChecked(false);
            NS2000.setChecked(false);
            P90.setChecked(false);
            MG3.setChecked(false);
            AC32.setChecked(false);
            HoneyBadger.setChecked(false);
            FAMAS.setChecked(false);

            /* Ammo */
            a45.setChecked(false);
            a9.setChecked(false);
            a7.setChecked(false);
            a300.setChecked(false);
            a5.setChecked(false);
            BMG50.setChecked(false);
            a12.setChecked(false);

            SniperCompensator.setChecked(false);
            DP28.setChecked(false);
            M249.setChecked(false);
            grenade.setChecked(false);
            smoke.setChecked(false);
            molotov.setChecked(false);
            painkiller.setChecked(false);
            injection.setChecked(false);
            energydrink.setChecked(false);
            firstaid.setChecked(false);
            bandage.setChecked(false);
            medkit.setChecked(false);
            FlareGun.setChecked(false);
            UMP.setChecked(false);
            bizon.setChecked(false);
            CompensatorSMG.setChecked(false);
            FlashHiderSMG.setChecked(false);
            FlashHiderAr.setChecked(false);
            ArCompensator.setChecked(false);
            TacticalStock.setChecked(false);
            Duckbill.setChecked(false);
            FlashHiderSniper.setChecked(false);
            SuppressorSMG.setChecked(false);
            HalfGrip.setChecked(false);
            StockMicroUZI.setChecked(false);
            SuppressorSniper.setChecked(false);
            SuppressorAr.setChecked(false);
            ExQdSniper.setChecked(false);
            QdSMG.setChecked(false);
            ExSMG.setChecked(false);
            QdSniper.setChecked(false);
            ExSniper.setChecked(false);
            ExAr.setChecked(false);
            ExQdAr.setChecked(false);
            QdAr.setChecked(false);
            ExQdSMG.setChecked(false);
            QuiverCrossBow.setChecked(false);
            BulletLoop.setChecked(false);
            ThumbGrip.setChecked(false);
            LaserSight.setChecked(false);
            AngledGrip.setChecked(false);
            LightGrip.setChecked(false);
            VerticalGrip.setChecked(false);
            GasCan.setChecked(false);
            Arrow.setChecked(false);
            Crossbow.setChecked(false);
            bag1.setChecked(false);
            bag2.setChecked(false);
            bag3.setChecked(false);
            helmet1.setChecked(false);
            helmet2.setChecked(false);
            helmet3.setChecked(false);
            vest1.setChecked(false);
            vest2.setChecked(false);
            vest3.setChecked(false);
            stun.setChecked(false);
            Crowbar.setChecked(false);
        });
    }

    private void aimbot(View aimbot) {
        TextView menutextaimtouch = aimbot.findViewById(R.id.texttouch);
        TextView aimpre = aimbot.findViewById(R.id.aimpre);
        LinearLayout aimsec = aimbot.findViewById(R.id.aimsec);
        LinearLayout menurotation = aimbot.findViewById(R.id.rotationmenu);
        LinearLayout aimspeedmenu = aimbot.findViewById(R.id.aimspeedmenu);
        LinearLayout recoilmenu = aimbot.findViewById(R.id.recoilmenu);
        LinearLayout recoilmenus2 = aimbot.findViewById(R.id.recoilmenus2);
        LinearLayout smoothnessmenu = aimbot.findViewById(R.id.smoothnessmenu);
        RadioButton touchsimulation = aimbot.findViewById(R.id.touchsimulation);
        RadioButton bullettrack = aimbot.findViewById(R.id.bullettrack);
        RadioButton aimbottt = aimbot.findViewById(R.id.aimbot);
        final LinearLayout touchLocationmenu = aimbot.findViewById(R.id.touchlocationmenu);
        final LinearLayout touchsizemenu = aimbot.findViewById(R.id.touchsizemenu);
        final LinearLayout posXmenu = aimbot.findViewById(R.id.posXmenu);
        final LinearLayout posYmenu = aimbot.findViewById(R.id.posYmenu);

        if (!modestatus) {
            aimbottt.setVisibility(View.GONE);
            bullettrack.setVisibility(View.VISIBLE);

        } else {
            aimbottt.setVisibility(View.VISIBLE);
            bullettrack.setVisibility(View.VISIBLE);
        }

        if (Kooontoool) {
            typelogin = "PREMIUM";
            aimpre.setVisibility(View.GONE);
            aimsec.setVisibility(View.VISIBLE);
            menurotation.setAlpha(1.0f);
            aimspeedmenu.setAlpha(1.0f);
            recoilmenu.setAlpha(1.0f);
            recoilmenus2.setAlpha(1.0f);
            smoothnessmenu.setAlpha(1.0f);
            touchsimulation.setEnabled(true);
            bullettrack.setEnabled(true);
            aimbottt.setEnabled(true);
            touchsimulation.setAlpha(1.0f);
            bullettrack.setAlpha(1.0f);
            aimbottt.setAlpha(1.0f);
            touchLocationmenu.setAlpha(1.0f);
            touchsizemenu.setAlpha(1.0f);
            posXmenu.setAlpha(1.0f);
            posYmenu.setAlpha(1.0f);
        } else {
            typelogin = "FREE";
            aimpre.setVisibility(View.VISIBLE);
            aimsec.setVisibility(View.GONE);
            menurotation.setAlpha(0.0f);
            aimspeedmenu.setAlpha(0.0f);
            recoilmenu.setAlpha(0.0f);
            recoilmenus2.setAlpha(0.0f);
            smoothnessmenu.setAlpha(0.0f);
            touchsimulation.setEnabled(false);
            bullettrack.setEnabled(false);
            aimbottt.setEnabled(false);
            touchsimulation.setAlpha(0.0f);
            bullettrack.setAlpha(0.0f);
            aimbottt.setAlpha(0.0f);
            touchLocationmenu.setAlpha(0.0f);
            touchsizemenu.setAlpha(0.0f);
            posXmenu.setAlpha(0.0f);
            posYmenu.setAlpha(0.0f);
        }

        RadioGroup aimgrup = aimbot.findViewById(R.id.grupaim);
        aimgrup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.disableaim:
                        StopAimBulletFloat();
                        StopAimFloat();
                        StopAimTouch();
                        menutextaimtouch.setVisibility(View.GONE);
                        menurotation.setVisibility(View.GONE);
                        aimspeedmenu.setVisibility(View.GONE);
                        smoothnessmenu.setVisibility(View.GONE);
                        break;

                    case R.id.aimbot:
                        StartAimFloat();
                        StopAimBulletFloat();
                        StopAimTouch();
                        menutextaimtouch.setVisibility(View.GONE);
                        menurotation.setVisibility(View.GONE);
                        aimspeedmenu.setVisibility(View.GONE);
                        smoothnessmenu.setVisibility(View.GONE);
                        touchLocationmenu.setVisibility(View.GONE);
                        touchsizemenu.setVisibility(View.GONE);
                        recoilmenu.setVisibility(View.VISIBLE);
                        posXmenu.setVisibility(View.GONE);
                        posYmenu.setVisibility(View.GONE);
                        break;

                    case R.id.touchsimulation:
                        StartAimTouch();
                        StopAimBulletFloat();
                        StopAimFloat();
                        menutextaimtouch.setVisibility(View.VISIBLE);
                        menurotation.setVisibility(View.VISIBLE);
                        aimspeedmenu.setVisibility(View.VISIBLE);
                        smoothnessmenu.setVisibility(View.VISIBLE);
                        touchLocationmenu.setVisibility(View.VISIBLE);
                        touchsizemenu.setVisibility(View.VISIBLE);
                        recoilmenu.setVisibility(View.VISIBLE);
                        recoilmenus2.setVisibility(View.VISIBLE);
                        posXmenu.setVisibility(View.VISIBLE);
                        posYmenu.setVisibility(View.VISIBLE);
                        break;

                    case R.id.bullettrack:
                        StartAimBulletFloat();
                        StopAimFloat();
                        StopAimTouch();
                        menutextaimtouch.setVisibility(View.GONE);
                        menurotation.setVisibility(View.GONE);
                        aimspeedmenu.setVisibility(View.GONE);
                        smoothnessmenu.setVisibility(View.GONE);
                        touchLocationmenu.setVisibility(View.GONE);
                        touchsizemenu.setVisibility(View.GONE);
                        recoilmenu.setVisibility(View.GONE);
                        recoilmenus2.setVisibility(View.GONE);
                        posXmenu.setVisibility(View.GONE);
                        posYmenu.setVisibility(View.GONE);
                        break;
                }
            }
        });


        final Switch aimKnocked = aimbot.findViewById(R.id.aimknocked);
        setaim(aimKnocked, 3);

        final Switch aimignore = aimbot.findViewById(R.id.aimignorebot);
        setaim(aimignore, 4);

        final Switch changerotation = aimbot.findViewById(R.id.rotationscren);
        setaim(changerotation, 5);

        final Switch touchlocation = aimbot.findViewById(R.id.touchlocation);
        setaim(touchlocation, 6);

        final SeekBar rangeSeekBar = aimbot.findViewById(R.id.range);
        final TextView rangeText = aimbot.findViewById(R.id.rangetext);
        setupSeekBar(rangeSeekBar, rangeText, getrangeAim(), new Runnable() {
            @Override
            public void run() {
                Range(rangeSeekBar.getProgress());
                getrangeAim(rangeSeekBar.getProgress());
            }
        });

        final SeekBar distancesSeekBar = aimbot.findViewById(R.id.distances);
        final TextView distancesText = aimbot.findViewById(R.id.distancetext);
        setupSeekBar(distancesSeekBar, distancesText, getDistances(), new Runnable() {
            @Override
            public void run() {
                distances(distancesSeekBar.getProgress());
                setDistances(distancesSeekBar.getProgress());
            }
        });


        final SeekBar recoilSeekBar2 = aimbot.findViewById(R.id.Recoil2);
        final TextView recoilText2 = aimbot.findViewById(R.id.recoiltext2);
        setupSeekBar(recoilSeekBar2, recoilText2, getrecoilAim(), new Runnable() {
            @Override
            public void run() {
                recoil(recoilSeekBar2.getProgress());
                getrecoilAim(recoilSeekBar2.getProgress());
            }
        });

        final SeekBar recoilSeekBar = aimbot.findViewById(R.id.Recoil);
        final TextView recoilText = aimbot.findViewById(R.id.recoiltext);
        setupSeekBar(recoilSeekBar, recoilText, getrecoilAim(), new Runnable() {
            @Override
            public void run() {
                recoil2(recoilSeekBar.getProgress());
                getrecoilAim2(recoilSeekBar.getProgress());
            }
        });

        final SeekBar recoilSeekBars2 = aimbot.findViewById(R.id.Recoils2);
        final TextView recoilTexts2 = aimbot.findViewById(R.id.recoiltexts2);
        setupSeekBar(recoilSeekBars2, recoilTexts2, getrecoilAim(), new Runnable() {
            @Override
            public void run() {
                recoil3(recoilSeekBars2.getProgress());
                getrecoilAim3(recoilSeekBars2.getProgress());
            }
        });

        final SeekBar bulletSpeedSeekBar = aimbot.findViewById(R.id.bulletspeed);
        final TextView bulletSpeedText = aimbot.findViewById(R.id.bulletspeedtext);
        setupSeekBar(bulletSpeedSeekBar, bulletSpeedText, getbulletspeedAim(), new Runnable() {
            @Override
            public void run() {
                Bulletspeed(bulletSpeedSeekBar.getProgress());
                getbulletspeedAim(bulletSpeedSeekBar.getProgress());
            }
        });

        final SeekBar AimSpeedSize = aimbot.findViewById(R.id.aimingspeed);
        final TextView AimSpeedText = aimbot.findViewById(R.id.aimingspeedtext);
        setupSeekBar(AimSpeedSize, AimSpeedText, getAimSpeed(), new Runnable() {
            @Override
            public void run() {
                AimingSpeed(AimSpeedSize.getProgress());
                setAimSpeed(AimSpeedSize.getProgress());
            }
        });

        final SeekBar SmoothSize = aimbot.findViewById(R.id.Smoothness);
        final TextView SmoothText = aimbot.findViewById(R.id.smoothtext);
        setupSeekBar(SmoothSize, SmoothText, getSmoothness(), new Runnable() {
            @Override
            public void run() {
                Smoothness(SmoothSize.getProgress());
                setSmoothness(SmoothSize.getProgress());
            }
        });

        final SeekBar touchsize = mainView.findViewById(R.id.touchsize);
        final TextView touchsizetext = mainView.findViewById(R.id.touchsizetext);
        setupSeekBar(touchsize, touchsizetext, getTouchSize(), new Runnable() {
            @Override
            public void run() {
                TouchSize(touchsize.getProgress());
                setTouchSize(touchsize.getProgress());
            }
        });

        final SeekBar touchPosX = mainView.findViewById(R.id.touchPosX);
        final TextView touchPosXtext = mainView.findViewById(R.id.touchPosXtext);
        setupSeekBar(touchPosX, touchPosXtext, getTouchPosX(), new Runnable() {
            @Override
            public void run() {
                TouchPosX(touchPosX.getProgress());
                setTouchPosX(touchPosX.getProgress());
            }
        });

        final SeekBar touchPosY = mainView.findViewById(R.id.touchPosY);
        final TextView touchPosYtext = mainView.findViewById(R.id.touchPosYtext);
        setupSeekBar(touchPosY, touchPosYtext, getTouchPosY(), new Runnable() {
            @Override
            public void run() {
                TouchPosY(touchPosY.getProgress());
                setTouchPosY(touchPosY.getProgress());
            }
        });


        final RadioGroup aimby = aimbot.findViewById(R.id.aimby);
        aimby.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = aimby.getCheckedRadioButtonId();
                RadioButton btn = aimbot.findViewById(chkdId);
                AimBy(Integer.parseInt(btn.getTag().toString()));
            }
        });

        final RadioGroup aimwhen = aimbot.findViewById(R.id.aimwhen);
        aimwhen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = aimwhen.getCheckedRadioButtonId();
                RadioButton btn = aimbot.findViewById(chkdId);
                AimWhen(Integer.parseInt(btn.getTag().toString()));
            }
        });

        final RadioGroup aimbotmode = aimbot.findViewById(R.id.aimbotmode);
        aimbotmode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int chkdId = aimbotmode.getCheckedRadioButtonId();
                RadioButton btn = aimbot.findViewById(chkdId);
                Target(Integer.parseInt(btn.getTag().toString()));
            }
        });
    }

    private void memory(View memory) {
        final Switch less = memory.findViewById(R.id.ishitx);
        memory(less, 1);
        final Switch Cross = memory.findViewById(R.id.issmallcross);
        memory(Cross, 2);
        final Switch amms = memory.findViewById(R.id.isaimlock);
        memory(amms, 3);
        final Switch ismagic = memory.findViewById(R.id.ismagichead);
        final Switch ishitx = memory.findViewById(R.id.ishitx);
        memory(ishitx, 5);
        final SeekBar wideviewSeekBar = memory.findViewById(R.id.rangewide);
        final TextView wideviewText = memory.findViewById(R.id.rangetextwide);
        final TextView aimpresdk = memory.findViewById(R.id.aimpresdk);
        LinearLayout memsec = memory.findViewById(R.id.memsec);

        if (Kooontoool) {
            typelogin = "PREMIUM";
            aimpresdk.setVisibility(View.GONE);
            memsec.setVisibility(View.VISIBLE);
        } else {
            typelogin = "FREE";
            aimpresdk.setVisibility(View.VISIBLE);
            memsec.setVisibility(View.GONE);
            less.setEnabled(false);
            Cross.setEnabled(false);
            amms.setEnabled(false);
            ismagic.setEnabled(false);
            ishitx.setEnabled(false);
            wideviewSeekBar.setEnabled(false);
            less.setAlpha(0.0f);
            Cross.setAlpha(0.0f);
            amms.setAlpha(0.0f);
            ismagic.setAlpha(0.0f);
            ishitx.setAlpha(0.0f);
        }


        // MAGIC BULLET


        ismagic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Exec("/SUFI 500 " + USERKEY + " " + game + " 44", getString(R.string.magic_launch_success_wait_20_sec));
                } else {
                    // Exec("/sufi 400 "+USERKEY+" "+game+" 001", getString(R.string.magic_launch_success));
                }
            }
        });


      /*  ishitx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Exec("/TANHA " + USERKEY + game, getString(R.string.hitx_launch_success));
                } else {
                    // Exec("/SUFI 400 "+USERKEY+" "+game+" 001", getString(R.string.hitx_launch_success));
                }
            }
        }); */


        setupSeekBar(wideviewSeekBar, wideviewText, getwideview(), new Runnable() {
            @Override
            public void run() {
                WideView(wideviewSeekBar.getProgress());
                getwideview(wideviewSeekBar.getProgress());
            }
        });
    }

  /*  public class Dapter extends PagerAdapter {
        LayoutInflater inflater;
        Context context;

        public Dapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

       *//* @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = null;
            view = inflater.inflate(position == 0 ? R.layout.esp_visual : position == 1 ? R.layout.esp_inventory : position == 2 ? R.layout.esp_aimbot : R.layout.esp_memory, null);
            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view);
            if (position == 0) {
                visual(view);
            } else if (position == 1) {
                items(view);
            } else if (position == 2) {
                aimbot(view);
            } else if (position == 3) {
                memory(view);
            }
            return view;
        }*//*
    }*/
}
