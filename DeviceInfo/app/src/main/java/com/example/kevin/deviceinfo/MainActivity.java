package com.example.kevin.deviceinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.info);
        try {
            textView.setText(getDeviceInfo()
                    + "\n\n所有应用：\n" + getAllApp()
                    + "\n\nCPU型号：\n" + getCpuInfo()
                    + "\n\n本地MAC地址：\n" + getLocalMacAddress()
                    + "\n\n通讯录：\n" + getContactInfo()
                    + "\n\nIP地址：\n" + getHostIP() + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取联系人信息，并把数据转换成json数据
     *
     * @return
     * @throws JSONException
     */
    public String getContactInfo() throws JSONException {
        List<ContactsContract.Contacts> list = new ArrayList<ContactsContract.Contacts>();
        JSONObject jsonObject = null;
        JSONObject  contactData = new JSONObject();
        String mimetype = "";
        int oldrid = -1;
        int contactId = -1;
        // 1.查询通讯录所有联系人信息，通过id排序，我们看下android联系人的表就知道，所有的联系人的数据是由RAW_CONTACT_ID来索引开的
        // 所以，先获取所有的人的RAW_CONTACT_ID
        Uri uri = ContactsContract.Data.CONTENT_URI; // 联系人Uri；
        Cursor cursor = getContentResolver().query(uri,
                null, null, null, ContactsContract.Data.RAW_CONTACT_ID);
        int numm = 0;
        while (cursor.moveToNext()) {
            contactId = cursor.getInt(cursor
                    .getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
            if (oldrid != contactId) {
                jsonObject = new JSONObject();
                contactData.put("contact" + numm, jsonObject);
                numm++;
                oldrid = contactId;
            }
            mimetype = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE)); // 取得mimetype类型,扩展的数据都在这个类型里面
            // 1.1,拿到联系人的各种名字
            if (ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)) {
                cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                String prefix = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
                jsonObject.put("prefix", prefix);
                String firstName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                jsonObject.put("firstName", firstName);
                String middleName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                jsonObject.put("middleName", middleName);
                String lastname = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                jsonObject.put("lastname", lastname);
            }
            // 1.2 获取各种电话信息
            if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
                int phoneType = cursor
                        .getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)); // 手机
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    String mobile = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    jsonObject.put("mobile", mobile);
                }
                // 住宅电话
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                    String homeNum = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    jsonObject.put("homeNum", homeNum);
                }
                // 单位电话
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                    String jobNum = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    jsonObject.put("jobNum", jobNum);
                }
            }
        }
        cursor.close();
        Log.i("contactData", contactData.toString());
        return contactData.toString();
    }

    private String getDeviceInfo() {
        TelephonyManager mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        String imsi = mTm.getSubscriberId();
        String deviceid = mTm.getDeviceId();//获取智能设备唯一编号
        String te1 = mTm.getLine1Number();//获取本机号码
        String sim = mTm.getSimSerialNumber();//获得SIM卡的序号
//        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        String mac = wifiInfo.getMacAddress();//MAC地址


        StringBuffer phoneInfo = new StringBuffer();
        phoneInfo.append("IMSI: " + imsi + "\n");
        phoneInfo.append("DEVICEID: " + deviceid + "\n");
        phoneInfo.append("TE1: " + te1 + "\n");
        phoneInfo.append("sim: " + sim + "\n");
        phoneInfo.append("NUMBER: " + numer + "\n");
        phoneInfo.append("MAC: " + mac + "\n");

        phoneInfo.append("Product: " + android.os.Build.PRODUCT + "\n");
        phoneInfo.append("CPU_ABI: " + android.os.Build.CPU_ABI + "\n");
        phoneInfo.append("TAGS: " + android.os.Build.TAGS + "\n");
        phoneInfo.append("VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE + "\n");
        phoneInfo.append("SDK: " + android.os.Build.VERSION.SDK + "\n");
        phoneInfo.append("VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE + "\n");
        phoneInfo.append("DEVICE: " + android.os.Build.DEVICE + "\n");
        phoneInfo.append("DISPLAY: " + android.os.Build.DISPLAY + "\n");
        phoneInfo.append("BRAND: " + android.os.Build.BRAND + "\n");
        phoneInfo.append("BOARD: " + android.os.Build.BOARD + "\n");
        phoneInfo.append("FINGERPRINT: " + android.os.Build.FINGERPRINT + "\n");
        phoneInfo.append("ID: " + android.os.Build.ID + "\n");
        phoneInfo.append("MANUFACTURER: " + android.os.Build.MANUFACTURER + "\n");
        phoneInfo.append("USER: " + android.os.Build.USER + "\n");
        phoneInfo.append("MODEL: " + android.os.Build.MODEL + "\n");
        phoneInfo.append("SERIAL: " + Build.SERIAL + "\n");
        phoneInfo.append("BOOTLOADER: " + Build.BOOTLOADER + "\n");
        phoneInfo.append("HARDWARE: " + Build.HARDWARE + "\n");
        phoneInfo.append("HOST: " + Build.HOST + "\n");
        phoneInfo.append("TYPE: " + Build.TYPE + "\n");
        phoneInfo.append("USER: " + Build.USER + "\n");
        return phoneInfo.toString();
    }


    //获取手机安装的应用信息（排除系统自带）

    private String getAllApp() {
        String result = "";
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (PackageInfo i : packages) {
            if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                result += i.applicationInfo.loadLabel(getPackageManager()).toString() + "-" + i.packageName + "-" + i.sharedUserId;
            }
        }
        return result.substring(0, result.length() - 1);

    }

    //手机CPU信息

    private String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cpuInfo[0] + "," + cpuInfo[1];

    }


    /**
     * 获取手机是否root信息
     *
     * @return
     */

    private String isRoot() {
        String bool = "Root:false";
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = "Root:false";
            } else {
                bool = "Root:true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;

    }

    //获取本机真实的物理地址

    public String getLocalMacAddress() {
        String macAddress = Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
        return macAddress;

    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String line = "";
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        line = jsonObject.optString("cip");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
