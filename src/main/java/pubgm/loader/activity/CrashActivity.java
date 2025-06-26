// CrashActivity.java
package pubgm.loader.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.molihuan.utilcode.util.ClipboardUtils;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import pubgm.loader.R;
import pubgm.loader.databinding.ActivityCrashBinding;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class CrashActivity extends AppCompatActivity {
    private ActivityCrashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure the activity shows even when the device is locked
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        binding = ActivityCrashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Crash Log");
        }

        // Get crash information from intent
        String error = getIntent().getStringExtra("Error");
        String software = getIntent().getStringExtra("Software");
        String date = getIntent().getStringExtra("Date");

        // Build the crash report
        StringBuilder crashReport = new StringBuilder();
        if (software != null) crashReport.append(software).append("\n\n");
        if (error != null) crashReport.append("Stack Trace:\n").append(error).append("\n\n");
        if (date != null) crashReport.append("Crash Date:\n").append(date);

        binding.result.setText(crashReport.toString());

        // Set up the copy button
        binding.fab.setOnClickListener(v -> {
            ClipboardUtils.copyText(binding.result.getText());
            ToastUtils.make()
                    .setBgColor(Color.GRAY)
                    .setLeftIcon(R.drawable.ic_launcher_foreground)
                    .setNotUseSystemToast()
                    .setTextColor(Color.WHITE)
                    .show("Crash log copied to clipboard");
        });
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem close = menu.add(getString(R.string.close));
        close.setContentDescription("Close App");
        close.setIcon(R.drawable.ic_close);
        close.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(getString(R.string.close))) {
            finishAndRemoveTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}