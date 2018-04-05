package mirror.android.os;


import mirror.RefClass;
import mirror.RefStaticObject;

public class Build {
    public static Class<?> TYPE = RefClass.load(Build.class, android.os.Build.class);
    public static RefStaticObject<String> DEVICE;
    public static RefStaticObject<String> SERIAL;

    public static RefStaticObject<String> PRODUCT;
    public static RefStaticObject<String> BRAND;
    public static RefStaticObject<String> FINGERPRINT;
    public static RefStaticObject<String> ID;
    public static RefStaticObject<String> MANUFACTURER;
    public static RefStaticObject<String> MODEL;
    public static RefStaticObject<String> DISPLAY;

    public static RefStaticObject<String> USER;
    public static RefStaticObject<String> BOOTLOADER;
    public static RefStaticObject<String> HARDWARE;
//    public static RefStaticObject<String> TYPE;
    public static RefStaticObject<String> CPU_ABI;
}