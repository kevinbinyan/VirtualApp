package io.virtualapp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Kevin on 2018/4/29.
 */

public class Tools {
    public static boolean isSupportEmulator(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo.metaData.getBoolean("SUPPORT_EMULATOR");
    }
}
