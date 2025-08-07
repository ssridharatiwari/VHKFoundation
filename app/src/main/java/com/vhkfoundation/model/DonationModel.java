package com.vhkfoundation.model;

public class DonationModel {
    String strUserImageUrl,strTitle,StrDate,strFeedImageUrl,strLike,strComment,strTarget,strTitle1;

    public DonationModel(String strUserImageUrl, String strTitle, String strDate, String strFeedImageUrl, String strLike, String strComment, String strTarget, String strTitle1) {
        this.strUserImageUrl = strUserImageUrl;
        this.strTitle = strTitle;
        this.StrDate = strDate;
        this.strFeedImageUrl = strFeedImageUrl;
        this.strLike = strLike;
        this.strComment = strComment;
        this.strTarget = strTarget;
        this.strTitle1 = strTitle1;
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

    public String getStrTarget() {
        return strTarget;
    }

    public void setStrTarget(String strTarget) {
        this.strTarget = strTarget;
    }

    public String getStrTitle1() {
        return strTitle1;
    }

    public void setStrTitle1(String strTitle1) {
        this.strTitle1 = strTitle1;
    }
}
