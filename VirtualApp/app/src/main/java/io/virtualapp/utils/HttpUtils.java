package io.virtualapp.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lody.virtual.helper.utils.Base64;
import com.lody.virtual.helper.utils.RSAUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by Kevin on 2018/3/25.
 */

public class HttpUtils {

    private static String modulus = "101139253338155537122681263551391401692066665916613487436275955722010199471415841485729163754132286657951275782618854770472010908407158470741951949410587800589127059181738617385251968563652490730289519152085655065302311553563299905910600441758613944432476284758060061258064772215795815169533468766442967476449";
    // 鍏挜鎸囨暟
    private static String public_exponent = "65537";
    public static String private_exponent = "77040033353587478351181338141034990369862215683099041858893937555861134440278777222165884672323082873057748117004376901547725049339972199183804313083082114860116154901276523598153162839702785813272951961243156651418620364910731144201588093748132726391031044890152993376853663320094215905479322137162494227093";

    private static String MAIN = "192.168.1.101";
    //    private static String MAIN = "47.95.6.17";
    private static String LOGIN = "http://" + MAIN + ":8080/lvt/logins";
    private static String CHECK_VERSION = "http://" + MAIN + ":8080/lvt/checkv";
    private static String VERTIFY_KEY = "http://" + MAIN + ":8080/lvt/checkl";
    private static String GET_KEY_DATE = "http://" + MAIN + ":8080/lvt/checkld";
    private static String SYNC_NET = "http://" + MAIN + ":8080/lvt/fetchn";

    private static final int MAX_OFFLINE = 5;
    //    private static String[] urls = {"http://47.95.6.17:8080/vd/CheckLisence?key=", "http://aaren.22ip.net:8081/vd/CheckLisence?key="};
    private static int offLineCount;

    public static void checkVersion(int versionCode, HttpCallBack httpCallBack) {
        try {
            URL url = new URL(CHECK_VERSION);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("version", versionCode);
            jsonObject.put("time", System.currentTimeMillis());
            String json = jsonObject.toString();
            postCallbackRequest(httpCallBack, url, json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void requestLogin(String key, String token, HttpCallBack httpCallBack) {
        try {
            URL url = new URL(LOGIN);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("token", token);
            jsonObject.put("time", System.currentTimeMillis());
            String json = jsonObject.toString();
            postCallbackRequest(httpCallBack, url, json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void verifyKey(String key, String token, HttpCallBack httpCallBack) {
        try {
            URL url = new URL(VERTIFY_KEY);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("token", token);
            jsonObject.put("time", System.currentTimeMillis());
            String json = jsonObject.toString();
            postCallbackRequest(httpCallBack, url, json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getKeyDate(String key, String token, ValueCallBack httpCallBack) {
        try {
            URL url = new URL(GET_KEY_DATE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("token", token);
            jsonObject.put("time", System.currentTimeMillis());
            String json = jsonObject.toString();
            postRequest(url, json, new HttpJsonCallBack() {
                @Override
                public void callback(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        try {
                            httpCallBack.callback(jsonObject.getInt("result"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            httpCallBack.callback(-1);
                        }
                        return;
                    }
                    httpCallBack.callback(-1);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void syncNet(String key, String token, TextCallBack textCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SYNC_NET);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("key", key);
                    jsonObject.put("token", token);
                    jsonObject.put("time", System.currentTimeMillis());
                    String json = jsonObject.toString();
                    RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
                    String enstr = RSAUtils.encryptByPublicKey(json, pubKey);
                    conn.setRequestProperty("Content-Length", String.valueOf(enstr.getBytes().length));
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(enstr.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                        byte[] temp = new byte[1024];
                        int size = 0;
                        while ((size = inputStream.read(temp)) != -1) {
                            out.write(temp, 0, size);
                        }
                        inputStream.close();

                        byte[] content = out.toByteArray();
                        inputStream.close();
                        out.close();
//                        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
//                        String responseJson = RSAUtils.decryptByPrivateKey(new String(content), priKey);
                        textCallBack.callback(new String(content));
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


    public static void postRequest(URL url, final String json, HttpJsonCallBack httpCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(90 * 1000);
                    conn.setReadTimeout(90 * 1000);
                    RSAPublicKey pubKey = RSAUtils.getPublicKey(modulus, public_exponent);
                    String enstr = RSAUtils.encryptByPublicKey(json, pubKey);
                    conn.setRequestProperty("Content-Length", String.valueOf(enstr.getBytes().length));
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(enstr.getBytes());
                    outwritestream.flush();
                    outwritestream.close();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        String result = br.readLine();
                        inputStream.close();
                        conn.disconnect();
                        RSAPrivateKey priKey = RSAUtils.getPrivateKey(modulus, private_exponent);
                        String responseJson = RSAUtils.decryptByPrivateKey(result, priKey);
                        JSONObject jsonObject = new JSONObject(responseJson);
                        long time = jsonObject.getLong("time");
                        if (System.currentTimeMillis() - time > 10 * 60 * 1000) {
                            httpCallBack.callback(null);
                        } else {
                            httpCallBack.callback(jsonObject);
                        }
                    } else {
                        httpCallBack.callback(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    httpCallBack.callback(null);
                }
            }
        }).start();

    }

    private static void postCallbackRequest(HttpCallBack httpCallBack, URL url, String json) {
        postRequest(url, json, new HttpJsonCallBack() {
            @Override
            public void callback(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString("result").contains("success")) {
                            httpCallBack.callback(true);
                        } else {
                            httpCallBack.callback(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        httpCallBack.callback(false);
                    }
                    return;
                }
                httpCallBack.callback(false);
            }
        });
    }

    public interface HttpCallBack {
        void callback(boolean value);
    }

    public interface HttpJsonCallBack {
        void callback(JSONObject jsonObject);
    }

    public interface TextCallBack {
        void callback(String txt);
    }

    public interface ValueCallBack {
        void callback(int txt);
    }

}
