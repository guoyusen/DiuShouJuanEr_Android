package com.bili.diushoujuaner.utils.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/4/14.
 */
public class MemberVo implements Parcelable {

    private long partyNo;
    private long userNo;
    private String memberName;
    private String addTime;
    private int type;
    private String picPath;
    protected String sortLetter;

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.partyNo);
        dest.writeLong(this.userNo);
        dest.writeString(this.memberName);
        dest.writeString(this.addTime);
        dest.writeInt(this.type);
        dest.writeString(this.picPath);
    }

    public MemberVo() {
    }

    protected MemberVo(Parcel in) {
        this.partyNo = in.readLong();
        this.userNo = in.readLong();
        this.memberName = in.readString();
        this.addTime = in.readString();
        this.type = in.readInt();
        this.picPath = in.readString();
    }

    public static final Parcelable.Creator<MemberVo> CREATOR = new Parcelable.Creator<MemberVo>() {
        @Override
        public MemberVo createFromParcel(Parcel source) {
            return new MemberVo(source);
        }

        @Override
        public MemberVo[] newArray(int size) {
            return new MemberVo[size];
        }
    };
}
