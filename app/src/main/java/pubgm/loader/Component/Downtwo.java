package pubgm.loader.Component;

import static pubgm.loader.server.ApiServer.activity;

import android.annotation.SuppressLint;

import pubgm.loader.R;
import pubgm.loader.activity.MainActivity;
import com.techiness.progressdialoglibrary.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class Downtwo extends AsyncTask<String, String, String> {
    private final Context context;
    private final ProgressDialog progressDialog;
    public boolean success = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    public Downtwo(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTheme(ProgressDialog.THEME_LIGHT);
        progressDialog.setMode(ProgressDialog.MODE_DETERMINATE);
        progressDialog.setMaxValue(100);
        progressDialog.showProgressTextAsFraction(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
       // progressDialog.setTitle(R.string.checking_data);
        progressDialog.setMessage(R.string.waiting_don_t_close_application);
        File filesLoaderDir = new File(context.getFilesDir(), "loader");
        if (filesLoaderDir.exists()) {
            for (File file : filesLoaderDir.listFiles()) {
                file.delete();
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[1]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int lengthOfFile = connection.getContentLength();
            InputStream input = connection.getInputStream();
            String fileName = "mahyong.zip";
            File pathBase = new File(context.getFilesDir().getPath());
            if (!pathBase.exists()) {
                pathBase.mkdirs();
            }
            File pathOutput = new File(pathBase.toString() + "/" + fileName);
            OutputStream output = new FileOutputStream(pathOutput.toString());
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                output.write(data, 0, count);
            }
            if (pathOutput.exists()) {
                new File(pathOutput.toString()).setExecutable(true, true);
            }
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        int progressValue = Integer.parseInt(progress[0]);
        if (progressValue > 0 && progressValue < 100) {
            progressDialog.setProgress(progressValue);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        File pathBase = new File(context.getFilesDir().getPath());
        File loaderDirectory = null; // Declare the variable here

        try {
            loaderDirectory = new File(pathBase, "loader"); // Assign a value here
            if (!loaderDirectory.exists()) {
                loaderDirectory.mkdirs();
            }

            String loaderPath = loaderDirectory.getAbsolutePath();
            progressDialog.dismiss();
            success = true;
            if (success){
                try {
                    Class DeviceInfo = Class.forName(activity());
                    context.startActivity(new Intent(context.getApplicationContext(), DeviceInfo));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            new ZipFile(pathBase + "/mahyong.zip", "mahyong".toCharArray()).extractAll(loaderDirectory.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure loaderDirectory is not null before calling setPermissions
        if (loaderDirectory != null) {
            setPermissions(loaderDirectory);
        }

        // Display a toast message when download is completed
        if (result != null) {
            Toast.makeText(context, context.getString(R.string.download_error) + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.download_complete, Toast.LENGTH_SHORT).show();
        }

        // Delete the temporary zip file
        File newFile = new File(pathBase + "/mahyong.zip");
        if (newFile.exists()) {
            newFile.delete();
        }
    }

    private void setPermissions(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                setPermissions(file); // Recursive call for subdirectories
            }
        } else {
            // Set permissions for files
            directory.setReadable(true, false);
            directory.setWritable(true, false);
            directory.setExecutable(true, false);
        }
    }
}