package pubgm.loader.service;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.content.Intent;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import pubgm.loader.utils.FLog;
import pubgm.loader.R;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class MainService extends Service {
    private static MainService instance;
    public static boolean isRunning = false;
    
    static {
        try {
                System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }
    
    public static native String InitBase();
    public static native void closeSocket();
    
    public static MainService get() {
    	return instance;
    }
    
    public static void startService(Context context, String packageName) {
    	if (instance == null) {
            /*Intent intent = new Intent(context, MainService.class);
            intent.putExtra("running_package", packageName);
            context.startService(intent);*/
        }
    }
    
    public static void stopService() {
    	if (instance != null) {
            instance.onDestroy();
        }
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        
        try {
            if (!isRunning) {
                RunServer();
                isRunning = true;
            }
        } catch(Exception err) {
        	FLog.error(err.getMessage());
        }
    }
    
    private static void RunServer() {
    	try {
    	    new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String respon = InitBase();
                    if (respon.equalsIgnoreCase("Server Accept")) {
                        toast(R.drawable.ic_check, "Server Connected");
                    } else {
                        toast(R.drawable.ic_error, respon);//"Error Server No Connected, Please restart.");
                        stopService();
                    }
                }
            }, 10 * 1000);
    	} catch(Exception err) {
    		FLog.error(err.getCause().getMessage());
            stopService();
    	}
    }
    
    @Override
    public void onDestroy() {
        closeSocket();
        isRunning = false;
        stopSelf();
        instance = null;
        super.onDestroy();
    }
    
    private static void toast(int id, CharSequence msg) {
        ToastUtils _toast = ToastUtils.make();
        _toast.setBgColor(android.R.color.white);
        _toast.setLeftIcon(id);
        _toast.setTextColor(android.R.color.black);
        _toast.setNotUseSystemToast();
        _toast.show(msg);
    }
    
    
}
