package com.vhkfoundation.model;

public class DonationItem extends ListItem {
    private DonationModel pojoOfJsonArray;

    public DonationModel getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }

    public void setPojoOfJsonArray(DonationModel pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    public int getType() {
        return TYPE_ITEM;
    }


}