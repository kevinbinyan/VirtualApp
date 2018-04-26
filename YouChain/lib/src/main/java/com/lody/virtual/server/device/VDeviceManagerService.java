package com.lody.virtual.server.device;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.RemoteException;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.utils.Tools;
import com.lody.virtual.remote.VDeviceInfo;
import com.lody.virtual.server.interfaces.IDeviceInfoManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Lody
 */

public class VDeviceManagerService implements IDeviceInfoManager {

    private static VDeviceManagerService sInstance = new VDeviceManagerService();
    private final SparseArray<VDeviceInfo> mDeviceInfos = new SparseArray<>();
    private DeviceInfoPersistenceLayer mPersistenceLayer = new DeviceInfoPersistenceLayer(this);
    private UsedDeviceInfoPool mPool = new UsedDeviceInfoPool();

    public static VDeviceManagerService get() {
        return sInstance;
    }

    private final class UsedDeviceInfoPool {
        List<String> deviceIds = new ArrayList<>();
        List<String> androidIds = new ArrayList<>();
        List<String> wifiMacs = new ArrayList<>();
        List<String> bluetoothMacs = new ArrayList<>();
        List<String> iccIds = new ArrayList<>();
        List<String> lineNumbers = new ArrayList<>();
        List<String> imsis = new ArrayList<>();
        List<String> sims = new ArrayList<>();
    }

    public VDeviceManagerService() {
        mPersistenceLayer.read();
        for (int i = 0; i < mDeviceInfos.size(); i++) {
            VDeviceInfo info = mDeviceInfos.valueAt(i);
            addDeviceInfoToPool(info);
        }
    }

    private void addDeviceInfoToPool(VDeviceInfo info) {
        mPool.deviceIds.add(info.deviceId);
        mPool.androidIds.add(info.androidId);
        mPool.wifiMacs.add(info.wifiMac);
        mPool.bluetoothMacs.add(info.bluetoothMac);
        mPool.iccIds.add(info.iccId);
        mPool.lineNumbers.add(info.lineNumber);
        mPool.imsis.add(info.imsi);
        mPool.sims.add(info.sim);
    }

    @Override
    public VDeviceInfo getDeviceInfo(int userId) {
        VDeviceInfo info = null;
        if (Tools.javaValidateSign(VirtualCore.get().getContext())) {
            synchronized (mDeviceInfos) {
                info = mDeviceInfos.get(userId);
                if (info == null) {
                    info = generateRandomDeviceInfo();
                    mDeviceInfos.put(userId, info);
                    mPersistenceLayer.save();
                }
            }
        }
        return info;
    }

//    private VDeviceInfo generateUniformDeviceInfo() {
//        VDeviceInfo info = new VDeviceInfo();
//        String value;
//        value = "123456789012345";
//        info.deviceId = value;
//        value = "1234567890123456";
//        info.androidId = value;
//        value = "123456789012345";
//        info.wifiMac = value;
//        value = "123456789012345";
//        info.bluetoothMac = value;
//
//        value = "123456789012345";
//        info.iccId = value;
//
//        info.serial = generateSerial();
//
//        value = "123456789012345";
//        info.imsi = value;
//
//        value = "123456789012345";
//        info.lineNumber = value;
//
//        value = "123456789012345";
//        info.sim = value;
//
//        info.product = "123456789012345";
//
//        info.device = "123456789012345";
//
//        info.display = "123456789012345";
//
//        info.id = info.display;
//
//        info.brand = "123456789012345";
//
//        info.model = "123456789012345";
//
//        info.fingerprint = "123456789012345";
//
//        info.manufacturer = "123456789012345";
//
//        info.ip = "1234".getBytes();
//
//        info.cpuAbi = "123456789012345";
//
//        info.hardware = "123456789012345";
//
//        info.type = "123456789012345";
//
//        info.host = "123456789012345";
//
//        info.user = "builder";
//
//        addDeviceInfoToPool(info);
//        return info;
//    }

    @Override
    public void updateDeviceInfo(int userId, VDeviceInfo info) {
        synchronized (mDeviceInfos) {
            if (info != null) {
                mDeviceInfos.put(userId, info);
                mPersistenceLayer.save();
            }
        }
    }

    private VDeviceInfo generateRandomDeviceInfo() {

        VDeviceInfo info = new VDeviceInfo();
        if (SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.TOKEN, "") == "") {
            return info;
        }
        String value;
        do {
            value = getIMEI();
            info.deviceId = value;
        } while (mPool.deviceIds.contains(value));
        do {
            value = generate16(16);
            info.androidId = value;
        } while (mPool.androidIds.contains(value));
        do {
            value = generateMac();
            info.wifiMac = value;
        } while (mPool.wifiMacs.contains(value));
        do {
            value = generateMac();
            info.bluetoothMac = value;
        } while (mPool.bluetoothMacs.contains(value));

        do {
            value = generate10(20);
            info.iccId = value;
        } while (mPool.iccIds.contains(value));

        info.serial = generateSerial();

        do {
            value = getImsi();
            info.imsi = value;
        } while (mPool.imsis.contains(value));

        do {
            value = getTel();
            info.lineNumber = value;
        } while (mPool.lineNumbers.contains(value));

        value = generate10(20);
        info.sim = value;

        info.product = generateLetters();

        info.device = info.product;

        info.display = generateLetters();

        info.id = info.display;

        info.brand = getRandowBrand();

        info.model = getRandowMode();

        info.fingerprint = info.brand + "/" + info.device + "/" + info.device + ":6.0.1" + "/" + info.display + "/release-key";

        info.manufacturer = info.brand + "_lvtmill";

        info.ip = getRandomIp();

        info.cpuAbi = getRandomABI();

        info.hardware = getRandomHardware();

        info.type = "user";

        info.host = generateLetters() + "lvtmill";

        info.user = "builder";

        addDeviceInfoToPool(info);
        return info;
    }

    private String getRandomHardware() {
        return "qCOM";
    }

    String[] abis = {"armabi-v7a", "arm64-v8a", "armabi-v5"};

    private String getRandomABI() {
        return abis[new Random().nextInt(abis.length)];
    }

    public static byte[] getRandomIp() {
//ip范围
        byte[] byte0 = new byte[]{-64, 10};
        byte[] byte1 = new byte[]{-88, 100};
        int type = new Random().nextInt(2);
        byte[] ip = new byte[4];
        ip[0] = byte0[type];//192
        ip[1] = byte1[type];//168
        int byte2ByType2 = new Random().nextInt(255);
        int byte3 = new Random().nextInt(254);
        ip[2] = type == 0 ? (byte) new Random().nextInt(100) : (byte) (byte2ByType2 >= 128 ? 128 - byte2ByType2 : byte2ByType2);
        ip[3] = (byte) (1 + (byte3 >= 128 ? 128 - byte3 : byte3));
        return ip;
    }

    private String getRandowMode() {
        String[] brands = new String[]{"RedMi 2S", "RedMi Note 4", "HLF S700L", "VIVO R15", "MEILAN S3", "LG G990", "SONY PLK", "SAMSUMG NOTE 8", "NOKIA p", "BLACKBERRY SUPER", "ONEPLUG TOP", "KONKA 1", "KING 100",
                "RedMi 5P", "RedMi Note 3", "HLF RONGYAN", "VIVO R13", "MEILAN 8", "LG 1000G", "SONY GOOD O", "SAMSUMG NOTE 7", "NOKIA L", "BLACKBERRY ONEW", "ONEPLUG CCC", "KONKA 2", "KING 110",
                "PPONWE 5P", "GGO Note 3", "HLF PL", "VIVO Q1", "MEILAN NOR", "LG G009", "SONY Sz0", "SAMSUMG S8", "NOKIA TOP ONE", "BLACKBERRY OOI", "ONEPLUG Z", "KONKA QOA", "KING XXX"};
        return brands[new Random().nextInt(brands.length)];
    }

    private String getRandowBrand() {
        String[] brands = new String[]{"XiaoMi", "HuaWei", "OPPO", "VIVO", "MEIZU", "LG", "SONY", "SAMSUMG", "NOKIA", "BLACKBERRY", "ONEPLUG", "KONKA", "KING"};

        return brands[new Random().nextInt(brands.length)];
    }

    private String generateLetters() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXWZabcdefghijklmnopqrstuvwxwz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int length = 5 + new Random().nextInt(10);
        for (int i = 0; i < length; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }


    SparseArray<VDeviceInfo> getDeviceInfos() {
        return mDeviceInfos;
    }

    private static String generate10(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private static String generate16(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int nextInt = random.nextInt(16);
            if (nextInt < 10) {
                sb.append(nextInt);
            } else {
                sb.append((char) (nextInt + 87));
            }
        }
        return sb.toString();
    }

    private static String generateMac() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int next = 1;
        int cur = 0;
        while (cur < 12) {
            int val = random.nextInt(16);
            if (val < 10) {
                sb.append(val);
            } else {
                sb.append((char) (val + 87));
            }
            if (cur == next && cur != 11) {
                sb.append(":");
                next += 2;
            }
            cur++;
        }
        return sb.toString();
    }

    @SuppressLint("HardwareIds")
    private static String generateSerial() {
        String serial;
        if (Build.SERIAL == null || Build.SERIAL.length() <= 0) {
            serial = "0123456789ABCDEF";
        } else {
            serial = Build.SERIAL;
        }
        List<Character> list = new ArrayList<>();
        for (char c : serial.toCharArray()) {
            list.add(c);
        }
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (Character c : list) {
            sb.append(c.charValue());
        }
        return sb.toString();
    }

    private static String getIMEI() {// calculator IMEI
        int r1 = 1000000 + new java.util.Random().nextInt(9000000);
        int r2 = 1000000 + new java.util.Random().nextInt(9000000);
        String input = r1 + "" + r2;
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        return input + last;
    }

    private static String getImsi() {
        // 460022535025034
        String title = "4600";
        int second = 0;
        do {
            second = new java.util.Random().nextInt(8);
        } while (second == 4);
        int r1 = 10000 + new java.util.Random().nextInt(90000);
        int r2 = 10000 + new java.util.Random().nextInt(90000);
        return title + "" + second + "" + r1 + "" + r2;
    }

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    private static String getTel() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

}
