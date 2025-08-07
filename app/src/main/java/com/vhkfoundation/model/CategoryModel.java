package com.vhkfoundation.model;

public class CategoryModel {
    public String strCatId,strCatName,strCatImage;

    public CategoryModel(String strCatId, String strCatName, String strCatImage) {
        this.strCatId = strCatId;
        this.strCatName = strCatName;
        this.strCatImage = strCatImage;
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
