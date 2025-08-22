package com.vhkfoundation.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.ActivityAdapter;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActActivityDetailsBinding;
import com.vhkfoundation.model.ActivityModel;

import java.util.List;

public class ActivityEventDetails extends BaseActivity {
    private Context svContext;
    private ShowCustomToast customToast;



    ActActivityDetailsBinding binding;

    List<ActivityModel> lstActivityModels;
    ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StartApp();

        Bundle bundle = getIntent().getExtras();

        String title = bundle.getString("title", "N/A");
        String icon_image = bundle.getString("icon_image", "N/A");
        String description = bundle.getString("description", "N/A");
        String date = bundle.getString("date", "N/A");


        binding.tvTitle.setText(title);
        binding.tvDesc.setText(description);
        Glide.with(this)
                .load(icon_image)
                .error(es.dmoral.toasty.R.drawable.ic_error_outline_white_24dp)
                .into( binding.ivPoster);
        binding.tvDate.setText(date);


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
        binding.llOurServices.setVisibility(View.GONE);
    }
    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.layActionbar.tvTitle.setText("Event Details");
    }


}
