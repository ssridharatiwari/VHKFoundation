package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActChangePasswordBinding;
import com.vhkfoundation.databinding.ActEditProfileBinding;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivityChangePassword extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;
    private AppCompatButton btn_update;
    private ImageView iv_old_eye,iv_new_eye,iv_confirm_eye;
    private ActChangePasswordBinding binding;
    private EditText et_old_password,et_new_password,et_confirm_password;

    private final EditText[] edTexts = {et_old_password,et_new_password, et_confirm_password};
    private final String[] edTextsError = {"Enter existing password", "Enter new password", "Enter confirm password"};
    private final int[] editTextsClickId = {R.id.et_old_password,R.id.et_new_password, R.id.et_confirm_password};
    private final View[] allViewWithClick = {iv_old_eye,iv_new_eye,iv_confirm_eye,btn_update};
    private final int[] allViewWithClickId = {R.id.iv_old_eye,R.id.iv_new_eye,R.id.iv_confirm_eye,R.id.btn_update};

    private boolean flagPasswordOld = false;
    private boolean flagPasswordNew = false;
    private boolean flagPasswordConfirm = false;

    LinkedList<String> lstUploadData = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StartApp();
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

    private void resumeApp() {
        loadToolBar();
        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
    }
    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        et_old_password= editTexts[0];
        et_new_password = editTexts[1];
        et_confirm_password = editTexts[2];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(v -> {
                Intent intent;
                switch (v.getId()) {
                    case R.id.iv_old_eye:
                        if(!et_old_password.getText().toString().isEmpty()) {
                            if (!flagPasswordOld) {
                                et_old_password.setInputType(InputType.TYPE_CLASS_TEXT);
                                binding.ivOldEye.setImageResource(R.drawable.eye_unhide_img);
                                flagPasswordOld = true;
                            } else {
                                et_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.ivOldEye.setImageResource(R.drawable.eye_hide_img);
                                flagPasswordOld = false;
                            }
                        }
                        break;
                    case R.id.iv_new_eye:
                        if(!et_new_password.getText().toString().isEmpty()) {
                            if (!flagPasswordNew) {
                                et_new_password.setInputType(InputType.TYPE_CLASS_TEXT);
                                binding.ivNewEye.setImageResource(R.drawable.eye_unhide_img);
                                flagPasswordNew = true;
                            } else {
                                et_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.ivNewEye.setImageResource(R.drawable.eye_hide_img);
                                flagPasswordNew = false;
                            }
                        }
                        break;
                    case R.id.iv_confirm_eye:
                        if(!et_confirm_password.getText().toString().isEmpty()) {
                            if (!flagPasswordConfirm) {
                                et_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
                                binding.ivConfirmEye.setImageResource(R.drawable.eye_unhide_img);
                                flagPasswordConfirm = true;
                            } else {
                                et_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.ivConfirmEye.setImageResource(R.drawable.eye_hide_img);
                                flagPasswordConfirm = false;
                            }
                        }
                        break;

                    case R.id.btn_update:
                        updatePassword();
                        break;
                }
            });
        }

    }

    private void updatePassword(){
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);
        if (response == 0) {
            if (!CheckValidation.isPasswordValid(getEditextValue(et_new_password))) {
                response++;
                et_new_password.setError("Password must be 8-20 characters and include letters and numbers");
            }
            if (!CheckValidation.isPasswordValid(getEditextValue(et_confirm_password))) {
                response++;
                et_confirm_password.setError("Password must be 8-20 characters and include letters and numbers");
            }
            if (!getEditextValue(et_new_password).equals(getEditextValue(et_confirm_password))) {
                response++;
                customToast.showCustomToast(svContext, "Password not matching", customToast.ToastyError);
            }
        }
        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(PreferenceConnector.readString(svContext,PreferenceConnector.USERID,""));
            lstUploadData.add(getEditextValue(et_old_password));
            lstUploadData.add(getEditextValue(et_new_password));
            lstUploadData.add(getEditextValue(et_confirm_password));
            callWebService(ApiInterface.CHANGEPASSWORDAUTH, lstUploadData,true);
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

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(view -> finish());

        binding.layActionbar.tvTitle.setText("Change Password");
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this,isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }


    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.CHANGEPASSWORDAUTH)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastySuccess);
                    finish();
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
