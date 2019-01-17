package com.example.hl.myweibo.Model;

import java.util.UUID;


public class Comment {
    UUID mCommenID;
    String mUserImgUrl;
    String mCommentDate;
    String mCreatorName;
    String mContent;
    String mCreatorID;
    String mWeiboID;
    String mBycomID;

    public Comment(){
        mCommenID=UUID.randomUUID();
    }

    public String getmBycomID() {
        return mBycomID;
    }

    public void setmBycomID(String mBycomID) {
        this.mBycomID = mBycomID;
    }

    public UUID getmCommenID() {
        return mCommenID;
    }

    public void setmCommenID(UUID mCommenID) {
        this.mCommenID = mCommenID;
    }

    public String getmCreatorName() {
        return mCreatorName;
    }

    public void setmCreatorName(String mCreatorName) {
        this.mCreatorName = mCreatorName;
    }

    public String getmCreatorID() {
        return mCreatorID;
    }

    public void setmCreatorID(String mCreatorID) {
        this.mCreatorID = mCreatorID;
    }

    public String getmWeiboID() {
        return mWeiboID;
    }

    public void setmWeiboID(String mWeiboID) {
        this.mWeiboID = mWeiboID;
    }

    public String getmCommentDate() {
        return mCommentDate;
    }

    public void setmCommentDate(String mCommentDate) {
        this.mCommentDate = mCommentDate;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmUserImgUrl() {
        return mUserImgUrl;
    }

    public void setmUserImgUrl(String mUserImgUrl) {
        this.mUserImgUrl = mUserImgUrl;
    }

    public String getmUserName() {
        return mCreatorName;
    }

    public void setmUserName(String mUserName) {
        this.mCreatorName = mUserName;
    }
}
