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

//    public String colors = "21C6FF|21C6FF|21C6FF|66858F|4A3B34|21C6FF|21C6FF|7B665F|FFFFFF|898989";
//    public String pointers = "238,298|254,307|236,322|248,317|240,307|237,323|246,296|244,323|242,329|242,341";

    private static String[] scriptLogin = {
            "搜索或输入网址,57,149,130,34",
            "一键安装,181,652,126,33",
            "未登录遨游账号时,110,264,140,27",
            "导入,290,450,49,26",
            "挖矿收入,18,237,63,22",
            "返回,19,33,34,21"
    };
    String[][] login = new String[][]{
            {
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971"
            },
            {
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.064,0.077",
                    "1000,input,tap,0.064,0.077",
                    "1000,input,tap,0.659,0.103",
                    "1000,input,tap,0.659,0.103",
                    "1000,input,tap,0.5,0.56",
                    "1000,input,tap,0.5,0.56",
                    "2000,input,tap,0.820,0.433",
                    "4000,input,tap,0.5,0.387",
                    "2000,input,text,<account>",
                    "1000,input,tap,0.188,0.188",
                    "1000,input,tap,0.188,0.188",
                    "2000,input,tap,0.6,0.672",
                    "5000,input,tap,0.5,0.387",
                    passWaitTime + ",input,text,<password>",
                    "1000,input,tap,0.188,0.188",
                    "1000,input,tap,0.188,0.188",
                    "2000,input,tap,0.6,0.672",
                    "6000,input,tap,0.291,0.577",
                    "3000,input,tap,0.5,0.577",
                    "1000,input,swipe,0.8,0.5,0.2,0.5",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    mineWaitTime + ",input,tap,0.5,0.84",
                    "2000,input,tap,0.5,0.84"
            }
    };

    private static Object passWaitTime;
    private static Object mineWaitTime;

    public static String[] getLoginScript(Context context) {
        boolean isEmulator = (boolean) SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.EMULATOR, false);
        passWaitTime = SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.PWD_WAIT_TIME, SettingsDialog.PWD_WAIT_TIME);
        mineWaitTime = SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.MINE_WAIN_TIME, SettingsDialog.MINE_WAIT_TIME);
        if (isEmulator) {
            String[] login = new String[]{
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.064,0.077",
                    "1000,input,tap,0.064,0.077",
                    "1000,input,tap,0.659,0.103",
                    "1000,input,tap,0.659,0.103",
                    "1000,input,tap,0.5,0.56",
                    "1000,input,tap,0.5,0.56",
                    "2000,input,tap,0.820,0.433",
                    "4000,input,tap,0.5,0.387",
                    "2000,input,text,<account>",
                    "1000,input,tap,0.188,0.188",
                    "1000,input,tap,0.188,0.188",
                    "2000,input,tap,0.6,0.672",
                    "5000,input,tap,0.5,0.387",
                    passWaitTime + ",input,text,<password>",
                    "1000,input,tap,0.188,0.188",
                    "1000,input,tap,0.188,0.188",
                    "2000,input,tap,0.6,0.672",
                    "6000,input,tap,0.291,0.577",
                    "3000,input,tap,0.5,0.577",
                    "1000,input,swipe,0.8,0.5,0.2,0.5",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    mineWaitTime + ",input,tap,0.5,0.84",
                    "2000,input,tap,0.5,0.84"
            };
            return login;
        } else {
            String[] login = new String[]{
                    "15000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.064,0.077",
                    "1000,input,tap,0.064,0.077",
                    "1000,input,tap,0.659,0.103",
                    "1000,input,tap,0.659,0.103",
                    "1000,input,tap,0.5,0.56",
                    "1000,input,tap,0.5,0.56",
                    "2000,input,tap,0.820,0.461",
                    "4000,input,tap,0.5,0.42",
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
                    "2000,input,tap,0.896,0.965",
                    "2000,input,tap,0.176,0.965",
                    "500,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    mineWaitTime + ",input,tap,0.5,0.893",
                    "2000,input,tap,0.5,0.893"
            };
            return login;
        }

    }

    public static String[] getOpScript(int index) {
        return batchOps[index];
    }

    public static String[] getOpScriptByReadMode(Context context, int readmode) {
        boolean isEmulator = (boolean) SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.EMULATOR, false);
        String[][] scripts = null;
        if (isEmulator) {
            scripts = emulator_batchOps;
        } else {
            scripts = batchOps;
        }
        switch (readmode) {
            case 0:
            case 1:
                return scripts[0];
            case 2:
                return scripts[4];
            case 3:
                return scripts[3];
        }
        return scripts[0];
    }

    private static final String[][] emulator_batchOps = {
            {//初始进入
                    "20000,input,swipe,0.977,0.3,0.977,0.6",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.5,0.84",
                    "10000,input,tap,0.5,0.84",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "3000,input,tap,0.094,0.302",
                    "2000,input,tap,0.5,0.0625",
                    "1000,input,tap,0.5,0.0625",
                    "1000,input,tap,0.85,0.06625",
                    "2000,input,text,<net>",
                    "10000,input,tap,0.933,0.06625",
                    "1000,input,tap,0.933,0.06625",
                    "4000,input,swipe,0.977,0.6,0.977,0.3",
                    "4000,input,swipe,0.977,0.6,0.977,0.3"
            },
            {//再次输入
                    "3000,input,swipe,0.977,0.3,0.977,0.6",
                    "3000,input,swipe,0.977,0.3,0.977,0.6",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<net>",
                    "10000,input,tap,0.913,0.0835",
                    "1000,input,tap,0.913,0.0835",
                    "4000,input,swipe,0.977,0.6,0.977,0.3",
                    "4000,input,swipe,0.977,0.6,0.977,0.3"
            },
            {//浏览模式（阅读）
                    "3000,input,swipe,0.977,0.7,0.977,0.2",
                    "3000,input,swipe,0.977,0.7,0.977,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "3000,input,swipe,0.977,0.3,0.977,0.6",
                    "3000,input,tap,0.102,0.971"
            },
            {//进入小说 （340.960）
                    "20000,input,swipe,0.977,0.3,0.977,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.3159,0.5",
                    "4000,input,swipe,0.977,0.6,0.977,0.3",
                    "4000,input,swipe,0.977,0.6,0.977,0.3"
            },
            {//进入百度新闻
                    "20000,input,swipe,0.977,0.3,0.977,0.6",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.5,0.84",
                    "10000,input,tap,0.5,0.84",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "3000,input,tap,0.0875,0.306",
                    "2000,input,tap,0.142,0.176",
                    "2000,input,swipe,0.977,0.3,0.977,0.6",
                    "3000,input,tap,0.243,0.333",
                    "8000,input,swipe,0.977,0.6,0.977,0.3",
                    "4000,input,swipe,0.977,0.6,0.977,0.3"
            },
            {//小说浏览模式（阅读）
                    "20000,input,swipe,0.977,0.3,0.977,0.6",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "2000,input,tap,0.5,0.84",
                    "10000,input,tap,0.5,0.84",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.176,0.971",
                    "3000,input,swipe,0.977,0.7,0.977,0.2",
                    "3000,input,swipe,0.977,0.7,0.977,0.2",
                    "3000,input,tap,0.5,0.5",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "3000,input,swipe,0.977,0.3,0.977,0.6",
                    "3000,input,tap,0.102,0.965"
            }
    };


    private static final String[][] batchOps = {
            {//初始进入
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "500,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "6000,input,tap,0.5,0.893",
                    "6000,input,tap,0.5,0.893",
                    "3000,input,tap,0.122,0.385",
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
            },
            {//进入小说 （340.960）
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "3000,input,tap,0.3159,0.5",
                    "4000,input,swipe,0.5,0.6,0.5,0.3",
                    "4000,input,swipe,0.5,0.6,0.5,0.3"
            },
            {//进入百度新闻
                    "20000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.176,0.965",
                    "500,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "2000,input,tap,0.896,0.965",
                    "2000,input,tap,0.176,0.965",
                    "500,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "6000,input,tap,0.5,0.893",
                    "6000,input,tap,0.5,0.893",
                    "3000,input,tap,0.122,0.385",
                    "2000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.257,0.432",
                    "4000,input,swipe,0.5,0.6,0.5,0.3",
                    "4000,input,swipe,0.5,0.6,0.5,0.3"
            },
            {//小说浏览模式（阅读）
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
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

    public static String[] enterMinig = {
            "enter,homepage",
            "click,mining",
    };
}
