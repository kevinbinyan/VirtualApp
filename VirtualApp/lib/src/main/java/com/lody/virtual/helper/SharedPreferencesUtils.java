package com.lody.virtual.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunb on 2018/3/14.
 */

public class SharedPreferencesUtils {

    public static final String MAX_EMULATOR = "max_emulator";
    public static final String TIME_BEGIN = "time_type";
    public static final String AUTO_LAUNCH_INDEX = "auto";
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";
    public static final String SCRIPT = "script";
    public static final String TIME_RANDOM = "time_end";
    public static final String KEY = "key";
    public static final String DEVICE = "device_num";
    public static final String SCRIPT_ANI = "script_ani";
    public static final String ONLY_ONE_PRO = "only_5_pro";
    public static final String V_CONTACTS = "v_contacts";
    public static final String USER_CONTACTS = "user_contacts";
    public static final String AUTO_RESTART = "auto_restart";
    public static final String PWD_WAIT_TIME = "pwd_wait_time";
    public static final String MINE_WAIN_TIME = "mine_wain_time";
    public static final String TOKEN = "token";
    public static final String EMULATOR = "emulator";
    public static final String LOGIN_NOW = "login_now";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }
}

