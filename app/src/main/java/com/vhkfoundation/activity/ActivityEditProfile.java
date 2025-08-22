package com.vhkfoundation.activity;

import static com.vhkfoundation.commonutility.CheckValidation.formatIsoDate;
import static com.vhkfoundation.commonutility.CheckValidation.getGenderRadioSelected;
import static com.vhkfoundation.commonutility.CheckValidation.getOccopationRadioSelected;
import static com.vhkfoundation.commonutility.CheckValidation.getPhysicalRadioSelected;
import static com.vhkfoundation.commonutility.CheckValidation.showToast;
import static com.vhkfoundation.commonutility.GlobalVariables.PRE_URL;
import static com.vhkfoundation.retrofit.ApiInterface.BankDetails;
import static com.vhkfoundation.retrofit.ApiInterface.FAMILYDETAIL;
import static com.vhkfoundation.retrofit.ApiInterface.KycDetails;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.SpinnerPopulateAdapter;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.FamilyDataItem;
import com.vhkfoundation.commonutility.GetFormattedDateTime;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActProfileBinding;
import com.vhkfoundation.model.AddMember;
import com.vhkfoundation.model.StateListModelItem;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditProfile extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;


    String selectedStateId="0";
    String selectedBStateId="0";
    String selectedSalaryMode="0";
    String selectedSalaryModeSelf="0";
    String selectedDistrictId="0";
    String selectedMaritalStatusId="0";
    String selectedQuelificationId="0";
    String selectedMemberTypeId="0";

    Calendar myCalendar;

    EditText et_name, et_mail, et_phone, et_dob, et_dob_member, et_member_name, et_member_mobile, et_member_occupation;
    AppCompatTextView btn_add_member,btn_cancel_tab3,btn_update_tab3;
    ImageView iv_dob, iv_dob_member;
    RelativeLayout rl_tab3_update;
    private final EditText[] edTexts = {et_name, et_mail, et_phone};
    private final String[] edTextsError = {"Enter name", "Enter mail id", "Enter phone number"};
    private final int[] editTextsClickId = {R.id.et_name, R.id.et_mail, R.id.et_phone};

    private final EditText[] edTextsTab3 = {et_member_name,et_dob_member};
    private final String[] edTextsErrorTab3 = {"Enter Member Name", "Enter DOB"};
    private final int[] editTextsClickIdTab3 = {R.id.et_member_name, R.id.et_dob_member};

    AppCompatButton choose_aadharfront, remove_aadharfront, choose_pancard, remove_pancard, btn_submit_tab1, btn_back_tab2, btn_submit_tab2, btn_back_tab3, btn_submit_tab3, btn_back_tab4, btn_submit_tab4, btn_back_tab5, btn_submit_tab5, btn_back_tab6, btn_submit_tab6;
    private final View[] allViewWithClick = {choose_aadharfront, remove_aadharfront, choose_pancard, remove_pancard, btn_submit_tab1, btn_back_tab2, btn_submit_tab2, btn_back_tab3, btn_submit_tab3, btn_back_tab4, btn_submit_tab4, btn_back_tab5, btn_submit_tab5, btn_back_tab6, btn_submit_tab6, et_dob, iv_dob, et_dob_member, iv_dob_member, btn_add_member,btn_cancel_tab3,btn_update_tab3};
    private final int[] allViewWithClickId = {R.id.choose_aadharfront, R.id.remove_aadharfront, R.id.choose_pancard, R.id.remove_pancard, R.id.btn_submit_tab1, R.id.btn_back_tab2, R.id.btn_submit_tab2, R.id.btn_back_tab3, R.id.btn_submit_tab3, R.id.btn_back_tab4, R.id.btn_submit_tab4, R.id.btn_back_tab5, R.id.btn_submit_tab5, R.id.btn_back_tab6, R.id.btn_submit_tab6, R.id.et_dob, R.id.iv_dob, R.id.et_dob_member, R.id.iv_dob_member, R.id.btn_add_member,R.id.btn_cancel_tab3,R.id.btn_update_tab3};
    private ImageView imgFrontAadharcard, imgPanCard;

    //ActEditProfileBinding binding;
    ActProfileBinding binding;
    private int selectedImageView = 0;
    private Uri imageFrontAadharCardUri = null, imagePanCardUri = null;
    LinkedList<String> lstUploadData = new LinkedList<>();

    private List<String> listSpinnerState;
    private List<String> listSpinnerDistrict;
    private List<String> listSpinnerMarital;
    private List<String> listSpinnerQualification;

    private List<String> listSpinnerSalaryMode;
    private List<String> listSpinnerEmploymentType;

    private List<String> listSpinnerMemberType;

    List<AddMember> lstaddmember1 = new ArrayList<>();

    Spinner spinner_member_type;

    boolean isEdit= false;
    int isEditItemPos=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
        EditTextDeclareTab3(edTextsTab3);
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
        myCalendar = Calendar.getInstance();
        try {
            resumeApp();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void resumeApp() throws JSONException {
        loadToolBar();
        imgFrontAadharcard = findViewById(R.id.img_frontaadharcard);
        imgPanCard = findViewById(R.id.img_pancard);
        iv_dob = findViewById(R.id.iv_dob);
        spinner_member_type = findViewById(R.id.spinner_member_type);
        iv_dob_member = findViewById(R.id.iv_dob_member);
        et_member_name = findViewById(R.id.et_member_name);
        et_dob_member = findViewById(R.id.et_dob_member);
        et_member_mobile = findViewById(R.id.et_member_mobile);
        et_member_occupation = findViewById(R.id.et_member_occupation);

        btn_add_member = findViewById(R.id.btn_add_member);
        btn_cancel_tab3= findViewById(R.id.btn_cancel_tab3);
        btn_update_tab3= findViewById(R.id.btn_update_tab3);
        rl_tab3_update = findViewById(R.id.rl_tab3_update);
        rl_tab3_update.setVisibility(View.GONE);
        btn_add_member.setVisibility(View.VISIBLE);


        binding.etUserId.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERCODE, ""));
        binding.etName.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERNAME, ""));
        binding.etMail.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMAIL, ""));
        binding.etPhone.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMOBILE, ""));
        String USERPERSONADATA = PreferenceConnector.readString(svContext, PreferenceConnector.USERPERSONADATA, "");

        JSONObject json = new JSONObject(USERPERSONADATA);
        JSONObject user_detail_obj = json.getJSONObject("data");

        int user_id = user_detail_obj.getInt("id");
        String name = user_detail_obj.getString("name");
        String email = user_detail_obj.getString("email");
        String mobile = user_detail_obj.getString("mobile");
        String ccode = user_detail_obj.getString("ccode");
        String role = user_detail_obj.getString("role");
        String decrypt_password = user_detail_obj.getString("decrypt_password");
        String user_type = user_detail_obj.getString("user_type");
        String verified = user_detail_obj.getString("verified");
        int verified_by = user_detail_obj.getInt("verified_by");
        String state_id = user_detail_obj.getString("state_id");
        String district_id = user_detail_obj.getString("district_id");
        String city_name = user_detail_obj.getString("city_name");
        String pincode = user_detail_obj.getString("pincode");

        binding.etUserId.setText(String.valueOf(user_id));
        binding.etPincode.setText(pincode);
        binding.etCity.setText(city_name);




        JSONObject personal_detail = user_detail_obj.getJSONObject("personal_detail");
        String father_name = personal_detail.getString("father_name");
        String mother_name = personal_detail.getString("mother_name");
        String dob = personal_detail.getString("dob");
        int physically_handicapped = personal_detail.getInt("physically_handicapped");
        int gender = personal_detail.getInt("gender");
        int marital_status = personal_detail.getInt("marital_status");
        String qualification = personal_detail.getString("qualification");
        String alternative_number = personal_detail.getString("alternative_number");

        binding.etFname.setText(father_name);
        binding.etMname.setText(mother_name);


        binding.etDob.setText(formatIsoDate(dob));
        binding.etAltPhone.setText(alternative_number);
        if(gender==1){
            binding.rbMale.setChecked(true);
            binding.rbFemale.setChecked(false);
        }else {
            binding.rbMale.setChecked(false);
            binding.rbFemale.setChecked(true);
        }

        if(physically_handicapped==1){
            binding.rbYes.setChecked(true);
            binding.rbNo.setChecked(false);
        }else {
            binding.rbYes.setChecked(false);
            binding.rbNo.setChecked(true);
        }


        JSONObject family_detail = user_detail_obj.getJSONObject("family_detail");
        String relation_name = family_detail.getString("relation_name");
        String et_member_name = family_detail.getString("name");
        String relation_mobile = family_detail.getString("relation_mobile");
        String relation_dob = family_detail.getString("relation_dob");
        String relation_occupation = family_detail.getString("relation_occupation");

        binding.etMemberName.setText(et_member_name);
        binding.etDobMember.setText(relation_dob);
        binding.etMemberMobile.setText(relation_mobile);
        binding.etMemberOccupation.setText(relation_occupation);



        JSONObject occupation_detail = user_detail_obj.getJSONObject("occupation_detail");
        String occupation_detail_type = occupation_detail.getString("occupation_detail_type");
        String company = occupation_detail.getString("company");
        String nameCompany = occupation_detail.getString("name");
        String department = occupation_detail.getString("department");
        String company_address = occupation_detail.getString("company_address");
        String et_company_city = occupation_detail.getString("city_name");
        //String state_id = occupation_detail.getString("state_id");
        String postal_code = occupation_detail.getString("postal_code");
        String employment_type = occupation_detail.getString("employment_type");
        String monthly_salary = occupation_detail.getString("monthly_salary");
        String office_address = occupation_detail.getString("office_address");
        String designation = occupation_detail.getString("designation");
        String mode_of_salary = occupation_detail.getString("mode_of_salary");

        binding.etBusinessName.setText(company);
        binding.etCompanyDepartement.setText(department);
        binding.etCompanyAddress.setText(company_address);
        binding.etCompanyCity.setText(et_company_city);
        binding.etCompanyPincode.setText(postal_code);
        binding.etCompanyNameSal.setText(nameCompany);
        binding.etEmpTypeSal.setText(employment_type);
        binding.etMonthlyTakeSalary.setText(monthly_salary);
        binding.etOfficeAddress.setText(office_address);
        binding.etDesignationSal.setText(designation);
        binding.etEmpTypeSelf.setText(employment_type);
        binding.etMonthlyTakeSalarySelf.setText(monthly_salary);
        binding.etOfficeAddressSelf.setText(office_address);
        binding.etDesignationSelf.setText(designation);


        JSONObject bank_detail = user_detail_obj.getJSONObject("bank_detail");
        String account_holder_name = bank_detail.getString("account_holder_name");
        String account_number = bank_detail.getString("account_number");
        String ifsc_code = bank_detail.getString("ifsc_code");
        String bank_name = bank_detail.getString("bank_name");

        binding.etAccHolderName.setText(account_holder_name);
        binding.etBankName.setText(bank_name);
        binding.etAccNumber.setText(account_number);
        binding.etIfscCode.setText(ifsc_code);



        JSONObject kyc_detail = user_detail_obj.getJSONObject("kyc_detail");
        String pancard_number = kyc_detail.getString("pancard_number");
        String adhar_number = kyc_detail.getString("adhar_number");
        String proof_of_address = kyc_detail.getString("proof_of_address");

        binding.etPanNumber.setText(pancard_number);
        binding.etAadharNumber.setText(adhar_number);



        binding.etMail.setEnabled(false);
        binding.etPhone.setEnabled(false);
        binding.tl.setClickable(false);
        binding.tl.getTabAt(0).view.setClickable(false);
        binding.tl.getTabAt(1).view.setClickable(false);
        binding.tl.getTabAt(2).view.setClickable(false);
        binding.tl.getTabAt(3).view.setClickable(false);
        binding.tl.getTabAt(4).view.setClickable(false);
        binding.tl.getTabAt(5).view.setClickable(false);
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
                Intent intent = new Intent(ActivityEditProfile.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });

        PopulateSpinnerState();

        PopulateSpinnerMaritial();
        PopulateSpinnerQualification();
        PopulateSpinnerSalaryMode();
        PopulateSpinnerEmploymentType();
        PopulateSpinnerMemberType();

//        lstaddmember1.add(new AddMember("1", "1#:#Father", "Test Father", "10-5-2014", "996541235", "Job"));
//        lstaddmember1.add(new AddMember("2", "2#:#Mother", "Test Mother", "10-5-2014", "996541235", "Job"));
//        lstaddmember1.add(new AddMember("3", "3#:#Brother", "Test Brother", "10-5-2014", "996541235", "Job"));
//        lstaddmember1.add(new AddMember("4", "4#:#Sister", "Test Sister", "10-5-2014", "996541235", "Job"));
//        lstaddmember1.add(new AddMember("5", "5#:#Wife", "Test Wife", "10-5-2014", "996541235", "Job"));
//        lstaddmember1.add(new AddMember("6", "6#:#Daughter", "Test Daughter", "10-5-2014", "996541235", "Job"));
//        lstaddmember1.add(new AddMember("7", "7#:#Son", "Test Son", "10-5-2014", "996541235", "Job"));
//        BindFamilyData(lstaddmember1);
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        et_name = editTexts[0];
        et_mail = editTexts[1];
        et_phone = editTexts[2];
    }

    private void EditTextDeclareTab3(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickIdTab3[j]);
        }
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

                        case R.id.btn_submit_tab1:
                            if (!basicDetails()){
                                break;
                            }
                            //updateProfile();
                            binding.tl.getTabAt(1).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.VISIBLE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(1, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_back_tab2:
                            binding.tl.getTabAt(0).select();
                            binding.ll1.setVisibility(View.VISIBLE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(0, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);

                            break;
                        case R.id.btn_submit_tab2:
                            if (!personalDetails()){
                                break;
                            }
                            binding.tl.getTabAt(2).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.VISIBLE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            //binding.sv1.scrollTo(0, binding.spinnerMemberType.getBottom());
                            binding.tl.setScrollPosition(2, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_back_tab3:
                            binding.tl.getTabAt(1).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.VISIBLE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            //binding.sv1.scrollTo(0, binding.spinnerMemberType.getBottom());
                            binding.tl.setScrollPosition(1, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_submit_tab3:
                            if (!famalyDetails()){
                                break;
                            }
                            binding.tl.getTabAt(3).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.VISIBLE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(3, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_back_tab4:
                            binding.tl.getTabAt(2).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.VISIBLE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(2, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_submit_tab4:
                            if (!occupationDetails()){
                                break;
                            }


                            binding.tl.getTabAt(4).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.VISIBLE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(4, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_back_tab5:
                            binding.tl.getTabAt(3).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.VISIBLE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(3, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;
                        case R.id.btn_submit_tab5:

                            if (!bankDetails()) {
                                break;
                            }


                            binding.tl.getTabAt(5).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.VISIBLE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(5, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;

                            case R.id.btn_submit_tab6:
                            if (!kycDetails()) {
                                break;
                            }
                           /* binding.tl.getTabAt(5).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.GONE);
                            binding.ll6.setVisibility(View.VISIBLE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setScrollPosition(5, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);*/
                            break;
                        case R.id.btn_back_tab6:
                            binding.tl.getTabAt(4).select();
                            binding.ll1.setVisibility(View.GONE);
                            binding.ll2.setVisibility(View.GONE);
                            binding.ll3.setVisibility(View.GONE);
                            binding.ll4.setVisibility(View.GONE);
                            binding.ll5.setVisibility(View.VISIBLE);
                            binding.ll6.setVisibility(View.GONE);
                            //binding.sv1.fullScroll(ScrollView.FOCUS_UP);
                            binding.sv1.scrollTo(0,0);
                            binding.tl.setSmoothScrollingEnabled(true);
                            binding.tl.setScrollPosition(4, 0f, true);
                            binding.tl.setSmoothScrollingEnabled(true);
                            break;

                        case R.id.et_dob:
                            iv_dob.performClick();
                            break;

                        case R.id.iv_dob:
                            openDOB();
                            break;

                        case R.id.et_dob_member:
                            iv_dob_member.performClick();
                            break;

                        case R.id.iv_dob_member:
                            openDOBMember();
                            break;
                        case R.id.btn_add_member:
                            addMember();
                            et_member_name.setText("");
                            et_member_mobile.setText("");
                            et_member_occupation.setText("");
                            spinner_member_type.setSelection(0);
                            et_dob_member.setText("");
                            break;

                        case R.id.btn_cancel_tab3:
                            rl_tab3_update.setVisibility(View.GONE);
                            btn_add_member.setVisibility(View.VISIBLE);
                            isEdit=false;
                            et_member_name.setText("");
                            et_member_mobile.setText("");
                            et_member_occupation.setText("");
                            spinner_member_type.setSelection(0);
                            et_dob_member.setText("");
                            break;
                        case R.id.btn_update_tab3:
                            addMember();

                            break;
                    }
                }
            });
        }
//        btnBack = (Button) allViewWithClick[0];
    }
    EditText[] et_basic;
    EditText[] et_personal;
    EditText[] et_famaly;
    EditText[] et_occopation1;
    EditText[] et_occopation2;
    EditText[] et_occopation3;
    EditText[] et_bank;
    EditText[] et_kyc;
            ;
    RadioButton[] genderRadioButton;
    RadioButton[] occopationRadioButton;
    RadioButton[] physicalRadioButton;

    public boolean basicDetails() {
        et_basic = new EditText[]{binding.etUserId, binding.etName, binding.etPhone, binding.etMail, binding.etPincode, binding.etCity};


        if (selectedStateId.equals("0")) {
            showToast("Please Select State", this);
            return false;
        }

        if (selectedDistrictId.equals("0")) {
            showToast("Please Select District",this);
            return false;
        }


        if (CheckValidation.validation(et_basic)){
            lstUploadData = new LinkedList<>();
            lstUploadData.clear();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
            lstUploadData.add(getEditextValue(binding.etPhone));
            lstUploadData.add(getEditextValue( binding.etMail));
            lstUploadData.add(binding.etPincode.getText().toString().trim());
            lstUploadData.add(selectedStateId);
            lstUploadData.add(selectedDistrictId);
            lstUploadData.add(binding.etCity.getText().toString().trim());

            callWebService(ApiInterface.UPDATEBASICDETAIL, lstUploadData, true);
        }else {
            return false;
        }
        return true;
    }


    public boolean personalDetails() {

        //, binding.etMail ,  , binding.rb_no , binding.spinner_marital_status, binding.spinner_qualification
        et_personal = new EditText[]{binding.etFname, binding.etMname, binding.etDob, binding.etAltPhone};

        genderRadioButton = new RadioButton[]{binding.rbMale,binding.rbFemale,binding.rbOther};

        physicalRadioButton = new RadioButton[]{binding.rbYes,binding.rbNo};

        String genderSelected = getGenderRadioSelected(genderRadioButton);

        String physicalSelected = getPhysicalRadioSelected(physicalRadioButton);


        if (selectedMaritalStatusId.equals("0")) {
            showToast("Please Select Marital Status", this);
            return false;
        }

        if (selectedQuelificationId.equals("0")) {
            showToast("Please Select Qualification",this);
            return false;
        }

        if (CheckValidation.validation(et_personal)){
            lstUploadData = new LinkedList<>();
            lstUploadData.clear();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));

            lstUploadData.add(binding.etFname.getText().toString().trim());
            lstUploadData.add(binding.etMname.getText().toString().trim());
            lstUploadData.add(binding.etDob.getText().toString().trim());
          //  lstUploadData.add("16-12-1996");

            lstUploadData.add(physicalSelected);
            lstUploadData.add(genderSelected);

            lstUploadData.add(selectedMaritalStatusId);
            lstUploadData.add(selectedQuelificationId);

         //   lstUploadData.add(binding.etAltPhone.getText().toString().trim());
            lstUploadData.add(binding.etAltPhone.getText().toString().trim());

            callWebService(ApiInterface.PERSONALDETAIL, lstUploadData, true);
        }else {
            return false;
        }


        return true;

    }

    ArrayList<FamilyDataItem> listFamilyData =new ArrayList<>();
    public Boolean famalyDetails() {
        if (listFamilyData.isEmpty()) {
            Log.d("FamilyDetails", "listFamilyData is empty");
            return false;
        }

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            String userId = PreferenceConnector.readString(svContext, PreferenceConnector.USERID, "");
            Log.d("FamilyDetails", "UserID: " + userId);

            // Build JSON payload
            JsonObject requestJson = new JsonObject();
            requestJson.addProperty("user_id", userId);
            requestJson.add("json_data", new Gson().toJsonTree(listFamilyData));
            String jsonData = new Gson().toJson(requestJson);
            Log.d("FamilyDetails", "Request JSON: " + jsonData);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(jsonData, JSON);

            Request request = new Request.Builder()
                    .url(PRE_URL + FAMILYDETAIL)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("FamilyDetails", "Response Code: " + response.code());
                    Log.d("FamilyDetails", "Response Body: " + responseData);

                    if (response.isSuccessful()) {
                        if (responseData.contains("1")) {
                            Log.d("FamilyDetails", "API Success");
                            // Handle success case
                        } else {
                            Log.d("FamilyDetails", "API returned non-success status");
                            // Handle failure case
                        }
                    } else {
                        Log.e("FamilyDetails", "API Error: HTTP " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    Log.e("FamilyDetails", "API Failure: " + e.getMessage());
                }
            });

            return true;
        } catch (Exception e) {
            Log.e("FamilyDetails", "Unexpected error: " + e.getMessage());
            return false;
        }
    }

    private String formBodyToString(FormBody formBody) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < formBody.size(); i++) {
            sb.append(formBody.name(i))
                    .append("=")
                    .append(formBody.value(i))
                    .append(i < formBody.size() - 1 ? "&" : "");
        }
        return sb.toString();
    }

    public Boolean occupationDetails() {
        Log.d("FamilyDetails", "Start1 ");
        et_occopation1 = new EditText[]{binding.etBusinessName, binding.etCompanyDepartement, binding.etCompanyAddress, binding.etCompanyCity, binding.etCompanyPincode};
        et_occopation2 = new EditText[]{binding.etCompanyNameSal, binding.etEmpTypeSal, binding.etMonthlyTakeSalary, binding.etOfficeAddress, binding.etDesignationSal};
        et_occopation3 = new EditText[]{binding.etEmpTypeSelf, binding.etMonthlyTakeSalarySelf, binding.etOfficeAddressSelf, binding.etDesignationSelf};

        occopationRadioButton = new RadioButton[]{binding.rbBusiness,binding.rbSalaried, binding.rbSelf};
        String occopationSelected = getOccopationRadioSelected(occopationRadioButton);



        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("user_id", PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""))
                .add("occupation_detail_type", String.valueOf(occopationSelected));

        switch (occopationSelected) {
            case "1" -> // Business
            {

                if (selectedBStateId.equals("0")) {
                    showToast("Please Select State", this);
                    return false;
                }



                if (CheckValidation.validation(et_occopation1)) {
                    formBuilder.add("company", binding.etBusinessName.getText().toString().trim())
                            .add("name", binding.etBusinessName.getText().toString().trim())
                            .add("department", binding.etCompanyDepartement.getText().toString().trim())
                            .add("company_address", binding.etCompanyAddress.getText().toString().trim())
                            .add("city_name", binding.etCompanyCity.getText().toString().trim())
                            .add("state_id", selectedBStateId)
                            .add("postal_code", binding.etCompanyPincode.getText().toString().trim());
                } else {
                    return false;
                }
            }
            case "2" -> { // Salaried

                if (selectedSalaryMode.equals("Select Salary Mode")) {
                    showToast("Please Select Salary Mode", this);
                    return false;
                }



                if (CheckValidation.validation(et_occopation2)) {
                    formBuilder.add("employment_type", binding.etEmpTypeSal.getText().toString().trim())
                            .add("monthly_salary", binding.etMonthlyTakeSalary.getText().toString().trim())
                            .add("office_address", binding.etOfficeAddress.getText().toString().trim())
                            .add("designation", binding.etDesignationSal.getText().toString().trim())
                            .add("mode_of_salary", selectedSalaryMode);

                } else {
                    return false;
                }
            }
            case "3" -> {// Self-Employed


                if (selectedSalaryModeSelf.equals("Select Salary Mode")) {
                    showToast("Please Select Salary Mode", this);
                    return false;
                }


                if (CheckValidation.validation(et_occopation3)) {

                formBuilder.add("employment_type", binding.etEmpTypeSelf.getText().toString().trim())
                        .add("monthly_salary", binding.etMonthlyTakeSalarySelf.getText().toString().trim())
                        .add("office_address", binding.etOfficeAddressSelf.getText().toString().trim())
                        .add("designation", binding.etDesignationSelf.getText().toString().trim())
                        .add("mode_of_salary", selectedSalaryModeSelf);

                } else {
                    return false;
                }
            }
            default ->
                    throw new IllegalArgumentException("Invalid occupation type: " + occopationSelected);
        }

        Request request = new Request.Builder()
                .url(PRE_URL + "occupationDetails")
                .post(formBuilder.build())
                .build();
        FormBody formBody = formBuilder.build();
        Log.d("API_DEBUG", "Request Body: " + formBodyToString(formBody));
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("FamilyDetails", "Response Code: " + response.code());
                Log.d("FamilyDetails", "Response Body: " + responseData);

                if (response.isSuccessful()) {
                    if (responseData.contains("1")) {
                        Log.d("FamilyDetails", "API Success");
                        // Handle success case
                    } else {
                        Log.d("FamilyDetails", "API returned non-success status");
                        // Handle failure case
                    }
                } else {
                    Log.e("FamilyDetails", "API Error: HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e("FamilyDetails", "API Failure: " + e.getMessage());
            }
        });
        return true;
       /* }else {
            return false;
        }*/
        //return true;
    }

    public Boolean bankDetails() {

        et_bank = new EditText[]{binding.etAccHolderName, binding.etBankName, binding.etAccNumber, binding.etIfscCode};



        if (CheckValidation.validation(et_bank)){
            lstUploadData = new LinkedList<>();
            lstUploadData.clear();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
            lstUploadData.add(binding.etAccHolderName.getText().toString().trim());
            lstUploadData.add(binding.etAccNumber.getText().toString().trim());
            lstUploadData.add(binding.etIfscCode.getText().toString().trim());
            lstUploadData.add(binding.etBankName.getText().toString().trim());
            callWebService(BankDetails, lstUploadData, true);
        }else {
            return false;
        }
        return true;
    }

    public Boolean kycDetails() {

        et_kyc = new EditText[]{binding.etPanNumber, binding.etAadharNumber, binding.etVoterId};



        if (CheckValidation.validation(et_kyc)){
            lstUploadData = new LinkedList<>();
            lstUploadData.clear();
            lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
            lstUploadData.add(binding.etPanNumber.getText().toString().trim());
            lstUploadData.add(binding.etAadharNumber.getText().toString().trim());
            lstUploadData.add(binding.etVoterId.getText().toString().trim());

            callWebService(KycDetails, lstUploadData, true);
        }else {
            return false;
        }
        return true;
    }


    private void openDOB() {
       /* String txtDate1 = binding.etDob.getText().toString();
        if (binding.etDob.getText().toString().equalsIgnoreCase("")) {
            txtDate1 = GetFormattedDateTime.getcurrentcalDate1();
        }
        String[] strDate1 = txtDate1.split("-");
        new DatePickerDialog(ActivityEditProfile.this, date_dob, Integer.parseInt(strDate1[2]), Integer.parseInt(strDate1[1]) - 1, Integer.parseInt(strDate1[0])).show();*/


       /* showDatePicker(this, new DatePickerCallback() {
            @Override
            public void onDateSelected(String selectedDate) {
                dialogBinding.srDates.setText(selectedDate);
            }
        });*/

        showDatePicker(this,true,"dd/MM/yyyy");

    }




    public  void showDatePicker(Context context, boolean currentBack, String dateFormat) {
        String txtDate1 = binding.etDob.getText().toString();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            binding.etDob.setText( sdf.format(selectedDate.getTime()));


        }, year, month, day);

        if (currentBack) {
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        } else {
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }

        datePickerDialog.show();


    }

    private void openDOBMember() {
        String txtDate1 = binding.etDobMember.getText().toString();
        if (binding.etDobMember.getText().toString().equalsIgnoreCase("")) {
            txtDate1 = GetFormattedDateTime.getcurrentcalDate1();
        }
        String[] strDate1 = txtDate1.split("-");
        new DatePickerDialog(ActivityEditProfile.this, date_dob_member, Integer.parseInt(strDate1[2]), Integer.parseInt(strDate1[1]) - 1, Integer.parseInt(strDate1[0])).show();
    }

    DatePickerDialog.OnDateSetListener date_dob = (view, year, monthOfYear, dayOfMonth) -> {
        int month = monthOfYear + 1;
        String selectedDate = (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth) + "-" + (month >= 10 ? month : "0" + month)
                + "-" + year;
        //String selectedDate = (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth) + "-" + (month >= 10 ? month : "0" + month) + "-" + year;
        binding.etDob.setText(selectedDate);
    };

    DatePickerDialog.OnDateSetListener date_dob_member = (view, year, monthOfYear, dayOfMonth) -> {
        int month = monthOfYear + 1;
        String selectedDate = (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth) + "-" + (month >= 10 ? month : "0" + month)
                + "-" + year;
        //String selectedDate = (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth) + "-" + (month >= 10 ? month : "0" + month) + "-" + year;
        binding.etDobMember.setText(selectedDate);
    };

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

        getStateList();
    }

    private void getStateList() {
        lstUploadData.clear();
        callWebService(ApiInterface.GETSTATES, lstUploadData, true);
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
                        Intent intent = new Intent(ActivityEditProfile.this, ActivityMain.class);
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
        else if (url.contains(ApiInterface.GETSTATES)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    Log.d("State list : ",result);
                    PreferenceConnector.writeString(svContext, PreferenceConnector.STATELIST, result);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
        else if (url.contains(ApiInterface.GETDISTRICTS)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DISTRICTS, result);
                    PopulateSpinnerDistrict();

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
        else if (url.contains(ApiInterface.UPDATEBASICDETAIL)) {
           /* try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DISTRICTS, result);
                    PopulateSpinnerDistrict();

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }*/
        }
        else if (url.contains(ApiInterface.PERSONALDETAIL)) {
           /* try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DISTRICTS, result);
                    PopulateSpinnerDistrict();

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }*/
        }
        else if (url.contains(FAMILYDETAIL)) {
           /* try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DISTRICTS, result);
                    PopulateSpinnerDistrict();

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }*/
        }
        else if (url.contains(BankDetails)) {
           /* try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DISTRICTS, result);
                    PopulateSpinnerDistrict();

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }*/
        } else if (url.contains(KycDetails)) {

            showToast("All Data Saved",getApplicationContext());
           /* try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.DISTRICTS, result);
                    PopulateSpinnerDistrict();

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }*/
        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }

    Type listType = new TypeToken<ArrayList<StateListModelItem>>() {}.getType();

    private void PopulateSpinnerState() {
        listSpinnerState = new ArrayList<>();
        List<StateListModelItem> list = new ArrayList<>();
        try {
            String stateListResult = PreferenceConnector.readString(svContext, PreferenceConnector.STATELIST, "");
            JSONObject json = new JSONObject(stateListResult);
            JSONArray dataArray = json.getJSONArray("data");
            list = new Gson().fromJson(dataArray.toString(), listType);
            listSpinnerState.add("0" + "#:#" + "Select State");
            if (!list.isEmpty()) {
                for (int i=0;i<list.size();i++){
                    listSpinnerState.add(String.valueOf(list.get(i).getId()) + "#:#" + list.get(i).getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        SpinnerPopulateAdapter LegAdapter1 = new SpinnerPopulateAdapter(svContext, listSpinnerState, true);
        binding.spinnerStatelist.setAdapter(LegAdapter1);
        binding.spinnerStatelist.setOnItemSelectedListener(onItemSelectedStateListener);

        SpinnerPopulateAdapter businessstate = new SpinnerPopulateAdapter(svContext, listSpinnerState, true);
        binding.spinnerBusinessState.setAdapter(businessstate);
        binding.spinnerBusinessState.setOnItemSelectedListener(onItemSelectedBStateListener);
    }

    private void PopulateSpinnerDistrict() {
        listSpinnerDistrict = new ArrayList<>();
        List<StateListModelItem> list = new ArrayList<>();
        try {
            String stateListResult = PreferenceConnector.readString(svContext, PreferenceConnector.DISTRICTS, "");
            JSONObject json = new JSONObject(stateListResult);
            JSONArray dataArray = json.getJSONArray("data");
            list = new Gson().fromJson(dataArray.toString(), listType);
            listSpinnerDistrict.add("0" + "#:#" + "Select District");
            if (!list.isEmpty()) {
                for (int i=0;i<list.size();i++){
                    listSpinnerDistrict.add(String.valueOf(list.get(i).getId()) + "#:#" + list.get(i).getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpinnerPopulateAdapter LegAdapter2 = new SpinnerPopulateAdapter(svContext, listSpinnerDistrict, true);
        binding.spinnerDistrictlist.setAdapter(LegAdapter2);
        binding.spinnerDistrictlist.setOnItemSelectedListener(onItemSelectedDistrictListener);
    }

    private void PopulateSpinnerMaritial() {
        listSpinnerMarital = new ArrayList<>();
        listSpinnerMarital.add("0" + "#:#" + "Select Marital Status");
        listSpinnerMarital.add("1" + "#:#" + "Single");
        listSpinnerMarital.add("2" + "#:#" + "Married");
        listSpinnerMarital.add("3" + "#:#" + "Divorced");
        listSpinnerMarital.add("4" + "#:#" + "Widowed");
        SpinnerPopulateAdapter LegAdapter3 = new SpinnerPopulateAdapter(svContext, listSpinnerMarital, true);
        binding.spinnerMaritalStatus.setAdapter(LegAdapter3);
        binding.spinnerMaritalStatus.setOnItemSelectedListener(onItemSelectedMaritialListener);
    }

    private void PopulateSpinnerQualification() {
        listSpinnerQualification = new ArrayList<>();
        listSpinnerQualification.add("0" + "#:#" + "Select Qualification");
        listSpinnerQualification.add("1" + "#:#" + "Primary");
        listSpinnerQualification.add("2" + "#:#" + "Secondary");
        listSpinnerQualification.add("3" + "#:#" + "Senior Secondary");
        listSpinnerQualification.add("4" + "#:#" + "Graduation");
        listSpinnerQualification.add("5" + "#:#" + "Post Graduation");
        SpinnerPopulateAdapter LegAdapter4 = new SpinnerPopulateAdapter(svContext, listSpinnerQualification, true);
        binding.spinnerQualification.setAdapter(LegAdapter4);
        binding.spinnerQualification.setOnItemSelectedListener(onItemSelectedQualificationListener);
    }

    private void PopulateSpinnerSalaryMode() {
        listSpinnerSalaryMode = new ArrayList<>();
        listSpinnerSalaryMode.add("0" + "#:#" + "Select Salary Mode");
        listSpinnerSalaryMode.add("1" + "#:#" + "Cash");
        listSpinnerSalaryMode.add("2" + "#:#" + "Cheque");
        listSpinnerSalaryMode.add("3" + "#:#" + "Bank Deposit");

        SpinnerPopulateAdapter SalaryMode = new SpinnerPopulateAdapter(svContext, listSpinnerSalaryMode, true);
        binding.spinnerSalaryMode.setAdapter(SalaryMode);
        binding.spinnerSalaryMode.setOnItemSelectedListener(onItemSelectedModeOfSalaryListener);

        SpinnerPopulateAdapter SalaryModeSelf = new SpinnerPopulateAdapter(svContext, listSpinnerSalaryMode, true);
        binding.spinnerSalaryModeSelf.setAdapter(SalaryModeSelf);
        binding.spinnerSalaryModeSelf.setOnItemSelectedListener(onItemSelectedModeOfSalarySelfListener);
    }

    private void PopulateSpinnerEmploymentType() {
        listSpinnerEmploymentType = new ArrayList<>();
        listSpinnerEmploymentType.add("0" + "#:#" + "Select Employment Type");
        listSpinnerEmploymentType.add("1" + "#:#" + "Full-time");
        listSpinnerEmploymentType.add("2" + "#:#" + "Part-time");
        listSpinnerEmploymentType.add("3" + "#:#" + "Trainee");
        listSpinnerEmploymentType.add("4" + "#:#" + "Internship");

        SpinnerPopulateAdapter EmploymentType = new SpinnerPopulateAdapter(svContext, listSpinnerEmploymentType, true);
        binding.spinnerEmploymentType.setAdapter(EmploymentType);
        binding.spinnerEmploymentType.setOnItemSelectedListener(onItemSelectedQualificationListener);

        SpinnerPopulateAdapter EmploymentTypeSelf = new SpinnerPopulateAdapter(svContext, listSpinnerEmploymentType, true);
        binding.spinnerEmploymentTypeSelf.setAdapter(EmploymentTypeSelf);
        binding.spinnerEmploymentTypeSelf.setOnItemSelectedListener(onItemSelectedQualificationListener);
    }



    private void PopulateSpinnerMemberType() {
        listSpinnerMemberType = new ArrayList<>();
        listSpinnerMemberType.add("0" + "#:#" + "Select Member Type");
        listSpinnerMemberType.add("1" + "#:#" + "Father");
        listSpinnerMemberType.add("2" + "#:#" + "Mother");
        listSpinnerMemberType.add("3" + "#:#" + "Brother");
        listSpinnerMemberType.add("4" + "#:#" + "Sister");
        listSpinnerMemberType.add("5" + "#:#" + "Wife");
        listSpinnerMemberType.add("6" + "#:#" + "Daughter");
        listSpinnerMemberType.add("7" + "#:#" + "Son");

        SpinnerPopulateAdapter memberType = new SpinnerPopulateAdapter(svContext, listSpinnerMemberType, true);
        binding.spinnerMemberType.setAdapter(memberType);
        binding.spinnerMemberType.setOnItemSelectedListener(onItemSelectedMemberTypeListener);


    }

    AdapterView.OnItemSelectedListener onItemSelectedStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                //textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                //Typeface face = Typeface.createFromAsset(getAssets(), Const.CUSTOMFONTNAME);
                //textView.setTypeface(face);

                //textView.setPadding(15, 0, 0, 0);
                 selectedStateId = listSpinnerState.get(binding.spinnerStatelist.getSelectedItemPosition()).split("#:#")[0];


                lstUploadData.clear();

               try {
                   lstUploadData.add(listSpinnerState.get(position).split("#")[0]);
                   callWebService(ApiInterface.GETDISTRICTS, lstUploadData, true);
               }catch (Exception  e){}

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedBStateListener =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                //textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                //Typeface face = Typeface.createFromAsset(getAssets(), Const.CUSTOMFONTNAME);
                //textView.setTypeface(face);

                //textView.setPadding(15, 0, 0, 0);
                 selectedBStateId = listSpinnerState.get(binding.spinnerStatelist.getSelectedItemPosition()).split("#:#")[0];


                lstUploadData.clear();

               try {
                   lstUploadData.add(listSpinnerState.get(position).split("#")[0]);
                   callWebService(ApiInterface.GETDISTRICTS, lstUploadData, true);
               }catch (Exception  e){}

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedDistrictListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                //textView.setTypeface(textView.getTypeface(),Typeface.BOLD);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                //Typeface face = Typeface.createFromAsset(getAssets(), Const.CUSTOMFONTNAME);
                //textView.setTypeface(face);
                selectedDistrictId = listSpinnerDistrict.get(binding.spinnerDistrictlist.getSelectedItemPosition()).split("#:#")[0];
                //textView.setPadding(15, 0, 0, 0);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedMaritialListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                selectedMaritalStatusId = listSpinnerMarital.get(binding.spinnerMaritalStatus.getSelectedItemPosition()).split("#:#")[0];
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedQualificationListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                selectedQuelificationId = listSpinnerQualification.get(binding.spinnerQualification.getSelectedItemPosition()).split("#:#")[0];
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    AdapterView.OnItemSelectedListener onItemSelectedModeOfSalaryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                selectedSalaryMode = listSpinnerSalaryMode.get(binding.spinnerSalaryMode.getSelectedItemPosition()).split("#:#")[1];
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    AdapterView.OnItemSelectedListener onItemSelectedModeOfSalarySelfListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                selectedSalaryModeSelf = listSpinnerSalaryMode.get(binding.spinnerSalaryModeSelf.getSelectedItemPosition()).split("#:#")[1];
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedMemberTypeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                selectedMemberTypeId = listSpinnerMemberType.get(binding.spinnerMemberType.getSelectedItemPosition()).split("#:#")[1];

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private void BindFamilyData(List<AddMember> lstaddmember) {

        binding.tlContent.removeAllViews();
        if (lstaddmember != null && lstaddmember.size() > 0) {
            for (int i = 0; i < lstaddmember.size(); i++) {
                TableRow tr_data = new TableRow(getApplicationContext());
                ViewGroup.LayoutParams layoutpParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tr_data.setLayoutParams(layoutpParams);
                TextView tv_s_no = new TextView(getApplicationContext());
                tv_s_no.setText(String.valueOf(i + 1));
                tv_s_no.setGravity(Gravity.CENTER_VERTICAL);
                //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                tv_s_no.setTextColor(Color.parseColor("#000000"));
                tv_s_no.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));
                TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_70), (int) getResources().getDimension(R.dimen.dp_30));
                textLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                tv_s_no.setPadding(10, 5, 5, 5);
                tv_s_no.setLayoutParams(textLayoutParams);
                tv_s_no.setTextSize(12);


                TextView tv_type = new TextView(getApplicationContext());
                tv_type.setText(lstaddmember.get(i).getStrType().split("#:#")[1]);
                tv_type.setGravity(Gravity.CENTER_VERTICAL);
                //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                tv_type.setTextColor(Color.parseColor("#000000"));
                tv_type.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));
                TableRow.LayoutParams tv_typeLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_100), (int) getResources().getDimension(R.dimen.dp_30));
                tv_typeLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                tv_type.setPadding(10, 5, 5, 5);
                tv_type.setLayoutParams(tv_typeLayoutParams);
                tv_type.setTextSize(12);


                TextView tv_name = new TextView(getApplicationContext());
                tv_name.setText(lstaddmember.get(i).getStrName());
                tv_name.setGravity(Gravity.CENTER_VERTICAL);
                //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                tv_name.setTextColor(Color.parseColor("#000000"));
                tv_name.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));
                TableRow.LayoutParams tv_nameLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_150), (int) getResources().getDimension(R.dimen.dp_30));
                tv_nameLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                tv_name.setPadding(10, 5, 5, 5);
                tv_name.setLayoutParams(tv_nameLayoutParams);
                tv_name.setTextSize(12);


                TextView tv_dob = new TextView(getApplicationContext());
                tv_dob.setText(lstaddmember.get(i).getStrDOB());
                tv_dob.setGravity(Gravity.CENTER_VERTICAL);
                //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                tv_dob.setTextColor(Color.parseColor("#000000"));
                tv_dob.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));
                TableRow.LayoutParams tv_dobLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_120), (int) getResources().getDimension(R.dimen.dp_30));
                tv_dobLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                tv_dob.setPadding(10, 5, 5, 5);
                tv_dob.setLayoutParams(tv_dobLayoutParams);
                tv_dob.setTextSize(12);


                TextView tv_mobile = new TextView(getApplicationContext());
                tv_mobile.setText(lstaddmember.get(i).getStrMobile());
                tv_mobile.setGravity(Gravity.CENTER_VERTICAL);
                //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                tv_mobile.setTextColor(Color.parseColor("#000000"));
                tv_mobile.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));
                TableRow.LayoutParams tv_mobileLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_120), (int) getResources().getDimension(R.dimen.dp_30));
                tv_mobileLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                tv_mobile.setPadding(10, 5, 5, 5);
                tv_mobile.setLayoutParams(tv_mobileLayoutParams);
                tv_mobile.setTextSize(12);


                TextView tv_occupation = new TextView(getApplicationContext());
                tv_occupation.setText(lstaddmember.get(i).getStrOccupation());
                tv_occupation.setGravity(Gravity.CENTER_VERTICAL);
                //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                tv_occupation.setTextColor(Color.parseColor("#000000"));
                tv_occupation.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));
                TableRow.LayoutParams tv_occupationLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_150), (int) getResources().getDimension(R.dimen.dp_30));
                tv_occupationLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                tv_occupation.setPadding(10, 5, 5, 5);
                tv_occupation.setLayoutParams(tv_occupationLayoutParams);
                tv_occupation.setTextSize(12);


                LinearLayout ll_result = new LinearLayout(getApplicationContext());
                ll_result.setOrientation(LinearLayout.HORIZONTAL);
                TableRow.LayoutParams ll_result_LayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_120), (int) getResources().getDimension(R.dimen.dp_30));
                ll_result_LayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, 0, (int) getResources().getDimension(R.dimen.dp_1));
                ll_result.setLayoutParams(ll_result_LayoutParams);
                ll_result.setPadding(10, 5, 5, 5);
                ll_result.setGravity(Gravity.CENTER_VERTICAL);
                ll_result.setBackgroundColor(ContextCompat.getColor(svContext, R.color.white));

                ImageView iv_delete = new ImageView(getApplicationContext());
                iv_delete.setImageResource(R.drawable.icon_delete);
                iv_delete.setTag("btnDel_" + i);
                TableRow.LayoutParams iv_deleteLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_40), (int) getResources().getDimension(R.dimen.dp_30));
                //iv_deleteLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, (int) getResources().getDimension(R.dimen.dp_1), (int) getResources().getDimension(R.dimen.dp_1));
                iv_delete.setPadding(10, 5, 5, 5);
                iv_delete.setLayoutParams(iv_deleteLayoutParams);
                ll_result.addView(iv_delete);

                ImageView iv_edit = new ImageView(getApplicationContext());
                iv_edit.setImageResource(R.drawable.ic_edit);
                iv_edit.setTag("btnEdit_" + i);
                TableRow.LayoutParams iv_editLayoutParams = new TableRow.LayoutParams((int) getResources().getDimension(R.dimen.dp_40), (int) getResources().getDimension(R.dimen.dp_30));
                //iv_editLayoutParams.setMargins((int) getResources().getDimension(R.dimen.dp_1), 0, (int) getResources().getDimension(R.dimen.dp_1), (int) getResources().getDimension(R.dimen.dp_1));
                iv_edit.setPadding(5, 5, 5, 5);
                iv_edit.setLayoutParams(iv_editLayoutParams);
                ll_result.addView(iv_edit);

                tr_data.addView(tv_s_no);
                tr_data.addView(tv_type);
                tr_data.addView(tv_name);
                tr_data.addView(tv_dob);
                tr_data.addView(tv_mobile);
                tr_data.addView(tv_occupation);
                tr_data.addView(ll_result);

                listFamilyData.add(
                    new FamilyDataItem(
                        tv_name.getText().toString(),
                        tv_dob.getText().toString(),
                        tv_mobile.getText().toString(),
                        tv_type.getText().toString(),
                        tv_occupation.getText().toString()
                    )
                );

                binding.tlContent.addView(tr_data);


                iv_delete.setOnClickListener(v -> {
                    isEdit=false;
                    int pos = 0;
                    //int str = Integer.parseInt(strtag.split("btnadd")[1]);
                    pos = Integer.parseInt(v.getTag().toString().split("btnDel_")[1]);
                    //topicDataList.get(str-1).remove(pos);
                    //customeAddDynamicLayout1(linearlayout,topicDataList.get(str-1),strtag);
                    //Toast.makeText(getApplicationContext(), iv_delete.getTag().toString(), Toast.LENGTH_LONG).show();
                    lstaddmember.remove(pos);
                    List<AddMember> lsttemp = new ArrayList<>();
                    lsttemp = lstaddmember;
                    BindFamilyData(lsttemp);
                });

                iv_edit.setOnClickListener(v -> {
                    isEdit = true;
                    int pos = 0;
                    pos = Integer.parseInt(v.getTag().toString().split("btnEdit_")[1]);
                    isEditItemPos= Integer.parseInt(v.getTag().toString().split("btnEdit_")[1]);
                    //Toast.makeText(getApplicationContext(), iv_edit.getTag().toString(), Toast.LENGTH_LONG).show();
//                    lstaddmember.remove(pos);
//                    List<AddMember> lsttemp = new ArrayList<>();
//                    lsttemp = lstaddmember;
//                    BindFamilyData(lsttemp);

                    int pos1 = listSpinnerMemberType.indexOf(lstaddmember.get(pos).getStrType());
                    spinner_member_type.setSelection(pos1);
                    et_member_name.setText(lstaddmember.get(pos).getStrName());
                    et_dob_member.setText(lstaddmember.get(pos).getStrDOB());
                    et_member_mobile.setText(lstaddmember.get(pos).getStrMobile());
                    et_member_occupation.setText(lstaddmember.get(pos).getStrOccupation());
                    rl_tab3_update.setVisibility(View.VISIBLE);
                    btn_add_member.setVisibility(View.GONE);
                });

            }
        }

    }

    private void addMember() {

        int response = 0;
        response = CheckValidation.emptyEditTextError(edTextsTab3, edTextsErrorTab3);

        if(spinner_member_type.getSelectedItemPosition()==0){
            response++;
            customToast.showCustomToast(svContext, "Please select member type", customToast.ToastyError);
        }

        if (response == 0) {
            if(isEdit){
                lstaddmember1.remove(isEditItemPos);
                lstaddmember1.add(isEditItemPos,new AddMember("1", listSpinnerMemberType.get(binding.spinnerMemberType.getSelectedItemPosition()), et_member_name.getText().toString().trim(), et_dob_member.getText().toString().trim(), et_member_mobile.getText().toString().trim(), et_member_occupation.getText().toString().trim()));
                et_member_name.setText("");
                et_member_mobile.setText("");
                et_member_occupation.setText("");
                spinner_member_type.setSelection(0);
                et_dob_member.setText("");
                rl_tab3_update.setVisibility(View.GONE);
                btn_add_member.setVisibility(View.VISIBLE);
                isEdit=false;
            } else {
                lstaddmember1.add(new AddMember("1", listSpinnerMemberType.get(binding.spinnerMemberType.getSelectedItemPosition()), et_member_name.getText().toString().trim(), et_dob_member.getText().toString().trim(), et_member_mobile.getText().toString().trim(), et_member_occupation.getText().toString().trim()));
            }

            BindFamilyData(lstaddmember1);
            isEdit = false;
        }


    }
}
