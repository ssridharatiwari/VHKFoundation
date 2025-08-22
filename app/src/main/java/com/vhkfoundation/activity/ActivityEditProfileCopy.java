package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.tabs.TabLayout;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActProfileBinding;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivityEditProfileCopy extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;



    EditText et_name, et_mail, et_phone;

    private final EditText[] edTexts = {et_name, et_mail, et_phone};
    private final String[] edTextsError = {"Enter name", "Enter mail id", "Enter phone number"};
    private final int[] editTextsClickId = {R.id.et_name, R.id.et_mail, R.id.et_phone};

    AppCompatButton choose_aadharfront, remove_aadharfront, choose_pancard, remove_pancard, btn_submit;
    private final View[] allViewWithClick = {choose_aadharfront, remove_aadharfront, choose_pancard, remove_pancard, btn_submit};
    private final int[] allViewWithClickId = {R.id.choose_aadharfront, R.id.remove_aadharfront, R.id.choose_pancard, R.id.remove_pancard, R.id.btn_submit};
    private ImageView imgFrontAadharcard, imgPanCard;

    //ActEditProfileBinding binding;
    ActProfileBinding binding;
    private int selectedImageView = 0;
    private Uri imageFrontAadharCardUri = null, imagePanCardUri = null;
    LinkedList<String> lstUploadData = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        StartApp();
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);

        ViewGroup root = findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        resumeApp();
    }

    private void resumeApp() {
        loadToolBar();
        imgFrontAadharcard = findViewById(R.id.img_frontaadharcard);
        imgPanCard = findViewById(R.id.img_pancard);
        binding.etName.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERNAME, ""));
        binding.etMail.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMAIL, ""));
        binding.etPhone.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMOBILE, ""));

        binding.etMail.setEnabled(false);
        binding.etPhone.setEnabled(false);
        binding.tl.setClickable(false);
//        binding.tl.getTabAt(0).view.setClickable(false);
//        binding.tl.getTabAt(1).view.setClickable(false);
//        binding.tl.getTabAt(0).view.setSelected(false);
//        binding.tl.getTabAt(1).select();

        binding.tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here
                if (tab.getPosition() == 0) {
                    binding.ll1.setVisibility(View.VISIBLE);
                    binding.ll2.setVisibility(View.GONE);
                    binding.ll4.setVisibility(View.GONE);
                    binding.ll5.setVisibility(View.GONE);
                    binding.ll6.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {
                    binding.ll1.setVisibility(View.GONE);
                    binding.ll2.setVisibility(View.VISIBLE);
                    binding.ll4.setVisibility(View.GONE);
                    binding.ll5.setVisibility(View.GONE);
                    binding.ll6.setVisibility(View.GONE);
                } else if (tab.getPosition() == 2) {
                    binding.ll1.setVisibility(View.GONE);
                    binding.ll2.setVisibility(View.GONE);
                    binding.ll4.setVisibility(View.GONE);
                    binding.ll5.setVisibility(View.GONE);
                    binding.ll6.setVisibility(View.GONE);
                } else if (tab.getPosition() == 3) {
                    binding.ll1.setVisibility(View.GONE);
                    binding.ll2.setVisibility(View.GONE);
                    binding.ll4.setVisibility(View.VISIBLE);
                    binding.ll5.setVisibility(View.GONE);
                    binding.ll6.setVisibility(View.GONE);
                } else if (tab.getPosition() == 4) {
                    binding.ll1.setVisibility(View.GONE);
                    binding.ll2.setVisibility(View.GONE);
                    binding.ll4.setVisibility(View.GONE);
                    binding.ll5.setVisibility(View.VISIBLE);
                    binding.ll6.setVisibility(View.GONE);
                } else if (tab.getPosition() == 5) {
                    binding.ll1.setVisibility(View.GONE);
                    binding.ll2.setVisibility(View.GONE);
                    binding.ll4.setVisibility(View.GONE);
                    binding.ll5.setVisibility(View.GONE);
                    binding.ll6.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.radioOccuType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_business) {
                    binding.llBusiness.setVisibility(View.VISIBLE);
                    binding.llSalaried.setVisibility(View.GONE);
                    binding.llSelf.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_salaried) {
                    binding.llBusiness.setVisibility(View.GONE);
                    binding.llSalaried.setVisibility(View.VISIBLE);
                    binding.llSelf.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_self) {
                    binding.llBusiness.setVisibility(View.GONE);
                    binding.llSalaried.setVisibility(View.GONE);
                    binding.llSelf.setVisibility(View.VISIBLE);
                }
            }
        });


//        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ActivityEditProfile.this,ActivityMain.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        binding.tvSkipNow.setVisibility(View.GONE);
        binding.tvSkipNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEditProfileCopy.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }

        et_name = editTexts[0];
        et_mail = editTexts[1];
        et_phone = editTexts[2];

    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent svIntent;
                    switch (v.getId()) {
                        case R.id.choose_aadharfront:
                            selectedImageView = 0;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            startActivity(svIntent);
                            break;

                        case R.id.remove_aadharfront:
                            imgFrontAadharcard.setImageURI(null);
                            imgFrontAadharcard.setVisibility(View.GONE);
                            break;

                        case R.id.choose_pancard:
                            selectedImageView = 2;
                            svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                            startActivity(svIntent);
                            break;

                        case R.id.remove_pancard:
                            imgPanCard.setImageURI(null);
                            imgPanCard.setVisibility(View.GONE);
                            break;

                        case R.id.btn_submit:
                            updateProfile();
                            break;
                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }

    private void updateProfile() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);
        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
            lstUploadData.add(getEditextValue(et_name));
            lstUploadData.add(getEditextValue(et_mail));
            lstUploadData.add(getEditextValue(et_phone));
            callWebService(ApiInterface.UPDATEUSERDETAILS, lstUploadData, true);
        }
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this, isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }


    private String getEditextValue(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return (edTexts[i]).getText().toString().trim();
            }
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            if (selectedImageView == 0) {
                imageFrontAadharCardUri = ActivityBrowseProfileImage.imageUri;
                binding.imgFrontaadharcard.setImageURI(null);
                binding.imgFrontaadharcard.setImageURI(ActivityBrowseProfileImage.imageUri);
                binding.imgFrontaadharcard.setVisibility(View.VISIBLE);
            } else if (selectedImageView == 2) {
                imagePanCardUri = ActivityBrowseProfileImage.imageUri;
                binding.imgPancard.setImageURI(null);
                binding.imgPancard.setImageURI(ActivityBrowseProfileImage.imageUri);
                binding.imgPancard.setVisibility(View.VISIBLE);
            }
            ActivityBrowseProfileImage.imageUri = null;
        }
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.layActionbar.tvTitle.setText("Edit Profile");
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.UPDATEUSERDETAILS)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERNAME, et_name.getText().toString().trim());
                    if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISSHOWPROFILE, false)) {
                        Intent intent = new Intent(ActivityEditProfileCopy.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
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
