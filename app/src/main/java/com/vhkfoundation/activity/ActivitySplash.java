package com.vhkfoundation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;


import com.google.gson.Gson;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class ActivitySplash extends BaseActivity implements WebServiceListenerRetroFit {
    private FrameLayout frame_layout;
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_STATUS = "status";
    public static final String TAG_TOKEN = "token";
    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkInternet;
    private ProgressBar progreesBar;
    private LinkedList<String> lstUploadData = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        progreesBar = findViewById(R.id.progressbar);
        progreesBar.setVisibility(View.VISIBLE);
        resumeApp();
    }

    public void resumeApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkInternet = new CheckInternet(svContext);

        ViewGroup root = findViewById(R.id.headlayout);
        if (!(GlobalVariables.CUSTOMFONTNAME).equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }

        hideKeyboard();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.AUTOLOGIN, false)) {
                lstUploadData = new LinkedList<>();
                lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
                lstUploadData.add("Bearer "+PreferenceConnector.readString(svContext, PreferenceConnector.TOKEN, ""));
                callWebService(ApiInterface.GETUSERDETAILS, lstUploadData, false);
            } else {

                Intent intent = new Intent(ActivitySplash.this, SlideImageActivity.class);
                progreesBar.setVisibility(View.INVISIBLE);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    public static void LoadUserData(String result, Context svContext) {
        LoadUserData(result, svContext, false, false);
    }

    public static void LoadUserData(String result, Context svContext, boolean isLogin, boolean isLogout) {
        try {
            JSONObject json = new JSONObject(result);
           // String str_status = json.getString(TAG_STATUS);
          //  String str_message = json.getString(TAG_MESSAGE);
            if (!TAG_TOKEN.isEmpty())//str_status.equalsIgnoreCase("1"))
            {
                JSONObject user_detail_obj = null;
                try {
                    user_detail_obj = json.getJSONObject("data");
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERPERSONADATA, result);
                }catch (Exception e){
                    user_detail_obj = json.getJSONObject("user");
                }


               // JSONObject user_detail_obj2 = user_detail_obj.getJSONObject("personal_detail");
              //  int user_detail_obj3 = user_detail_obj2.getInt("user_id");
                /*JSONObject user_detail_obj3 = json.getJSONObject("family_detail");
                JSONObject user_detail_obj4 = json.getJSONObject("occupation_detail");
                JSONObject user_detail_obj5 = json.getJSONObject("bank_detail");
                JSONObject user_detail_obj6 = json.getJSONObject("kyc_detail");*/

              //  Log.d("Login Data : 2 ", String.valueOf(user_detail_obj2));
              //  Log.d("Login Data : 2 ", String.valueOf(user_detail_obj3));
             /*   Log.d("Login Data : 3 ", String.valueOf(user_detail_obj3));
                Log.d("Login Data : 4 ", String.valueOf(user_detail_obj4));
                Log.d("Login Data : 5 ", String.valueOf(user_detail_obj5));
                Log.d("Login Data : 6 ", String.valueOf(user_detail_obj6));*/


                //JSONObject user_detail_obj = json.getJSONObject("user_detail");
                /*String str_user_id = user_detail_obj.getString("userID");
                String str_user_code = user_detail_obj.getString("user_code");
                String str_user_name = user_detail_obj.getString("name");
                String str_user_email = user_detail_obj.getString("email");
                String str_user_mobile = user_detail_obj.getString("mobile");
                String str_is_volunteer = user_detail_obj.getString("is_volunteer");*/
                String str_user_id = user_detail_obj.getString("id");
                String str_user_name = user_detail_obj.getString("name");
                String str_user_email = user_detail_obj.getString("email");
                String str_user_mobile = user_detail_obj.getString("mobile");


                /*PreferenceConnector.writeString(svContext, PreferenceConnector.USERID, str_user_id);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USERCODE, str_user_code);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USERNAME, str_user_name);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USEREMAIL, str_user_email);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USEREMOBILE, str_user_mobile);
                PreferenceConnector.writeString(svContext, PreferenceConnector.ISVOLUNTEER, str_is_volunteer);*/

                PreferenceConnector.writeString(svContext, PreferenceConnector.USERID, str_user_id);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USERNAME, str_user_name);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USEREMAIL, str_user_email);
                PreferenceConnector.writeString(svContext, PreferenceConnector.USEREMOBILE, str_user_mobile);



                if (isLogin) {
                    //Intent intent = new Intent(svContext, ActivityEditProfile.class);
                    Intent intent = new Intent(svContext, ActivityMain.class);
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISSHOWPROFILE, true);
                    svContext.startActivity(intent);
                    ((Activity) svContext).finishAffinity();
                }
                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);

            } else {
                if (!isLogout) {
                    PreferenceConnector.cleanPrefrences(svContext);
                    ShowCustomToast customToast = new ShowCustomToast(svContext);
                    //customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    ActivitySplash.LogoutNow(svContext);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void LogoutNow(Context svContext) {
        PreferenceConnector.cleanPrefrences(svContext);
        if (GlobalVariables.ISTESTING) Log.d("Splash", "Logout Preferences Cleared.");
        Intent svIntent = new Intent(svContext, ActivityLogin.class);
        svIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        svContext.startActivity(svIntent);
        ((Activity) svContext).finishAffinity();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this, isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.GETUSERDETAILS)) {
            try {
                json = new JSONObject(result);
                ActivitySplash.LoadUserData(result, svContext, true, true);
                progreesBar.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }
}
