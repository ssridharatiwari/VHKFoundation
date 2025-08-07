package com.vhkfoundation.commonutility.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vhkfoundation.commonutility.GlobalVariables;


public class FontUtils {
    public FontUtils() {}

    public static void setFont(ViewGroup group, Typeface font) {
        Context context = group.getContext();
        Typeface fontBold = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAMEBOLD);
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                if (((TextView) v).getTypeface().getStyle() == Typeface.BOLD ) {
                    ((TextView) v).setTypeface(fontBold);
                }else {
                    ((TextView) v).setTypeface(font);
                }
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v, font);
            }
        }
    }
}
