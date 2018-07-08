package io.virtualapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

public class RuoKuai {

    /**
     * 字符串MD5加密
     *
     * @param s 原始字符串
     * @return 加密后字符串
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提交验证码识别，并返回结果
     *
     * @param username 用户名
     * @param password 密码
     * @param byteArr  图片二进制数据
     * @return
     */
    public static String httpPostImage(String username, String password, byte[] byteArr) {
        String result = "";
        String param = String.format("username=%s&password=%s&typeid=%s&timeout=%s&softid=%s&softkey=%s", username,
                password, "3000", "90", "107680", "38f334648fb44acf9f96066ccf8df49c");
        try {
            result = RuoKuai.httpPost("http://api.ruokuai.com/create.json", param, byteArr);
        } catch (Exception e) {
            result = "{\"Error\":\"连接异常\",\"Error_Code\":\"100000\",\"Request\":\"\"}";
        }

        return result;
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 平台返回结果json样式
     * @throws IOException
     */
    public static String getInfo(String username, String password) {
        String param = String.format("username=%s&password=%s", username, password);
        String result;
        try {
            result = RuoKuai.httpRequestData("http://api.ruokuai.com/info.json", param);
        } catch (IOException e) {
            result = "出现异常:" + e.getMessage();
        }
        return result;
    }

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @return 平台返回结果json样式
     * @throws IOException
     */
    public static String register(String username, String password, String email) {
        String param = String.format("username=%s&password=%s&email=%s", username, password, email);
        String result;
        try {
            result = RuoKuai.httpRequestData("http://api.ruokuai.com/register.json", param);
        } catch (IOException e) {
            result = "出现异常:" + e.getMessage();
        }
        return result;
    }

    /**
     * 充值
     *
     * @param username 用户名
     * @param id       卡号
     * @param password 密码
     * @return 平台返回结果json样式
     * @throws IOException
     */
    public static String recharge(String username, String id, String password) {

        String param = String.format("username=%s&password=%s&id=%s", username, password, id);
        String result;
        try {
            result = RuoKuai.httpRequestData("http://api.ruokuai.com/recharge.json", param);
        } catch (IOException e) {
            result = "出现异常:" + e.getMessage();
        }
        return result;
    }

    /**
     * 上报错题
     *
     * @param username 用户名
     * @param password 用户密码
     * @param id       报错题目的ID
     * @return
     * @throws IOException
     */
    public static String report(String username, String password, String id) {

        String param = String.format("username=%s&password=%s&id=%s&softid=%s&softkey=%s", username, password, id, "107680", "38f334648fb44acf9f96066ccf8df49c");
        String result;
        try {
            result = RuoKuai.httpRequestData("http://api.ruokuai.com/reporterror.json", param);
        } catch (IOException e) {
            result = "出现异常:" + e.getMessage();
        }

        return result;
    }

    /**
     * post提交二进制数据
     *
     * @param url   提交路径
     * @param param 请求参数，如：username=test&password=1
     * @param data  图片二进制流
     * @return 平台返回结果json样式
     * @throws IOException
     */
    private static String httpPost(String url, String param, byte[] data) throws IOException {
        long time = (new Date()).getTime();
        URL u = null;
        HttpURLConnection con = null;
        String boundary = "----------" + MD5(String.valueOf(time));
        String boundarybytesString = "\r\n--" + boundary + "\r\n";
        OutputStream out = null;

        u = new URL(url);

        con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");
        // con.setReadTimeout(95000);
        con.setConnectTimeout(95000); // 此值与timeout参数相关，如果timeout参数是90秒，这里就是95000，建议多5秒
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(true);
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.connect();
        out = con.getOutputStream();

        for (String paramValue : param.split("[&]")) {
            out.write(boundarybytesString.getBytes("UTF-8"));
            String paramString = "Content-Disposition: form-data; name=\"" + paramValue.split("[=]")[0] + "\"\r\n\r\n"
                    + paramValue.split("[=]")[1];
            out.write(paramString.getBytes("UTF-8"));
        }
        out.write(boundarybytesString.getBytes("UTF-8"));

        String paramString = "Content-Disposition: form-data; name=\"image\"; filename=\"" + "sample.gif"
                + "\"\r\nContent-Type: image/gif\r\n\r\n";
        out.write(paramString.getBytes("UTF-8"));

        out.write(data);

        String tailer = "\r\n--" + boundary + "--\r\n";
        out.write(tailer.getBytes("UTF-8"));

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String temp;
        while ((temp = br.readLine()) != null) {
            buffer.append(temp);
            buffer.append("\n");
        }
        con.disconnect();
        return buffer.toString();
    }

    /**
     * 通用URL请求方法
     *
     * @param url   请求URL，不带参数 如：http://api.ruokuai.com/register.json
     * @param param 请求参数，如：username=test&password=1
     * @return 平台返回结果json样式
     * @throws IOException
     */
    private static String httpRequestData(String url, String param) throws IOException {
        URL u;
        HttpURLConnection con = null;
        OutputStreamWriter osw;
        StringBuffer buffer = new StringBuffer();

        u = new URL(url);
        con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        osw.write(param);
        osw.flush();
        osw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String temp;
        while ((temp = br.readLine()) != null) {
            buffer.append(temp);
            buffer.append("\n");
        }

        return buffer.toString();
    }

}
