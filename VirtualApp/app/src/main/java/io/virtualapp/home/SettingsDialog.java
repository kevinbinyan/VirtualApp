package io.virtualapp.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

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
}
