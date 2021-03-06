package com.bili.diushoujuaner.utils.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/18.
 */
public class FriendVo extends SortVo implements Parcelable {

    private long friendNo;
    private String nickName;
    private String mobile;
    private String autograph;
    private Integer gender;
    private String birthday;
    private String homeTown;
    private String location;
    private String picPath;
    private String smallNick;
    private String registTime;
    private String updateTime;
    private String wallPaper;

    public long getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(long friendNo) {
        this.friendNo = friendNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getSmallNick() {
        return smallNick;
    }

    public void setSmallNick(String smallNick) {
        this.smallNick = smallNick;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWallPaper() {
        return wallPaper;
    }

    public void setWallPaper(String wallPaper) {
        this.wallPaper = wallPaper;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.friendNo);
        dest.writeString(this.nickName);
        dest.writeString(this.mobile);
        dest.writeString(this.autograph);
        dest.writeValue(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.homeTown);
        dest.writeString(this.location);
        dest.writeString(this.picPath);
        dest.writeString(this.smallNick);
        dest.writeString(this.registTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.wallPaper);
    }

    public FriendVo() {
    }

    protected FriendVo(Parcel in) {
        super(in);
        this.friendNo = in.readLong();
        this.nickName = in.readString();
        this.mobile = in.readString();
        this.autograph = in.readString();
        this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.birthday = in.readString();
        this.homeTown = in.readString();
        this.location = in.readString();
        this.picPath = in.readString();
        this.smallNick = in.readString();
        this.registTime = in.readString();
        this.updateTime = in.readString();
        this.wallPaper = in.readString();
    }

    public static final Creator<FriendVo> CREATOR = new Creator<FriendVo>() {
        @Override
        public FriendVo createFromParcel(Parcel source) {
            return new FriendVo(source);
        }

        @Override
        public FriendVo[] newArray(int size) {
            return new FriendVo[size];
        }
    };
}
