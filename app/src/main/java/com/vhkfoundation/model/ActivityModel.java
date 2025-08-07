package com.vhkfoundation.model;

public class ActivityModel {
    String strUserImageUrl,strTitle,StrDate,strFeedImageUrl,strLike,strComment;

    public ActivityModel(String strUserImageUrl, String strTitle, String strDate, String strFeedImageUrl, String strLike, String strComment) {
        this.strUserImageUrl = strUserImageUrl;
        this.strTitle = strTitle;
        StrDate = strDate;
        this.strFeedImageUrl = strFeedImageUrl;
        this.strLike = strLike;
        this.strComment = strComment;
    }

    public String getStrUserImageUrl() {
        return strUserImageUrl;
    }

    public void setStrUserImageUrl(String strUserImageUrl) {
        this.strUserImageUrl = strUserImageUrl;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDate() {
        return StrDate;
    }

    public void setStrDate(String strDate) {
        StrDate = strDate;
    }

    public String getStrFeedImageUrl() {
        return strFeedImageUrl;
    }

    public void setStrFeedImageUrl(String strFeedImageUrl) {
        this.strFeedImageUrl = strFeedImageUrl;
    }

    public String getStrLike() {
        return strLike;
    }

    public void setStrLike(String strLike) {
        this.strLike = strLike;
    }

    public String getStrComment() {
        return strComment;
    }

    public void setStrComment(String strComment) {
        this.strComment = strComment;
    }
}
