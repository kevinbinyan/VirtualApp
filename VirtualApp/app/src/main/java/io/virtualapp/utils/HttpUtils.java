package io.virtualapp.utils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Kevin on 2018/3/25.
 */

public class HttpUtils {

//    private static String MAIN = "192.168.1.116";
        private static String MAIN = "47.95.6.17";
    private static String LOGIN = "http://" + MAIN + ":8080/lvt/Login?";
    private static String CHECK_VERSION = "http://" + MAIN + ":8080/lvt/CheckVersion?";
    private static String VERTIFY_KEY = "http://" + MAIN + ":8080/lvt/CheckLisence?";
    private static String SYNC_NET = "http://" + MAIN + ":8080/lvt/FetchNets";

    private static final int MAX_OFFLINE = 5;
    //    private static String[] urls = {"http://47.95.6.17:8080/vd/CheckLisence?key=", "http://aaren.22ip.net:8081/vd/CheckLisence?key="};
    private static int offLineCount;

    public interface HttpCallBack {
        void callback(boolean value);
    }

    public interface TextCallBack {
        void callback(String txt);
    }

    public static void requestLogin(String key, String token, HttpCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(LOGIN + "key=" + key + "&token=" + token);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        boolean isLoginsuccess = false;
                        if (result.contains("success")) {
                            isLoginsuccess = true;
                        }
                        httpCallBack.callback(isLoginsuccess);
                        inputStream.close();
                    } else {
                        httpCallBack.callback(false);
                    }
                    offLineCount = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpCallBack.callback(false);
                }
            }
        }).start();

//        httpCallBack.callback(true);
    }

    public static void checkVersion(int version, HttpCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(CHECK_VERSION + "version=" + version);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        boolean upgrade = false;
                        if (result.contains("upgrade")) {
                            upgrade = true;
                        }
                        httpCallBack.callback(upgrade);
                        inputStream.close();
                    } else {
                        httpCallBack.callback(false);
                    }
                    offLineCount = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    httpCallBack.callback(false);
                }
            }
        }).start();

//        httpCallBack.callback(false);
    }


    public static void verifyKey(String key, String token, HttpCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(VERTIFY_KEY + "key=" + key + "&token=" + token);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        boolean upgrade = false;
                        if (result.contains("success")) {
                            upgrade = true;
                        }
                        httpCallBack.callback(upgrade);
                        inputStream.close();
                    } else {
                        httpCallBack.callback(false);
                    }
                    offLineCount = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    offLineCount++;
                    if (offLineCount > MAX_OFFLINE) {
                        httpCallBack.callback(false);
                    } else {
                        httpCallBack.callback(true);
                    }
                }
            }
        }).start();

    }

    public static void syncNet(TextCallBack textCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SYNC_NET);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        InputStreamReader isr = new InputStreamReader(inputStream);//字节转字符，字符集是utf-8
                        BufferedReader bufferedReader = new BufferedReader(isr);//通过BufferedReader可以读取一行字符串
                        StringBuffer lines = new StringBuffer();
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            lines.append(line + "\n");
                        }
                        bufferedReader.close();
                        isr.close();
                        inputStream.close();
                        textCallBack.callback(lines.toString());
                    } else {
                        textCallBack.callback(null);
                    }
                    offLineCount = 0;
                } catch (Exception e) {
                    textCallBack.callback(null);
                }
            }
        }).start();
    }

}
