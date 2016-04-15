package com.bili.diushoujuaner.utils.entity.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by BiLi on 2016/3/5.
 */
public class RecallDto implements Parcelable {


    /**
     * publishTime : 2016-03-05 15:01:17
     * recallNo : 37
     * userName : 曹孟德
     * userNo : 10001
     * userPicPath : images/head/2015/12/11/10001.png
     * content : 打算发的撒范德萨范德萨
     */

    private String publishTime;
    private long recallNo;
    private String userName;
    private long userNo;
    private String userPicPath;
    private String content;
    private List<CommentDto> commentList;
    private List<GoodDto> goodList;
    private List<PictureDto> pictureList;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public long getRecallNo() {
        return recallNo;
    }

    public void setRecallNo(long recallNo) {
        this.recallNo = recallNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    public String getUserPicPath() {
        return userPicPath;
    }

    public void setUserPicPath(String userPicPath) {
        this.userPicPath = userPicPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }

    public List<GoodDto> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodDto> goodList) {
        this.goodList = goodList;
    }

    public List<PictureDto> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<PictureDto> pictureList) {
        this.pictureList = pictureList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publishTime);
        dest.writeLong(this.recallNo);
        dest.writeString(this.userName);
        dest.writeLong(this.userNo);
        dest.writeString(this.userPicPath);
        dest.writeString(this.content);
        dest.writeTypedList(commentList);
        dest.writeTypedList(goodList);
        dest.writeTypedList(pictureList);
    }

    public RecallDto() {
    }

    protected RecallDto(Parcel in) {
        this.publishTime = in.readString();
        this.recallNo = in.readLong();
        this.userName = in.readString();
        this.userNo = in.readLong();
        this.userPicPath = in.readString();
        this.content = in.readString();
        this.commentList = in.createTypedArrayList(CommentDto.CREATOR);
        this.goodList = in.createTypedArrayList(GoodDto.CREATOR);
        this.pictureList = in.createTypedArrayList(PictureDto.CREATOR);
    }

    public static final Parcelable.Creator<RecallDto> CREATOR = new Parcelable.Creator<RecallDto>() {
        @Override
        public RecallDto createFromParcel(Parcel source) {
            return new RecallDto(source);
        }

        @Override
        public RecallDto[] newArray(int size) {
            return new RecallDto[size];
        }
    };
}
