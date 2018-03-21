package io.virtualapp.home;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.virtualapp.R;

/**
 * Created by sunb on 2018/3/14.
 */

public class SettingsDialog extends Dialog {

    public SettingsDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_layout);
        EditText edtextmax = (EditText) findViewById(R.id.max_emulator);
        edtextmax.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.MAX_EMULATOR, 100) + "");
        EditText edtime = (EditText) findViewById(R.id.auto_time);
        edtime.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.TIME_TYPE, 5) + "");
        EditText position = (EditText) findViewById(R.id.position);
        position.setText(((int) SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.AUTO_LAUNCH_INDEX, 0) + 1) + "");
    }

    public SettingsDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_layout);
        EditText edtextmax = (EditText) findViewById(R.id.max_emulator);
        edtextmax.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.MAX_EMULATOR, 100) + "");
        EditText edtime = (EditText) findViewById(R.id.auto_time);
        edtime.setText(SharedPreferencesUtils.getParam(getContext(), SharedPreferencesUtils.TIME_TYPE, 5) + "");
    }

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

    public int getTime() {
        EditText edtime = (EditText) findViewById(R.id.auto_time);
        return Integer.parseInt(edtime.getText().toString());
    }

    public int getPosition() {
        EditText position = (EditText) findViewById(R.id.position);
        return Integer.parseInt(position.getText().toString());
    }
}
