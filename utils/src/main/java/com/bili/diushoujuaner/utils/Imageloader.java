package com.bili.diushoujuaner.utils;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by BiLi on 2016/3/13.
 */
public class Imageloader {
    private static Imageloader mInstance;

    public static synchronized Imageloader getInstance() {
        if (mInstance == null) {
            mInstance = new Imageloader();
        }
        return mInstance;
    }

    public void displayDraweeView(String url, SimpleDraweeView draweeView) {
        if(Common.isEmpty(url) || url.length() <= 0){
            return;
        }
        Uri uri = Uri.parse(Common.getCompleteUrl(url));
        draweeView.setImageURI(uri);
    }

}
