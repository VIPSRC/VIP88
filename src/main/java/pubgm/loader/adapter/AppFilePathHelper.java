package pubgm.loader.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;

public class AppFilePathHelper {

    public static void replaceLibWithRoot(Context context, String packageName, String libraryName, Runnable onSuccess) throws Exception {
        try {
            if ("com.pubg.imobile".equals(packageName)) {
                if (onSuccess != null) {
                    onSuccess.run();
                }
                return;
            }

            File filesDir = context.getFilesDir();
            File sourceFile = new File(filesDir, "parrotroot");
            if (!sourceFile.exists()) {
                Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
                return;
            }

            String targetDirPath = getLibraryFilePath(context, packageName, libraryName);
            if (targetDirPath == null) {
                Toast.makeText(context, "Failed to find target directory for package: " + packageName, Toast.LENGTH_SHORT).show();
                return;
            }

            File targetDir = new File(targetDirPath).getParentFile();
            if (!targetDir.exists()) {
                Toast.makeText(context, "Target directory does not exist: " + targetDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                return;
            }

            File targetFile = new File(targetDir, libraryName);
            if (!targetFile.exists()) {
                throw new Exception("Library file " + libraryName + " not found in package: " + packageName);
            }
            if (copyAndRenameWithRoot(sourceFile, targetFile)) {
                if (onSuccess != null) {
                    onSuccess.run();
                }
            } else {
                Toast.makeText(context, "Failed to replace the file with root permissions.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error replacing library file: " + e.getMessage());
        }
    }



    private static boolean copyAndRenameWithRoot(File source, File target) {
        try {
            if (!source.exists()) {
                return false;
            }
            String command = "cp " + source.getAbsolutePath() + " " + target.getAbsolutePath() + "\n";
            String chmodCommand = "chmod 755 " + target.getAbsolutePath() + "\n";
            Process process = Runtime.getRuntime().exec("su");
            try (DataOutputStream os = new DataOutputStream(process.getOutputStream())) {
                os.writeBytes(command);
                os.writeBytes(chmodCommand);
                os.writeBytes("exit\n");
                os.flush();
            }

            int result = process.waitFor();
            return result == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getLibraryFilePath(Context context, String packageName, String libraryName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            String libDirPath = applicationInfo.nativeLibraryDir;
            return libDirPath + "/" + libraryName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "App not found: " + packageName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error finding library path.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
