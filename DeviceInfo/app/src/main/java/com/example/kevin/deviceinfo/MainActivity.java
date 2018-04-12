package com.example.kevin.deviceinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.snail.antifake.jni.EmulatorDetectUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView deviInfo;
    private TextView build;
    private TextView apps;
    private TextView others;
    private TextView contacts;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviInfo = findViewById(R.id.info);
        build = findViewById(R.id.build);
        apps = findViewById(R.id.apps);
        others = findViewById(R.id.others);
        contacts = findViewById(R.id.contacts);

        try {
            deviInfo.append("当前设备信息：\n");
            deviInfo.append(getDeviceInfo());

            build.append("当前设备信息：\n");
            build.append(getBuildInfo());

            others.append("其它信息：\n");
            others.append("CPU型号：" + getCpuInfo() + "\n");
            others.append("本地MAC地址：" + getLocalMacAddress() + "\n");
            others.append("IP地址：" + getHostIP() + "\n");
            others.append("是否" + isRoot() + "\n");
            others.append("当前是否是模拟器:" + isEmulator() + "\n");
            others.append("是否存在eth0网卡:" + hasEth0Interface() + "\n");
            others.append("是否存在Debug反调试:" + isBeingDebugged() + "\n");
            others.append("是否Monky测试:" + isUserAMonkey() + "\n");
            others.append("是否支持闪光灯:" + (isSupportFlashLight() == null));

            apps.append("当前安装的手机应用：\n");
            apps.append(getAllApp());

            contacts.append("当前联系人如下：\n");
            contacts.append(getContactInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Object isSupportFlashLight() {
        Object localObject1 = fildClass("android.os.ServiceManager");

        Object localObject2 = findMethod((Class) localObject1, "getService", new Class[]{String.class});
        if (localObject2 == null) {
            return null;
        }

        localObject1 = invokeMethod((Method) localObject2, null, new Object[]{"hardware"});
        if (localObject1 == null) {
            return null;
        }
        localObject1 = fildClass("android.os.IHardwareService$Stub");
        if (localObject1 == null) {
            return null;
        }
        localObject2 = findMethod((Class) localObject1, "asInterface", new Class[]{IBinder.class});
        if (localObject2 == null) {
            return null;
        }
        localObject1 = invokeMethod((Method) localObject2, null, new Object[]{localObject1});
        return localObject1;
    }

    private static Object invokeMethod(Method paramMethod, Object paramObject, Object... paramVarArgs) {
        try {
            paramObject = paramMethod.invoke(paramObject, paramVarArgs);
            return paramObject;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (RuntimeException e) {
        }
        return null;
    }

    private static Method findMethod(Class<?> paramClass, String paramString, Class<?>... paramVarArgs) {
        try {
            return paramClass.getMethod(paramString, paramVarArgs);
        } catch (RuntimeException e) {
            return null;
        } catch (NoSuchMethodException e) {
        }
        return null;
    }

    private static Class<?> fildClass(String paramString) {
        try {
            Class localClass = Class.forName(paramString);
            return localClass;
        } catch (RuntimeException e) {
            return null;
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static boolean isUserAMonkey() {
        return ActivityManager.isUserAMonkey();
    }

    /**
     * 你信或不信, 还真有许多加固程序使用这个方法...
     */
    public static boolean isBeingDebugged() {
        return Debug.isDebuggerConnected();
    }

    private static boolean hasEth0Interface() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().equals("eth0"))
                    return true;
            }
        } catch (SocketException ex) {
        }
        return false;
    }

    private boolean isEmulator() {
        return EmulatorDetectUtil.detect();
//        String tracerpid = "TracerPid";
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/self/status")), 1000);
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                if (line.length() > tracerpid.length()) {
//                    if (line.substring(0, tracerpid.length()).equalsIgnoreCase(tracerpid)) {
//                        if (Integer.decode(line.substring(tracerpid.length() + 1).trim()) > 0) {
//                            return true;
//                        }
//                        break;
//                    }
//                }
//            }
//            reader.close();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        } finally {
//
//        }
//        return false;
    }

    private void initLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }

        // 为获取地理位置信息时设置查询条件
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        // 获取位置信息
        // 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        Location location = lm.getLastKnownLocation(bestProvider);
        updateView(location);
        // 监听状态
        lm.addGpsStatusListener(listener);
        // 绑定监听，有4个参数
        // 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        // 参数2，位置信息更新周期，单位毫秒
        // 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        // 参数4，监听
        // 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        // 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
    }

    /**
     * 获取联系人信息，并把数据转换成json数据
     *
     * @return
     * @throws JSONException
     */
    public String getContactInfo() throws JSONException {
        List<ContactsContract.Contacts> list = new ArrayList<ContactsContract.Contacts>();
//        JSONObject jsonObject = null;
//        JSONObject contactData = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
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
//                jsonObject = new JSONObject();
//                contactData.put("contact" + numm, jsonObject);
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
//                jsonObject.put("prefix", prefix);
                String firstName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
//                jsonObject.put("firstName", firstName);
                stringBuffer.append(TextUtils.isEmpty(firstName) ? "" : firstName);
                String middleName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
//                jsonObject.put("middleName", middleName);
                stringBuffer.append(TextUtils.isEmpty(middleName) ? "" : middleName);
                String lastname = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
//                jsonObject.put("lastname", lastname);
                stringBuffer.append(TextUtils.isEmpty(lastname) ? "" : lastname);
                stringBuffer.append("\n");
            }
            // 1.2 获取各种电话信息
            if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
                int phoneType = cursor
                        .getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)); // 手机
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    String mobile = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    jsonObject.put("mobile", mobile);
                    stringBuffer.append("手机电话" + mobile + "\n");
                }
                // 住宅电话
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                    String homeNum = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    jsonObject.put("homeNum", homeNum);
                    stringBuffer.append("家庭电话" + homeNum + "\n");
                }
                // 单位电话
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                    String jobNum = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    jsonObject.put("jobNum", jobNum);
                    stringBuffer.append("工作电话" + jobNum + "\n");
                }
            }
        }
        cursor.close();
//        Log.i("contactData", contactData.toString());
        return stringBuffer.toString();
    }

    private String getDeviceInfo() {
        TelephonyManager mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        String imsi = mTm.getSubscriberId();
        String deviceid = mTm.getDeviceId();//获取智能设备唯一编号
        String te1 = mTm.getLine1Number();//获取本机号码
        String sim = mTm.getSimSerialNumber();//获得SIM卡的序号
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        String mac = wifiInfo.getMacAddress();//MAC地址
        int ip = wifiInfo.getIpAddress();//MAC地址
        String bssid = wifiInfo.getBSSID();//MAC地址
        String ssid = wifiInfo.getSSID();//MAC地址
        int rssi = wifiInfo.getRssi();//MAC地址


        StringBuffer phoneInfo = new StringBuffer();
        phoneInfo.append("IMSI: " + imsi + "\n");
        phoneInfo.append("DEVICEID: " + deviceid + "\n");
        phoneInfo.append("TE1: " + te1 + "\n");
        phoneInfo.append("SIM: " + sim + "\n");
        phoneInfo.append("NUMBER: " + numer + "\n");
        phoneInfo.append("MAC: " + mac + "\n");
        phoneInfo.append("IP: " + ip + "\n");
        phoneInfo.append("BSSID: " + bssid + "\n");
        phoneInfo.append("SSID: " + ssid + "\n");
        phoneInfo.append("RSSI: " + rssi);

//        phoneInfo.append("当前Build信息如下：\n");
//        phoneInfo.append("PRODUCT: " + android.os.Build.PRODUCT + "\n");
//        phoneInfo.append("CPU_ABI: " + android.os.Build.CPU_ABI + "\n");
//        phoneInfo.append("TAGS: " + android.os.Build.TAGS + "\n");
//        phoneInfo.append("VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE + "\n");
//        phoneInfo.append("SDK: " + android.os.Build.VERSION.SDK + "\n");
//        phoneInfo.append("VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE + "\n");
//        phoneInfo.append("DEVICE: " + android.os.Build.DEVICE + "\n");
//        phoneInfo.append("DISPLAY: " + android.os.Build.DISPLAY + "\n");
//        phoneInfo.append("BRAND: " + android.os.Build.BRAND + "\n");
//        phoneInfo.append("BOARD: " + android.os.Build.BOARD + "\n");
//        phoneInfo.append("FINGERPRINT: " + android.os.Build.FINGERPRINT + "\n");
//        phoneInfo.append("ID: " + android.os.Build.ID + "\n");
//        phoneInfo.append("MANUFACTURER: " + android.os.Build.MANUFACTURER + "\n");
//        phoneInfo.append("USER: " + android.os.Build.USER + "\n");
//        phoneInfo.append("MODEL: " + android.os.Build.MODEL + "\n");
//        phoneInfo.append("SERIAL: " + Build.SERIAL + "\n");
//        phoneInfo.append("BOOTLOADER: " + Build.BOOTLOADER + "\n");
//        phoneInfo.append("HARDWARE: " + Build.HARDWARE + "\n");
//        phoneInfo.append("HOST: " + Build.HOST + "\n");
//        phoneInfo.append("TYPE: " + Build.TYPE + "\n");
//        phoneInfo.append("USER: " + Build.USER + "\n");
        return phoneInfo.toString();
    }


    public String getBuildInfo() {
        StringBuffer phoneInfo = new StringBuffer();
//        phoneInfo.append("当前Build信息如下：\n");
        phoneInfo.append("PRODUCT: " + android.os.Build.PRODUCT + "\n");
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
//            if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//            result += i.applicationInfo.loadLabel(getPackageManager()).toString() + "-" + i.packageName + "-" + i.sharedUserId;
            result += i.applicationInfo.loadLabel(getPackageManager()).toString() + "\n";
//            }
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
//            Log.i("yao", "SocketException");
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


    // 位置监听
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateView(location);
//            Log.i(TAG, "时间：" + location.getTime());
//            Log.i(TAG, "经度：" + location.getLongitude());
//            Log.i(TAG, "纬度：" + location.getLatitude());
//            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
            Location location = lm.getLastKnownLocation(provider);
            updateView(location);
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            updateView(null);
        }

    };

    // 状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // 第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
//                    Log.i(TAG, "第一次定位");
                    break;
                // 卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                    Log.i(TAG, "卫星状态改变");
                    // 获取当前状态
                    GpsStatus gpsStatus = lm.getGpsStatus(null);
                    // 获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    // 创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
                            .iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
//                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                // 定位启动
                case GpsStatus.GPS_EVENT_STARTED:
//                    Log.i(TAG, "定位启动");
                    break;
                // 定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
//                    Log.i(TAG, "定位结束");
                    break;
            }
        }

        ;
    };

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            deviInfo.setText(deviInfo.getText() + "\n" + "设备位置信息\n\n经度：");
            deviInfo.append(String.valueOf(location.getLongitude()));
            deviInfo.append("\n纬度：");
            deviInfo.append(String.valueOf(location.getLatitude()));
        }
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

}
