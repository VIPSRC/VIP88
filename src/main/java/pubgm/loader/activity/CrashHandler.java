// CrashHandler.java
package pubgm.loader.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class CrashHandler implements Thread.UncaughtExceptionHandler {
  private static final String TAG = "CrashHandler";
  private final Context context;
  private final Thread.UncaughtExceptionHandler defaultHandler;

  public CrashHandler(Context context) {
    this.context = context.getApplicationContext(); // Use application context to prevent leaks
    this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
  }

  @Override
  public void uncaughtException(@NonNull Thread thread, @NonNull Throwable exception) {
    try {
      // Collect crash information
      CrashInfo crashInfo = collectCrashInfo(exception);

      // Log the crash
      Log.e(TAG, "Application crashed: " + crashInfo.getErrorMessage());

      // Launch crash activity with FLAG_NEW_TASK
      Intent intent = new Intent(context, CrashActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      intent.putExtra("Error", crashInfo.getErrorMessage());
      intent.putExtra("Software", crashInfo.getSoftwareInfo());
      intent.putExtra("Date", crashInfo.getDateInfo());

      context.startActivity(intent);

      // Wait for the activity to start
      try {
        Thread.sleep(20000);
      } catch (InterruptedException ignored) {
      }

      // Kill the process
      Process.killProcess(Process.myPid());
      System.exit(1);
    } catch (Exception e) {
      // If our handling fails, call the default handler
      if (defaultHandler != null) {
        defaultHandler.uncaughtException(thread, exception);
      }
    }
  }

  private CrashInfo collectCrashInfo(Throwable exception) {
    StringWriter stackTrace = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stackTrace);
    exception.printStackTrace(printWriter);

    StringBuilder softwareInfo = new StringBuilder()
            .append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
            .append("Android: ").append(Build.VERSION.RELEASE).append("\n")
            .append("Model: ").append(Build.VERSION.INCREMENTAL).append("\n")
            .append("Brand: ").append(Build.BRAND).append("\n")
            .append("Device: ").append(Build.DEVICE).append("\n")
            .append("Product: ").append(Build.PRODUCT);

    String dateInfo = Calendar.getInstance().getTime().toString();

    return new CrashInfo(stackTrace.toString(), softwareInfo.toString(), dateInfo);
  }

  private static class CrashInfo {
    private final String errorMessage;
    private final String softwareInfo;
    private final String dateInfo;

    CrashInfo(String errorMessage, String softwareInfo, String dateInfo) {
      this.errorMessage = errorMessage;
      this.softwareInfo = softwareInfo;
      this.dateInfo = dateInfo;
    }

    String getErrorMessage() { return errorMessage; }
    String getSoftwareInfo() { return softwareInfo; }
    String getDateInfo() { return dateInfo; }
  }
}