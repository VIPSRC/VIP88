package pubgm.loader.floating;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import pubgm.loader.floating.FloatService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import pubgm.loader.R;
import pubgm.loader.utils.FLog;


public class FightMode extends Service {

    private boolean checkStatus;
    private View mainView;
    private RelativeLayout miniFloatView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams paramsView;

    static {
        try {
            System.loadLibrary("client");
        } catch (UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ShowMainView();
    }

    private void ShowMainView() {
        mainView = LayoutInflater.from(this).inflate(R.layout.toggle_fight, null);
        paramsView = getParaams();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(mainView, paramsView);
        }
        InitShowMainView();
    }

    private LayoutParams getParaams() {
        final LayoutParams params = new LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            getLayoutType(),
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.CENTER | Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        return params;
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

    private void InitShowMainView() {
        miniFloatView = mainView.findViewById(R.id.miniFloatMenu);
        RelativeLayout layoutView = mainView.findViewById(R.id.layout_icon_control_aim);
        final ImageView myImageView = mainView.findViewById(R.id.imageview_aim);

        layoutView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsView.x;
                        initialY = paramsView.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 5 && Ydiff < 5) {
                            if (miniFloatView.getVisibility() == View.VISIBLE) {
                                if (!checkStatus) {
                                    checkStatus = true;
                                    myImageView.animate().rotationBy(0).rotation(-360);
                                    myImageView.setImageResource(R.drawable.fighton);
                                    toggleBooleanValues(true);
                                } else {
                                    checkStatus = false;
                                    myImageView.animate().rotationBy(-360).rotation(0);
                                    myImageView.setImageResource(R.drawable.fightoff);
                                    toggleBooleanValues(false);
                                }
                            }
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        paramsView.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsView.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mainView, paramsView);
                        return true;
                }
                return false;
            }
        });
    }

    private void toggleBooleanValues(boolean isFightModeOn) {
        SharedPreferences prefs = getSharedPreferences("espValue", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String[] ignoredBooleans = {"Health", "Throwables", "Box", "Weapon", "Name", "Weapon Icon", "Hide Bot", "Line", "Head Dots", "Skeleton", "Alert","kernelSelected","esp64Selected","espSafe"};

        for (String key : prefs.getAll().keySet()) {
            if (!shouldIgnoreBooleanKey(key, ignoredBooleans)) {
                Object value = prefs.getAll().get(key);
                if (value instanceof Boolean) {
                    boolean currentValue = (Boolean) value;
                    if (isFightModeOn && currentValue) {
                        editor.putBoolean(key, false);
                    } else if (!isFightModeOn && !currentValue) {
                        editor.putBoolean(key, true);
                    }
                }
            }
        }
        editor.apply();
    }

    private boolean shouldIgnoreBooleanKey(String key, String[] ignoredBooleans) {
        for (String ignoredKey : ignoredBooleans) {
            if (key.equals(ignoredKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkStatus = false;
        if (mainView != null) {
            windowManager.removeView(mainView);
        }
    }
}
