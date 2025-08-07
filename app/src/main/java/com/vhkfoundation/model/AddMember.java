package com.vhkfoundation.model;

public class AddMember {

    private String strSNo, strType, strName, strDOB, strMobile,strOccupation;

    public AddMember(String strSNo, String strType, String strName, String strDOB, String strMobile, String strOccupation) {
        this.strSNo = strSNo;
        this.strType = strType;
        this.strName = strName;
        this.strDOB = strDOB;
        this.strMobile = strMobile;
        this.strOccupation = strOccupation;
    }

    public String getStrSNo() {
        return strSNo;
    }

    public void setStrSNo(String strSNo) {
        this.strSNo = strSNo;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDOB() {
        return strDOB;
    }

    public void setStrDOB(String strDOB) {
        this.strDOB = strDOB;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public String getStrOccupation() {
        return strOccupation;
    }

    public void setStrOccupation(String strOccupation) {
        this.strOccupation = strOccupation;
    }
}
