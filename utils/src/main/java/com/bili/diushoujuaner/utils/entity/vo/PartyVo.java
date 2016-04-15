package com.bili.diushoujuaner.utils.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/19.
 */
public class PartyVo extends SortVo implements Parcelable {

    private long partyNo;
    private long ownerNo;
    private String information;
    private String registerTime;
    private String picPath;

    public long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(long partyNo) {
        this.partyNo = partyNo;
    }

    public long getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(long ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

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
        dest.writeLong(this.partyNo);
        dest.writeLong(this.ownerNo);
        dest.writeString(this.information);
        dest.writeString(this.registerTime);
        dest.writeString(this.picPath);
    }

    public PartyVo() {
    }

    protected PartyVo(Parcel in) {
        this.partyNo = in.readLong();
        this.ownerNo = in.readLong();
        this.information = in.readString();
        this.registerTime = in.readString();
        this.picPath = in.readString();
    }

    public static final Parcelable.Creator<PartyVo> CREATOR = new Parcelable.Creator<PartyVo>() {
        @Override
        public PartyVo createFromParcel(Parcel source) {
            return new PartyVo(source);
        }

        @Override
        public PartyVo[] newArray(int size) {
            return new PartyVo[size];
        }
    };
}
