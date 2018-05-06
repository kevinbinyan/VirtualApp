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

    private static String LOGIN = "http://47.95.6.17:8080/lvt/Login?";
    private static String CHECK_VERSION = "http://47.95.6.17:8080/lvt/CheckVersion?";
    private static String VERTIFY_KEY = "http://47.95.6.17:8080/lvt/CheckLisence?";

    private static final int MAX_OFFLINE = 5;
    private static String[] urls = {"http://47.95.6.17:8080/vd/CheckLisence?key=", "http://aaren.22ip.net:8081/vd/CheckLisence?key="};
    private static int offLineCount;

    public interface HttpCallBack {
        void callback(boolean value);
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

//        httpCallBack.callback(true);
    }
    //------------------------以下老的逻辑------------------------------------------------


    //get方式登录
    public static void requestNetForGetLogin(final String key, HttpCallBack httpCallBack, boolean force) {

        if (TextUtils.isEmpty(key)) {
            httpCallBack.callback(false);
            return;
        }
        //在子线程中操作网络请求
        tryRequest(key, httpCallBack, 0, force);
    }

    private static void tryRequest(String key, HttpCallBack httpCallBack, int index, boolean force) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //urlConnection请求服务器，验证
                try {
                    //1：url对象
                    URL url = new URL(urls[index] + URLEncoder.encode(key));
                    //2;url.openconnection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //3
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
//                    conn.setChunkedStreamingMode(0);
                    //4
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        boolean isLoginsuccess = false;
                        if (result.contains("OK")) {
                            isLoginsuccess = true;
                        }
                        httpCallBack.callback(isLoginsuccess);
                    } else {
                        httpCallBack.callback(false);
                    }
                    offLineCount = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (index + 1 < urls.length) {
                        tryRequest(key, httpCallBack, index + 1, force);
                    } else {
                        offLineCount++;
                        if (force) {
                            httpCallBack.callback(false);
                        } else {
                            if (offLineCount > MAX_OFFLINE) {
                                httpCallBack.callback(false);
                            } else {
                                httpCallBack.callback(true);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    //post方式登录
    public static void requestNetForPOSTLogin(final Handler handler, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //urlConnection请求服务器，验证
                try {
                    //1：url对象
                    URL url = new URL("http://192.168.1.100:8081//servlet/LoginServlet");

                    //2;url.openconnection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    //3设置请求参数
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(10 * 1000);
                    //请求头的信息
                    String body = "username=" + URLEncoder.encode(username) + "&pwd=" + URLEncoder.encode(password);
                    conn.setRequestProperty("Content-Length", String.valueOf(body.length()));
                    conn.setRequestProperty("Cache-Control", "max-age=0");
                    conn.setRequestProperty("Origin", "http://192.168.1.100:8081");

                    //设置conn可以写请求的内容
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(body.getBytes());

                    //4响应码
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        System.out.println("=====================服务器返回的信息：：" + result);
                        boolean isLoginsuccess = false;
                        if (result.contains("success")) {
                            isLoginsuccess = true;
                        }
                        Message msg = Message.obtain();
                        msg.obj = isLoginsuccess;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}