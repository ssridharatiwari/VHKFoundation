package com.vhkfoundation.fragment;

import static com.vhkfoundation.activity.ActivitySplash.TAG_TOKEN;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.vhkfoundation.R;
import com.vhkfoundation.activity.ActivityBrowseProfileImage;
import com.vhkfoundation.activity.ActivityChangePassword;
import com.vhkfoundation.activity.ActivityDonationHistory;
import com.vhkfoundation.activity.ActivityEditProfile;
import com.vhkfoundation.activity.ActivityLogin;
import com.vhkfoundation.activity.ActivityMain;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class FragmentProfile extends Fragment implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;
    private View aiView = null;
    CircularImageView iv_user_profile;
    FrameLayout fl_edit;
    TextView tv_user_name,tv_user_id,tv_post_count,tv_donation_count,tv_referral_count;
    Switch swth_notification,swth_volunteer;
    RelativeLayout rl_edit_profile,rl_donation_history,rl_logout,rl_password;
    private final View[] allViewWithClick = {fl_edit,rl_edit_profile,rl_donation_history,rl_logout,swth_notification,rl_password,swth_volunteer};
    private final int[] allViewWithClickId = {R.id.fl_edit,R.id.rl_edit_profile,R.id.rl_donation_history,R.id.rl_logout,R.id.swth_notification,R.id.rl_password,R.id.swth_volunteer};

    private Uri imageDpUri = null;
    LinkedList<String> lstUploadData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_profile, container, false);
        }
        return aiView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resumeApp();
    }
    public void resumeApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        swth_notification = aiView.findViewById(R.id.swth_notification);
        swth_volunteer= aiView.findViewById(R.id.swth_volunteer);
        iv_user_profile=aiView.findViewById(R.id.iv_user_profile);
        tv_user_name = aiView.findViewById(R.id.tv_user_name);
        tv_user_id=aiView.findViewById(R.id.tv_user_id);

        ImageView iv_create_post = ((ActivityMain)getActivity()).findViewById(R.id.iv_create_post);
        LinearLayout ll_image = ((ActivityMain)getActivity()).findViewById(R.id.ll_user_image);
        iv_create_post.setVisibility(View.GONE);
        ll_image.setVisibility(View.GONE);
        //tv_user_name,tv_user_id,tv_post_count,tv_donation_count,tv_referral_count
        OnClickCombineDeclare(allViewWithClick);
        getUserDetails();
    }
    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = aiView.findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (v.getId()) {
                        case R.id.fl_edit:
                            ActivityBrowseProfileImage.openBrowseImageScreen(svContext, true);
                            //customToast.showCustomToast(svContext,"Edit Image", customToast.ToastySuccess);
                            break;
                        case R.id.rl_edit_profile:
                            PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISSHOWPROFILE, false);
                            intent = new Intent(svContext, ActivityEditProfile.class);
                            svContext.startActivity(intent);
                            //customToast.showCustomToast(svContext,"Edit Profile", customToast.ToastySuccess);
                            break;
                        case R.id.rl_donation_history:

                            intent = new Intent(svContext, ActivityDonationHistory.class);
                            svContext.startActivity(intent);
                            //customToast.showCustomToast(svContext,"Donation", customToast.ToastySuccess);
                            break;
                        case R.id.rl_logout:
                            openbottomLogout();
                            //customToast.showCustomToast(svContext,"Logout", customToast.ToastySuccess);
                            break;
                        case R.id.swth_notification:
                            if(swth_notification.isChecked()){
                                customToast.showCustomToast(svContext,"True", customToast.ToastySuccess);
                            } else {
                                customToast.showCustomToast(svContext,"False", customToast.ToastySuccess);
                            }
                            break;

                        case R.id.rl_password:
                            intent = new Intent(svContext, ActivityChangePassword.class);
                            svContext.startActivity(intent);
                            break;
                        case R.id.swth_volunteer:
//                            if(swth_volunteer.isChecked()){
//                                customToast.showCustomToast(svContext,"True", customToast.ToastySuccess);
//                            } else {
//                                customToast.showCustomToast(svContext,"False", customToast.ToastySuccess);
//                            }
                            setVolunteer();
                            break;

                    }
                }
            });
        }

    }

    private void openbottomLogout(){
        BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(svContext, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(svContext).inflate(R.layout.bottom_sheet_dialog_logout, bottomSheetDialog1.findViewById(R.id.BottomSheetContainer));
        bottomSheetDialog1.setContentView(bottomSheetView);
        bottomSheetDialog1.setCancelable(false);
        bottomSheetDialog1.show();

        AppCompatTextView btn_cancel = bottomSheetDialog1.findViewById(R.id.btn_cancel);
        AppCompatTextView btn_logout = bottomSheetDialog1.findViewById(R.id.btn_logout);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.dismiss();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.dismiss();
                Intent intent = new Intent(svContext, ActivityLogin.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            imageDpUri = ActivityBrowseProfileImage.imageUri;
            iv_user_profile.setImageURI(null);
            Glide.with(iv_user_profile.getContext())
                    .load(imageDpUri)
                    .thumbnail(0.5f)
                    .centerCrop()
                    .error(R.drawable.ic_profile_image)
                    .placeholder(R.drawable.loader)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(iv_user_profile);
            ActivityBrowseProfileImage.imageUri=null;
            openbottomSaveImage();
        } else {
            iv_user_profile.setImageResource(R.drawable.ic_profile_image);
        }

        tv_user_name.setText(PreferenceConnector.readString(svContext,PreferenceConnector.USERNAME,""));
        tv_user_id.setText("ID: " + PreferenceConnector.readString(svContext,PreferenceConnector.USERCODE,""));
        if(PreferenceConnector.readString(svContext,PreferenceConnector.ISVOLUNTEER,"").equals("1")){
            swth_volunteer.setChecked(true);
        } else {
            swth_volunteer.setChecked(false);
        }
    }

    private void openbottomSaveImage(){
        BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(svContext, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(svContext).inflate(R.layout.bottom_sheet_dialog_saveimage, bottomSheetDialog1.findViewById(R.id.BottomSheetContainer));
        bottomSheetDialog1.setContentView(bottomSheetView);
        bottomSheetDialog1.setCancelable(false);
        bottomSheetDialog1.show();

        AppCompatTextView btn_cancel = bottomSheetDialog1.findViewById(R.id.btn_cancel);
        AppCompatTextView btn_save = bottomSheetDialog1.findViewById(R.id.btn_save);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_user_profile.setImageResource(R.drawable.ic_profile_image);
                bottomSheetDialog1.hide();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog1.hide();
            }
        });
    }

    private void getUserDetails() {
        lstUploadData = new LinkedList<>();
        lstUploadData.clear();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add("Bearer "+PreferenceConnector.readString(svContext, PreferenceConnector.TOKEN, ""));
        callWebService(ApiInterface.GETUSERDETAILS, lstUploadData, false);
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.SETVOLUNTEER)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if(str_status.equalsIgnoreCase("1")){
                    swth_volunteer.setChecked(true);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }else if (url.contains(ApiInterface.GETUSERDETAILS)) {
            try {
                saveUserDetails(result);
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }
    private void saveUserDetails(String result) throws JSONException {
        if (!TAG_TOKEN.isEmpty())//str_status.equalsIgnoreCase("1"))
        {
            PreferenceConnector.writeString(svContext, PreferenceConnector.USERPERSONADATA, result);
        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }

    private void setVolunteer(){
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext,PreferenceConnector.USERID,""));
        if(swth_volunteer.isChecked()){
            lstUploadData.add("1");
        } else {
            lstUploadData.add("0");
        }
        callWebService(ApiInterface.SETVOLUNTEER, lstUploadData,true);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this,isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }
}
