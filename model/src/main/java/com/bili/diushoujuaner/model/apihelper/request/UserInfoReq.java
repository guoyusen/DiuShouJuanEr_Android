package com.bili.diushoujuaner.model.apihelper.request;

/**
 * Created by BiLi on 2016/4/7.
 */
public class UserInfoReq {

    private String nickName;
    private short gender;
    private String email;
    private String smallNick;
    private String birthday;
    private String location;
    private String homeTown;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public short getGender() {
        return gender;
    }

    public void setGender(short gender) {
        this.gender = gender;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSmallNick() {
        return smallNick;
    }

    public void setSmallNick(String smallNick) {
        this.smallNick = smallNick;
    }
}
