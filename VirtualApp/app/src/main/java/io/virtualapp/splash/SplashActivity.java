package io.virtualapp.splash;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.MD5Utils;

import io.virtualapp.R;
import io.virtualapp.VCommends;
import io.virtualapp.abs.ui.VActivity;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.FlurryROMCollector;
import io.virtualapp.home.HomeActivity;

import com.lody.virtual.helper.SharedPreferencesUtils;

import io.virtualapp.utils.HttpUtils;
import jonathanfinerty.once.Once;

public class SplashActivity extends VActivity {


//    public static final String pass = "e19d5cd5af0378da05f63f891c7467af";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        @SuppressWarnings("unused")
        boolean enterGuide = !Once.beenDone(Once.THIS_APP_INSTALL, VCommends.TAG_NEW_VERSION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView title = (TextView) findViewById(R.id.title);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        title.setText(title.getText().toString() + "" + packageInfo.versionName);
        VUiKit.defer().when(() -> {
            if (!Once.beenDone("collect_flurry")) {
                FlurryROMCollector.startCollect();
                Once.markDone("collect_flurry");
            }
            long time = System.currentTimeMillis();
            doActionInThread();
            time = System.currentTimeMillis() - time;
            long delta = 3000L - time;
            if (delta > 0) {
                VUiKit.sleep(delta);
            }
        }).done((res) -> {
            showDialog();

        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        View dialogView = View.inflate(this, R.layout.password, null);
        //设置对话框布局
        dialog.setView(dialogView);
        dialog.show();
        EditText etName = (EditText) dialogView.findViewById(R.id.et_name);
        etName.setText((String) SharedPreferencesUtils.getParam(SplashActivity.this, SharedPreferencesUtils.KEY, ""));
        Button btnLogin = (Button) dialogView.findViewById(R.id.btn_login);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SplashActivity.this, "秘钥不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferencesUtils.setParam(SplashActivity.this, SharedPreferencesUtils.KEY, name);
//                if (MD5Utils.encrypt(name).equals(pass)) {
//                    HomeActivity.goHome(SplashActivity.this);
//                    finish();
//                } else {
//                    finish();
//                }
                HttpUtils.requestNetForGetLogin(name, new HttpUtils.HttpCallBack() {

                    @Override
                    public void callback(boolean value) {
                        if (value) {
                            HomeActivity.goHome(SplashActivity.this, name);
                        }
                        finish();
                    }
                }, true);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void doActionInThread() {
        if (!VirtualCore.get().isEngineLaunched()) {
            VirtualCore.get().waitForEngine();
        }
    }


}
