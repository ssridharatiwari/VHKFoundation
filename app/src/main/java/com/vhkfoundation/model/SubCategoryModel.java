package com.vhkfoundation.model;

public class SubCategoryModel {
    public String strMCatId,strCatId,strCatName,strCatImage;

    public SubCategoryModel(String strMCatId,String strCatId, String strCatName, String strCatImage) {
        this.strMCatId = strMCatId;
        this.strCatId = strCatId;
        this.strCatName = strCatName;
        this.strCatImage = strCatImage;
    }

    public String getStrMCatId() {
        return strMCatId;
    }

    public void setStrMCatId(String strMCatId) {
        this.strMCatId = strMCatId;
    }

    public String getStrCatId() {
        return strCatId;
    }

    public void setStrCatId(String strCatId) {
        this.strCatId = strCatId;
    }

    public String getStrCatName() {
        return strCatName;
    }

    public void setStrCatName(String strCatName) {
        this.strCatName = strCatName;
    }

    public String getStrCatImage() {
        return strCatImage;
    }

    public void setStrCatImage(String strCatImage) {
        this.strCatImage = strCatImage;
    }
}
