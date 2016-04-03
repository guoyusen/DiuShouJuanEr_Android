package com.bili.diushoujuaner.model.apihelper.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by BiLi on 2016/3/21.
 */
public class CommentDto implements Parcelable {
    /**
     * addTime : 2016-01-20 14:42:02
     * commentNo : 41
     * content : fdfdsafdsafds
     * fromNo : 10001
     * fromPicPath : images/head/2015/12/11/10001.png
     * nickName : 曹孟德
     */

    private String timeStamp;
    private Long recallNo;
    private String addTime;
    private Long commentNo;
    private String content;
    private Long fromNo;
    private String fromPicPath;
    private String nickName;
    private List<ResponDto> responList;

    public Long getRecallNo() {
        return recallNo;
    }

    public void setRecallNo(Long recallNo) {
        this.recallNo = recallNo;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public Long getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(Long commentNo) {
        this.commentNo = commentNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFromNo() {
        return fromNo;
    }

    public void setFromNo(Long fromNo) {
        this.fromNo = fromNo;
    }

    public String getFromPicPath() {
        return fromPicPath;
    }

    public void setFromPicPath(String fromPicPath) {
        this.fromPicPath = fromPicPath;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<ResponDto> getResponList() {
        return responList;
    }

    public void setResponList(List<ResponDto> responList) {
        this.responList = responList;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timeStamp);
        dest.writeValue(this.recallNo);
        dest.writeString(this.addTime);
        dest.writeValue(this.commentNo);
        dest.writeString(this.content);
        dest.writeValue(this.fromNo);
        dest.writeString(this.fromPicPath);
        dest.writeString(this.nickName);
        dest.writeTypedList(responList);
    }

    public CommentDto() {
    }

    protected CommentDto(Parcel in) {
        this.timeStamp = in.readString();
        this.recallNo = (Long) in.readValue(Long.class.getClassLoader());
        this.addTime = in.readString();
        this.commentNo = (Long) in.readValue(Long.class.getClassLoader());
        this.content = in.readString();
        this.fromNo = (Long) in.readValue(Long.class.getClassLoader());
        this.fromPicPath = in.readString();
        this.nickName = in.readString();
        this.responList = in.createTypedArrayList(ResponDto.CREATOR);
    }

    public static final Creator<CommentDto> CREATOR = new Creator<CommentDto>() {
        @Override
        public CommentDto createFromParcel(Parcel source) {
            return new CommentDto(source);
        }

        @Override
        public CommentDto[] newArray(int size) {
            return new CommentDto[size];
        }
    };
}
