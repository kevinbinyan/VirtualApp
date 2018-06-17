package com.lody.virtual.helper.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * Created by Kevin on 2018/4/14.
 */

public class Tools {

    private static String sign1 = "2EC4534E051F6F809290AD8FBA5AB731";
    private static String sign2 = "5F5F3BA6BFF8EB57AF1CDD2339A2A93D";

    public static boolean javaValidateSign(Context context) {

        boolean isValidated = false;
        //将签名文件MD5编码一下
//        String signStr = MD5Utils.encrypt(getSignature(context));
        String signStr = getSignMd5Str(context);

        //将应用现在的签名MD5值和我们正确的MD5值对比
        return signStr.equalsIgnoreCase(sign1) || signStr.equalsIgnoreCase(sign2);
    }

    /**
     * 获取app签名md5值
     */
    public static String getSignMd5Str(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

//    public static boolean isProessRunning(Context context, String proessName) {
//
//        checkActivityStatus(context);
//        boolean isRunning = false;
//        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
//
//        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
//        for (ActivityManager.RunningAppProcessInfo info : lists) {
//            if (info.processName.equals(proessName)) {
//                isRunning = true;
//            }
//        }
//
//        return isRunning;
//    }


    public static boolean checkActivityStatus(Context context) {
        boolean mark = false;
        String className;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appTask = am.getRunningTasks(10);

        if (appTask.size() > 0) {
            for (ActivityManager.RunningTaskInfo info : appTask) {
                className = info.topActivity.getClassName();
                if ("io.virtualapp.home.HomeActivity".equals(className)) {
                    if (info.numRunning > 0) {
                        mark = true;
                    }
                    return mark;
                }
            }
        }
        return mark;
    }

    public static boolean isSupportEmulator(Context context) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo.metaData.getBoolean("SUPPORT_EMULATOR");
    }

    public static boolean isMahthon(Context context, String packageName) {

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> it = apps.iterator();
        while (it.hasNext()) {
            PackageInfo info = it.next();
            if (info.packageName.equals(packageName)) {
                String signStr = encryptionMD5(info.signatures[0].toByteArray());
                return signStr.equalsIgnoreCase(sign1) || signStr.equalsIgnoreCase(sign2);
            }
        }
        return true;
    }
}
