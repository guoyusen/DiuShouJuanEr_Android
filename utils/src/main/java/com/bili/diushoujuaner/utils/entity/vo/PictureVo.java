package com.bili.diushoujuaner.utils.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/25.
 */
public class PictureVo implements Parcelable {

    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picPath);
    }

    public PictureVo() {
    }

    protected PictureVo(Parcel in) {
        this.picPath = in.readString();
    }

    public static final Parcelable.Creator<PictureVo> CREATOR = new Parcelable.Creator<PictureVo>() {
        @Override
        public PictureVo createFromParcel(Parcel source) {
            return new PictureVo(source);
        }

        @Override
        public PictureVo[] newArray(int size) {
            return new PictureVo[size];
        }
    };
}
