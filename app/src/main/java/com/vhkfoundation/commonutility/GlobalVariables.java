package com.vhkfoundation.commonutility;

import android.os.Environment;

import java.io.File;

public class GlobalVariables {
    public static final String defaultAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Recharge/";

    public static final String CUSTOMFONTNAME       = "font/font_medium.ttf";
    public static final String CUSTOMFONTNAMEBOLD   = "font/font_bold.ttf";
    public static String PRE_URL_MAIN = "https://vhkfoundation.org/";
    public static String PRE_URL = PRE_URL_MAIN + "api/";
    public static final String CURRENCYSYMBOL = "₹ ";
    public static final boolean ISTESTING = true;
    public static final String TAGPOSTTEXT = ".............tagprint..............";
    public static int isRefresh = 0;
}