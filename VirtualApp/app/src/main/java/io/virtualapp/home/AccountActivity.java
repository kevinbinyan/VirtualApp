package io.virtualapp.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.lody.virtual.helper.SharedPreferencesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

import io.virtualapp.R;
import io.virtualapp.abs.ui.VActivity;

public class AccountActivity extends VActivity {

    public static final String CONTENT = "script_content";
    private EditText path;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findViewById(R.id.browse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        path = (EditText) findViewById(R.id.path);
        content = (EditText) findViewById(R.id.text);
        content.setText((String) SharedPreferencesUtils.getParam(this,SharedPreferencesUtils.SCRIPT,""));
        initMenu();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                path.setText(uri.toString());
                if (uri != null) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(new File(new URI(uri.toString())));
                        // 将指定输入流包装成BufferedReader
                        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                        StringBuilder sb = new StringBuilder("");
                        String line = null;
                        // 循环读取文件内容
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        // 关闭资源
                        br.close();
                        content.setText(sb.toString());
                        SharedPreferencesUtils.setParam(AccountActivity.this,SharedPreferencesUtils.SCRIPT,sb.toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initMenu() {
        View mMenuView = findViewById(R.id.home_menu);
        PopupMenu mPopupMenu = new PopupMenu(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light), mMenuView);
        Menu menu = mPopupMenu.getMenu();
        setIconEnable(menu, true);
        menu.add("确定").setIcon(R.drawable.ic_notification).setOnMenuItemClickListener(item -> {
            Intent data = new Intent();
            data.putExtra(CONTENT, content.getText().toString());
            setResult(Activity.RESULT_OK, data);
            finish();
            SharedPreferencesUtils.setParam(AccountActivity.this,SharedPreferencesUtils.SCRIPT,content.getText().toString());
            return false;
        });

        menu.add("退出").setIcon(R.drawable.ic_about).setOnMenuItemClickListener(item -> {
            finish();
            return false;
        });
        mMenuView.setOnClickListener(v -> mPopupMenu.show());
    }

    private static void setIconEnable(Menu menu, boolean enable) {
        try {
            @SuppressLint("PrivateApi")
            Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
