package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

    private static CustomToast instance;
    private static Toast toast;

    private CustomToast() {
    }

    public static CustomToast getInstance() {
        if (instance == null) {
            instance = new CustomToast();
        }
        return instance;
    }

    public void showError(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, offsetX, offsetY);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(com.bili.diushoujuaner.utils.R.layout.layout_toast_validate_failed, null);
        TextView tip = (TextView) ll.findViewById(com.bili.diushoujuaner.utils.R.id.tip);
        tip.setText(msg);
        toast.setView(ll);
        toast.show();
    }

    public void showWarning(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, offsetX, offsetY);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(com.bili.diushoujuaner.utils.R.layout.layout_toast_validate_warning, null);
        TextView tip = (TextView) ll.findViewById(com.bili.diushoujuaner.utils.R.id.tip);
        tip.setText(msg);
        toast.setView(ll);
        toast.show();
    }

    public void showSuccess(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, offsetX, offsetY);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(com.bili.diushoujuaner.utils.R.layout.layout_toast_validate_success, null);
        TextView tip = (TextView) ll.findViewById(com.bili.diushoujuaner.utils.R.id.tip);
        tip.setText(msg);
        toast.setView(ll);
        toast.show();
    }

}
