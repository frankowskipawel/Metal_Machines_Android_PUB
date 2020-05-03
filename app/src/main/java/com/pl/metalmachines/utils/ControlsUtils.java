package com.pl.metalmachines.utils;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ControlsUtils {

    public static void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            } else {
                if (child instanceof EditText) {
                    child.setEnabled(enable);
                    ((EditText) child).setTextColor(Color.parseColor("#000000"));
                } else {
                    child.setClickable(enable);
                }
            }
        }
    }
}
