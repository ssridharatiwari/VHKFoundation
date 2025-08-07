package com.vhkfoundation.model;

public class DonationListItem {

    String strId,strUserImageUrl,strTitle,StrDate,strFeedImageUrl,strLike,strComment,strTarget,strDesc,strCatid,strCreatedBy,strTotalLike,strCatName;

    public DonationListItem(String strId,String strUserImageUrl, String strTitle, String strDate, String strFeedImageUrl, String strLike, String strComment,String strTarget,String strDesc,String strCatid,String strCreatedBy,String strTotalLike,String strCatName) {
        this.strId = strId;
        this.strUserImageUrl = strUserImageUrl;
        this.strTitle = strTitle;
        this.StrDate = strDate;
        this.strFeedImageUrl = strFeedImageUrl;
        this.strLike = strLike;
        this.strComment = strComment;
        this.strTarget = strTarget;
        this.strDesc = strDesc;
        this.strCatid = strCatid;
        this.strCreatedBy = strCreatedBy;
        this.strTotalLike = strTotalLike;
        this.strCatName = strCatName;

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

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public String getStrCatid() {
        return strCatid;
    }

    public void setStrCatid(String strCatid) {
        this.strCatid = strCatid;
    }

    public String getStrCreatedBy() {
        return strCreatedBy;
    }

    public void setStrCreatedBy(String strCreatedBy) {
        this.strCreatedBy = strCreatedBy;
    }

    public String getStrTotalLike() {
        return strTotalLike;
    }

    public void setStrTotalLike(String strTotalLike) {
        this.strTotalLike = strTotalLike;
    }

    public String getStrCatName() {
        return strCatName;
    }

    public void setStrCatName(String strCatName) {
        this.strCatName = strCatName;
    }
}