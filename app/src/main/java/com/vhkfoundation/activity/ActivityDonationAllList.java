package com.vhkfoundation.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vhkfoundation.adapter.DonationAllItemAdapter;
import com.vhkfoundation.adapter.NotificationAdapter;
import com.vhkfoundation.databinding.ActDonationAllListBinding;
import com.vhkfoundation.databinding.ActNotificationBinding;
import com.vhkfoundation.model.DateItem;
import com.vhkfoundation.model.DonationHeader;
import com.vhkfoundation.model.DonationItem;
import com.vhkfoundation.model.DonationModel;
import com.vhkfoundation.model.GeneralItem;
import com.vhkfoundation.model.ListItem;
import com.vhkfoundation.model.NotificationModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ActivityDonationAllList extends BaseActivity {
    private Context svContext;
    LinearLayout ll_back;
    RecyclerView rv_notifiction;
    List<DonationModel> lstDonationModelsList;
    String[] headerItem;
    DonationAllItemAdapter donationAllItemAdapter;
    List<ListItem> consolidatedList;
    ActDonationAllListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActDonationAllListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StartApp();
    }

    private void StartApp() {
        svContext = this;
        initToolbar();
        resumeApp();
    }

    private void resumeApp() {
        LoadDataTemp();
    }


    private void LoadDataTemp() {
        consolidatedList = new ArrayList<>();
        lstDonationModelsList = new ArrayList<>();
        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Emergency Help"));
        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Food Donation"));
        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Food Donation"));
        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Emergency Help"));


        HashMap<String, List<DonationModel>> groupedHashMap = groupDataIntoHashMap(lstDonationModelsList);

        for (String title : groupedHashMap.keySet()) {
            DonationHeader dateItem = new DonationHeader();
            dateItem.setTitle(title);
            consolidatedList.add(dateItem);

            for (DonationModel pojoOfJsonArray : groupedHashMap.get(dateItem)) {
                DonationItem generalItem = new DonationItem();
                generalItem.setPojoOfJsonArray(pojoOfJsonArray);
                consolidatedList.add(generalItem);
            }
        }

        binding.rvDonateList.setLayoutManager(new LinearLayoutManager(svContext,LinearLayoutManager.HORIZONTAL, false));
        binding.rvDonateList.setHasFixedSize(true);

        donationAllItemAdapter = new DonationAllItemAdapter(svContext, consolidatedList);
        binding.rvDonateList.setAdapter(donationAllItemAdapter);

        donationAllItemAdapter.setOnItemClickListener((view, obj, position) -> Toast.makeText(getApplicationContext(), obj.getStrTitle(), Toast.LENGTH_LONG).show());
    }

    private HashMap<String, List<DonationModel>> groupDataIntoHashMap(List<DonationModel> listOfPojosOfJsonArray) {
        LinkedHashMap<String, List<DonationModel>> groupedHashMap = new LinkedHashMap<>();
        for (DonationModel pojoOfJsonArray : listOfPojosOfJsonArray) {
            String hashMapKey = pojoOfJsonArray.getStrTitle1();
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<DonationModel> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    private void initToolbar() {
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
        binding.layActionbar.tvTitle.setText("Donation");
    }
}
