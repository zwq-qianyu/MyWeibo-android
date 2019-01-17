package com.example.hl.myweibo;

import java.util.UUID;


public class HomePageItem {
    private String mPortraitURL;    // 头像地址
    private UUID mId;               // 随机生成的微博ID
    private String mUserName;
    private String mCreateDate;
    private String mContent;
    private String mLikeInf;
    private String mCommentInf;
    private String mWeiboURL;
    private String mUserID;

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getmWeiboURL() {
        return mWeiboURL;
    }

    public void setmWeiboURL(String mWeiboURL) {
        this.mWeiboURL = mWeiboURL;
    }

    public String getWeiboURL() {
        return mWeiboURL;
    }

    public void setWeiboURL(String weiboURL) {
        this.mWeiboURL = weiboURL;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    HomePageItem(){
        mId=UUID.randomUUID();  //随机生成
    }

    public String getmCreateDate() {
        return mCreateDate;
    }

    public void setmCreateDate(String mCreateDate) {
        this.mCreateDate = mCreateDate;
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getmPortraitURL() {
        return mPortraitURL;
    }

    public void setmPortraitURL(String mPortraitURL) {
        this.mPortraitURL = mPortraitURL;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmCommentInf() {
        return mCommentInf;
    }

    public void setmCommentInf(String mCommentInf) {
        this.mCommentInf = mCommentInf;
    }

    public String getmLikeInf() {
        return mLikeInf;
    }

    public void setmLikeInf(String mLikeInf) {
        this.mLikeInf = mLikeInf;
    }
}
