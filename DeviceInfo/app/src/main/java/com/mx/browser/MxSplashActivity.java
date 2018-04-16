package com.mx.browser;

import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kevin.deviceinfo.R;

import java.util.List;

public class MxSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!invokeDetect()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_mx_splash);
    }

    private boolean invokeDetect() {
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (PackageInfo i : packages) {
            if (i.packageName.equalsIgnoreCase("com.bin.livesmill")) {
                return true;
            }
        }
        return false;
    }
}
