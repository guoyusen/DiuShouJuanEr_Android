package com.bili.diushoujuaner.utils.entity.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/13.
 */
public class MemberDto implements Parcelable {
    private Long memberNo;

    private Long partyNo;

    private Long userNo;

    private String addTime;

    private String memberName;

    private Integer type;

    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(Long memberNo) {
        this.memberNo = memberNo;
    }

    public Long getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(Long partyNo) {
        this.partyNo = partyNo;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime == null ? null : addTime.trim();
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName == null ? null : memberName.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.memberNo);
        dest.writeValue(this.partyNo);
        dest.writeValue(this.userNo);
        dest.writeString(this.addTime);
        dest.writeString(this.memberName);
        dest.writeValue(this.type);
        dest.writeString(this.picPath);
    }

    public MemberDto() {
    }

    protected MemberDto(Parcel in) {
        this.memberNo = (Long) in.readValue(Long.class.getClassLoader());
        this.partyNo = (Long) in.readValue(Long.class.getClassLoader());
        this.userNo = (Long) in.readValue(Long.class.getClassLoader());
        this.addTime = in.readString();
        this.memberName = in.readString();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.picPath = in.readString();
    }

    public static final Parcelable.Creator<MemberDto> CREATOR = new Parcelable.Creator<MemberDto>() {
        @Override
        public MemberDto createFromParcel(Parcel source) {
            return new MemberDto(source);
        }

        @Override
        public MemberDto[] newArray(int size) {
            return new MemberDto[size];
        }
    };
}
