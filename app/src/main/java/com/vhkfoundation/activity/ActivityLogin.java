package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.CheckValidation;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActLoginBinding;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class ActivityLogin extends AppCompatActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    LinearLayout ll_login;
    EditText et_login_id,et_password;
    TextView tv_sign_up,tv_forgot_password;
    AppCompatCheckBox cb_remember;
    AppCompatButton btn_login;
    ImageView iv_eye;

    private EditText[] edTexts = {et_login_id, et_password};
    private int[] editTextsClickId = {R.id.et_login_id, R.id.et_password};
    private int[] allViewWithClickId = {R.id.tv_sign_up,R.id.tv_forgot_password, R.id.btn_login,R.id.iv_eye};
    private ShowCustomToast customToast;
    private boolean flagPassword = false;


    ActLoginBinding binding;

    String strLoginType="Admin";
    LinkedList<String> lstUploadData = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.act_login);
        binding = ActLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClickCombineDeclare(allViewWithClickId);
        EditTextDeclare(edTexts);
        StartApp();
    }


    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (findViewById(allViewWithClickId[j])).setOnClickListener(v -> {
                Intent svIntent = null;
                switch (v.getId()) {

                    case R.id.tv_sign_up:
                        Intent intentSignUp = new Intent(ActivityLogin.this,ActivityRegister.class);
                        startActivity(intentSignUp);
                        break;

                    case R.id.tv_forgot_password:
                        Intent intentForgot = new Intent(ActivityLogin.this,ActivityForgotPassword.class);
                        startActivity(intentForgot);
                        break;
                    case R.id.btn_login:
                        //Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
                        hideKeyboard();
                        LoginStart();
                        break;
                    case R.id.iv_eye:
                        if(!et_password.getText().toString().isEmpty()) {
                            if (!flagPassword) {
                                et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                                binding.ivEye.setImageResource(R.drawable.eye_unhide_img);
                                flagPassword = true;
                            } else {
                                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                binding.ivEye.setImageResource(R.drawable.eye_hide_img);
                                flagPassword = false;
                            }
                        }
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
        et_login_id = (EditText) editTexts[0];
        et_password = (EditText) editTexts[1];
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);

        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        resumeApp();
    }
    public void resumeApp() {}

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void LoginStart() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, new String[]{"Enter Your Email Id/Member Id/Mobile", "Enter password"});
        if (response == 0) {
            lstUploadData = new LinkedList<>();
            lstUploadData.add(getEditextValue(et_login_id));
            lstUploadData.add(getEditextValue(et_password));
            callWebService(ApiInterface.LOGINAUTH, lstUploadData,true);
        }

//
//        Intent intent=new Intent(ActivityLogin.this,ActivityProfile.class);
//        startActivity(intent);


        //Toast.makeText(ActivityLogin.this, "Selected Radio Button is : " + strLoginType, Toast.LENGTH_SHORT).show();

//        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERETYPE, strLoginType);
//        Intent intent=new Intent(ActivityLogin.this,ActivityEditProfile.class);
//        startActivity(intent);

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
        Log.d("url == ",url);
        Log.d("result == ",result);

        JSONObject json = null;
        if (url.contains(ApiInterface.LOGINAUTH)) {
            try {
                json = new JSONObject(result);
               // String str_message = json.getString("message");
               // String str_status = json.getString("status");
                String token = json.getString("token");
                if(!token.isEmpty())//str_status.equalsIgnoreCase("1")  )
                {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.TOKEN, token);

                    ActivitySplash.LoadUserData(result, svContext, true, false);
                } else {
                     String str_message = json.getString("message");
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
