package io.virtualapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.utils.RSAUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;

import io.virtualapp.home.SettingsDialog;

/**
 * Created by Kevin on 2018/3/11.
 */

public class ParamSettings {

    private static Object passWaitTime;
    private static Object mineWaitTime;

    public static String[] getLoginScript(Context context) {
        passWaitTime = SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.PWD_WAIT_TIME, SettingsDialog.PWD_WAIT_TIME);
        mineWaitTime = SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.MINE_WAIN_TIME, SettingsDialog.MINE_WAIT_TIME);
        String[] login = new String[]{
                "15000,input,swipe,0.5,0.3,0.5,0.6",
                "1000,input,tap,0.064,0.077",
                "1000,input,tap,0.064,0.077",
                "1000,input,tap,0.659,0.103",
                "1000,input,tap,0.659,0.103",
                "1000,input,tap,0.5,0.56",
                "1000,input,tap,0.5,0.56",
                "2000,input,tap,0.820,0.461",
                "3000,input,tap,0.5,0.42",
                "2000,input,text,<account>",
                "1000,input,tap,0.188,0.188",
                "1000,input,tap,0.188,0.188",
                "2000,input,tap,0.6,0.704",
                "5000,input,tap,0.5,0.42",
                passWaitTime + ",input,text,<password>",
                "1000,input,tap,0.188,0.188",
                "1000,input,tap,0.188,0.188",
                "2000,input,tap,0.6,0.704",
                "6000,input,tap,0.291,0.596",
                "3000,input,tap,0.5,0.596",
                "1000,input,swipe,0.8,0.5,0.2,0.5",
                "500,input,tap,0.5,0.485",
                "500,input,tap,0.5,0.485",
                "2000,input,tap,0.5,0.965",
                "2000,input,tap,0.122,0.385",
                "500,input,tap,0.5,0.485",
                "500,input,tap,0.5,0.485",
                mineWaitTime + ",input,tap,0.5,0.893",
                "2000,input,tap,0.5,0.893"

        };
        return login;
    }

    public static String[] getOpScript(int index) {
        return batchOps[index];
    }

    private static final String[][] batchOps = {
            {//初始进入
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
//                    "3000,input,tap,0.5,0.485",
//                    "15000,input,tap,0.5,0.893",
//                    "10000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
//                    "3000,input,tap,0.5,0.262",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<net>",
                    "10000,input,tap,0.913,0.0835",
                    "1000,input,tap,0.913,0.0835",
                    "4000,input,swipe,0.5,0.6,0.5,0.3",
                    "4000,input,swipe,0.5,0.6,0.5,0.3"
            },
            {//再次输入
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<net>",
                    "10000,input,tap,0.913,0.0835",
                    "1000,input,tap,0.913,0.0835",
                    "4000,input,swipe,0.5,0.6,0.5,0.3",
                    "4000,input,swipe,0.5,0.6,0.5,0.3"
            },
            {//浏览模式（阅读）
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965"
            }
    };


//    private static String[] imeis;
//    private static String[] macs;
//    private static String[] imsies;
//
//    public static String[] getDeviceIds() {
//        if (imeis != null) {
//            return imeis;
//        }
//        return readDeviceInfo("imeis");
//    }
//
//    private static String[] readDeviceInfo(String type) {
//        try {
////            String path = (String)SharedPreferencesUtils.getParam( VirtualCore.get().getContext(), SharedPreferencesUtils.DEVICE,"");
////            Log.e("LLLLVV",path);
////            ApplicationInfo appInfo = VirtualCore.get().getContext().getPackageManager().getApplicationInfo(VirtualCore.get().getContext().getPackageName(),
////                    PackageManager.GET_META_DATA);
////            String value = appInfo.metaData.getString("BUILD_PKG");
////            InputStream inputStream  = new FileInputStream(path);
////            InputStream inputStream  = VirtualCore.get().getContext().getContentResolver().openInputStream(Uri.parse(path));
////            InputStream inputStream = VirtualCore.get().getContext().getAssets().open(MD5Utils.encrypt(value));
//            String text = (String) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.DEVICE, "");
////            Log.e("LLLLVV",text);
//            String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
//            //私钥指数
//            String private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";
//            RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
//            //解密后的明文
//            text = RSAUtils.decryptByPrivateKey(text, priKey);
//            JSONObject jsonObject = new JSONObject(text);
//            JSONArray imeiArray = jsonObject.getJSONArray("imeis");
//            imeis = new String[imeiArray.length()];
//            for (int j = 0; j < imeiArray.length(); j++) {
//                imeis[j] = imeiArray.getString(j);
//            }
//            JSONArray macArray = jsonObject.getJSONArray("macs");
//            macs = new String[macArray.length()];
//            for (int j = 0; j < macArray.length(); j++) {
//                macs[j] = macArray.getString(j);
//            }
//            JSONArray imsiArray = jsonObject.getJSONArray("imsies");
//            imsies = new String[imsiArray.length()];
//            for (int j = 0; j < imsiArray.length(); j++) {
//                imsies[j] = imsiArray.getString(j);
//            }
//            switch (type) {
//                case "imeis":
//                    return imeis;
//                case "macs":
//                    return macs;
//                case "imsies":
//                    return imsies;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String[] getMacAddresses() {
//        if (macs != null) {
//            return macs;
//        }
//        return readDeviceInfo("macs");
//    }
//
//    public static String[] getImsis() {
//        if (imsies != null) {
//            return imsies;
//        }
//        return readDeviceInfo("imsies");
//    }
//
//
//    private static String getIMEI() {// calculator IMEI
//        int r1 = 1000000 + new java.util.Random().nextInt(9000000);
//        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
//        String input = r1 + "" + r2;
//        char[] ch = input.toCharArray();
//        int a = 0, b = 0;
//        for (int i = 0; i < ch.length; i++) {
//            int tt = Integer.parseInt(ch[i] + "");
//            if (i % 2 == 0) {
//                a = a + tt;
//            } else {
//                int temp = tt * 2;
//                b = b + temp / 10 + temp % 10;
//            }
//        }
//        int last = (a + b) % 10;
//        if (last == 0) {
//            last = 0;
//        } else {
//            last = 10 - last;
//        }
//        return input + last;
//    }
//
//    private static String getImsi() {
//        // 460022535025034
//        String title = "4600";
//        int second = 0;
//        do {
//            second = new java.util.Random().nextInt(8);
//        } while (second == 4);
//        int r1 = 10000 + new java.util.Random().nextInt(90000);
//        int r2 = 10000 + new java.util.Random().nextInt(90000);
//        return title + "" + second + "" + r1 + "" + r2;
//    }
//
//    private static String getMac() {
//        char[] char1 = "ABCDEF".toCharArray();
//        char[] char2 = "0123456789".toCharArray();
//        StringBuffer mBuffer = new StringBuffer();
//        for (int i = 0; i < 6; i++) {
//            int t = new java.util.Random().nextInt(char1.length);
//            int y = new java.util.Random().nextInt(char2.length);
//            int key = new java.util.Random().nextInt(2);
//            if (key == 0) {
//                mBuffer.append(char2[y]).append(char1[t]);
//            } else {
//                mBuffer.append(char1[t]).append(char2[y]);
//            }
//
//            if (i != 5) {
//                mBuffer.append(":");
//            }
//        }
//        return mBuffer.toString();
//    }
}
