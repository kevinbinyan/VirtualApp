package io.virtualapp.splash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.utils.MD5Utils;
import com.lody.virtual.helper.utils.Tools;

import java.util.UUID;

import io.virtualapp.R;
import io.virtualapp.abs.ui.VActivity;
import io.virtualapp.abs.ui.VUiKit;
import io.virtualapp.home.FlurryROMCollector;
import io.virtualapp.home.HomeActivity;
import io.virtualapp.utils.HttpUtils;
import jonathanfinerty.once.Once;

public class SplashActivity extends VActivity {
    private String token;


//    public static final String pass = "e19d5cd5af0378da05f63f891c7467af";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferencesUtils.setParam(VirtualCore.get().getContext(), SharedPreferencesUtils.AUTO_OP, false);
        SharedPreferencesUtils.setParam(VirtualCore.get().getContext(), SharedPreferencesUtils.LOGIN_NOW, false);
//        boolean enterGuide = !Once.beenDone(Once.THIS_APP_INSTALL, VCommends.TAG_NEW_VERSION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        TextView title = (TextView) findViewById(R.id.title);
        TextView qq = (TextView) findViewById(R.id.qq);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        title.setText(title.getText().toString() + "" + packageInfo.versionName);
        if(Tools.isBigClient(this)){
            qq.setText("大客户指定版本(模拟百度)");
        }else {
            qq.setText("指定代理QQ：97302134");
        }
        TelephonyManager mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        token = mTm.getDeviceId() + android.os.Build.BRAND + UUID.randomUUID();

        HttpUtils.checkVersion(packageInfo.versionCode, new HttpUtils.HttpCallBack() {
            //
            @Override
            public void callback(boolean value) {
                if (!value) {
                    Looper.prepare();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("版本过低")
                            .setMessage("当前版本过低，无法使用，请更新\n(请检查网络是否异常)")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();
                    Looper.loop();
                } else {
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
            }
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
//        CheckBox moni = (CheckBox) dialogView.findViewById(R.id.moni);
        etName.setText((String) SharedPreferencesUtils.getParam(VirtualCore.get().getContext(), SharedPreferencesUtils.KEY, ""));
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
                SharedPreferencesUtils.setParam(VirtualCore.get().getContext(), SharedPreferencesUtils.KEY, name);
                SharedPreferencesUtils.setParam(VirtualCore.get().getContext(), SharedPreferencesUtils.TOKEN, token);
                HttpUtils.requestLogin(name, MD5Utils.encrypt(token), new HttpUtils.HttpCallBack() {
                    //
                    @Override
                    public void callback(boolean value) {
                        if (value) {
                            goHome();
                            HttpUtils.getKeyDate(name, MD5Utils.encrypt(token), new HttpUtils.ValueCallBack() {
                                @Override
                                public void callback(int days) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (days == -1) {
                                                Toast.makeText(VirtualCore.get().getContext(), "VIP用户", Toast.LENGTH_SHORT).show();
                                            } else if (days == 0) {
                                                Toast.makeText(VirtualCore.get().getContext(), "账号即将过期", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(VirtualCore.get().getContext(), "账号剩余天数：" + days + " 天", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlertDialog("登录失败或者账号过期");
                                }
                            });
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }

    private void goHome() {
        SharedPreferencesUtils.setParam(SplashActivity.this, SharedPreferencesUtils.VALIDATE, true);
        HomeActivity.goHome(SplashActivity.this);
        finish();
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("验证失败");
        builder.setMessage(msg);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void doActionInThread() {
        if (!VirtualCore.get().isEngineLaunched()) {
            VirtualCore.get().waitForEngine();
        }
    }


}
