package com.andkeys.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AndKeysIME extends InputMethodService {
    private LinearLayout root;
    private int ink = Color.rgb(42, 55, 68);
    private int paper = Color.rgb(250, 249, 241);

    @Override public View onCreateInputView() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(225, 224, 216));
        String[][] top = {
            {"Past","Copy","Copy\nScreen","Select","ReDo","UnDo","History","Tool","File","Send"},
            {"","","","","","↑","↓","Resize","Muse"}
        };
        int[] colors = {0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0,0xFFF9F8F0};
        LinearLayout row1 = row();
        for (int i=0;i<top[0].length;i++) {
            final String label = top[0][i];
            Button b = key(label, colors[i], 0);
            if (label.equals("Send")) b.setOnClickListener(v -> sendAction());
            else b.setOnClickListener(v -> doTop(label));
            row1.addView(b, weight(1));
        }
        root.addView(row1, new LinearLayout.LayoutParams(-1, dp(48)));

        LinearLayout row2 = row();
        for (String label : top[1]) {
            Button b = key(label, paper, 0);
            if (!label.isEmpty()) b.setOnClickListener(v -> {
                if (label.equals("↑")) moveCursor(-1);
                else if (label.equals("↓")) moveCursor(1);
                else if (label.equals("Resize")) { /* reserved for later sizing controls */ }
                else if (label.equals("Muse")) { /* existing layout label; no device connection */ }
            });
            row2.addView(b, weight(1));
        }
        root.addView(row2, new LinearLayout.LayoutParams(-1, dp(38)));

        LinearLayout digits = row();
        String[] digitKeys = {"! ۱","@ ۲","# ۳","$ ۴","% ۵","^ ۶","& ۷","* ۸","( ۹",") ۰","⌫"};
        for (int i=0;i<digitKeys.length;i++) {
            final int ix=i;
            Button b=key(digitKeys[i], i==10?0xFFF8D9DE:paper, 19);
            b.setOnClickListener(v -> {
                if (ix==10) delete();
                else {
                    String[] values={"۱","۲","۳","۴","۵","۶","۷","۸","۹","۰"};
                    commit(values[ix]);
                }
            });
            digits.addView(b, weight(i==10?1.5f:1));
        }
        root.addView(digits, new LinearLayout.LayoutParams(-1, dp(42)));

        String[][] letters = {
            {"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"},
            {"ش","س","ی","ب","ل","ا","ت","ن","م","ک","گ"},
            {"Caps","ظ","ط","ز","ر","ذ","د","پ","و","چ","=","-"}
        };
        for (int r=0;r<letters.length;r++) {
            LinearLayout row=row();
            for (String label:letters[r]) {
                Button b=key(label, paper, 24);
                b.setOnClickListener(v -> {
                    if(label.equals("Caps")) return;
                    if(label.equals("=")||label.equals("-")) commit(label);
                    else commit(label);
                });
                row.addView(b, weight(1));
            }
            root.addView(row,new LinearLayout.LayoutParams(-1,dp(r==2?40:42)));
        }

        LinearLayout bottom=row();
        String[] bottomKeys={"😀","&@#\n*¥✓","اعراب","🌐 FA","Space","،",".","؟","⬅","➡","↑","↓","[","]"};
        float[] ws={0.8f,0.8f,0.7f,1.0f,2.7f,0.8f,0.8f,0.8f,1.1f,1.1f,0.5f,0.5f,0.5f,0.5f};
        for(int i=0;i<bottomKeys.length;i++){
            final String label=bottomKeys[i];
            Button b=key(label, paper, label.equals("Space")?18:16);
            b.setOnClickListener(v -> {
                switch(label){
                    case "Space": commit(" "); break;
                    case "،": commit("،"); break;
                    case ".": commit("."); break;
                    case "؟": commit("؟"); break;
                    case "⬅": moveCursor(-1); break;
                    case "➡": moveCursor(1); break;
                    case "↑": moveCursor(-1); break;
                    case "↓": moveCursor(1); break;
                    case "🌐 FA": commit(""); break;
                    case "😀": commit("😀"); break;
                    case "[": commit("["); break;
                    case "]": commit("]"); break;
                    default: break;
                }
            });
            bottom.addView(b,weight(ws[i]));
        }
        root.addView(bottom,new LinearLayout.LayoutParams(-1,dp(44)));
        return root;
    }

    private LinearLayout row(){
        LinearLayout l=new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER);
        return l;
    }
    private LinearLayout.LayoutParams weight(float w){
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-1,w);
        p.setMargins(dp(1),dp(1),dp(1),dp(1));
        return p;
    }
    private int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+0.5f);}
    private Button key(String text,int bg,int size){
        Button b=new Button(this);
        b.setText(text);
        b.setAllCaps(false);
        b.setTextColor(ink);
        b.setTextSize(size==0?17:size);
        b.setGravity(Gravity.CENTER);
        b.setPadding(dp(1),0,dp(1),0);
        b.setMinWidth(0); b.setMinimumWidth(0);
        b.setMinHeight(0); b.setMinimumHeight(0);
        b.setBackground(shape(bg));
        b.setTypeface(Typeface.DEFAULT);
        return b;
    }
    private GradientDrawable shape(int color){
        GradientDrawable d=new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(5));
        d.setStroke(dp(1),0xFFD0CFC7);
        return d;
    }
    private void commit(String s){
        InputConnection ic=getCurrentInputConnection();
        if(ic!=null && s!=null && !s.isEmpty()) ic.commitText(s,1);
    }
    private void delete(){
        InputConnection ic=getCurrentInputConnection();
        if(ic!=null) {
            CharSequence selected=ic.getSelectedText(0);
            if(selected!=null && selected.length()>0) ic.commitText("",1);
            else ic.deleteSurroundingText(1,0);
        }
    }
    private void moveCursor(int amount){
        InputConnection ic=getCurrentInputConnection();
        if(ic!=null) ic.sendKeyEvent(new android.view.KeyEvent(amount<0?android.view.KeyEvent.ACTION_DOWN:android.view.KeyEvent.ACTION_DOWN,
            amount<0?android.view.KeyEvent.KEYCODE_DPAD_LEFT:android.view.KeyEvent.KEYCODE_DPAD_RIGHT));
    }
    private void sendAction(){
        InputConnection ic=getCurrentInputConnection();
        if(ic==null) return;
        EditorInfo ei=getCurrentInputEditorInfo();
        int action=ei==null?EditorInfo.IME_ACTION_NONE:(ei.imeOptions & EditorInfo.IME_MASK_ACTION);
        if(action!=EditorInfo.IME_ACTION_NONE && action!=EditorInfo.IME_ACTION_UNSPECIFIED
            && ic.performEditorAction(action)) return;
        // Web editors often expose no IME action. Use Enter locally; never contact another device.
        ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER));
        ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER));
    }
    private void doTop(String label){
        switch(label){
            case "Past": { InputConnection ic=getCurrentInputConnection(); if(ic!=null) ic.performContextMenuAction(android.R.id.paste); break; }
            case "Copy": { InputConnection ic=getCurrentInputConnection(); if(ic!=null) ic.performContextMenuAction(android.R.id.copy); break; }
            case "Select": { InputConnection ic=getCurrentInputConnection(); if(ic!=null) ic.performContextMenuAction(android.R.id.selectAll); break; }
            case "ReDo": case "UnDo": break;
            case "Copy\nScreen": { InputConnection ic=getCurrentInputConnection(); if(ic!=null) ic.performContextMenuAction(android.R.id.copy); break; }
            default: break;
        }
    }
}
