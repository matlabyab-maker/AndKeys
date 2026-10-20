package com.andkeys.keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 32, 24, 24);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title = new TextView(this);
        title.setText("AndKeys");
        title.setTextSize(26);
        root.addView(title);
        TextView info = new TextView(this);
        info.setText("Enable AndKeys in Android keyboard settings, then choose it from the keyboard switcher.");
        info.setTextSize(16);
        info.setPadding(0, 20, 0, 20);
        root.addView(info);
        Button enable = new Button(this);
        enable.setText("Open keyboard settings");
        enable.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)));
        root.addView(enable);
        setContentView(root);
    }
}
