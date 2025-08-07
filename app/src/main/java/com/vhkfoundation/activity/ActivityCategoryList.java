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
import com.vhkfoundation.adapter.AllCategoryAdapter;
import com.vhkfoundation.adapter.EmergencyHelpAdapter;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActCategoryListBinding;
import com.vhkfoundation.databinding.ActForgotPasswordBinding;
import com.vhkfoundation.model.CategoryModel;
import com.vhkfoundation.model.EmergencyHelpModel;

import java.util.ArrayList;
import java.util.List;

public class ActivityCategoryList extends AppCompatActivity {
    private Context svContext;
    private ShowCustomToast customToast;
    private ActCategoryListBinding binding;
    private List<CategoryModel> lstCategoryModel;
    private List<EmergencyHelpModel> lstEmergencyHelpModel;
    private AllCategoryAdapter allCategoryAdapter;
    private EmergencyHelpAdapter emergencyHelpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActCategoryListBinding.inflate(getLayoutInflater());
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
        LoadCategoryData();
        LoadEmergencyData();
        loadToolBar();
    }

    private void LoadEmergencyData() {
        lstEmergencyHelpModel = new ArrayList<>();
        lstEmergencyHelpModel.add(new EmergencyHelpModel("","","","","","",""));
        lstEmergencyHelpModel.add(new EmergencyHelpModel("","","","","","",""));
        lstEmergencyHelpModel.add(new EmergencyHelpModel("","","","","","",""));
        binding.rvEmergency.setLayoutManager(new LinearLayoutManager(svContext,LinearLayoutManager.HORIZONTAL, true));
        binding.rvEmergency.setHasFixedSize(true);
        emergencyHelpAdapter = new EmergencyHelpAdapter(svContext,lstEmergencyHelpModel,0);
        binding.rvEmergency.setAdapter(emergencyHelpAdapter);
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.layActionbar.tvTitle.setText("Category List");
    }

    private void LoadCategoryData() {
        lstCategoryModel = new ArrayList<>();
        lstCategoryModel.add(new CategoryModel("1","Campaigns",String.valueOf(R.drawable.ic_campaigns)));
        lstCategoryModel.add(new CategoryModel("2","Donate",String.valueOf(R.drawable.ic_donate)));
        lstCategoryModel.add(new CategoryModel("3","Charity",String.valueOf(R.drawable.ic_charity_category)));

        binding.rvCategory.setLayoutManager(new GridLayoutManager(svContext, 3));
        binding.rvCategory.setHasFixedSize(true);
        allCategoryAdapter = new AllCategoryAdapter(svContext,lstCategoryModel,0);
        binding.rvCategory.setAdapter(allCategoryAdapter);

        allCategoryAdapter.setOnItemClickListener(new AllCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CategoryModel obj, int position) {
                if(obj.strCatName.equalsIgnoreCase("Campaigns")){
                    Intent intent = new Intent(svContext,ActivitySubCategoryList.class);
                    startActivity(intent);
                } else if(obj.strCatName.equalsIgnoreCase("Donate")){
                    Intent intent = new Intent(svContext,ActivityDonate.class);
                    startActivity(intent);
                }
            }
        });

    }
}
