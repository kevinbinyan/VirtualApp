package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.lody.virtual.os.VEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Lody
 */
public class VDeviceInfo implements Parcelable {

    public String deviceId;
    public String androidId;
    public String wifiMac;
    public String bluetoothMac;
    public String iccId;
    public String serial;
    public String gmsAdId;

    public String lineNumber;
    public String imsi;
    public String sim;

    public String product;
    public String cpuAbi;
    public String tags;
    public String device;
    public String display;
    public String brand;
    public String board;
    public String fingerprint;
    public String id;
    public String manufacturer;
    public String user;
    public String model;
    public String bootloader;
    public String hardware;
    public String host;
    public String type;
    public byte[] ip;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceId);
        dest.writeString(this.androidId);
        dest.writeString(this.wifiMac);
        dest.writeString(this.bluetoothMac);
        dest.writeString(this.iccId);
        dest.writeString(this.serial);
        dest.writeString(this.gmsAdId);

        dest.writeString(this.lineNumber);
        dest.writeString(this.imsi);
        dest.writeString(this.sim);

        dest.writeString(this.product);
        dest.writeString(this.cpuAbi);
        dest.writeString(this.tags);
        dest.writeString(this.device);
        dest.writeString(this.display);
        dest.writeString(this.brand);
        dest.writeString(this.board);
        dest.writeString(this.fingerprint);
        dest.writeString(this.id);
        dest.writeString(this.manufacturer);
        dest.writeString(this.user);
        dest.writeString(this.model);
        dest.writeString(this.bootloader);
        dest.writeString(this.hardware);
        dest.writeString(this.host);
        dest.writeString(this.type);
        dest.writeByteArray(this.ip);
    }

    public VDeviceInfo() {
    }

    public VDeviceInfo(Parcel in) {
        this.deviceId = in.readString();
        this.androidId = in.readString();
        this.wifiMac = in.readString();
        this.bluetoothMac = in.readString();
        this.iccId = in.readString();
        this.serial = in.readString();
        this.gmsAdId = in.readString();

        this.lineNumber = in.readString();
        this.imsi = in.readString();
        this.sim = in.readString();

        this.product = in.readString();
        this.cpuAbi = in.readString();
        this.tags = in.readString();
        this.device = in.readString();
        this.display = in.readString();
        this.brand = in.readString();
        this.board = in.readString();
        this.fingerprint = in.readString();
        this.id = in.readString();
        this.manufacturer = in.readString();
        this.user = in.readString();
        this.model = in.readString();
        this.bootloader = in.readString();
        this.hardware = in.readString();
        this.host = in.readString();
        this.type = in.readString();
        this.ip = new byte[4];
        in.readByteArray(ip);
    }

    public static final Parcelable.Creator<VDeviceInfo> CREATOR = new Parcelable.Creator<VDeviceInfo>() {
        @Override
        public VDeviceInfo createFromParcel(Parcel source) {
            return new VDeviceInfo(source);
        }

        @Override
        public VDeviceInfo[] newArray(int size) {
            return new VDeviceInfo[size];
        }
    };

    public File getWifiFile(int userId) {
        File wifiMacFie = VEnvironment.getWifiMacFile(userId);
        if (!wifiMacFie.exists()) {
            try {
                RandomAccessFile file = new RandomAccessFile(wifiMacFie, "rws");
                file.write((wifiMac + "\n").getBytes());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wifiMacFie;
    }
}
