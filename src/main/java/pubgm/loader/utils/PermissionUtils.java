package pubgm.loader.utils;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.DocumentsContract;
import pubgm.loader.Config;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class PermissionUtils {
    public static int OPEN_FOLDER_REQUEST_CODE = -1;

    public static void openData(Activity activity, int index, String packageName) {
        OPEN_FOLDER_REQUEST_CODE = Config.GAME_LIST_ICON[index];
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Uri muri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fdata%2F" + packageName);
        i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
        activity.startActivityForResult(i, OPEN_FOLDER_REQUEST_CODE);
    }

    public static void openobb(Activity activity, int index, String packageName) {
        OPEN_FOLDER_REQUEST_CODE = Config.GAME_LIST_ICON[index];
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Uri muri = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fobb%2F" + packageName);
        i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
        activity.startActivityForResult(i, OPEN_FOLDER_REQUEST_CODE);
    }
}
