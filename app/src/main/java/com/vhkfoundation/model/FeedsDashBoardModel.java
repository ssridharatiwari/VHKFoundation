package com.vhkfoundation.model;

public class FeedsDashBoardModel {
    String strId,strTitle,strDesc,strEmergency,strDonate,strUserImageUrl,StrDate,strFeedImageUrl,strLike,strComment, strisLike,strCreatedBy;

    public FeedsDashBoardModel(String strId, String strTitle, String strDesc, String strEmergency, String strDonate, String strUserImageUrl, String strDate, String strFeedImageUrl, String strLike, String strComment,String strisLike,String strCreatedBy) {
        this.strId = strId;
        this.strTitle = strTitle;
        this.strDesc = strDesc;
        this.strEmergency = strEmergency;
        this.strDonate = strDonate;
        this.strUserImageUrl = strUserImageUrl;
        StrDate = strDate;
        this.strFeedImageUrl = strFeedImageUrl;
        this.strLike = strLike;
        this.strComment = strComment;
        this.strisLike = strisLike;
        this.strCreatedBy = strCreatedBy;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public String getStrEmergency() {
        return strEmergency;
    }

    public void setStrEmergency(String strEmergency) {
        this.strEmergency = strEmergency;
    }

    public String getStrDonate() {
        return strDonate;
    }

    public void setStrDonate(String strDonate) {
        this.strDonate = strDonate;
    }

    public String getStrUserImageUrl() {
        return strUserImageUrl;
    }

    public void setStrUserImageUrl(String strUserImageUrl) {
        this.strUserImageUrl = strUserImageUrl;
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

    public String getStrisLike() {
        return strisLike;
    }

    public void setStrisLike(String strisLike) {
        this.strisLike = strisLike;
    }

    public String getStrCreatedBy() {
        return strCreatedBy;
    }

    public void setStrCreatedBy(String strCreatedBy) {
        this.strCreatedBy = strCreatedBy;
    }
}
