package com.bili.diushoujuaner.utils.entity.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiLi on 2016/3/6.
 */
public class ContactDto implements Parcelable {
    private Long contNo;
    private String displayName;
    private String picPath;
    private int type;
    private String startTime;
    // friend
    private String nickName;
    private String autograph;
    private Integer gender;
    private String homeTown;
    private String smallNick;
    private String wallPaper;
    // party
    private Long ownerNo;
    private String information;
    private List<MemberDto> memberList;

    public String getWallPaper() {
        return wallPaper;
    }

    public void setWallPaper(String wallPaper) {
        this.wallPaper = wallPaper;
    }

    public Long getContNo() {
        return contNo;
    }
    public void setContNo(Long contNo) {
        this.contNo = contNo;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getAutograph() {
        return autograph;
    }
    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    public String getHomeTown() {
        return homeTown;
    }
    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }
    public String getSmallNick() {
        return smallNick;
    }
    public void setSmallNick(String smallNick) {
        this.smallNick = smallNick;
    }
    public Long getOwnerNo() {
        return ownerNo;
    }
    public void setOwnerNo(Long ownerNo) {
        this.ownerNo = ownerNo;
    }
    public String getInformation() {
        return information;
    }
    public void setInformation(String information) {
        this.information = information;
    }
    public List<MemberDto> getMemberList() {
        return memberList;
    }
    public void setMemberList(List<MemberDto> memberList) {
        this.memberList = memberList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.contNo);
        dest.writeString(this.displayName);
        dest.writeString(this.picPath);
        dest.writeInt(this.type);
        dest.writeString(this.startTime);
        dest.writeString(this.nickName);
        dest.writeString(this.autograph);
        dest.writeValue(this.gender);
        dest.writeString(this.homeTown);
        dest.writeString(this.smallNick);
        dest.writeString(this.wallPaper);
        dest.writeValue(this.ownerNo);
        dest.writeString(this.information);
        dest.writeList(this.memberList);
    }

    public ContactDto() {
    }

    protected ContactDto(Parcel in) {
        this.contNo = (Long) in.readValue(Long.class.getClassLoader());
        this.displayName = in.readString();
        this.picPath = in.readString();
        this.type = in.readInt();
        this.startTime = in.readString();
        this.nickName = in.readString();
        this.autograph = in.readString();
        this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.homeTown = in.readString();
        this.smallNick = in.readString();
        this.wallPaper = in.readString();
        this.ownerNo = (Long) in.readValue(Long.class.getClassLoader());
        this.information = in.readString();
        this.memberList = new ArrayList<MemberDto>();
        in.readList(this.memberList, MemberDto.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContactDto> CREATOR = new Parcelable.Creator<ContactDto>() {
        @Override
        public ContactDto createFromParcel(Parcel source) {
            return new ContactDto(source);
        }

        @Override
        public ContactDto[] newArray(int size) {
            return new ContactDto[size];
        }
    };
}
