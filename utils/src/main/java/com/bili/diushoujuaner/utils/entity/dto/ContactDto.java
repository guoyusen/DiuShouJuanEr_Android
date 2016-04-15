package com.bili.diushoujuaner.utils.entity.dto;

import java.util.List;

/**
 * Created by BiLi on 2016/3/6.
 */
public class ContactDto {
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
}
