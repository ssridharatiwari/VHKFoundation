 package com.vhkfoundation.model;

public class CommentsModel {
    public String strId,steUserName,strDate,strComment,strLike,strComments,strIsLike;

    public CommentsModel(String strId, String steUserName, String strDate, String strComment, String strLike, String strComments,String strIsLike) {
        this.strId = strId;
        this.steUserName = steUserName;
        this.strDate = strDate;
        this.strComment = strComment;
        this.strLike = strLike;
        this.strComments = strComments;
        this.strIsLike= strIsLike;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getSteUserName() {
        return steUserName;
    }

    public void setSteUserName(String steUserName) {
        this.steUserName = steUserName;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrComment() {
        return strComment;
    }

    public void setStrComment(String strComment) {
        this.strComment = strComment;
    }

    public String getStrLike() {
        return strLike;
    }

    public void setStrLike(String strLike) {
        this.strLike = strLike;
    }

    public String getStrComments() {
        return strComments;
    }

    public void setStrComments(String strComments) {
        this.strComments = strComments;
    }

    public String getStrIsLike() {
        return strIsLike;
    }

    public void setStrIsLike(String strIsLike) {
        this.strIsLike = strIsLike;
    }
}
