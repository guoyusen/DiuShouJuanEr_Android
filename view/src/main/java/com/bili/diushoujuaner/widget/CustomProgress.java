package com.bili.diushoujuaner.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomProgress extends Dialog {


    private static CustomProgress dialog;
    private Context context;

    private CustomProgress(Context context) {
        super(context);
        this.context = context;
    }

    public static synchronized CustomProgress getInstance(Context context) {
        if (dialog == null) {
            dialog = new CustomProgress(context, com.bili.diushoujuaner.utils.R.style.Custom_Progress);
        }
        return dialog;
    }


    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(com.bili.diushoujuaner.utils.R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(com.bili.diushoujuaner.utils.R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(com.bili.diushoujuaner.utils.R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    @Override
    public void dismiss() {
        dialog = null;
        super.dismiss();
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     * @return
     */
    public void show(CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        dialog.setTitle("");
        dialog.setContentView(com.bili.diushoujuaner.utils.R.layout.progress_custom);
        if (message == null || message.length() == 0) {
            dialog.findViewById(com.bili.diushoujuaner.utils.R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(com.bili.diushoujuaner.utils.R.id.message);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.FILL_HORIZONTAL | Gravity.TOP;
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.3f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

}
