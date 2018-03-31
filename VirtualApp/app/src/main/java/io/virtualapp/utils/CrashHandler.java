package io.virtualapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
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

        log.info(e.getMessage());
    }

}
