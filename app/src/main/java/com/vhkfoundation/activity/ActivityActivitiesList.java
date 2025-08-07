package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vhkfoundation.R;
import com.vhkfoundation.adapter.ActivityAdapter;
import com.vhkfoundation.adapter.AllCategoryAdapter;
import com.vhkfoundation.adapter.EmergencyHelpAdapter;
import com.vhkfoundation.adapter.FeedsActivityAdapter;
import com.vhkfoundation.adapter.FeedsAdapter;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActActivitiesListBinding;
import com.vhkfoundation.databinding.ActCategoryListBinding;
import com.vhkfoundation.model.ActivityModel;
import com.vhkfoundation.model.CategoryModel;
import com.vhkfoundation.model.EmergencyHelpModel;
import com.vhkfoundation.model.FeedsModel;

import java.util.ArrayList;
import java.util.List;

public class ActivityActivitiesList extends AppCompatActivity {
    private Context svContext;
    private ShowCustomToast customToast;
    ActActivitiesListBinding binding;

    List<ActivityModel> lstActivityModels;
    ActivityAdapter activityAdapter;

    FeedsActivityAdapter feedsActivityAdapter;
    List<FeedsModel> lstfeedsmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActActivitiesListBinding.inflate(getLayoutInflater());
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
        LoadFeedsData();
        LoadActivityData();
        loadToolBar();
    }



    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void LoadActivityData() {
        lstActivityModels = new ArrayList<>();

        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));

        binding.rvActivities.setLayoutManager(new GridLayoutManager(svContext, 3));
        binding.rvActivities.setHasFixedSize(true);
        activityAdapter = new ActivityAdapter(svContext,lstActivityModels,0);
        binding.rvActivities.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ActivityModel obj, int position) {
                Intent intent = new Intent(svContext,ActivityActivitiesDetails.class);
                startActivity(intent);
            }
        });

    }

    private void LoadFeedsData() {
        lstfeedsmodel=new ArrayList<>();
        lstfeedsmodel.add(new FeedsModel("","","","","",""));
        //lstfeedsmodel.add(new FeedsModel("","","","","",""));
        //lstfeedsmodel.add(new FeedsModel("","","","","",""));
        //lstfeedsmodel.add(new FeedsModel("","","","","",""));

        binding.rvFeeds.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvFeeds.setHasFixedSize(true);

        feedsActivityAdapter = new FeedsActivityAdapter(svContext, lstfeedsmodel,0);
        binding.rvFeeds.setAdapter(feedsActivityAdapter);

    }
}
