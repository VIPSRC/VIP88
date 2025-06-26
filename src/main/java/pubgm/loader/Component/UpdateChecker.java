package pubgm.loader.Component;

import static pubgm.loader.server.ApiServer.URLJSON;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import pubgm.loader.BuildConfig;
import pubgm.loader.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class UpdateChecker extends AppCompatActivity {

    private Context context;
    private static final String AUTHORITY = "pubgm.loader.provider";
    private static final int SOCKET_TIMEOUT_MS = 5000;
    private static final int MAX_RETRIES = 1;
    private RequestQueue requestQueue;
    private BroadcastReceiver downloadReceiver;
    private AlertDialog progressDialog;
    private TextView downloadPercentage;
    private TextView downloadSize;
    private ProgressBar progressBar;
    private long currentDownloadId = -1;
    private boolean isDownloading = false;

    public UpdateChecker(Context context) {
        this.context = context;
        initializeRequestQueue();
    }

    private void initializeRequestQueue() {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public void checkForUpdate() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URLJSON(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleUpdateResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }
        ) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setShouldCache(true);
        requestQueue.add(jsonObjectRequest);
    }

    private void handleUpdateResponse(JSONObject response) {
        try {
            String latestVersion = response.getString("version");
            String currentVersion = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;

            if (!currentVersion.equals(latestVersion)) {
                String apkUrl = response.getString("url");
                showUpdateDialog(context, apkUrl);
            } else {
                Toast.makeText(context, R.string.application_already_latest_versiob,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error checking for updates",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(VolleyError error) {
        error.printStackTrace();
        String errorMessage = "Network error occurred";
        if (error.networkResponse != null) {
            errorMessage += ": " + error.networkResponse.statusCode;
        }
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(Context context, final String apkUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_one, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.heading_alert_info);
        TextView dialogMessage = dialogView.findViewById(R.id.title_alert_info);
        TextView buttonYes = dialogView.findViewById(R.id.alertok);
        TextView buttonNo = dialogView.findViewById(R.id.alertno);
        builder.setCancelable(false);
        dialogTitle.setText("Update Available");
        dialogMessage.setText("A new version of the app is available. Would you like to update?");

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                downloadAndUpdate(apkUrl);
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                // Force close the app
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        alertDialog.show();
    }

    private void downloadAndUpdate(String apkUrl) {
        if (isDownloading) {
            Toast.makeText(context, "Download already in progress", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();

        File apkFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "parrotrelease.apk");
        if (apkFile.exists()) {
            apkFile.delete();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setTitle(context.getString(R.string.downloading_update));
        request.setDescription(context.getString(R.string.downloading_new_version_of_app));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "parrotrelease.apk");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        currentDownloadId = downloadManager.enqueue(request);
        isDownloading = true;

        startProgressMonitoring(downloadManager, currentDownloadId);
        registerDownloadReceiver(downloadManager);
    }

    private void startProgressMonitoring(final DownloadManager downloadManager, final long downloadId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;

                while (downloading && isDownloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);

                    try (android.database.Cursor cursor = downloadManager.query(q)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            int status = cursor.getInt(cursor.getColumnIndex(
                                    DownloadManager.COLUMN_STATUS));

                            if (status == DownloadManager.STATUS_SUCCESSFUL ||
                                    status == DownloadManager.STATUS_FAILED) {
                                downloading = false;
                                isDownloading = false;
                            } else {
                                updateProgressUI(cursor);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }).start();
    }

    private void updateProgressUI(final android.database.Cursor cursor) {
        long bytesDownloaded = cursor.getLong(
                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        long bytesTotal = cursor.getLong(
                cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

        if (bytesTotal > 0) {
            final int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
            final String downloadedSize = formatFileSize(bytesDownloaded);
            final String totalSize = formatFileSize(bytesTotal);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        downloadPercentage.setText(progress + "%");
                        downloadSize.setText(downloadedSize + " / " + totalSize);
                        progressBar.setProgress(progress);
                    }
                }
            });
        }
    }

    private void registerDownloadReceiver(final DownloadManager downloadManager) {
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == currentDownloadId) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    try (android.database.Cursor cursor = downloadManager.query(query)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            int status = cursor.getInt(
                                    cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                dismissProgressDialog();
                                File file = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS), "impact.apk");
                                installApk(file);
                            } else if (status == DownloadManager.STATUS_FAILED) {
                                dismissProgressDialog();
                                Toast.makeText(context, "Download failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    cleanup();
                }
            }
        };

        context.registerReceiver(downloadReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.download_progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        progressDialog = builder.create();
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        downloadPercentage = dialogView.findViewById(R.id.download_percentage);
        downloadSize = dialogView.findViewById(R.id.download_size);
        progressBar = dialogView.findViewById(R.id.download_progress);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDownload();
            }
        });

        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void cancelDownload() {
        if (currentDownloadId != -1) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(
                    Context.DOWNLOAD_SERVICE);
            downloadManager.remove(currentDownloadId);
            isDownloading = false;
            dismissProgressDialog();
            cleanup();

            // Exit the application
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).finishAffinity();
            } else {
                // If context is not an activity, use intent to exit
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                // Force close the app
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }


    private void installApk(File file) {
        Uri apkUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    public void cleanup() {
        isDownloading = false;
        if (downloadReceiver != null) {
            try {
                context.unregisterReceiver(downloadReceiver);
                downloadReceiver = null;
            } catch (IllegalArgumentException e) {
                // Receiver not registered
            }
        }

        if (requestQueue != null) {
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
            requestQueue.stop();
        }

        dismissProgressDialog();
        currentDownloadId = -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanup();
    }
}