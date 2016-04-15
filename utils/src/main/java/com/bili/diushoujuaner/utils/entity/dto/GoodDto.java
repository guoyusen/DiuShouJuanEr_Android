package com.bili.diushoujuaner.utils.entity.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/21.
 */
public class GoodDto implements Parcelable {
    /**
     * goodNo : 38
     * goodTime : 2016-01-11 16:33:53
     * recallNo : 18
     * userNo : 10002
     * userPicPath : images/head/2015/12/12/10002.png
     */

    private long goodNo;
    private String goodTime;
    private long userNo;
    private String userPicPath;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getGoodNo() {
        return goodNo;
    }

    public void setGoodNo(long goodNo) {
        this.goodNo = goodNo;
    }

    public String getGoodTime() {
        return goodTime;
    }

    public void setGoodTime(String goodTime) {
        this.goodTime = goodTime;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    public String getUserPicPath() {
        return userPicPath;
    }

    public void setUserPicPath(String userPicPath) {
        this.userPicPath = userPicPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.goodNo);
        dest.writeString(this.goodTime);
        dest.writeLong(this.userNo);
        dest.writeString(this.userPicPath);
        dest.writeString(this.nickName);
    }

    public GoodDto() {
    }

    protected GoodDto(Parcel in) {
        this.goodNo = in.readLong();
        this.goodTime = in.readString();
        this.userNo = in.readLong();
        this.userPicPath = in.readString();
        this.nickName = in.readString();
    }

    public static final Creator<GoodDto> CREATOR = new Creator<GoodDto>() {
        @Override
        public GoodDto createFromParcel(Parcel source) {
            return new GoodDto(source);
        }

        @Override
        public GoodDto[] newArray(int size) {
            return new GoodDto[size];
        }
    };
}
