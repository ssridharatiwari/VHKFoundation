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
import com.vhkfoundation.adapter.AllSubCategoryAdapter;
import com.vhkfoundation.adapter.EmergencyHelpAdapter;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActCategoryListBinding;
import com.vhkfoundation.model.CategoryModel;
import com.vhkfoundation.model.EmergencyHelpModel;
import com.vhkfoundation.model.SubCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubCategoryList extends AppCompatActivity {
    private Context svContext;
    private ShowCustomToast customToast;



    ActCategoryListBinding binding;

    List<SubCategoryModel> lstsubCategoryModel;
    AllSubCategoryAdapter allsubCategoryAdapter;

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
        loadToolBar();
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.layActionbar.tvTitle.setText("Campaigns");
    }

    private void LoadCategoryData() {
        lstsubCategoryModel = new ArrayList<>();

        lstsubCategoryModel.add(new SubCategoryModel("1","6","Foods",String.valueOf(R.drawable.ic_food)));
        lstsubCategoryModel.add(new SubCategoryModel("1","7","Environment",String.valueOf(R.drawable.ic_environment)));
        lstsubCategoryModel.add(new SubCategoryModel("1","8","Humanity",String.valueOf(R.drawable.ic_humanity)));
        lstsubCategoryModel.add(new SubCategoryModel("1","9","Health",String.valueOf(R.drawable.ic_health)));

        binding.rvCategory.setLayoutManager(new GridLayoutManager(svContext, 3));
        binding.rvCategory.setHasFixedSize(true);
        allsubCategoryAdapter = new AllSubCategoryAdapter(svContext,lstsubCategoryModel,0);
        binding.rvCategory.setAdapter(allsubCategoryAdapter);

        allsubCategoryAdapter.setOnItemClickListener(new AllSubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SubCategoryModel obj, int position) {
                String strCatId="";
                if(obj.getStrCatName().equals("Foods")){
                    strCatId="1";
                } else if(obj.getStrCatName().equals("Environment")){
                    strCatId="2";
                } else if(obj.getStrCatName().equals("Humanity")){
                    strCatId="3";
                }else if(obj.getStrCatName().equals("Health")){
                    strCatId="4";
                }

                Intent intent = new Intent(svContext,ActivityCatWiseDonationList.class);
                intent.putExtra("CatId",strCatId);
                intent.putExtra("CatName",obj.getStrCatName());
                svContext.startActivity(intent);
                //customToast.showCustomToast(svContext, "Item Click " + obj.getStrCatName(), customToast.ToastyError);
            }
        });

    }
}
