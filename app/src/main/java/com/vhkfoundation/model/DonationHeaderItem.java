package com.vhkfoundation.model;

import java.util.List;

public class DonationHeaderItem  {

    private String title;
    private int id;

    public List<DonationListItem> donationListItemList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DonationListItem> getDonationListItemList() {
        return donationListItemList;
    }

    public void setDonationListItemList(List<DonationListItem> donationListItemList) {
        this.donationListItemList = donationListItemList;
    }

    public DonationHeaderItem(String title, int id, List<DonationListItem> donationListItemList) {
        this.title = title;
        this.id = id;
        this.donationListItemList = donationListItemList;
    }
}
