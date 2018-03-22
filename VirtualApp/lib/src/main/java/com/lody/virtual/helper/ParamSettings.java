package com.lody.virtual.helper;

import com.lody.virtual.helper.idbound.BinRY6;
import com.lody.virtual.helper.idbound.BinXM3;
import com.lody.virtual.helper.idbound.BinXM4;
import com.lody.virtual.helper.idbound.DDong1;
import com.lody.virtual.helper.idbound.STWQ1;
import com.lody.virtual.helper.idbound.WDZ1;
import com.lody.virtual.helper.idbound.WDZ10;
import com.lody.virtual.helper.idbound.WDZ11;
import com.lody.virtual.helper.idbound.WDZ12;
import com.lody.virtual.helper.idbound.WDZ13;
import com.lody.virtual.helper.idbound.WDZ14;
import com.lody.virtual.helper.idbound.WDZ15;
import com.lody.virtual.helper.idbound.WDZ16;
import com.lody.virtual.helper.idbound.WDZ17;
import com.lody.virtual.helper.idbound.WDZ18;
import com.lody.virtual.helper.idbound.WDZ19;
import com.lody.virtual.helper.idbound.WDZ2;
import com.lody.virtual.helper.idbound.WDZ20;
import com.lody.virtual.helper.idbound.WDZ21;
import com.lody.virtual.helper.idbound.WDZ22;
import com.lody.virtual.helper.idbound.WDZ23;
import com.lody.virtual.helper.idbound.WDZ3;
import com.lody.virtual.helper.idbound.WDZ4;
import com.lody.virtual.helper.idbound.WDZ5;
import com.lody.virtual.helper.idbound.WDZ6;
import com.lody.virtual.helper.idbound.WDZ7;
import com.lody.virtual.helper.idbound.WDZ8;
import com.lody.virtual.helper.idbound.WDZ9;
import com.lody.virtual.helper.idbound.WXJG1;
import com.lody.virtual.helper.idbound.WXJG3;

/**
 * Created by Kevin on 2018/3/11.
 */

public class ParamSettings {

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
        return WDZ23.deviceIds;
//        return WDZ6.deviceIds;
//        return BinRY6.deviceIds;
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
        return WDZ23.macAddresses;
//        return WDZ6.macAddresses;
//        return BinRY6.macAddresses;
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
        return WDZ23.imsis;
//        return WDZ6.imsis;
//        return BinRY6.imsis;

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
                    "2000,input,tap,0.5,0.42",
                    "2000,input,text,<account>",
                    "1000,input,tap,0.188,0.188",
                    "1000,input,tap,0.188,0.188",
                    "2000,input,tap,0.6,0.704",
                    "2000,input,tap,0.5,0.42",
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
