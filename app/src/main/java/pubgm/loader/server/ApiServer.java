package pubgm.loader.server;

import pubgm.loader.utils.FLog;

public class ApiServer {

    static {
        try {
            System.loadLibrary("client");
        } catch(UnsatisfiedLinkError w) {
            FLog.error(w.getMessage());
        }
    }

    public static native String mainURL();
    public static native String getOwner();
    public static native String getTelegram();
    public static native String getGrup();
    public static native String activity();
    public static native String sockindia();
    public static native String sockallversion();
    public static native String CheckServer();
    public static native String FixCrash();
    public static native String EXP();
    public static native String Pw();
    public static native String URLJSON();
    public static native String ApiKeyBox();

}
