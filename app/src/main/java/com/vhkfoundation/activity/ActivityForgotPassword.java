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
import android.widget.LinearLayout;
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
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActForgotPasswordBinding;
import com.vhkfoundation.databinding.ActLoginBinding;


public class ActivityForgotPassword extends AppCompatActivity {
    private Context svContext;
    LinearLayout ll_login;
    EditText et_email,et_code,et_new_password,et_confirm_password;
    TextView tv_forgot_password;
    AppCompatCheckBox cb_remember;
    AppCompatButton btn_send_email,btn_send_code,btn_forgot_password;
    ImageView iv_eye_new_pw,iv_eye_confirm;
    LinearLayout ll_email,ll_otp,ll_password,ll_back;

    private EditText[] edTextsMail = {et_email};
    private int[] editTextsMailClickId = {R.id.et_email};

    private EditText[] edTextsCode = {et_code};
    private int[] editTextsCodeClickId = {R.id.et_code};
    private EditText[] edTexts = {et_new_password, et_confirm_password};
    private int[] editTextsClickId = {R.id.et_new_password, R.id.et_confirm_password};

    private int[] allViewWithClickId = {R.id.btn_send_email, R.id.btn_send_code,R.id.btn_forgot_password,R.id.iv_eye_new_pw,R.id.iv_eye_confirm,R.id.ll_back};
    private ShowCustomToast customToast;
    private boolean flagPasswordNew = false;
    private boolean flagPasswordConfirm = false;


    ActForgotPasswordBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.act_login);
        binding = ActForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        OnClickCombineDeclare(allViewWithClickId);
        EditTextDeclareMail(edTextsMail);
        EditTextDeclareCode(edTextsCode);
        EditTextDeclare(edTexts);
        StartApp();
    }


    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (findViewById(allViewWithClickId[j])).setOnClickListener(v -> {
                Intent svIntent = null;
                switch (v.getId()) {
                    case R.id.btn_send_email:
                        sendMail();
                        //Toast.makeText(getApplicationContext(),"Forgot",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.btn_send_code:
                        //Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
                        verifyCode();
                        break;
                    case R.id.btn_forgot_password:
                        //Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
                        forgotPaswordStart();
                        break;
                    case R.id.iv_eye_new_pw:
                        if (!flagPasswordNew) {
                            et_new_password.setInputType(InputType.TYPE_CLASS_TEXT);
                            binding.ivEyeNewPw.setImageResource(R.drawable.ic_password_open);
                            flagPasswordNew = true;
                        } else {
                            et_new_password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                            binding.ivEyeNewPw.setImageResource(R.drawable.ic_password_close);
                            flagPasswordNew = false;
                        }
                        break;
                    case R.id.iv_eye_confirm:
                        if (!flagPasswordConfirm) {
                            et_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
                            binding.ivEyeConfirm.setImageResource(R.drawable.ic_password_open);
                            flagPasswordConfirm = true;
                        } else {
                            et_confirm_password.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                            binding.ivEyeConfirm.setImageResource(R.drawable.ic_password_close);
                            flagPasswordConfirm = false;
                        }
                        break;

                    case R.id.ll_back:
                        finish();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + v.getId());
                }
            });
        }
    }

    private void EditTextDeclareMail(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsMailClickId[j]);
        }
        et_email = (EditText) editTexts[0];
    }

    private void EditTextDeclareCode(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsCodeClickId[j]);
        }
        et_code = (EditText) editTexts[0];
    }
    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        et_new_password = (EditText) editTexts[0];
        et_confirm_password = (EditText) editTexts[1];
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
        binding.llEmail.setVisibility(View.VISIBLE);
        binding.llOtp.setVisibility(View.GONE);
        binding.llPassword.setVisibility(View.GONE);
    }

    private void sendMail(){
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                edTextsMail,
                new String[]{"Enter Your Email"});
        if(response == 0){
            binding.llEmail.setVisibility(View.GONE);
            binding.llOtp.setVisibility(View.VISIBLE);
            binding.llPassword.setVisibility(View.GONE);
        }
    }

    private void verifyCode(){
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                edTextsCode,
                new String[]{"Enter 6 Digit Code"});
        if(response == 0){
            if(et_code.getText().toString().trim().length()<6){
                customToast.showCustomToast(svContext, "Enter 6 Digit Code", customToast.ToastyError);
            } else {
                binding.llEmail.setVisibility(View.GONE);
                binding.llOtp.setVisibility(View.GONE);
                binding.llPassword.setVisibility(View.VISIBLE);
            }

        }
    }

    private void forgotPaswordStart(){
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                edTexts,
                new String[]{"Enter New Password","Enter Confirm Password"});

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

        if(response == 0){
            binding.llEmail.setVisibility(View.GONE);
            binding.llOtp.setVisibility(View.GONE);
            binding.llPassword.setVisibility(View.VISIBLE);
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

//
    private void LoginStart() {
//        int response = 0;
//        response = CheckValidation.emptyEditTextError(
//                edTexts,
//                new String[]{"Enter User ID", "Enter Password"});

//        Intent intent=new Intent(ActivityLogin.this,ActivityProfile.class);
//        startActivity(intent);


        //Toast.makeText(ActivityLogin.this, "Selected Radio Button is : " + strLoginType, Toast.LENGTH_SHORT).show();

//        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERETYPE, strLoginType);
//        Intent intent=new Intent(ActivityLogin.this,ActivityMain.class);
//        startActivity(intent);

    }
}
