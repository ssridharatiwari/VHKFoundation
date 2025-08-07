package com.vhkfoundation.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.vhkfoundation.R;
import com.vhkfoundation.adapter.ActivityAdapter;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActActivitiesListBinding;
import com.vhkfoundation.databinding.ActActivityDetailsBinding;
import com.vhkfoundation.model.ActivityModel;

import java.util.ArrayList;
import java.util.List;

public class ActivityActivitiesDetails extends AppCompatActivity {
    private Context svContext;
    private ShowCustomToast customToast;
    private ActActivityDetailsBinding binding;
    private List<ActivityModel> lstActivityModels;
    private ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActActivityDetailsBinding.inflate(getLayoutInflater());
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
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
        binding.layActionbar.tvTitle.setText("");
    }
}
