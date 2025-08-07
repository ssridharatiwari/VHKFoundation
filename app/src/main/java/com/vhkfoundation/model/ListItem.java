package com.vhkfoundation.model;

public abstract class ListItem {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL = 1;

    public static final int TYPE_HEADER = 2;

    public static final int TYPE_ITEM = 3;

    abstract public int getType();
}