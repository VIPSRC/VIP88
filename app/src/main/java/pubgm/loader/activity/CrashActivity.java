package pubgm.loader.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.blankj.molihuan.utilcode.util.ClipboardUtils;
import com.blankj.molihuan.utilcode.util.DeviceUtils;
import com.blankj.molihuan.utilcode.util.ToastUtils;
import pubgm.loader.R;
import pubgm.loader.databinding.ActivityCrashBinding;

import pubgm.loader.utils.ActivityCompat;
import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class CrashActivity extends ActivityCompat {
    private ActivityCrashBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Application Crash", Toast.LENGTH_LONG).show();
        
        binding = ActivityCrashBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setTitle("FoxCheats M1 Crash");

        StringBuilder error = new StringBuilder();

        error.append("Manufacturer: " + DeviceUtils.getManufacturer() + "\n");
        error.append("Device: " + DeviceUtils.getModel() + "\n");
        error.append(getIntent().getStringExtra("Software"));
        error.append("\n\n");
        error.append(getIntent().getStringExtra("Error"));
        error.append("\n\n");
        error.append(getIntent().getStringExtra("Date"));

        binding.result.setText(error.toString());

        binding.fab.setOnClickListener(v -> {
            ToastUtils.make().setBgColor(Color.GRAY).setLeftIcon(R.drawable.ic_launcher_foreground).setNotUseSystemToast().setTextColor(Color.WHITE).show("Text Copy");
            ClipboardUtils.copyText(binding.result.getText());
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem close = menu.add(getString(R.string.close));
        close.setContentDescription("Close App");
        close.setIcon(R.drawable.ic_close);
        close.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.close))) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void finish() {
        super.finish();
        finishAndRemoveTask();
    }
}
