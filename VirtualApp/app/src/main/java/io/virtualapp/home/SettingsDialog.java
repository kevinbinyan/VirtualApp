package io.virtualapp.home;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.lody.virtual.helper.SharedPreferencesUtils;
import com.lody.virtual.helper.utils.Tools;

import io.virtualapp.R;

/**
 * Created by sunb on 2018/3/14.
 */

public class SettingsDialog extends Dialog {

    public static final int DEFAULT_MAX_EMULATOR = 20;
    public static final int DEFAULT_TIME = 5;
    public static final int DEFAULT_RANDOM = 0;
    public static final int PWD_WAIT_TIME = 2000;
    public static final int MINE_WAIT_TIME = 10000;

    public SettingsDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_layout);
        EditText edtextmax = (EditText) findViewById(R.id.max_emulator);
        edtextmax.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.MAX_EMULATOR, DEFAULT_MAX_EMULATOR) + "");
        EditText timeBegin = (EditText) findViewById(R.id.time_begin);
        timeBegin.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.TIME_BEGIN, DEFAULT_TIME) + "");
        EditText timeEnd = (EditText) findViewById(R.id.time_end);
        timeEnd.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.TIME_RANDOM, DEFAULT_RANDOM) + "");
        EditText position = (EditText) findViewById(R.id.position);
        position.setText(((int) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.AUTO_LAUNCH_INDEX, 0) + 1) + "");
        CheckBox checkBox = (CheckBox) findViewById(R.id.keep_one);
        checkBox.setChecked((boolean) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.ONLY_ONE_PRO, true));
        CheckBox contacts = (CheckBox) findViewById(R.id.contacts);
        contacts.setChecked((boolean) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.V_CONTACTS, false));
        CheckBox autoRestart = (CheckBox) findViewById(R.id.auto_restart);
        autoRestart.setChecked((boolean) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.AUTO_RESTART, false));
        CheckBox emulator = (CheckBox) findViewById(R.id.emulator);
        emulator.setChecked((boolean) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.EMULATOR, false));
        if (Tools.emulator) {
            emulator.setVisibility(View.VISIBLE);
        } else {
            emulator.setVisibility(View.GONE);
        }
        EditText pwd_wait_time = (EditText) findViewById(R.id.pwd_wait_time);
        pwd_wait_time.setText((int) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.PWD_WAIT_TIME, PWD_WAIT_TIME) + "");
        EditText mine_wait_time = (EditText) findViewById(R.id.mine_wait_time);
        mine_wait_time.setText((int) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.MINE_WAIN_TIME, MINE_WAIT_TIME) + "");
    }

//    public SettingsDialog(Context context, int theme) {
//        super(context, theme);
//        setContentView(R.layout.dialog_layout);
//        EditText edtextmax = (EditText) findViewById(R.id.max_emulator);
//        edtextmax.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.MAX_EMULATOR, 100) + "");
//        EditText edtime = (EditText) findViewById(R.id.auto_time);
//        edtime.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.TIME_BEGIN, 5) + "");
//    }

    public void setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
        Button view = (Button) findViewById(R.id.positiveButton);
        view.setText(positiveButtonText);
        view.setOnClickListener(listener);
    }

    public void setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
        Button view = (Button) findViewById(R.id.negativeButton);
        view.setText(negativeButtonText);
        view.setOnClickListener(listener);
    }

    public int getMaxNumber() {
        EditText edtextmax = (EditText) findViewById(R.id.max_emulator);
        return Integer.parseInt(edtextmax.getText().toString());
    }

    public int getTimeBegin() {
        EditText edtime = (EditText) findViewById(R.id.time_begin);
        return Integer.parseInt(edtime.getText().toString());
    }

    public int getTimeRandom() {
        EditText edtime = (EditText) findViewById(R.id.time_end);
        return Integer.parseInt(edtime.getText().toString());
    }

    public int getPosition() {
        EditText position = (EditText) findViewById(R.id.position);
        return Integer.parseInt(position.getText().toString());
    }

    public boolean isOnly5Pro() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.keep_one);
        return checkBox.isChecked();
    }

    public boolean isVContacts() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.contacts);
        return checkBox.isChecked();
    }

    public boolean isAutoRestart() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.auto_restart);
        return checkBox.isChecked();
    }

    public boolean isEmulator() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.emulator);
        return checkBox.isChecked();
    }

    public int getPwdWaitTime() {
        EditText edtime = (EditText) findViewById(R.id.pwd_wait_time);
        return Integer.parseInt(edtime.getText().toString());
    }

    public int getMimeWaitTime() {
        EditText edtime = (EditText) findViewById(R.id.mine_wait_time);
        return Integer.parseInt(edtime.getText().toString());
    }

}
