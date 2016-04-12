package com.bili.diushoujuaner.widget;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bili.diushoujuaner.R;

public class CustomToast {

    private static CustomToast instance;
    private static Toast toast;
    private static Handler mHandler = new Handler();

    private static Runnable r = new Runnable() {
        public void run() {
            toast.show();
        }
    };

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
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_toast_validate_failed, null);
        TextView tip = (TextView) ll.findViewById(R.id.tip);
        tip.setText(msg);
        toast.setView(ll);
        toast.show();
    }

    public synchronized void showWarning(Context context, String msg) {
        if(toast == null){
            toast = new Toast(context);
        }
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, offsetX, offsetY);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_toast_validate_warning, null);
        TextView tip = (TextView) ll.findViewById(R.id.tip);
        tip.setText(msg);
        toast.setView(ll);
        mHandler.removeCallbacks(r);
        mHandler.postDelayed(r, 1000);
    }

    public void showSuccess(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        int offsetX = 0;
        int offsetY = 0;
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.TOP, offsetX, offsetY);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_toast_validate_success, null);
        TextView tip = (TextView) ll.findViewById(R.id.tip);
        tip.setText(msg);
        toast.setView(ll);
        toast.show();
    }

}
