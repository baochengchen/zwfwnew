package com.ztesoft.zwfw.moudle.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ztesoft.zwfw.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by bcc on 2017/3/27 0027.
 */

public class UpdateService extends Service {


    private static final String TAG = UpdateService.class.getSimpleName();
    private String downloadFilePath;
    private Notification mNotification;
    private NotificationManager nm;


    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        downloadFilePath = Environment.getExternalStorageDirectory() + "/app/download/"+getString(R.string.app_name_en)+".apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       //if(null != intent) {
            notifyUser("", "", 0);
            startDownLoad(intent.getStringExtra("downloadUrl"), downloadFilePath);
        //}
        return START_REDELIVER_INTENT;
    }


    private void startDownLoad(final String downLoadUrl, final String filePath) {
        Log.d(TAG,"startDownLoad"+downLoadUrl);
        checkLocalFilePath(filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(downLoadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(20000);
                    connection.setReadTimeout(20000);
                    connection.setRequestMethod("GET");
                    //connection.setRequestProperty("Range", "bytes = 0-");
                    Log.d(TAG,"responseCode:"+connection.getResponseCode());
                    if (200 == connection.getResponseCode()) {
                        int totalSize = connection.getContentLength();
                        int currentSize = 0;
                        int process = 0;
                        int count = 0;
                        InputStream ins = connection.getInputStream();
                        FileOutputStream fos = new FileOutputStream(filePath);
                        byte[] buffer = new byte[1024];
                        int lenth = 0;
                        while ((lenth = ins.read(buffer)) != -1) {
                            fos.write(buffer, 0, lenth);
                            currentSize += lenth;
                            process = (int) (Float.parseFloat(getTwoPointFloatStr(currentSize * 100 / (float) totalSize)));
                            if (count % 30 == 0 && process <= 100) {
                                Message msg = processHandler.obtainMessage();
                                msg.what = process;
                                processHandler.sendMessage(msg);
                            }
                            count++;
                        }

                        Message msg = processHandler.obtainMessage();
                        msg.what = 100;
                        processHandler.sendMessage(msg);
                    } else {
                        notifyUser("", "", -1);
                    }
                } catch (MalformedURLException e) {
                    notifyUser("", "", -1);
                } catch (IOException e) {
                    notifyUser("", "", -1);
                }
            }
        }).start();
    }


    /**
     * 检查文件路径是否存在
     *
     * @param path
     */
    private void checkLocalFilePath(String path) {
        File dir = new File(path.substring(0, path.lastIndexOf("/") + 1));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void notifyUser(String result, String msg, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icon_app_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_logo))
                .setContentTitle(getString(R.string.app_name));
        if (progress > 0 && progress <= 100) {

            builder.setProgress(100, progress, false);
            builder.setContentText(progress == 100 ? getString(R.string.finish_download) : getString(R.string.downloading) + progress + "%");

        } else {
            builder.setProgress(0, 0, false);
            builder.setContentText(progress == 0 ? getString(R.string.start_download) : getString(R.string.download_failed));
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(progress >= 100 ? getContentIntent() :
                PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        mNotification = builder.build();
        nm.notify(0, mNotification);


    }


    private PendingIntent getContentIntent() {
        File apkFile = new File(downloadFilePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
                "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startActivity(intent);
        return pendingIntent;

    }

    private Handler processHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                notifyUser("", "", msg.what);
            }
        }
    };


    private String getTwoPointFloatStr(float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
