package com.lody.virtual.server.device;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.RemoteException;

import com.google.gson.JsonObject;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.utils.Tools;
import com.lody.virtual.remote.VDeviceInfo;
import com.lody.virtual.server.interfaces.IDeviceInfoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    info = generateRandomDeviceInfo(userId);
                    mDeviceInfos.put(userId, info);
                    mPersistenceLayer.save();
                }
            }
        }
        return info;
    }

    private VDeviceInfo generateRandomDeviceInfo(int userId) {
        String devices = (String) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.DEVICES, "");
        VDeviceInfo vDeviceInfo = null;
        try {
            JSONArray jsonArray = new JSONArray(devices);
            JSONObject jsonObject = jsonArray.getJSONObject(userId);
            vDeviceInfo = new VDeviceInfo();
            vDeviceInfo.deviceId = jsonObject.getString("deviceId");
            vDeviceInfo.androidId = jsonObject.getString("androidId");
            vDeviceInfo.wifiMac = jsonObject.getString("wifiMac");
            vDeviceInfo.bluetoothMac = jsonObject.getString("bluetoothMac");
            vDeviceInfo.iccId = jsonObject.getString("iccId");
            vDeviceInfo.serial = jsonObject.getString("serial");
//            vDeviceInfo.gmsAdId = jsonObject.getString("gmsAdId");

            vDeviceInfo.lineNumber = jsonObject.getString("lineNumber");
            vDeviceInfo.imsi = jsonObject.getString("imsi");
            vDeviceInfo.sim = jsonObject.getString("sim");

            vDeviceInfo.product = jsonObject.getString("product");
            vDeviceInfo.cpuAbi = jsonObject.getString("cpuAbi");
//            vDeviceInfo.tags = jsonObject.getString("tags");
            vDeviceInfo.device = jsonObject.getString("device");
            vDeviceInfo.display = jsonObject.getString("display");
            vDeviceInfo.brand = jsonObject.getString("brand");
//            vDeviceInfo.board = jsonObject.getString("board");
            vDeviceInfo.fingerprint = jsonObject.getString("fingerprint");
            vDeviceInfo.id = jsonObject.getString("id");
            vDeviceInfo.manufacturer = jsonObject.getString("manufacturer");
            vDeviceInfo.user = jsonObject.getString("user");
            vDeviceInfo.model = jsonObject.getString("model");
//            vDeviceInfo.bootloader = jsonObject.getString("bootloader");
            vDeviceInfo.hardware = jsonObject.getString("hardware");
            vDeviceInfo.host = jsonObject.getString("host");
            vDeviceInfo.type = jsonObject.getString("type");
            vDeviceInfo.ip = getRandomIp();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vDeviceInfo;
    }

    @Override
    public void updateDeviceInfo(int userId, VDeviceInfo info) {
        synchronized (mDeviceInfos) {
            if (info != null) {
                mDeviceInfos.put(userId, info);
                mPersistenceLayer.save();
            }
        }
    }

    SparseArray<VDeviceInfo> getDeviceInfos() {
        return mDeviceInfos;
    }


    public static byte[] getRandomIp() {
        // ip范围
        byte[] byte0 = new byte[]{-64, 10};
        byte[] byte1 = new byte[]{-88, 100};
        int type = new Random().nextInt(2);
        byte[] ip = new byte[4];
        ip[0] = byte0[type];// 192
        ip[1] = byte1[type];// 168
        int byte2ByType2 = new Random().nextInt(255);
        int byte3 = new Random().nextInt(254);
        ip[2] = type == 0 ? (byte) new Random().nextInt(100)
                : (byte) (byte2ByType2 >= 128 ? 128 - byte2ByType2 : byte2ByType2);
        ip[3] = (byte) (1 + (byte3 >= 128 ? 128 - byte3 : byte3));
        return ip;
    }
}
