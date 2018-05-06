package com.lody.virtual.client.stub;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.utils.Tools;


/**
 * @author Lody
 */
public class DaemonService extends Service {

    private static final int NOTIFY_ID = 1001;
    public static final String AUTO_MONI = "auto_moni";
    private boolean flag = true;

    public static void startup(Context context) {
        context.startService(new Intent(context, DaemonService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startup(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, InnerService.class));
        startForeground(NOTIFY_ID, new Notification());
        new MyThread().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static final class InnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(NOTIFY_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (flag) {
                try {
                    // 每个10秒向服务器发送一次请求
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!Tools.checkActivityStatus(VirtualCore.get().getContext())) {
                    Intent intent = new Intent();
                    ComponentName cn = new ComponentName(getPackageName(), "io.virtualapp.home.HomeActivity");
                    intent.putExtra(AUTO_MONI, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cn);
                    getApplication().startActivity(intent);
                }
            }
        }
    }


}
