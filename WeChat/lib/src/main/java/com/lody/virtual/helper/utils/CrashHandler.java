package com.lody.virtual.helper.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.DaemonService;
import com.lody.virtual.helper.SharedPreferencesUtils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2018/3/31.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler

{

    private static CrashHandler sInstance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    // 保存手机信息和异常信息
    private Map<String, String> mMessage = new HashMap<>();
    private Logger log;

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                sInstance = new CrashHandler();
            }
        }
        return sInstance;
    }

    private CrashHandler() {
    }

    /**
     * 初始化默认异常捕获
     *
     * @param context context
     */
    public void init(Context context, Logger logger) {
        mContext = context;
        this.log = logger;
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将此类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e == null) {// 异常是否为空
            return;
        }

        if (mContext.getPackageName().equals(VirtualCore.get().getContext().getPackageName())) {
            log.info(e.getMessage());
            log.info("尝试自动重启....");
            boolean autoRestart = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.AUTO_RESTART, false);
            if (!Tools.isProessRunning(mContext, mContext.getPackageName()) && autoRestart) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(mContext.getPackageName(), "io.virtualapp.home.HomeActivity");
                intent.putExtra(DaemonService.AUTO_MONI, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cn);
                mContext.startActivity(intent);
            }
        }
    }

}
