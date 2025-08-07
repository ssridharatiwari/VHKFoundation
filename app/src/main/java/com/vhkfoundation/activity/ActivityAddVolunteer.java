package com.vhkfoundation.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActVolunteerBinding;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedList;

public class ActivityAddVolunteer extends AppCompatActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;
    private EditText et_name, et_mail, et_phone, et_address, et_pincode;
    private AppCompatTextView btn_add_volunteer;
    private final EditText[] edTexts = {et_name, et_mail, et_phone, et_address, et_pincode};
    private final String[] edTextsError = {"Enter name", "Enter mail id", "Enter phone number", "Enter address", "Enter pincode"};
    private final int[] editTextsClickId = {R.id.et_name, R.id.et_mail, R.id.et_phone, R.id.et_address, R.id.et_pincode};
    private final View[] allViewWithClick = {btn_add_volunteer};
    private final int[] allViewWithClickId = {R.id.btn_add_volunteer};
    private ActVolunteerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActVolunteerBinding.inflate(getLayoutInflater());
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
        binding.etName.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERNAME, ""));
        binding.etMail.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMAIL, ""));
        binding.etPhone.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMOBILE, ""));
        binding.etMail.setEnabled(false);
        binding.etPhone.setEnabled(false);
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        et_name = editTexts[0];
        et_mail = editTexts[1];
        et_phone = editTexts[2];
        et_address = editTexts[3];
        et_pincode = editTexts[4];
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(v -> {
                if (v.getId() == R.id.btn_add_volunteer) {
                    addMember();
                }
            });
        }
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this, isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
        binding.layActionbar.tvTitle.setText("Add Volunteer");
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.UPDATEUSERDETAILS)) {
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }

    private void addMember() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (response == 0) {

        }
    }
}
