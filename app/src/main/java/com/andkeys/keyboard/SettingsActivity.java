package com.andkeys.keyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(24, 32, 24, 24);
        TextView title = new TextView(this);
        title.setText("AndKeys"); title.setTextSize(26); root.addView(title);
        TextView info = new TextView(this);
        info.setText("Enable AndKeys in Android keyboard settings, then select it from the keyboard picker.");
        info.setTextSize(16); info.setPadding(0, 20, 0, 20); root.addView(info);
        Button open = new Button(this);
        open.setText("Open keyboard settings");
        open.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)));
        root.addView(open); setContentView(root);
    }
}
