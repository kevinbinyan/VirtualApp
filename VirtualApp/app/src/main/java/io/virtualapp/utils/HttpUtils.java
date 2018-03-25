package io.virtualapp.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import io.virtualapp.home.HomeActivity;

/**
 * Created by Kevin on 2018/3/25.
 */

public class HttpUtils {

    public interface HttpCallBack {

        void callback(boolean value);
    }

    //get方式登录
    public static void requestNetForGetLogin(final String key, HttpCallBack httpCallBack) {

        //在子线程中操作网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                //urlConnection请求服务器，验证
                try {
                    //1：url对象
                    URL url = new URL("http://192.168.1.104:8080/vd/Validate?key=" + URLEncoder.encode(key));
                    //2;url.openconnection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //3
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10 * 1000);
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
                    }else{
                        httpCallBack.callback(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
