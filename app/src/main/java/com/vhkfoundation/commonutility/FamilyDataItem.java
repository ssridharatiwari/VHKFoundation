package com.vhkfoundation.commonutility;

public class FamilyDataItem {
    private String name;
    private String relation_dob;
    private String relation_mobile;
    private String relation_name;
    private String relation_occupation;

    // Constructor
    public FamilyDataItem(String name, String relation_dob, String relation_mobile, String relation_name, String relation_occupation) {
        this.name = name;
        this.relation_dob = relation_dob;
        this.relation_mobile = relation_mobile;
        this.relation_name = relation_name;
        this.relation_occupation = relation_occupation;
    }

    // Getters and Setters (standard boilerplate for Java)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation_dob() {
        return relation_dob;
    }

    public void setRelation_dob(String relation_dob) {
        this.relation_dob = relation_dob;
    }

    public String getRelation_mobile() {
        return relation_mobile;
    }

    public void setRelation_mobile(String relation_mobile) {
        this.relation_mobile = relation_mobile;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }

    public String getRelation_occupation() {
        return relation_occupation;
    }

    public void setRelation_occupation(String relation_occupation) {
        this.relation_occupation = relation_occupation;
    }

}
