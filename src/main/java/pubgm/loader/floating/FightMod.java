package pubgm.loader.floating;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import pubgm.loader.R;
import pubgm.loader.utils.FPrefs;

public class FightMod extends Service {

    private float downRawX, downRawY;
    private View mainView;
    private PowerManager.WakeLock mWakeLock;
    public boolean isBtnChecked = false;
    public static final String LOG_TAG = new String(Base64.decode("emVjbGF5eA==", 0));
    private WindowManager windowManagerMainView;
    private WindowManager.LayoutParams paramsMainView;
    private RelativeLayout layout_icon_control_view;

    public FPrefs getPref() {
        return FPrefs.with(this);
    }
    static {
        System.loadLibrary("client");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate() {
        super.onCreate();

        ShowMainView();
    }

    @SuppressLint({"InvalidWakeLockTag", "WakelockTimeout"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
            mWakeLock.acquire();
        }
        return START_NOT_STICKY;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void ShowMainView() {
        mainView = LayoutInflater.from(this).inflate(R.layout.window_hide_item, null);
        layout_icon_control_view = mainView.findViewById(R.id.layout_icon_control_hideitem);
        paramsMainView = getHieItemFloatParams();
        windowManagerMainView = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerMainView.addView(mainView, paramsMainView);
        isBtnChecked = false;

        layout_icon_control_view.setOnTouchListener(new View.OnTouchListener() {
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

                        downRawX = event.getRawX();
                        downRawY = event.getRawY();

                        return true;

                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewFlesed()) {
                                final ImageView aim = mainView.findViewById(R.id.imageview_hideitem);
                                if (isBtnChecked == false) {
                                    aim.setImageDrawable(getResources().getDrawable(R.drawable.loot_off));
                                    SettingValue(1, true);
                                    isBtnChecked = true;
                                } else {
                                    aim.setImageDrawable(getResources().getDrawable(R.drawable.loot_on));
                                    SettingValue(1, false);
                                    isBtnChecked = false;
                                }
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
        });

    }

    private boolean isViewFlesed() {
        return mainView == null || layout_icon_control_view.getVisibility() == View.VISIBLE;
    }

    private WindowManager.LayoutParams getHieItemFloatParams() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getLayoutType(),
                getFlagsType(),
                PixelFormat.TRANSLUCENT);
        if (getPref().readBoolean("anti_recorder")) {
            HideRecorder.setFakeRecorderWindowLayoutParams(params);
        }
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

    private int getFlagsType() {
        int LAYOUT_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        return LAYOUT_FLAG;
    }

    public native void SettingValue(int setting_code, boolean value);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBtnChecked == true) {
            /*off*/
            isBtnChecked = false;
        }

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

}

