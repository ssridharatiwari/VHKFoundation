package com.vhkfoundation.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActRegisterBinding;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class ActivityRegister extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    EditText et_name,et_email,et_phone,et_password,et_password2;
    String strReferral="No";

    private EditText[] edTexts = {et_name, et_email,et_phone,et_password,et_password2};
    private int[] editTextsClickId = {R.id.et_name, R.id.et_email,R.id.et_phone,R.id.et_password, R.id.et_password2};
    private int[] allViewWithClickId = {R.id.btn_sign_up,R.id.iv_eye,R.id.iv_eye2,R.id.ll_back,
            R.id.tv_sign_in};
    private ShowCustomToast customToast;
    private boolean flagPassword = false;
    private boolean flagPassword2 = false;


    ActRegisterBinding binding;
    LinkedList<String> lstUploadData = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClickCombineDeclare(allViewWithClickId);
        EditTextDeclare(edTexts);
        StartApp();
    }


    /**
     * Attach a single click listener to a group of view IDs to reduce boilerplate.
     */
    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (findViewById(allViewWithClickId[j])).setOnClickListener(v -> {
                switch (v.getId()) {

                    case R.id.btn_sign_up:
                        hideKeyboard();
                        SignUpStart();

                        break;
                    case R.id.iv_eye:
                        if (!flagPassword) {
                            et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                            binding.ivEye.setImageResource(R.drawable.eye_unhide_img);
                            flagPassword = true;
                        } else {
                            et_password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                            binding.ivEye.setImageResource(R.drawable.eye_hide_img);
                            flagPassword = false;
                        }
                        break;
                        case R.id.iv_eye2:
                        if (!flagPassword2) {
                            et_password2.setInputType(InputType.TYPE_CLASS_TEXT);
                            binding.ivEye2.setImageResource(R.drawable.eye_unhide_img);
                            flagPassword2 = true;
                        } else {
                            et_password2.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                            binding.ivEye2.setImageResource(R.drawable.eye_hide_img);
                            flagPassword2 = false;
                        }
                        break;
                    case R.id.ll_back:
                        finish();
                        break;
                    case R.id.tv_sign_in:
                        finish();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + v.getId());
                }
            });
        }
    }
    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        et_name = (EditText) editTexts[0];
        et_email = (EditText) editTexts[1];
        et_phone = (EditText) editTexts[2];
        et_password = (EditText) editTexts[3];
        et_password2 = (EditText) editTexts[4];
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);

        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        resumeApp();
    }
    public void resumeApp() {

        binding.rgRef.check(R.id.rb_no);
        binding.rgRef.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if(radioButton.getText().equals("Yes")){
                    strReferral = "Yes";
                    binding.llRef.setVisibility(View.VISIBLE);
                } else {
                    strReferral = "No";
                    binding.llRef.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * Validate user inputs and trigger the sign-up web service.
     */
    private void SignUpStart() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                edTexts,
                new String[]{
                        "Enter Your Name", "Enter Your Email Id","Enter Your Phone","Enter Your Password","Enter Your Password"
                });

        if (response == 0) {
            if (!CheckValidation.isPasswordValid(getEditextValue(et_password))) {
                response++;
                et_password.setError("Password must be 8-20 characters and include letters and numbers");
            }
            if (!CheckValidation.isPasswordValid(getEditextValue(et_password2))) {
                response++;
                et_password2.setError("Password must be 8-20 characters and include letters and numbers");
            }
            if (!binding.etPassword.getText().toString().equals(binding.etPassword2.getText().toString())){
                response++;
                binding.etPassword.setError("Enter Same Password");
                binding.etPassword2.setError("Enter Same Password");
            }
        }

        if(response==0){
            if(strReferral.equalsIgnoreCase("Yes")){
                if(TextUtils.isEmpty(binding.etRefCode.getText())){
                    customToast.showCustomToast(svContext, "Please enter refferal code", customToast.ToastyError);
                } else {
                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(getEditextValue(et_name));
                    lstUploadData.add(getEditextValue(et_email));
                    lstUploadData.add(getEditextValue(et_password));
                    lstUploadData.add(getEditextValue(et_phone));
                    lstUploadData.add(getEditextValue(et_password2));
                     lstUploadData.add(getEditextValue(binding.etRefCode));
                    callWebService(ApiInterface.REGISTRATIONAUTH, lstUploadData,true);
                }
            } else {
                binding.etRefCode.setText("");
                lstUploadData = new LinkedList<>();
                lstUploadData.add(getEditextValue(et_name));
                lstUploadData.add(getEditextValue(et_email));
                lstUploadData.add(getEditextValue(et_password));
                lstUploadData.add(getEditextValue(et_phone));
                lstUploadData.add(getEditextValue(et_password2));
                lstUploadData.add(getEditextValue(binding.etRefCode));
                callWebService(ApiInterface.REGISTRATIONAUTH, lstUploadData,true);
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private String getEditextValue(EditText editText) {
        for (int i = 0; i < edTexts.length; i++) {
            if (editText == edTexts[i]) {
                return (edTexts[i]).getText().toString().trim();
            }
        }
        return "";
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this,isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        final String TAG = "WebServiceResponse";

        // Log the raw response and URL
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Raw response: " + result);

        // First check if result is null or empty
        if (result == null || result.trim().isEmpty()) {
            Log.e(TAG, "Empty response from server for URL: " + url);
            customToast.showCustomToast(svContext, "Empty response from server", customToast.ToastyError);
            return;
        }
        JSONObject json = new JSONObject(result);

        if (url.contains(ApiInterface.REGISTRATIONAUTH)) {
            Log.d(TAG, "Processing REGISTRATIONAUTH response");

            try {
                Log.d(TAG, "Parsed JSON: " + json.toString());
                String token = json.getString("token");

                if (token.isEmpty()) {
                    Log.e(TAG, "token is null");
                    customToast.showCustomToast(svContext, "token is null", customToast.ToastyError);
                    return;
                }
                customToast.showCustomToast(svContext, "Please Login", customToast.ToastySuccess);
                finish();
            } catch (JSONException e) {
                Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);

                String str_message = json.getString("message");


                customToast.showCustomToast(svContext, "Error parsing response: " + e.getMessage(), customToast.ToastyError);
                customToast.showCustomToast(svContext, "Error parsing response: " + str_message, customToast.ToastyError);
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
                customToast.showCustomToast(svContext, "Unexpected error: " + e.getMessage(), customToast.ToastyError);
            }
        } else {
            Log.d(TAG, "URL not handled: " + url);
        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }


}
