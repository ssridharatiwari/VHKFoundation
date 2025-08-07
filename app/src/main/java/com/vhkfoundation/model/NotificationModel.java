package com.vhkfoundation.model;

public class NotificationModel {

    String strTitle,strDesc,strDate,strTime;

    public NotificationModel(String strTitle, String strDesc, String strDate, String strTime) {
        this.strTitle = strTitle;
        this.strDesc = strDesc;
        this.strDate = strDate;
        this.strTime = strTime;
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

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
}
