package com.bili.diushoujuaner.utils.entity.dto;

/**
 * Created by BiLi on 2016/3/13.
 */
public class MemberDto {
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
}
