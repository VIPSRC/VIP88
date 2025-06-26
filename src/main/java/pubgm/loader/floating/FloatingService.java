package pubgm.loader.floating;


import static androidx.core.app.ActivityCompat.recreate;
import static pubgm.loader.activity.MainActivity.bitversi;
import static pubgm.loader.activity.MainActivity.game;
import static pubgm.loader.activity.MainActivity.gameint;
import static pubgm.loader.activity.MainActivity.kernel;
import static pubgm.loader.utils.ActivityCompat.toastImage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import android.widget.ToggleButton;

import pubgm.loader.R;
import pubgm.loader.activity.MainActivity;
import pubgm.loader.utils.FLog;
import com.topjohnwu.superuser.Shell;

import java.util.Locale;

import io.michaelrocks.paranoid.Obfuscate;
import pubgm.loader.utils.FPrefs;
import pubgm.loader.utils.myTools;

/**************************
 * BUILD ON Android Studio
 * TELEGRAM : JungliCheats
 **************************/

@Obfuscate
public class FloatingService extends Service {

    static {
        try {
            System.loadLibrary("client");
        } catch (UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    public static int islandint = 0;

    private boolean hideitem = false;
    Context ctx;
    private View mainView;
    private PowerManager.WakeLock mWakeLock;
    private WindowManager windowManagerMainView;
    private WindowManager.LayoutParams paramsMainView;
    private LinearLayout layout_main_view;
    private RelativeLayout layout_icon_control_view;

    public native void SettingValue(int setting_code, boolean value);

    public native void SettingMemory(int setting_code, boolean value);

    public native void SettingAim(int setting_code, boolean value);

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

    private void StartAimTouch() {
        startService(new Intent(getApplicationContext(), ToggleSimulation.class));
    }
    public FPrefs getPref() {
        return FPrefs.with(this);
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


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    myTools m = new myTools(this);
    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        String currentLang = m.getSt("myKey", "mapLang", "en");
        setLocale(this, currentLang);
        InitShowMainView();
     //   ESPView.sleepTime = 1000 / 120;
        ESPView.ChangeFps(120);
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
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustLayoutWidth();
    }
    private void adjustLayoutWidth() {
        // Get window manager service
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        // Get screen dimensions
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        int screenWidth = size.x;

        // Get current orientation
        int orientation = getResources().getConfiguration().orientation;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_main_view.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // In portrait mode, use 90% of screen width
            params.width = (int) (screenWidth * 0.9);
        } else {
            // In landscape mode, use fixed width of 400dp
            float density = getResources().getDisplayMetrics().density;
            params.width = (int) (400 * density); // Convert 400dp to pixels
        }

        // Apply the new layout parameters
        layout_main_view.setLayoutParams(params);
    }
    private void InitShowMainView() {


        mainView = LayoutInflater.from(this).inflate(R.layout.float_service, null);
        paramsMainView = getparams();
        windowManagerMainView = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerMainView.addView(mainView, paramsMainView);
        layout_icon_control_view = mainView.findViewById(R.id.layout_icon_control_view);
        layout_main_view = mainView.findViewById(R.id.layout_main_view);
        adjustLayoutWidth();
        ImageView layout_close_main_view = mainView.findViewById(R.id.close);
        layout_close_main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                layout_main_view.setVisibility(View.GONE);
                layout_icon_control_view.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout layout_view = mainView.findViewById(R.id.layout_view);
        layout_view.setOnTouchListener(onTouchListener());

        VisualOverlay(mainView);
        AimbotOverlay(mainView);
        initOverlayItems(mainView);
        memory(mainView);
        initDesign();
    }

    void animation(View v){
        Animator scale = ObjectAnimator.ofPropertyValuesHolder(v,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 0.7f, 1),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 0.7f, 1));
        scale.setDuration(100);
        scale.start();
    }

    void effectnav(LinearLayout a, LinearLayout b, LinearLayout c, LinearLayout d,LinearLayout e){
        a.setBackgroundResource(R.drawable.button_coming);
        b.setBackgroundResource(R.drawable.background_trans);
        c.setBackgroundResource(R.drawable.background_trans);
        d.setBackgroundResource(R.drawable.background_trans);
        e.setBackgroundResource(R.drawable.background_trans);
    }

    public void initDesign() {
        final RadioButton fps1 = mainView.findViewById(R.id.fps30);
        final RadioButton fps2 = mainView.findViewById(R.id.fps60);
        final RadioButton fps3 = mainView.findViewById(R.id.fps90);
        final RadioButton fps4 = mainView.findViewById(R.id.fps120);
        int CheckFps = getFps();
        if (CheckFps == 30) {
            fps1.setChecked(true);
            ESPView.sleepTime = 1000 / 30;
        } else if (CheckFps == 60) {
            fps2.setChecked(true);
            ESPView.sleepTime = 1000 / 60;
        } else if (CheckFps == 90) {
            fps3.setChecked(true);
            ESPView.sleepTime = 1000 / 90;
        } else if (CheckFps == 120) {
            fps4.setChecked(true);
            ESPView.sleepTime = 1000 / 120;
        } else {
            fps1.setChecked(true);
            ESPView.sleepTime = 1000 / 30;
        }

        fps1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps2.setChecked(false);
                    fps3.setChecked(false);
                    fps4.setChecked(false);
                    setFps(30);
                    ESPView.ChangeFps(30);
                }
            }
        });

        fps2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps1.setChecked(false);
                    fps3.setChecked(false);
                    fps4.setChecked(false);
                    setFps(60);
                    ESPView.ChangeFps(60);
                }
            }
        });

        fps3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps2.setChecked(false);
                    fps1.setChecked(false);
                    fps4.setChecked(false);
                    setFps(90);
                    ESPView.ChangeFps(90);
                }
            }
        });

        fps4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fps2.setChecked(false);
                    fps3.setChecked(false);
                    fps1.setChecked(false);
                    setFps(120);
                    ESPView.ChangeFps(120);
                }
            }
        });
        LinearLayout f1 = mainView.findViewById(R.id.fvisual);
        LinearLayout f2 = mainView.findViewById(R.id.fitems);
        LinearLayout f3 = mainView.findViewById(R.id.faimbot);
        LinearLayout f4 = mainView.findViewById(R.id.fmemory);
        LinearLayout f5 = mainView.findViewById(R.id.fskin);
        LinearLayout menu1 = mainView.findViewById(R.id.menuf1);
        LinearLayout menu2 = mainView.findViewById(R.id.menuf2);
        LinearLayout menu3 = mainView.findViewById(R.id.menuf3);
        LinearLayout menu4 = mainView.findViewById(R.id.menuf4);
        LinearLayout menu5 = mainView.findViewById(R.id.menuf5);
        LinearLayout e1 = mainView.findViewById(R.id.effectfvisual);
        LinearLayout e2 = mainView.findViewById(R.id.effectfitems);
        LinearLayout e3 = mainView.findViewById(R.id.effectfaimbot);
        LinearLayout e4 = mainView.findViewById(R.id.effectfmemory);
        LinearLayout e5 = mainView.findViewById(R.id.effectfskin);


        f1.setOnClickListener(v -> {
            menu1.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu5.setVisibility(View.GONE);
            effectnav(e1,e2,e3,e4,e5);
            animation(v);
        });

        f2.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu5.setVisibility(View.GONE);
            effectnav(e2,e1,e3,e4,e5);
            animation(v);
        });

        f3.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.VISIBLE);
            menu4.setVisibility(View.GONE);
            menu5.setVisibility(View.GONE);
            effectnav(e3,e2,e1,e4,e5);
            animation(v);
        });

        f4.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.VISIBLE);
            menu5.setVisibility(View.GONE);
            effectnav(e4,e2,e3,e1,e5);
            animation(v);
        });
        f5.setOnClickListener(v -> {
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu5.setVisibility(View.VISIBLE);
            effectnav(e5,e4,e2,e3,e1);
            animation(v);
        });

    }

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
        if (getPref().readBoolean("anti_recorder")) {
            HideRecorder.setFakeRecorderWindowLayoutParams(params);
        }
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
    void setFps(int fps) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("fps", fps);
        ed.apply();
    }

    int getFps() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("fps", 100);
    }

    boolean getConfig(String key) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
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
        return sp.getInt("getrangeAim", 150);
    }

    private void getrangeAim(int getrangeAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrangeAim", getrangeAim);
        ed.apply();
    }

    private int getDistances() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("Distances", 100);
    }

    private void setDistances(int Distances) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("Distances", Distances);
        ed.apply();
    }

    private int getrecoilAim() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getrecoilAim", 50);
    }

    private void getrecoilAim(int getrecoilAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrecoilAim", getrecoilAim);
        ed.apply();
    }

    private void getrecoilAim2(int getrecoilAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrecoilAim2", getrecoilAim);
        ed.apply();
    }

    private void getrecoilAim3(int getrecoilAim) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("getrecoilAim2", getrecoilAim);
        ed.apply();
    }

    private int getbulletspeedAim() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("getbulletspeedAim", 600);
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


    int getTouchSize() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("touchsize", 600);
    }

    void setTouchSize(int touchsize) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("touchsize", touchsize);
        ed.apply();
    }

    int getTouchPosX() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("posX", 650);
    }

    void setTouchPosX(int posX) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("posX", posX);
        ed.apply();
    }

    int getTouchPosY() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("posY", 1400);
    }

    void setTouchPosY(int posY) {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("posY", posY);
        ed.apply();
    }

    private int getAimSpeed() {
        SharedPreferences sp = this.getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.getInt("AimingSpeed", 750);
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

    public void Linearmemory(final LinearLayout a, int b, String s) {
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islandint == 0) {
                    SettingMemory(b, true);
                    a.setBackgroundResource(R.drawable.radius_button_off);
                    islandint = 1;
                } else if (islandint == 1) {
                    SettingMemory(b, false);
                    a.setBackgroundResource(R.drawable.radius_button);
                    islandint = 0;
                }
            }
        });
    }


    private void StartHideItem() {
        startService(new Intent(this,FightMod.class));
    }

    private void StopHideItem() {
        stopService(new Intent(this,FightMod.class));
    }
    public void memory(final ToggleButton a, final int b) {
        a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean isChecked) {
                setValue(String.valueOf(a.getText()), a.isChecked());
                SettingMemory(b, a.isChecked());
            }
        });
    }

    public void countoption (final CheckBox a, int b){
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

    public void Linearaimset(final LinearLayout a, int b, String s) {
        SettingAim(b, getConfig((String) s));
        a.setBackgroundResource(getConfig((String) s) ? R.drawable.radius_button_off : R.drawable.radius_button);
        islandint = getConfig((String) s) ? 1 : 0; // Update islandint sesuai status

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islandint == 0) {
                    setValue(String.valueOf(s), true);
                    SettingAim(b, true);
                    a.setBackgroundResource(R.drawable.radius_button_off);
                    islandint = 1;
                } else if (islandint == 1) {
                    setValue(String.valueOf(s), false);
                    SettingAim(b, false);
                    a.setBackgroundResource(R.drawable.radius_button);
                    islandint = 0;
                }
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
    public void excpp(String path) {
        try {
            ExecuteElf("su -c chmod 777 " + getFilesDir() + path);
            ExecuteElf("su -c " + getFilesDir() + path);
            ExecuteElf("chmod 777 " + getFilesDir() + path);
            ExecuteElf(getFilesDir() + path);
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

    public void VisualOverlay(View visual) {
        final Switch drawesp = visual.findViewById(R.id.isenableesp);
        final Switch menuisland = visual.findViewById(R.id.menuisland);
        final Switch menuloho = visual.findViewById(R.id.menuloho);
        final CheckBox alert2 = visual.findViewById(R.id.alert2);

        if (Shell.rootAccess()) {
            menuloho.setVisibility(View.VISIBLE);
        } else {
            menuloho.setVisibility(View.VISIBLE);
        }



        drawesp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    DrawESP();
                }else{
                    StopESP();
                    StopAimFloat();
                    StopAimBulletFloat();
                    StopAimTouch();
                }
            }
        });

        countoption(alert2,20);

        menuisland.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if (bitversi == 64) {
                        if (gameint == 5) {
                            Exec("/ParrotCheat 65", "REPORT 64 BLOCK ENABLE");
                        } else {
                            Exec("/ParrotCheat 65", "REPORT 64 BLOCK ENABLE");
                        }
                    }
                }else{
                    if (bitversi == 64) {
                        if (gameint == 5) {
                            Exec("/ParrotCheat 66", "REPORT 64 BLOCK DISABLE");
                        } else {
                            Exec("/ParrotCheat 66", "REPORT 64 BLOCK DISABLE");
                        }
                    }
                }
            }
        });

        menuloho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (bitversi == 64) {
                        if (gameint == 5) {
                            Exec("/ParrotCheat 9898", "BYPASS 64 ENABLE");
                        } else {
                            Exec("/ParrotCheat 9898", "BYPASS 64 ENABLE");
                        }
                    } else if (bitversi == 32) {
                        if (gameint == 5) {
                            Exec("/ParrotCheat 9832", "BYPASS 32 ENABLE");
                        } else {
                            Exec("/ParrotCheat 9832", "BYPASS 32 ENABLE");
                        }
                    }
                }


        });
        final Switch fightmode = visual.findViewById(R.id.fightmode);
        fightmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                if(p2){
                    StartHideItem();
                    hideitem = true;
                }else{
                    StopHideItem();
                    hideitem = false;
                }
            }
        });
        final CheckBox skin1 = visual.findViewById(R.id.skin1);
        skin1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 1");
            }
        });
        final CheckBox skin2 = visual.findViewById(R.id.skin2);
        skin2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 2");
            }
        });
        final CheckBox skin3 = visual.findViewById(R.id.skin3);
        skin3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 3");
            }
        });
        final CheckBox skin4 = visual.findViewById(R.id.skin4);
        skin4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 4");
            }
        });
        final CheckBox skin5 =visual.findViewById(R.id.skin5);
        skin5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 5");
            }
        });
        final CheckBox skin6 = visual.findViewById(R.id.skin6);
        skin6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 6");
            }
        });
        final CheckBox skin7 = visual.findViewById(R.id.skin7);
        skin7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 7");
            }
        });
        final CheckBox skin8 = visual.findViewById(R.id.skin8);
        skin8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 8");
            }
        });
        final CheckBox skin9 = visual.findViewById(R.id.skin9);
        skin9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 9");
            }
        });
        final CheckBox skin10 = visual.findViewById(R.id.skin10);
        skin10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 10");
            }
        });
        final CheckBox skin11 = visual.findViewById(R.id.skin11);
        skin11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 11");
            }
        });
        final CheckBox skin12 = visual.findViewById(R.id.skin12);
        skin12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 12");
            }
        });
        final CheckBox skin13 = visual.findViewById(R.id.skin13);
        skin13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 13");
            }
        });final CheckBox skin14 = visual.findViewById(R.id.skin14);
        skin14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 14");
            }
        });final CheckBox skin15 = visual.findViewById(R.id.skin15);
        skin15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 15");
            }
        });
        final CheckBox skin16 = visual.findViewById(R.id.skin16);
        skin16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 16");
            }
        });
        final CheckBox skin17 = visual.findViewById(R.id.skin17);
        skin17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 17");
            }
        });
        final CheckBox skin18 = visual.findViewById(R.id.skin18);
        skin18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 18");
            }
        });
        final CheckBox skin19 = visual.findViewById(R.id.skin19);
        skin19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 19");
            }
        });
        final CheckBox skin20 = visual.findViewById(R.id.skin20);
        skin20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 20");
            }
        });
        final CheckBox skin21 = visual.findViewById(R.id.skin21);
        skin21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 21");
            }
        });

        final CheckBox skin22 = visual.findViewById(R.id.skin22);
        skin22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 22");
            }
        });

        final CheckBox skin23 = visual.findViewById(R.id.skin23);
        skin23.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 23");
            }
        });

        final CheckBox skin24 = visual.findViewById(R.id.skin24);
        skin24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 24");
            }
        });

        final CheckBox skin25 = visual.findViewById(R.id.skin25);
        skin25.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 25");
            }
        });

        final CheckBox skin26 = visual.findViewById(R.id.skin26);
        skin26.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 26");
            }
        });

        final CheckBox skin27 = visual.findViewById(R.id.skin27);
        skin27.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 27");
            }
        });

        final CheckBox skin28 = visual.findViewById(R.id.skin28);
        skin28.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 28");
            }
        });

        final CheckBox skin29 = visual.findViewById(R.id.skin29);
        skin29.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 29");
            }
        });

        final CheckBox skin30 = visual.findViewById(R.id.skin30);
        skin30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 30");
            }
        });

        final CheckBox skin31 = visual.findViewById(R.id.skin31);
        skin31.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 31");
            }
        });

        final CheckBox skin32 = visual.findViewById(R.id.skin32);
        skin32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 32");
            }
        });

        final CheckBox skin33 = visual.findViewById(R.id.skin33);
        skin33.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 33");
            }
        });

        final CheckBox skin34 = visual.findViewById(R.id.skin34);
        skin34.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 34");
            }
        });

        final CheckBox skin35 = visual.findViewById(R.id.skin35);
        skin35.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 35");
            }
        });

        final CheckBox skin36 = visual.findViewById(R.id.skin36);
        skin36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 36");
            }
        });

        final CheckBox skin37 = visual.findViewById(R.id.skin37);
        skin37.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 37");
            }
        });

        final CheckBox skin38 = visual.findViewById(R.id.skin38);
        skin38.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 38");
            }
        });

        final CheckBox skin39 = visual.findViewById(R.id.skin39);
        skin39.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 39");
            }
        });

        final CheckBox skin40 = visual.findViewById(R.id.skin40);
        skin40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 40");
            }
        });

        final CheckBox skin41 = visual.findViewById(R.id.skin41);
        skin41.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 41");
            }
        });

        final CheckBox skin42 = visual.findViewById(R.id.skin42);
        skin42.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 42");
            }
        });

        final CheckBox skin43 = visual.findViewById(R.id.skin43);
        skin43.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 43");
            }
        });

        final CheckBox skin44 = visual.findViewById(R.id.skin44);
        skin44.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 44");
            }
        });

        final CheckBox skin45 = visual.findViewById(R.id.skin45);
        skin45.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                excpp("ParrotSkin 45");
            }
        });
      ///  final SeekBar radarSizeSeekBar = visual.findViewById(R.id.strokeradar);
      //  final TextView radarSizeText = visual.findViewById(R.id.radartext);

//        setupSeekBar(radarSizeSeekBar, radarSizeText, getradarSize(), new Runnable() {
//            @Override
//            public void run() {
//                int pos = radarSizeSeekBar.getProgress();
//                setradarSize(pos);
//                RadarSize(pos);
//                String a = String.valueOf(pos);
//                radarSizeText.setText(a);
//            }
//        });


        final CheckBox isLine = visual.findViewById(R.id.isline);
        espvisual(isLine,2);
        final CheckBox isbox = visual.findViewById(R.id.isBox);
        espvisual(isbox,3);
        final CheckBox isskeleton = visual.findViewById(R.id.isskeleton);
        espvisual(isskeleton,4);
        final CheckBox isdistance = visual.findViewById(R.id.isdistance);
        espvisual(isdistance,5);
        final CheckBox ishealth = visual.findViewById(R.id.ishealth);
        espvisual(ishealth,6);
        final CheckBox isname = visual.findViewById(R.id.isName);
        espvisual(isname,7);
        final CheckBox ishead = visual.findViewById(R.id.ishead);
        espvisual(ishead,8);
        final CheckBox isweapon = visual.findViewById(R.id.isweapon);
        espvisual(isweapon,10);
        final CheckBox isthrowables = visual.findViewById(R.id.isthrowables);
        espvisual(isthrowables,11);
        final CheckBox isnobot = visual.findViewById(R.id.isnobot);
        espvisual(isnobot,15);
        final CheckBox isweaponicon = visual.findViewById(R.id.isweaponicon);
        espvisual(isweaponicon,16);
    }

    public void AimbotOverlay(View aimbot) {
        TextView menutextaimtouch = aimbot.findViewById(R.id.texttouch);
        LinearLayout aimspeedmenu = aimbot.findViewById(R.id.aimspeedmenu);
        LinearLayout recoilmenu = aimbot.findViewById(R.id.recoilmenu);
        LinearLayout smoothnessmenu = aimbot.findViewById(R.id.smoothnessmenu);
        final LinearLayout touchLocationmenu = aimbot.findViewById(R.id.touchlocationmenu);
        final LinearLayout touchsizemenu = aimbot.findViewById(R.id.touchsizemenu);
        final LinearLayout posXmenu = aimbot.findViewById(R.id.posXmenu);
        final LinearLayout posYmenu = aimbot.findViewById(R.id.posYmenu);


       // LinearLayout aimbottoggle = aimbot.findViewById(R.id.aimbot);
        LinearLayout touchttoggle = aimbot.findViewById(R.id.touchsimulation);
        LinearLayout bttoggle = aimbot.findViewById(R.id.bullettrack);

        if (kernel) {
            bttoggle.setVisibility(View.GONE);
           // aimbottoggle.setVisibility(View.GONE);
        } else {
            bttoggle.setVisibility(View.VISIBLE);
          //  aimbottoggle.setVisibility(View.VISIBLE);
        }

        if (!Shell.rootAccess()) {
            touchttoggle.setVisibility(View.GONE);
        } else {
            touchttoggle.setVisibility(View.VISIBLE);
            touchttoggle.setVisibility(View.VISIBLE);
        }

//        aimbottoggle.setOnClickListener(view -> {
//            if (islandint == 0){
//                StartAimFloat();
//                StopAimBulletFloat();
//                StopAimTouch();
//                menutextaimtouch.setVisibility(View.GONE);
//                aimspeedmenu.setVisibility(View.GONE);
//                smoothnessmenu.setVisibility(View.GONE);
//                touchLocationmenu.setVisibility(View.GONE);
//                touchsizemenu.setVisibility(View.GONE);
//                recoilmenu.setVisibility(View.VISIBLE);
//                posXmenu.setVisibility(View.GONE);
//                posYmenu.setVisibility(View.GONE);
//                aimbottoggle.setBackgroundResource(R.drawable.radius_button_off);
//                islandint = 1;
//            }else if (islandint == 1){
//                StopAimBulletFloat();
//                StopAimFloat();
//                StopAimTouch();
//                aimbottoggle.setBackgroundResource(R.drawable.radius_button);
//                islandint = 0;
//            }
//        });

        bttoggle.setOnClickListener(view -> {
            if (islandint == 0){
                StartAimBulletFloat();
                StopAimFloat();
                StopAimTouch();
                menutextaimtouch.setVisibility(View.GONE);
                aimspeedmenu.setVisibility(View.GONE);
                smoothnessmenu.setVisibility(View.GONE);
                touchLocationmenu.setVisibility(View.GONE);
                touchsizemenu.setVisibility(View.GONE);
                recoilmenu.setVisibility(View.GONE);
                posXmenu.setVisibility(View.GONE);
                posYmenu.setVisibility(View.GONE);
                bttoggle.setBackgroundResource(R.drawable.radius_button_off);
                islandint = 1;
            }else if (islandint == 1){
                StopAimBulletFloat();
                StopAimFloat();
                StopAimTouch();
                bttoggle.setBackgroundResource(R.drawable.radius_button);
                islandint = 0;
            }
        });

        touchttoggle.setOnClickListener(view -> {
            if (islandint == 0){
                StartAimTouch();
                StopAimBulletFloat();
                StopAimFloat();
                menutextaimtouch.setVisibility(View.VISIBLE);
                aimspeedmenu.setVisibility(View.VISIBLE);
                smoothnessmenu.setVisibility(View.VISIBLE);
                touchLocationmenu.setVisibility(View.VISIBLE);
                touchsizemenu.setVisibility(View.VISIBLE);
                recoilmenu.setVisibility(View.VISIBLE);
                posXmenu.setVisibility(View.VISIBLE);
                posYmenu.setVisibility(View.VISIBLE);
                touchttoggle.setBackgroundResource(R.drawable.radius_button_off);
                islandint = 1;
            }else if (islandint == 1){
                menutextaimtouch.setVisibility(View.GONE);
                aimspeedmenu.setVisibility(View.GONE);
                smoothnessmenu.setVisibility(View.GONE);
                touchLocationmenu.setVisibility(View.GONE);
                touchsizemenu.setVisibility(View.GONE);
                recoilmenu.setVisibility(View.GONE);
                posXmenu.setVisibility(View.GONE);
                posYmenu.setVisibility(View.GONE);
                StopAimBulletFloat();
                StopAimFloat();
                StopAimTouch();
                touchttoggle.setBackgroundResource(R.drawable.radius_button);
                islandint = 0;
            }
        });

        final LinearLayout aimKnocked = aimbot.findViewById(R.id.aimknocked);
        Linearaimset(aimKnocked, 3, "aimKnocked");

        final LinearLayout aimignore = aimbot.findViewById(R.id.aimignorebot);
        Linearaimset(aimignore, 4, "aimignore");

        final LinearLayout changerotation = aimbot.findViewById(R.id.rotationscren);
        Linearaimset(changerotation, 5, "changerotation");

        final LinearLayout touchlocation = aimbot.findViewById(R.id.touchlocation);
        Linearaimset(touchlocation, 6, "touchlocation");

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
        final ToggleButton less = memory.findViewById(R.id.isreducerecoil);
        memory(less, 1);
        final ToggleButton Cross = memory.findViewById(R.id.issmallcross);
        memory(Cross, 2);
        final ToggleButton amms = memory.findViewById(R.id.isaimlock);
        memory(amms, 3);

        final ToggleButton mbh = memory.findViewById(R.id.ismagichead);
        final ToggleButton mbb = memory.findViewById(R.id.ismagicbody);

        mbh.setOnClickListener(view -> {
            if (islandint == 0){
                Exec("/VNG 2","Magic Bullet Head Actived");
                islandint = 1;
            }else if (islandint == 1){
                Exec("/VNG 3","Magic Bullet Head Disable");
                islandint = 0;
            }
        });

        mbb.setOnClickListener(view -> {
            if (islandint == 0){
                Exec("/VNG 1","Magic Bullet Body Actived");
                islandint = 1;
            }else if (islandint == 1){
                Exec("/VNG 3","Magic Bullet Body Disable");
                islandint = 0;
            }
        });

        final SeekBar wideviewSeekBar = memory.findViewById(R.id.rangewide);
        final TextView wideviewText = memory.findViewById(R.id.rangetextwide);
        setupSeekBar(wideviewSeekBar, wideviewText, getwideview(), new Runnable() {
            @Override
            public void run() {
                WideView(wideviewSeekBar.getProgress());
                getwideview(wideviewSeekBar.getProgress());
            }
        });
    }

    void initOverlayItems(View items){
        LinearLayout menui1 = items.findViewById(R.id.isimenuitems);
        LinearLayout menui2 = items.findViewById(R.id.isimenuvehicle);
        View bottomi1 = items.findViewById(R.id.bottomi1);
        View bottomi2 = items.findViewById(R.id.bottomi2);
        LinearLayout navi1 = items.findViewById(R.id.navitems);
        LinearLayout navi2 = items.findViewById(R.id.navvehicle);

        navi1.setOnClickListener(v -> {
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
        });


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
}
