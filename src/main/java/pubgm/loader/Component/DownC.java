package pubgm.loader.Component;

import static pubgm.loader.server.ApiServer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import pubgm.loader.activity.MainActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import pubgm.loader.R;

import net.lingala.zip4j.ZipFile;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownC extends AsyncTask<String, Integer, String> {
    private final Context context;
    private Dialog dialog;
    private TextView percentageText;
    private LottieAnimationView animationView;
    private final SharedPreferences prefs;
    private boolean isSameVersion;

    public DownC(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("myKey", Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.animation_downloading);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        animationView = dialog.findViewById(R.id.animation_view);
        percentageText = dialog.findViewById(R.id.percentage_text);

        dialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        String jsonUrl = urls[0];
        String zipUrl = urls[1];

        try {
            // Step 1: Check JSON for version
            URL url = new URL(jsonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonBuilder.toString());
            int newBypassVersion = jsonObject.getInt("bypassVersion");

            // Get file paths
            File cacheDir = context.getCacheDir();
            File tempFile = new File(cacheDir, "dyzmod.zip");
            File finalFile = new File(cacheDir, "dyzupdate.zip");

            // Step 2: Check if dynamic.zip exists and is valid
            boolean needsDownload = true;
            if (finalFile.exists()) {
                try {
                    ZipFile zipCheck = new ZipFile(finalFile);
                    if (zipCheck.isValidZipFile()) {
                        needsDownload = false;
                    }
                } catch (Exception e) {
                    // If file is invalid or corrupted, delete it
                    finalFile.delete();
                }
            }

            // Step 3: Compare versions
            int currentVersion = prefs.getInt("bypassVersion", 0);
            if (newBypassVersion != currentVersion || needsDownload) {
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                clearDirectory(context.getCacheDir());
                clearDirectory(context.getFilesDir());

                // Download to temporary file
                URL downloadUrl = new URL(zipUrl);
                connection = (HttpURLConnection) downloadUrl.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();
                InputStream input = connection.getInputStream();
                OutputStream output = new FileOutputStream(tempFile);

                byte[] data = new byte[4096];
                long total = 0;
                int count;
                boolean downloadSuccess = true;

                try {
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress((int) ((total * 100) / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    downloadSuccess = false;
                    e.printStackTrace();
                } finally {
                    output.close();
                    input.close();
                }

                // Verify download was successful
                if (downloadSuccess && tempFile.exists() && tempFile.length() > 0) {
                    try {
                        // Verify ZIP integrity
                        ZipFile zipCheck = new ZipFile(tempFile);
                        if (zipCheck.isValidZipFile()) {
                            // Delete old dynamic.zip if it exists
                            if (finalFile.exists()) {
                                finalFile.delete();
                            }
                            // Rename temporary file to dynamic.zip
                            if (tempFile.renameTo(finalFile)) {
                                // Save new version
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("bypassVersion", newBypassVersion);
                                editor.apply();
                                isSameVersion = false;
                            } else {
                                return "RENAME_FAILED";
                            }
                        } else {
                            tempFile.delete();
                            return "INVALID_ZIP";
                        }
                    } catch (Exception e) {
                        tempFile.delete();
                        return "VERIFY_FAILED";
                    }
                } else {
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                    return "DOWNLOAD_FAILED";
                }
            } else {
                isSameVersion = true;
            }

            // Extract the ZIP file
            if (finalFile.exists()) {
                extractZipFile(finalFile, new File(context.getFilesDir().getPath()));
                return "SUCCESS";
            } else {
                return "FILE_NOT_FOUND";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
    private void clearDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    clearDirectory(file); // Recursively clear subdirectories
                } else {
                    file.delete(); // Delete file
                }
            }
        }}
    private void extractZipFile(File zipFile, File destinationDir) {
        try {
            new ZipFile(zipFile).extractAll(destinationDir.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        percentageText.setText(progress[0] + "%");
        animationView.setProgress(progress[0] / 100f);
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();

        switch (result) {
            case "SUCCESS":
                if (isSameVersion) {
                    try {
                        Class DeviceInfo = Class.forName(activity());
                        context.startActivity(new Intent(context.getApplicationContext(), DeviceInfo));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "DOWNLOAD_FAILED":
                Toast.makeText(context, "Download failed. Please check your connection.", Toast.LENGTH_LONG).show();
                break;
            case "INVALID_ZIP":
                Toast.makeText(context, "Downloaded file is corrupted. Please try again.", Toast.LENGTH_LONG).show();
                break;
            case "RENAME_FAILED":
                Toast.makeText(context, "Failed to process download. Please try again.", Toast.LENGTH_LONG).show();
                break;
            case "FILE_NOT_FOUND":
                Toast.makeText(context, "Required files not found. Please try again.", Toast.LENGTH_LONG).show();
                break;
            default:
                if (result.startsWith("ERROR:")) {
                    Toast.makeText(context, "Restart App " , Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}