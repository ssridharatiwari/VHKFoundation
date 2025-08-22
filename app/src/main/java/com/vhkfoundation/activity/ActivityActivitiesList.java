package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vhkfoundation.R;
import com.vhkfoundation.adapter.ActivityAdapter;
import com.vhkfoundation.adapter.FeedsActivityAdapter;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActActivitiesListBinding;
import com.vhkfoundation.model.ActivityModel;
import com.vhkfoundation.model.FeedsModel;

import java.util.ArrayList;
import java.util.List;

public class ActivityActivitiesList extends BaseActivity {
    private Context svContext;
    private ActActivitiesListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActActivitiesListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StartApp();
    }

    private void StartApp() {
        svContext = this;
        ViewGroup root = findViewById(R.id.headlayout);
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
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
    }

    private void LoadActivityData() {
        List<ActivityModel> lstActivityModels = new ArrayList<>();

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
        ActivityAdapter activityAdapter = new ActivityAdapter(svContext, lstActivityModels, 0);
        binding.rvActivities.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener((view, obj, position) -> {
            Intent intent = new Intent(svContext,ActivityActivitiesDetails.class);
            startActivity(intent);
        });

    }

    private void LoadFeedsData() {
        List<FeedsModel> lstfeedsmodel = new ArrayList<>();
        lstfeedsmodel.add(new FeedsModel("","","","","",""));
        binding.rvFeeds.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvFeeds.setHasFixedSize(true);

        FeedsActivityAdapter feedsActivityAdapter = new FeedsActivityAdapter(svContext, lstfeedsmodel, 0);
        binding.rvFeeds.setAdapter(feedsActivityAdapter);

    }
}
