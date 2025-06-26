package pubgm.loader.floating;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import pubgm.loader.R;
import pubgm.loader.utils.FLog;
import pubgm.loader.utils.FPrefs;
/*import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;*/

public class ToggleBullet extends Service {
    
    private boolean checkStatus;
    private View mainView;
    private RelativeLayout miniFloatView;
    private WindowManager windowManager;
	private LayoutParams paramsView;
    
    public native void ToggleBullet(boolean value);

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
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
        mainView = LayoutInflater.from(this).inflate(R.layout.toggle_aimbullet, null);
        paramsView = getParaams();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mainView, paramsView);
        InitShowMainView();
    }
    public FPrefs getPref() {
        return FPrefs.with(this);
    }
    private LayoutParams getParaams() {
        final LayoutParams params =
            new LayoutParams(LayoutParams.WRAP_CONTENT,
                                           LayoutParams.WRAP_CONTENT, getLayoutType(), LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
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
            LAYOUT_FLAG = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LAYOUT_FLAG = LayoutParams.TYPE_PHONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LAYOUT_FLAG = LayoutParams.TYPE_TOAST;
        } else {
            LAYOUT_FLAG = LayoutParams.TYPE_SYSTEM_ALERT;
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
                                        ToggleBullet(true);
                                        myImageView.animate().rotationBy(0).rotation(-45);
                                        myImageView.setImageResource(R.drawable.b2);

                                    } else {
                                        checkStatus = false;
                                        ToggleBullet(false);
                                        myImageView.animate().rotationBy(-45).rotation(0);
                                        myImageView.setImageResource(R.drawable.b1);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkStatus = false;
        ToggleBullet(false);
        if (mainView != null)
            windowManager.removeView(mainView);
    }
    
}

    /*private float downRawX, downRawY;
    private View mainView;
    private PowerManager.WakeLock mWakeLock;
    public boolean isBtnChecked = false;
    public static final String LOG_TAG = new String(Base64.decode("emVjbGF5eA==", 0));
    private WindowManager windowManagerMainView;
    private WindowManager.LayoutParams paramsMainView;
    private RelativeLayout layout_icon_control_view;

    static {
        System.loadLibrary("native-lib");
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
        mainView = LayoutInflater.from(this).inflate(R.layout.toggle_aimbullet, null);
        layout_icon_control_view = mainView.findViewById(R.id.layout_icon_control_aim);
        paramsMainView = getParamss();
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
                                final ImageView aim = mainView.findViewById(R.id.imageview_aim);
                                if (!isBtnChecked) {
                                    ToggleBullet(1, true);
                                    aim.setImageDrawable(getResources().getDrawable(R.drawable.a3));
                                    isBtnChecked = true;
                                } else {
                                    ToggleBullet(1, false);
                                    aim.setImageDrawable(getResources().getDrawable(R.drawable.a2));
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

    private static WindowManager.LayoutParams getParamss() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getLayoutType(),
                getFlagsType(),
                PixelFormat.TRANSLUCENT);
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

    private static int getFlagsType() {
        int LAYOUT_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        return LAYOUT_FLAG;
    }

    public native void ToggleBullet(int setting_code, boolean value);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBtnChecked) {
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

class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
}*/
