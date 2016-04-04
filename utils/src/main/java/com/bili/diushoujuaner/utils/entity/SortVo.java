package com.bili.diushoujuaner.utils.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BiLi on 2016/3/19.
 */
public class SortVo implements Parcelable {

    protected String sortLetter;
    protected String displayName;

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sortLetter);
        dest.writeString(this.displayName);
    }

    public SortVo() {
    }

    protected SortVo(Parcel in) {
        this.sortLetter = in.readString();
        this.displayName = in.readString();
    }

}
