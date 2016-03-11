package com.bili.diushoujuaner.utils;

import android.content.Context;
import android.content.Intent;

import com.bili.diushoujuaner.activity.LoginActivity;
import com.bili.diushoujuaner.widget.CustomToast;

/**
 * Created by BiLi on 2016/3/11.
 */
public class ResponseMessageType {

    public static boolean showMessage(Context context, String retCode, String message) {
        switch (retCode) {
            case "fail":
                CustomToast.getInstance().showWarning(context, message);
                return false;
            default:
                return true;
        }
    }

    public static void showError(Context context, int errorCode) {
        switch (errorCode) {
            case 401:
                context.startActivity(new Intent(context, LoginActivity.class));
                CustomToast.getInstance().showWarning(context, "您的登录已过期，请重新登录");
            case 403:
                context.startActivity(new Intent(context, LoginActivity.class));
                CustomToast.getInstance().showWarning(context, "非法请求，请重新登录");
                break;
            case 500:
                CustomToast.getInstance().showError(context, "服务器维护中...");
                break;
            case 100:
                CustomToast.getInstance().showWarning(context, "网不好，好捉急...");
                break;
            default:
                break;
        }
    }

}
