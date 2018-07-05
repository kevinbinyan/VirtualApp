package com.lody.virtual.helper;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

    public static final String KEY = "key";
    public static final String SCRIPT = "script";
    public static final String BOUND_SCRIPT = "bound_script";

    public static Properties loadConfig() {
        File parent_path = Environment.getExternalStorageDirectory();
        File dir = new File(parent_path, "VirtualLives");
        dir.mkdir();
        File config = new File(dir, "config.ini");
        Properties properties = new Properties();
        try {
            if (config == null || !config.exists()) {
                config.createNewFile();
                saveConfig(properties);
                loadConfig();
            }
            FileInputStream s = new FileInputStream(config);
            properties.load(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static void saveConfig(Properties properties) {
        try {
            File parent_path = Environment.getExternalStorageDirectory();
            File dir = new File(parent_path, "VirtualLives");
            dir.mkdir();
            File config = new File(dir, "config.ini");
            FileOutputStream s = new FileOutputStream(config, false);
            properties.store(s, "config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(String key, String value) {
        Properties properties = loadConfig();
        properties.put(key, value);
        saveConfig(properties);
    }

    public static String getConfig(String key, String value) {
        Properties properties = loadConfig();
        return properties.getProperty(key, value);
    }

}
