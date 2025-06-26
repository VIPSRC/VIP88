package pubgm.loader;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Config {
    public static String STATUS_BY = "online";
    public static final int USER_ID = 0;
    public static final int[] GAME_LIST_ICON = new int[]{ R.drawable.pubg_global, R.drawable.pubg_korea, R.drawable.pubg_vietnam, R.drawable.pubg_taiwan, R.drawable.pubg_india};
    public static final String GAME_LIST_PKG[] = {"com.tencent.ig","com.pubg.krmobile", "com.vng.pubgmobile", "com.rekoo.pubgm", "com.pubg.imobile"};
}
