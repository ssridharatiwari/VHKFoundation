package com.vhkfoundation.model;

public class GeneralItem extends ListItem {
    private NotificationModel pojoOfJsonArray;

    public NotificationModel getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }

    public void setPojoOfJsonArray(NotificationModel pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }


}