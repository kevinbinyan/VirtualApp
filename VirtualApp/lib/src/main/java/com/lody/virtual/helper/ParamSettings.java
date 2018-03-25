package com.lody.virtual.helper;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.idbound.DDong1;
import com.lody.virtual.helper.utils.MD5Utils;
import com.lody.virtual.helper.utils.RSAUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;

/**
 * Created by Kevin on 2018/3/11.
 */

public class ParamSettings {

    private static String[] imeis;
    private static String[] macs;
    private static String[] imsies;

    public static String[] getDeviceIds() {

//        return BinXM3.deviceIds;
//        return STWQ1.deviceIds;
//        return WXJG1.deviceIds;
//        return WXJG3.deviceIds;
//        return BinRY6.deviceIds;
//        return BinXM4.deviceIds;
//        return WDZ1.deviceIds;
//        return WDZ2.deviceIds;
//        return WDZ3.deviceIds;
//        return WDZ4.deviceIds;
//        return WDZ5.deviceIds;
//        return WDZ7.deviceIds;
//        return WDZ8.deviceIds;
//        return WDZ9.deviceIds;
//        return WDZ10.deviceIds;
//        return WDZ11.deviceIds;
//        return WDZ12.deviceIds;
//        return WDZ13.deviceIds;
//        return WDZ14.deviceIds;
//        return WDZ15.deviceIds;
//        return WDZ16.deviceIds;
//        return WDZ17.deviceIds;
//        return WDZ18.deviceIds;
//        return WDZ19.deviceIds;
//        return WDZ20.deviceIds;
//        return WDZ21.deviceIds;
//        return WDZ22.deviceIds;
//        return WDZ23.deviceIds;
//        return WDZ6.deviceIds;
//        return BinRY6.deviceIds;
//        return WDZPY1.deviceIds;
//        return WDZPY2.deviceIds;
//        return WDZPY3.deviceIds;
        if(imeis != null){
            return imeis;
        }
        return readDeviceInfo("imeis");
    }

    private static String[] readDeviceInfo(String type) {
        try {

            ApplicationInfo appInfo = VirtualCore.get().getContext().getPackageManager().getApplicationInfo(VirtualCore.get().getContext().getPackageName(),
                    PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString("BUILD_PKG");
            InputStream inputStream = VirtualCore.get().getContext().getAssets().open(MD5Utils.encrypt(value));
            String text = readAssetsTxt(inputStream);
            String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
            //私钥指数
            String private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";
            RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
            //解密后的明文
            text = RSAUtils.decryptByPrivateKey(text, priKey);
            JSONObject jsonObject = new JSONObject(text);
            JSONArray imeiArray = jsonObject.getJSONArray("imeis");
            imeis = new String[imeiArray.length()];
            for (int j = 0; j < imeiArray.length(); j++) {
                imeis[j] = imeiArray.getString(j);
            }
            JSONArray macArray = jsonObject.getJSONArray("imeis");
            macs = new String[macArray.length()];
            for (int j = 0; j < macArray.length(); j++) {
                macs[j] = macArray.getString(j);
            }
            JSONArray imsiArray = jsonObject.getJSONArray("imeis");
            imsies = new String[imsiArray.length()];
            for (int j = 0; j < imsiArray.length(); j++) {
                imsies[j] = imsiArray.getString(j);
            }
            switch (type){
                case "imeis":
                    return imeis;
                case "macs":
                    return macs;
                case "imsies":
                    return imsies;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @return
     */
    public static String readAssetsTxt(InputStream is) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while ((n = is.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            String text = new String(out.toByteArray());
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }


    public static String[] getMacAddresses() {
//        return BinXM3.macAddresses;
//        return STWQ1.macAddresses;
//        return WXJG1.macAddresses;
//        return WXJG3.macAddresses;
//        return BinRY6.macAddresses;
//        return BinXM4.macAddresses;
//        return WDZ1.macAddresses;
//        return WDZ2.macAddresses;
//        return WDZ3.macAddresses;
//        return WDZ4.macAddresses;
//        return WDZ5.macAddresses;
//        return WDZ7.macAddresses;
//        return WDZ8.macAddresses;
//        return WDZ9.macAddresses;
//        return WDZ10.macAddresses;
//        return WDZ11.macAddresses;
//        return WDZ12.macAddresses;
//        return WDZ13.macAddresses;
//        return WDZ14.macAddresses;
//        return WDZ15.macAddresses;
//        return WDZ16.macAddresses;
//        return WDZ17.macAddresses;
//        return WDZ18.macAddresses;
//        return WDZ19.macAddresses;
//        return WDZ20.macAddresses;
//        return WDZ21.macAddresses;
//        return WDZ22.macAddresses;
//        return WDZ23.macAddresses;
//        return WDZ6.macAddresses;
//        return BinRY6.macAddresses;
//        return WDZPY1.macAddresses;
//        return WDZPY2.macAddresses;
//        return WDZPY3.macAddresses;
//        return WDZPY4.macAddresses;
        if(macs != null) {
            return macs;
        }
       return readDeviceInfo("macs");
    }

    public static String[] getImsis() {
//        return BinXM3.imsis;
//        return STWQ1.imsis;
//        return WXJG1.imsis;
//        return WXJG3.imsis;
//        return BinRY6.imsis;
//        return BinXM4.imsis;
//        return WDZ1.imsis;
//        return WDZ2.imsis;
//        return WDZ3.imsis;
//        return WDZ4.imsis;
//        return WDZ5.imsis;
//        return WDZ7.imsis;
//        return WDZ8.imsis;
//        return WDZ9.imsis;
//        return WDZ10.imsis;
//        return WDZ11.imsis;
//        return WDZ12.imsis;
//        return WDZ13.imsis;
//        return WDZ14.imsis;
//        return WDZ15.imsis;
//        return WDZ16.imsis;
//        return WDZ17.imsis;
//        return WDZ18.imsis;
//        return WDZ19.imsis;
//        return WDZ20.imsis;
//        return WDZ21.imsis;
//        return WDZ22.imsis;
//        return WDZ23.imsis;
//        return WDZ6.imsis;
//        return BinRY6.imsis;
//        return WDZPY1.imsis;
//        return WDZPY2.imsis;
//        return WDZPY3.imsis;
//        return WDZPY4.imsis;
        if(imsies != null){
            return imsies;
        }
        return readDeviceInfo("imsies");
    }


    private static String getIMEI() {// calculator IMEI
        int r1 = 1000000 + new java.util.Random().nextInt(9000000);
        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
        String input = r1 + "" + r2;
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        return input + last;
    }

    private static String getImsi() {
        // 460022535025034
        String title = "4600";
        int second = 0;
        do {
            second = new java.util.Random().nextInt(8);
        } while (second == 4);
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 10000 + new java.util.Random().nextInt(90000);
        return title + "" + second + "" + r1 + "" + r2;
    }

    private static String getMac() {
        char[] char1 = "ABCDEF".toCharArray();
        char[] char2 = "0123456789".toCharArray();
        StringBuffer mBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int t = new java.util.Random().nextInt(char1.length);
            int y = new java.util.Random().nextInt(char2.length);
            int key = new java.util.Random().nextInt(2);
            if (key == 0) {
                mBuffer.append(char2[y]).append(char1[t]);
            } else {
                mBuffer.append(char1[t]).append(char2[y]);
            }

            if (i != 5) {
                mBuffer.append(":");
            }
        }
        return mBuffer.toString();
    }

    public static final String[][] batchOps = {
            {//读小说
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "30000,input,tap,0.5,0.965",
                    "1000,input,tap,0.312,0.485",
                    "5000,input,swipe,0.5,0.6,0.5,0.3",
                    "1000,input,tap,0.5,0.5",
                    "1000,input,tap,0.5,0.6",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.6,0.5,0.3",
                    "1000,input,tap,0.5,0.5",
                    "1000,input,tap,0.5,0.6",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            },
            {//汽车之家
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "30000,input,tap,0.5,0.965",
                    "3000,input,tap,0.687,0.385",
                    "5000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "1000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "1000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            },
            {//百度 5分钟
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "15000,input,tap,0.5,0.893",
                    "15000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
                    "3000,input,tap,0.354,0.326",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            },
            {//百度 10分钟
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "15000,input,tap,0.5,0.893",
                    "15000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
                    "3000,input,tap,0.354,0.326",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            },
            {//百度 15分钟
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "15000,input,tap,0.5,0.893",
                    "15000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
                    "3000,input,tap,0.354,0.326",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            },
            {//登录
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
                    "2000,input,text,<password>",
                    "1000,input,tap,0.188,0.188",
                    "1000,input,tap,0.188,0.188",
                    "2000,input,tap,0.6,0.704",
                    "6000,input,tap,0.291,0.596",
                    "3000,input,tap,0.5,0.596",
                    "1000,input,swipe,0.8,0.5,0.2,0.5",
                    "500,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "10000,input,tap,0.5,0.893",
                    "500,input,tap,0.5,0.893"
            },
            {//百度 30分钟
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "15000,input,tap,0.5,0.893",
                    "10000,input,tap,0.5,0.965",
                    "8000,input,tap,0.122,0.385",
//                    "3000,input,tap,0.354,0.326",
                    "3000,input,swipe,0.5,0.2,0.5,0.7",
                    "3000,input,tap,0.265,0.431",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            },
            {//百度 20分钟
                    "30000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.5,0.485",
                    "15000,input,tap,0.5,0.893",
                    "15000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
                    "3000,input,tap,0.354,0.326",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "5000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "10000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.102,0.965",
                    "5000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965"
            }

    };

}
