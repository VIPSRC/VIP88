package pubgm.loader.Component;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pubgm.loader.R;
import com.techiness.progressdialoglibrary.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


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
public class DownloadZip extends AsyncTask<String, String, String> {
    private Context context;
    private ProgressDialog progressDialog;
    public static boolean checkdonwload = false ;

    @SuppressLint("UseCompatLoadingForDrawables")
	private native String pw();
    public
    DownloadZip(Context context){
        this.context = context;
        progressDialog = new com.techiness.progressdialoglibrary.ProgressDialog(context);
        progressDialog.setTheme(com.techiness.progressdialoglibrary.ProgressDialog.THEME_LIGHT);
        progressDialog.setMode(com.techiness.progressdialoglibrary.ProgressDialog.MODE_DETERMINATE);
        progressDialog.setMaxValue(100);
        progressDialog.showProgressTextAsFraction(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
       // progressDialog.setTitle(R.string.checking_data);
        progressDialog.setMessage(R.string.waiting_don_t_close_application);
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[1]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int lenghtOfFile = connection.getContentLength();
            InputStream input = connection.getInputStream();
            String fileName = null;
			fileName = "assets.zip";
            File pathBase = new File(context.getFilesDir().getPath());
            if (!pathBase.exists()){
                pathBase.mkdirs();
            }
            File pathOutput = new File(pathBase.toString() + "/" + fileName);
            OutputStream output = new FileOutputStream(pathOutput.toString());
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }
            if (pathOutput.exists()){
                new File(pathOutput.toString()).setExecutable(true, true);
            }
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       return null;
    } 

    @SuppressLint("SetTextI18n")
    @Override
    protected void onProgressUpdate(String... progress) {
        int progressValue = Integer.parseInt(progress[0]);
        if (progressValue > 0 && progressValue < 100) {
            progressDialog.setProgress(progressValue);
        }
    }

    

    protected void onPostExecute(String result) {
        File pathBase = new File(context.getFilesDir().getPath());
        File pathBase2 = new File(context.getFilesDir().getPath());

        try {

            Zip.unzip(new File(pathBase2.toString()+"/assets.zip"),new File(pathBase.toString()+"/"));
        } catch (IOException e) {

            e.printStackTrace();
        }

        progressDialog.dismiss();
        checkdonwload = true;
        zip4j(context.getFilesDir() + "/assets.zip", context.getFilesDir() + "" , pw());

        File newFile = new File(pathBase.toString()+"/assets.zip");
        if (newFile.exists()){
            newFile.delete();
        }

    }

	private boolean zip4j(String path, String outpath, String password) {       
        try {
            new ZipFile(path, password.toCharArray()).extractAll(outpath);
        } catch (Exception e) {
            e.printStackTrace();
			
            return false;
			
        }
        return true;
	}


	private void ExecuteElf(String shell) {
		String s=shell;

		try {
			Runtime.getRuntime().exec(s, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

	public void jknfthrbvgy(String path) {
		try {
			ExecuteElf("chmod 777 " + context.getFilesDir() + path);
			ExecuteElf(context.getFilesDir() + path);
			ExecuteElf("su -c chmod 777 " + context.getFilesDir() + path);
			ExecuteElf("su -c " + context.getFilesDir() + path);
		} catch (Exception e) {

		}
	}
	
	void deleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);

		fileOrDirectory.delete();
	}

		

}
	
