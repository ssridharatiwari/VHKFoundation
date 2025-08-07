package com.vhkfoundation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vhkfoundation.R;
import com.vhkfoundation.adapter.DonationAllItemAdapter;
import com.vhkfoundation.model.DonationHeader;
import com.vhkfoundation.model.DonationItem;
import com.vhkfoundation.model.DonationModel;
import com.vhkfoundation.model.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FragmentDonation11 extends Fragment {
//    private View aiView = null;
//    RecyclerView rv_donate_list;
//
//    List<DonationModel> lstDonationModelsList;
//    DonationAllItemAdapter donationAllItemAdapter;
//    List<ListItem> consolidatedList;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (aiView == null) {
//            aiView = inflater.inflate(R.layout.frag_donation, container, false);
//        }
//        return aiView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        startApp();
//    }
//
//    private void startApp() {
//        rv_donate_list = aiView.findViewById(R.id.rv_donate_list);
//        LoadDataTemp();
//    }
//
//    private void LoadDataTemp() {
//        consolidatedList = new ArrayList<>();
//        lstDonationModelsList = new ArrayList<>();
//        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Emergency Help"));
//        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Food Donation"));
//        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Food Donation"));
//        lstDonationModelsList.add(new DonationModel("", "", "", "","","","","Emergency Help"));
//
//
//        HashMap<String, List<DonationModel>> groupedHashMap = groupDataIntoHashMap(lstDonationModelsList);
//
//        for (String title : groupedHashMap.keySet()) {
//            DonationHeader dateItem = new DonationHeader();
//            dateItem.setTitle(title);
//            consolidatedList.add(dateItem);
//
//            for (DonationModel pojoOfJsonArray : groupedHashMap.get(title)) {
//                DonationItem generalItem = new DonationItem();
//                generalItem.setPojoOfJsonArray(pojoOfJsonArray);//setBookingDataTabs(bookingDataTabs);
//                consolidatedList.add(generalItem);
//            }
//        }
//
//        //rv_donate_list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
//        rv_donate_list.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv_donate_list.setHasFixedSize(true);
//
//        donationAllItemAdapter = new DonationAllItemAdapter(getActivity(), consolidatedList);
//        rv_donate_list.setAdapter(donationAllItemAdapter);
//
//        donationAllItemAdapter.setOnItemClickListener(new DonationAllItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, DonationModel obj, int position) {
//                Toast.makeText(getActivity(), obj.getStrTitle(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
//
//    private HashMap<String, List<DonationModel>> groupDataIntoHashMap(List<DonationModel> listOfPojosOfJsonArray) {
//
//        LinkedHashMap<String, List<DonationModel>> groupedHashMap = new LinkedHashMap<>();
//
//        for (DonationModel pojoOfJsonArray : listOfPojosOfJsonArray) {
//
//            String hashMapKey = pojoOfJsonArray.getStrTitle1();
//
//            if (groupedHashMap.containsKey(hashMapKey)) {
//                // The key is already in the HashMap; add the pojo object
//                // against the existing key.
//                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
//            } else {
//                // The key is not there in the HashMap; create a new key-value pair
//                List<DonationModel> list = new ArrayList<>();
//                list.add(pojoOfJsonArray);
//                groupedHashMap.put(hashMapKey, list);
//            }
//        }
//        return groupedHashMap;
//    }

}
