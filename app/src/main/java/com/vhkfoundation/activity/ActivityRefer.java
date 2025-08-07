package com.vhkfoundation.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActDonateBinding;
import com.vhkfoundation.databinding.ActReferBinding;

public class ActivityRefer extends AppCompatActivity {
    private Context svContext;
    private ShowCustomToast customToast;



    ActReferBinding binding;
    boolean isCheck_500=false,isCheck_1000=false,isCheck_1500=false,isCheck_2000=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActReferBinding.inflate(getLayoutInflater());
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
    }



    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.layActionbar.tvTitle.setText("Refer Code");
    }


}
