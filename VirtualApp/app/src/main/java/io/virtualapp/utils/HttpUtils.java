package io.virtualapp.utils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lody.virtual.helper.utils.Base64;
import com.lody.virtual.helper.utils.RSAUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by Kevin on 2018/3/25.
 */

public class HttpUtils {

    private static String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
    // 鍏挜鎸囨暟
    // public_exponent = publicKey.getPublicExponent().toString();
    private static String public_exponent = "65537";
    //    private static String MAIN = "192.168.1.116";
    private static String MAIN = "47.95.6.17";
    private static String LOGIN = "http://" + MAIN + ":8080/lvt/Logins";
    private static String CHECK_VERSION = "http://" + MAIN + ":8080/lvt/Checkv";
    private static String VERTIFY_KEY = "http://" + MAIN + ":8080/lvt/Checkl";
    private static String GET_KEY_DATE = "http://" + MAIN + ":8080/lvt/Checkld";
    private static String SYNC_NET = "http://" + MAIN + ":8080/lvt/Fetchn";

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
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("key",key);
                    jsonObject.put("token",token);
                    String json = jsonObject.toString();
                    RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
                    String enstr = RSAUtils.encryptByPublicKey(Base64.encode(json.getBytes()), pubKey);
                    conn.setRequestProperty("Content-Length", String.valueOf(enstr.length()));
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(enstr.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
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
                    conn.disconnect();
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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("version",version);
                    String json = jsonObject.toString();
                    RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
                    String enstr = RSAUtils.encryptByPublicKey(Base64.encode(json.getBytes()), pubKey);
                    conn.setRequestProperty("Content-Length", String.valueOf(enstr.length()));
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(enstr.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
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
                    conn.disconnect();
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
                    URL url = new URL(VERTIFY_KEY );
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("key",key);
                    jsonObject.put("token",token);
                    String json = jsonObject.toString();
                    RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
                    String enstr = RSAUtils.encryptByPublicKey(Base64.encode(json.getBytes()), pubKey);
                    conn.setRequestProperty("Content-Length", String.valueOf(enstr.length()));
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(enstr.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
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
                    conn.disconnect();
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

    public static void getKeyDate(String key, String token, TextCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(GET_KEY_DATE);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("key",key);
                    jsonObject.put("token",token);
                    String json = jsonObject.toString();
                    RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
                    String enstr = RSAUtils.encryptByPublicKey(Base64.encode(json.getBytes()), pubKey);
                    conn.setRequestProperty("Content-Length", String.valueOf(enstr.length()));
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(enstr.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        httpCallBack.callback(result);
                        inputStream.close();
                    }
                    offLineCount = 0;
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
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
                    conn.disconnect();
                } catch (Exception e) {
                    textCallBack.callback(null);
                }
            }
        }).start();
    }

}
