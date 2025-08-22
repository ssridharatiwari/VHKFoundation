package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;


import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActDonateBinding;
import com.vhkfoundation.databinding.ActEditProfileBinding;

public class ActivityDonate extends BaseActivity {
    private Context svContext;
    private ShowCustomToast customToast;
    private ActDonateBinding binding;
    private boolean isCheck_500=false, isCheck_1000=false, isCheck_1500=false, isCheck_2000=false;
    private String isMenu="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActDonateBinding.inflate(getLayoutInflater());
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
        if(getIntent().getExtras()!=null){
            isMenu = getIntent().getStringExtra("isMenu");
        }
        resumeApp();
    }

    private void resumeApp() {
        loadToolBar();
        if(isMenu.equals("0")){
            binding.etName.setEnabled(true);
            binding.etMail.setEnabled(true);
            binding.etPhone.setEnabled(true);
            binding.etAddress.setEnabled(true);
            binding.etName.setText("");
            binding.etMail.setText("");
            binding.etPhone.setText("");
            binding.etAddress.setText("");
        } else {
            binding.etName.setEnabled(false);
            binding.etMail.setEnabled(false);
            binding.etPhone.setEnabled(false);
            binding.etAddress.setEnabled(false);

            binding.etName.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USERNAME, ""));
            binding.etMail.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMAIL, ""));
            binding.etPhone.setText(PreferenceConnector.readString(svContext, PreferenceConnector.USEREMOBILE, ""));
            binding.etAddress.setText("");
        }

        binding.tv500.setOnClickListener(view -> {
            if(isCheck_500){
                isCheck_500 = false;
                binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv500.setTextColor(getColor(R.color.black));
                binding.etAmount.setText("");
                binding.etAmount.setHint("Enter Amount");
                binding.etAmount.setEnabled(true);
            } else {
                isCheck_500 = true;
                isCheck_1000 = false;
                isCheck_1500 = false;
                isCheck_2000 = false;
                binding.etAmount.setText("500");
                binding.etAmount.setEnabled(true);
                binding.tv500.setTextColor(getColor(R.color.white));
                binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange);
                binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange_light);

                binding.tv1000.setTextColor(getColor(R.color.black));
                binding.tv1500.setTextColor(getColor(R.color.black));
                binding.tv2000.setTextColor(getColor(R.color.black));
            }
        });

        binding.tv1000.setOnClickListener(view -> {
            if(isCheck_1000){
                isCheck_1000 = false;
                binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1000.setTextColor(getColor(R.color.black));
                binding.etAmount.setText("");
                binding.etAmount.setHint("Enter Amount");
                binding.etAmount.setEnabled(true);
            } else {
                isCheck_1000 = true;
                isCheck_500 = false;
                isCheck_1500 = false;
                isCheck_2000 = false;
                binding.etAmount.setEnabled(true);
                binding.etAmount.setText("1000");
                binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                binding.tv1000.setTextColor(getColor(R.color.white));
                binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange_light);

                binding.tv500.setTextColor(getColor(R.color.black));
                binding.tv1500.setTextColor(getColor(R.color.black));
                binding.tv2000.setTextColor(getColor(R.color.black));
            }
        });

        binding.tv1500.setOnClickListener(view -> {
            if(isCheck_1500){
                isCheck_1500 = false;
                binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1500.setTextColor(getColor(R.color.black));
                binding.etAmount.setText("");
                binding.etAmount.setHint("Enter Amount");
                binding.etAmount.setEnabled(true);
            } else {
                isCheck_1500 = true;
                isCheck_500 = false;
                isCheck_1000 = false;
                isCheck_2000 = false;
                binding.etAmount.setText("1500");
                binding.etAmount.setEnabled(true);
                binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange);
                binding.tv1500.setTextColor(getColor(R.color.white));
                binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange_light);

                binding.tv500.setTextColor(getColor(R.color.black));
                binding.tv1000.setTextColor(getColor(R.color.black));
                binding.tv2000.setTextColor(getColor(R.color.black));
            }
        });

        binding.tv2000.setOnClickListener(view -> {
            if(isCheck_2000){
                isCheck_2000 = false;
                binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv2000.setTextColor(getColor(R.color.black));
                binding.etAmount.setText("");
                binding.etAmount.setHint("Enter Amount");
                binding.etAmount.setEnabled(true);
            } else {
                isCheck_2000 = true;
                isCheck_500 = false;
                isCheck_1000 = false;
                isCheck_1500 = false;
                binding.etAmount.setText("2000");
                binding.etAmount.setEnabled(true);
                binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange);
                binding.tv2000.setTextColor(getColor(R.color.white));
                binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange_light);

                binding.tv500.setTextColor(getColor(R.color.black));
                binding.tv1000.setTextColor(getColor(R.color.black));
                binding.tv1500.setTextColor(getColor(R.color.black));
            }
        });

        binding.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.etAmount.getText().toString().equals("500")){
                    binding.tv500.setTextColor(getColor(R.color.white));
                    binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange);
                    isCheck_500 = true;
                } else if(binding.etAmount.getText().toString().equals("1000")){
                    binding.tv1000.setTextColor(getColor(R.color.white));
                    binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange);
                    isCheck_1000 = true;
                } else if(binding.etAmount.getText().toString().equals("1500")){
                    binding.tv1500.setTextColor(getColor(R.color.white));
                    binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange);
                    isCheck_1500 = true;
                } else if(binding.etAmount.getText().toString().equals("2000")){
                    binding.tv2000.setTextColor(getColor(R.color.white));
                    binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange);
                    isCheck_2000 = true;
                } else {
                    binding.tv500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                    binding.tv1000.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                    binding.tv1500.setBackgroundResource(R.drawable.bg_rounded_orange_light);
                    binding.tv2000.setBackgroundResource(R.drawable.bg_rounded_orange_light);

                    binding.tv500.setTextColor(getColor(R.color.black));
                    binding.tv1000.setTextColor(getColor(R.color.black));
                    binding.tv1500.setTextColor(getColor(R.color.black));
                    binding.tv2000.setTextColor(getColor(R.color.black));

                    isCheck_1000 = false;
                    isCheck_500 = false;
                    isCheck_1500 = false;
                    isCheck_2000 = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
        binding.layActionbar.tvTitle.setText("Donate");
    }
}
