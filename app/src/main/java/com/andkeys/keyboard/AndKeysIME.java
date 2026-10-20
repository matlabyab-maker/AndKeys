package com.andkeys.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
import android.widget.LinearLayout;

public class AndKeysIME extends InputMethodService {
    private final int ink = Color.rgb(42,55,68);
    private final int paper = Color.rgb(250,249,241);

    @Override public View onCreateInputView() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFFE2E1D9);

        String[] top = {"Past","Copy","Copy\nScreen","Select","ReDo","UnDo","8\nHistory","Tool","File","Send"};
        LinearLayout r1=row();
        for(String label:top){
            Button b=key(label, label.equals("Send") ? Color.rgb(25,118,210) : paper,
                    label.contains("\n")?14:18);
            if(label.equals("Send")) b.setTextColor(Color.WHITE);
            if(label.equals("Past")||label.equals("ReDo")||label.equals("UnDo")||label.equals("File"))
                b.setTypeface(Typeface.SERIF);
            b.setEnabled(true);
            b.setOnClickListener(v->{ if(label.equals("Send")) sendAction(); else topAction(label); });
            r1.addView(b,lp(1));
        }
        root.addView(r1,new LinearLayout.LayoutParams(-1,dp(49)));

        LinearLayout r2=row();
        String[] controls={"","","","","","↑","↓","Resize","Muse"};
        for(String label:controls){
            Button b=key(label,paper,17);
            if(!label.isEmpty()) b.setOnClickListener(v->{
                if(label.equals("↑")) move(KeyEvent.KEYCODE_DPAD_UP);
                else if(label.equals("↓")) move(KeyEvent.KEYCODE_DPAD_DOWN);
                // Existing pictured Resize/Muse positions retained; no extra device-connection control.
            });
            r2.addView(b,lp(1));
        }
        root.addView(r2,new LinearLayout.LayoutParams(-1,dp(38)));

        LinearLayout nums=row();
        String[] labels={"! ۱","@ ۲","# ۳","$ ۴","% ۵","^ ۶","& ۷","* ۸","( ۹",") ۰","⌫"};
        String[] digits={"۱","۲","۳","۴","۵","۶","۷","۸","۹","۰"};
        for(int i=0;i<labels.length;i++){
            final int ix=i; Button b=key(labels[i],i==10?0xFFF9DDE0:paper,16);
            b.setOnClickListener(v->{if(ix==10) delete(); else commit(digits[ix]);});
            nums.addView(b,lp(ix==10?1.35f:1));
        }
        root.addView(nums,new LinearLayout.LayoutParams(-1,dp(42)));

        String[][] rows={
            {"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"},
            {"ش","س","ی","ب","ل","ا","ت","ن","م","ک","گ"},
            {"Caps","ظ","ط","ژ","ز","ر","ذ","د","پ","و","چ","=","-"}
        };
        for(int ri=0;ri<rows.length;ri++){
            LinearLayout rr=row();
            for(String label:rows[ri]){
                Button b=key(label,paper,ri==2?20:24);
                b.setOnClickListener(v->{if(!label.equals("Caps")) commit(label);});
                rr.addView(b,lp(1));
            }
            root.addView(rr,new LinearLayout.LayoutParams(-1,dp(ri==2?40:42)));
        }

        LinearLayout bottom=row();
        String[] last={"😀","&@#\n*¥✓","اعراب","🌐 FA","Space","،",".","؟","⬅","➡","↑","↓","[","]"};
        float[] weights={0.8f,0.8f,0.7f,1f,2.7f,0.8f,0.8f,0.8f,1.1f,1.1f,0.5f,0.5f,0.5f,0.5f};
        for(int i=0;i<last.length;i++){
            String label=last[i]; Button b=key(label,paper,label.equals("Space")?17:15);
            b.setOnClickListener(v->{
                switch(label){
                    case "Space": commit(" "); break;
                    case "،": commit("،"); break;
                    case ".": commit("."); break;
                    case "؟": commit("؟"); break;
                    case "⬅": move(KeyEvent.KEYCODE_DPAD_LEFT); break;
                    case "➡": move(KeyEvent.KEYCODE_DPAD_RIGHT); break;
                    case "↑": move(KeyEvent.KEYCODE_DPAD_UP); break;
                    case "↓": move(KeyEvent.KEYCODE_DPAD_DOWN); break;
                    case "😀": commit("😀"); break;
                    case "[": commit("["); break;
                    case "]": commit("]"); break;
                    default: break;
                }
            });
            bottom.addView(b,lp(weights[i]));
        }
        root.addView(bottom,new LinearLayout.LayoutParams(-1,dp(44)));
        return root;
    }

    // Send stays connected to the focused app through Android's InputConnection.
    // Try the editor's declared action first; if none exists, send Enter for editors
    // (including many web message fields) that bind Enter to their Send behavior.
    private void sendAction(){
        InputConnection ic=getCurrentInputConnection();
        if(ic==null) return;
        EditorInfo info=getCurrentInputEditorInfo();
        int action=info==null?EditorInfo.IME_ACTION_NONE:(info.imeOptions & EditorInfo.IME_MASK_ACTION);
        if(action!=EditorInfo.IME_ACTION_NONE && action!=EditorInfo.IME_ACTION_UNSPECIFIED){
            if(ic.performEditorAction(action)) return;
        }
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_ENTER));
    }
    private void topAction(String label){
        InputConnection ic=getCurrentInputConnection(); if(ic==null)return;
        switch(label){
            case "Past": ic.performContextMenuAction(android.R.id.paste); break;
            case "Copy": case "Copy\nScreen": ic.performContextMenuAction(android.R.id.copy); break;
            case "Select": ic.performContextMenuAction(android.R.id.selectAll); break;
            default: break;
        }
    }
    private LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER);return l;}
    private LinearLayout.LayoutParams lp(float w){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-1,w);p.setMargins(dp(1),dp(1),dp(1),dp(1));return p;}
    private int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+0.5f);}
    private Button key(String text,int color,int size){
        Button b=new Button(this);b.setText(text);b.setAllCaps(false);b.setTextColor(ink);b.setTextSize(size);
        b.setGravity(Gravity.CENTER);b.setPadding(dp(1),0,dp(1),0);b.setMinWidth(0);b.setMinimumWidth(0);b.setMinHeight(0);b.setMinimumHeight(0);
        GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(5));d.setStroke(dp(1),0xFFD0CFC7);b.setBackground(d);return b;
    }
    private void commit(String s){InputConnection ic=getCurrentInputConnection();if(ic!=null&&s!=null&&!s.isEmpty())ic.commitText(s,1);}
    private void delete(){InputConnection ic=getCurrentInputConnection();if(ic!=null){CharSequence sel=ic.getSelectedText(0);if(sel!=null&&sel.length()>0)ic.commitText("",1);else ic.deleteSurroundingText(1,0);}}
    private void move(int code){InputConnection ic=getCurrentInputConnection();if(ic!=null){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,code));ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,code));}}
}
