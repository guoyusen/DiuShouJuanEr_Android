package com.bili.diushoujuaner.utils.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/21.
 */
public class ResponDto implements Parcelable {
    /**
     * addTime : 2016-01-24 13:28:36
     * commentNo : 19
     * content : 范德萨
     * fromNo : 10002
     * fromPicPath : images/head/2015/12/12/10002.png
     * nickNameFrom : 张翼德
     * nickNameTo : 刘玄德
     * responNo : 14
     * toNo : 10004
     */

    private String addTime;
    private long commentNo;
    private String content;
    private long fromNo;
    private String fromPicPath;
    private String nickNameFrom;
    private String nickNameTo;
    private long responNo;
    private long toNo;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(long commentNo) {
        this.commentNo = commentNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getFromNo() {
        return fromNo;
    }

    public void setFromNo(long fromNo) {
        this.fromNo = fromNo;
    }

    public String getFromPicPath() {
        return fromPicPath;
    }

    public void setFromPicPath(String fromPicPath) {
        this.fromPicPath = fromPicPath;
    }

    public String getNickNameFrom() {
        return nickNameFrom;
    }

    public void setNickNameFrom(String nickNameFrom) {
        this.nickNameFrom = nickNameFrom;
    }

    public String getNickNameTo() {
        return nickNameTo;
    }

    public void setNickNameTo(String nickNameTo) {
        this.nickNameTo = nickNameTo;
    }

    public long getResponNo() {
        return responNo;
    }

    public void setResponNo(long responNo) {
        this.responNo = responNo;
    }

    public long getToNo() {
        return toNo;
    }

    public void setToNo(long toNo) {
        this.toNo = toNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addTime);
        dest.writeLong(this.commentNo);
        dest.writeString(this.content);
        dest.writeLong(this.fromNo);
        dest.writeString(this.fromPicPath);
        dest.writeString(this.nickNameFrom);
        dest.writeString(this.nickNameTo);
        dest.writeLong(this.responNo);
        dest.writeLong(this.toNo);
    }

    public ResponDto() {
    }

    protected ResponDto(Parcel in) {
        this.addTime = in.readString();
        this.commentNo = in.readLong();
        this.content = in.readString();
        this.fromNo = in.readLong();
        this.fromPicPath = in.readString();
        this.nickNameFrom = in.readString();
        this.nickNameTo = in.readString();
        this.responNo = in.readLong();
        this.toNo = in.readLong();
    }

    public static final Parcelable.Creator<ResponDto> CREATOR = new Parcelable.Creator<ResponDto>() {
        @Override
        public ResponDto createFromParcel(Parcel source) {
            return new ResponDto(source);
        }

        @Override
        public ResponDto[] newArray(int size) {
            return new ResponDto[size];
        }
    };
}
