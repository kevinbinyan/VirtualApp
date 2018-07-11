package com.lody.virtual.helper.utils;

import android.content.Context;

import org.apache.log4j.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2018/3/31.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler

{

    private static CrashHandler sInstance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private WeakReference<Context> mContext;
    // 保存手机信息和异常信息
    private Map<String, String> mMessage = new HashMap<>();
    private WeakReference<Logger> log;

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
        mContext = new WeakReference<Context>(context);
        this.log = new WeakReference<Logger>(logger);
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
        log.get().info("崩溃错误信息", e);
//        if (mContext.get() != null && mContext.get().getPackageName().equals(Constants.HOOK_APK)) {
//        }
//        if (mContext.get() != null && mContext.get().getPackageName().equals(Constants.VM)) {
//        }

//            boolean autoRestart = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.AUTO_OP, false);
//            log.get().info("!Tools.isProessRunning(mContext.get(), mContext.get().getPackageName())" + !Tools.isProessRunning(mContext.get(), mContext.get().getPackageName()));
//            log.get().info("autoRestart" + autoRestart);
//            if (autoRestart) {
//                log.get().info("杀死当前进程，等待重启...." + android.os.Process.myPid());
//                VirtualCore.get().killAllApps();
//                ActivityManager mActivityManager = (ActivityManager) mContext.get()
//                        .getSystemService(Context.ACTIVITY_SERVICE);
//                mActivityManager.killBackgroundProcesses(VirtualCore.get().getContext().getPackageName());
//                //把服务给杀死了
////                android.os.Process.killProcess(android.os.Process.myPid());
//            }
//        }
    }

}
