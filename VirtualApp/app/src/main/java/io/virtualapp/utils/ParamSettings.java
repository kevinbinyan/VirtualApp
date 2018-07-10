package io.virtualapp.utils;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.utils.Tools;

import java.util.Calendar;

/**
 * Created by Kevin on 2018/3/11.
 */

public class ParamSettings {

    public static String[] getOpScript(int index, int currentLaunchIndex) {
        boolean isEmulator = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.EMULATOR, false);
        boolean clear = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.CLEAR, false);
        boolean needClearToday = isNeeded(currentLaunchIndex);
        String[][] scripts = null;
        if (isEmulator) {
            scripts = emulator_batchOps;
        } else {
            if (clear && needClearToday) {
                scripts = clear_batchOps;
            } else {
                scripts = batchOps;
            }
        }
        return scripts[index];
    }

    public static String[] getOpScriptByReadMode(int currentLaunchIndex) {
        boolean isEmulator = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.EMULATOR, false);
        boolean clear = (boolean) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.CLEAR, false);
        boolean needClearToday = isNeeded(currentLaunchIndex);
        String[][] scripts = null;
        if (isEmulator) {
            scripts = emulator_batchOps;
        } else {
            if (clear && needClearToday) {
                scripts = clear_batchOps;
                SharedPreferencesUtils.setParam(VirtualCore.get().getContext(), SharedPreferencesUtils.CLEAR + currentLaunchIndex, getTimesTodayMorning());
            } else {
                scripts = batchOps;
            }
        }
        if (Tools.isBigClient(VirtualCore.get().getContext())) {
            return scripts[0];
        }
        return scripts[2];
    }

    private static boolean isNeeded(int currentLaunchIndex) {
        long recordTime = (long) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.CLEAR + currentLaunchIndex, 0l);
        if (recordTime == getTimesTodayMorning()) {
            return false;
        } else {
            return true;
        }
    }

    private static final String[][] emulator_batchOps = {
            {//进入百度
                    "20000,input,swipe,0.977,0.3,0.977,0.6",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.129,0.971",
                    "500,input,tap,0.129,0.971",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "6000,input,tap,0.5,0.84",
                    "6000,input,tap,0.5,0.971",
                    "3000,input,tap,0.0875,0.306",
                    "2000,input,tap,0.142,0.129",
                    "2000,input,tap,0.142,0.129",
                    "2000,input,tap,0.5,0.0625",
                    "1000,input,tap,0.5,0.0625",
                    "1000,input,tap,0.85,0.06625",
                    "2000,input,text,<net>",
                    "10000,input,tap,0.933,0.06625",
                    "1000,input,tap,0.933,0.06625",
                    "8000,input,swipe,0.977,0.6,0.977,0.3",
                    "8000,input,swipe,0.977,0.6,0.977,0.3"
            },
            {//浏览模式
                    "3000,input,swipe,0.977,0.7,0.977,0.2",
                    "3000,input,swipe,0.977,0.7,0.977,0.2",
                    "3000,input,tap,0.5,0.5",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "10000,input,swipe,0.977,0.6,0.977,0.3",
                    "3000,input,keyevent,4"
            },
            {//初始进入主页
                    "10000,input,swipe,0.977,0.3,0.977,0.6",
                    "2000,input,tap,0.904,0.971",
                    "2000,input,tap,0.129,0.971",
                    "500,input,tap,0.129,0.971",
                    "3000,input,tap,0.5,0.971",
                    "500,input,tap,0.5,0.392",
                    "500,input,tap,0.5,0.392",
                    "6000,input,tap,0.5,0.84",
                    "6000,input,tap,0.5,0.971",
                    "3000,input,tap,0.0875,0.306",
                    "2000,input,tap,0.142,0.129",
                    "2000,input,tap,0.142,0.129"
            },
            {//输入网址并浏览
                    "3000,input,swipe,0.977,0.3,0.977,0.6",
                    "1000,input,tap,0.5,0.0675",
                    "1000,input,tap,0.5,0.0675",
                    "1000,input,tap,0.85,0.0675",
                    "2000,input,text,<net>",
                    "10000,input,tap,0.937,0.0675",
                    "50000,input,tap,0.937,0.0675"
//                    "8000,input,swipe,0.977,0.6,0.977,0.3",
//                    "8000,input,swipe,0.977,0.6,0.977,0.3",
//                    "8000,input,swipe,0.977,0.6,0.977,0.3",
//                    "8000,input,swipe,0.977,0.6,0.977,0.3",
//                    "8000,input,swipe,0.977,0.6,0.977,0.3",
//                    "8000,input,swipe,0.977,0.6,0.977,0.3"
            }
    };


    private static final String[][] batchOps = {

            {//进入大站
                    "15000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.129,0.965",
                    "500,input,tap,0.129,0.965",
                    "3000,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "25000,input,tap,0.5,0.548",
                    "2000,input,tap,0.5,0.893",
                    "6000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
                    "1000,input,tap,0.133,0.243",
                    "1000,input,tap,0.133,0.243",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<main_net>",
                    "5000,input,tap,0.913,0.0835",
                    "1000,input,tap,0.913,0.0835",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2"
            },
            {//浏览模式
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,keyevent,4"
            },
            {//初始进入主页
                    "15000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.129,0.965",
                    "500,input,tap,0.129,0.965",
                    "3000,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "25000,input,tap,0.5,0.548",
                    "2000,input,tap,0.5,0.893",
                    "6000,input,tap,0.5,0.965",
                    "5000,input,tap,0.122,0.385",
                    "1000,input,tap,0.133,0.243",
                    "1000,input,tap,0.133,0.243"
            },
            {//输入网址并浏览
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<net>",
                    "5000,input,tap,0.913,0.0835",
                    "5000,input,tap,0.913,0.0835",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3"
            }

    };

    private static final String[][] clear_batchOps = {

            {//进入大站
                    "15000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.129,0.965",
                    "500,input,tap,0.129,0.965",
                    "1500,input,tap,0.095,0.079",
                    "3000,input,tap,0.427,0.7",
                    "3000,input,tap,0.5,0.58",
                    "3000,input,tap,0.5,0.555",
                    "3000,input,keyevent,4",
                    "3000,input,keyevent,4",
                    "3000,input,keyevent,4",
                    "3000,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "25000,input,tap,0.5,0.548",
                    "2000,input,tap,0.5,0.893",
                    "6000,input,tap,0.5,0.965",
                    "3000,input,tap,0.122,0.385",
                    "1000,input,tap,0.133,0.243",
                    "1000,input,tap,0.133,0.243",
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<main_net>",
                    "5000,input,tap,0.913,0.0835",
                    "1000,input,tap,0.913,0.0835",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2"
            },
            {//浏览模式
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,swipe,0.5,0.7,0.5,0.2",
                    "3000,input,tap,0.5,0.5",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "3000,input,keyevent,4"
            },
            {//初始进入主页
                    "15000,input,swipe,0.5,0.3,0.5,0.6",
                    "3000,input,tap,0.896,0.965",
                    "3000,input,tap,0.129,0.965",
                    "500,input,tap,0.129,0.965",
                    "1500,input,tap,0.095,0.079",
                    "3000,input,tap,0.427,0.7",
                    "3000,input,tap,0.5,0.58",
                    "3000,input,tap,0.5,0.555",
                    "3000,input,keyevent,4",
                    "3000,input,keyevent,4",
                    "3000,input,keyevent,4",
                    "3000,input,tap,0.5,0.485",
                    "500,input,tap,0.5,0.485",
                    "25000,input,tap,0.5,0.548",
                    "2000,input,tap,0.5,0.893",
                    "6000,input,tap,0.5,0.965",
                    "5000,input,tap,0.122,0.385",
                    "1000,input,tap,0.133,0.243",
                    "1000,input,tap,0.133,0.243"
            },
            {//输入网址并浏览
                    "3000,input,swipe,0.5,0.3,0.5,0.6",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.5,0.0835",
                    "1000,input,tap,0.801,0.0835",
                    "2000,input,text,<net>",
                    "5000,input,tap,0.913,0.0835",
                    "5000,input,tap,0.913,0.0835",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3",
                    "8000,input,swipe,0.5,0.6,0.5,0.3"
            }

    };

    public static long getTimesTodayMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
