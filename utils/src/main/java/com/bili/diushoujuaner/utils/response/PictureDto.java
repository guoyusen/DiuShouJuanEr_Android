package com.bili.diushoujuaner.utils.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/8.
 */
public class PictureDto implements Parcelable {


    /**
     * height : 0
     * offSetLeft : 0
     * offSetTop : -9
     * picId : 0
     * picPath : images/album/2016/01/20/1453288415603.jpg
     * pictureNo : 22
     * recallNo : 33
     * width : 176
     */

    private String picPath;
    private long pictureNo;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public long getPictureNo() {
        return pictureNo;
    }

    public void setPictureNo(long pictureNo) {
        this.pictureNo = pictureNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picPath);
        dest.writeLong(this.pictureNo);
    }

    public PictureDto() {
    }

    protected PictureDto(Parcel in) {
        this.picPath = in.readString();
        this.pictureNo = in.readLong();
    }

    public static final Parcelable.Creator<PictureDto> CREATOR = new Parcelable.Creator<PictureDto>() {
        @Override
        public PictureDto createFromParcel(Parcel source) {
            return new PictureDto(source);
        }

        @Override
        public PictureDto[] newArray(int size) {
            return new PictureDto[size];
        }
    };
}
