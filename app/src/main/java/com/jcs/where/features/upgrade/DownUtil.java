package com.jcs.where.features.upgrade;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DownUtil {


    public Context mContext;

    private String path;
    private String downloadUrl;
    private String app_name;
    private int resIcon;

    public DownUtil(Context mContext) {
        this.mContext = mContext;
    }


    public void startDownload(String downloadUrl, String app_name, int resIcon) {
        this.downloadUrl = downloadUrl;
        this.app_name = app_name;
        this.resIcon = resIcon;

//        File filesDir = mContext.getExternalFilesDir("/download/");
//        String path = filesDir.getPath();
//        this.path = path + "/where" + System.currentTimeMillis() + ".apk";

        String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/where/download/";
        this.path = path + "/where" + System.currentTimeMillis() + ".apk";
        File folder = new File(SDCard);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        download(true);
    }

    @SuppressLint("StaticFieldLeak")
    private void download(boolean autoInstall) {
        new AsyncTask<String, Integer, File>() {
            NotificationManager mNotifyManager;
            NotificationCompat.Builder mBuilder;
            private int notifyId;

            @Override
            protected void onPreExecute() {
                notifyId = new Random().nextInt(1000);
                initNoticicationBar(app_name, resIcon);
                ToastUtils.showShort(StringUtils.getString(R.string.downloadStart));
                super.onPreExecute();
            }

            private void initNoticicationBar(String title, int iconId) {
                mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(mContext);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(), // Dummy
                        // Intent
                        // do
                        // nothing
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentTitle(title).setContentText(StringUtils.getString(R.string.downloading)).setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(iconId);
                mBuilder.setContentInfo(StringUtils.getString(R.string.app_name));
                mBuilder.setProgress(100, 0, false);
                mNotifyManager.notify(notifyId, mBuilder.build());
            }

            @Override
            protected File doInBackground(String... params) {

                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(downloadUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return null;
                    }

                    int fileLength = connection.getContentLength();
                    File file = new File(path);
                    // download the file
                    input = connection.getInputStream();
                    output = new FileOutputStream(path);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled()) {
                            input.close();
                            return null;
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) {
                            publishProgress((int) (total * 100 / fileLength));
                        }
                        output.write(data, 0, count);
                    }
                    return file;
                } catch (Exception e) {
                    return null;
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                        }
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException ignored) {
                    }

                    if (connection != null) {
                        connection.disconnect();
                    }

                }

            }

            @Override
            protected void onPostExecute(File result) {
                if (result == null) {
                    ToastUtils.showShort(StringUtils.getString(R.string.downloadError));

                    mBuilder.setContentText(StringUtils.getString(R.string.downloadError)).setProgress(0, 0, false);
                    mNotifyManager.notify(notifyId, mBuilder.build());

                    FeaturesUtil.gotoGooglePlay(mContext);


                } else {
                    ToastUtils.showShort(StringUtils.getString(R.string.downloadOver));
                    mBuilder.setContentText(StringUtils.getString(R.string.downloadOver)).setProgress(0, 0, false);
                    mNotifyManager.notify(notifyId, mBuilder.build());

                    if (autoInstall) {
                        openFile(result);
                    } else {
                        mBuilder.setContentText(StringUtils.getString(R.string.downloadOver_Install)).setProgress(0, 0, false);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, getOpenFileIntent(result), // Dummy
                                // Intent
                                // do
                                // nothing
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setAutoCancel(true);// 点击消失
                        mNotifyManager.notify(notifyId, mBuilder.build());
                    }

                }

                super.onPostExecute(result);
            }

            void openFile(File file) {
                mNotifyManager.cancel(notifyId);
                mContext.startActivity(getOpenFileIntent(file));
            }

            Intent getOpenFileIntent(File file) {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 24) {//Build.VERSION_CODES.N
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                }

                return intent;
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                if (values[0] == 5 || values[0] == 10 || values[0] == 20 || values[0] == 30 || values[0] == 40 || values[0] == 50 || values[0] == 60 || values[0] == 70 || values[0] == 80 || values[0] == 90 || values[0] == 100) {
                    mBuilder.setProgress(100, values[0], false);
                    mNotifyManager.notify(notifyId, mBuilder.build());
                }
                super.onProgressUpdate(values);
            }
        }.execute("");

    }


}
