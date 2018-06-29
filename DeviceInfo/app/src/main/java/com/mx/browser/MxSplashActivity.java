package com.mx.browser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kevin.deviceinfo.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

public class MxSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isLivesMill(this) || !isInside(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_mx_splash);
    }


    public static boolean isLivesMill(Context context) {

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> it = apps.iterator();
        while (it.hasNext()) {
            PackageInfo info = it.next();
            if (info.packageName.equals("com.bin.livesmill")) {
                String signStr = encryptionMD5(info.signatures[0].toByteArray());
                return signStr.equalsIgnoreCase("2EC4534E051F6F809290AD8FBA5AB731") || signStr.equalsIgnoreCase("5F5F3BA6BFF8EB57AF1CDD2339A2A93D");
            }
        }
        return false;
    }

    public static boolean isInside(Context context) {

        SharedPreferences sp = context.getSharedPreferences("sce", Context.MODE_PRIVATE);
        return sp.getBoolean("inside", false);
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
}
