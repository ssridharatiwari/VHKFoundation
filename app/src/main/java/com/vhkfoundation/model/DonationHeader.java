package com.vhkfoundation.model;

public class DonationHeader extends ListItem {

    private String title;

//    public DonationHeader(String title) {
//        this.title = title;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
