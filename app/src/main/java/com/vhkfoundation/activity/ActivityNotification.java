package com.vhkfoundation.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vhkfoundation.R;
import com.vhkfoundation.adapter.NotificationAdapter;
import com.vhkfoundation.databinding.ActDonationHistoryListBinding;
import com.vhkfoundation.databinding.ActNotificationBinding;
import com.vhkfoundation.model.DateItem;
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

public class ActivityNotification extends AppCompatActivity {
    private Context svContext;
    LinearLayout ll_back;
    RecyclerView rv_notifiction;
    List<NotificationModel> lstNotificationModelList;
    String[] headerItem;
    NotificationAdapter notificationAdapter;
    List<ListItem> consolidatedList;
    ActNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StartApp();
    }

    private void StartApp() {
        svContext = this;
        initToolbar();
        resumeApp();
    }

    private void resumeApp() {
        //rv_notifiction=(RecyclerView) findViewById(R.id.rv_notifiction);
        //LoadData();
        LoadDataTemp();
    }


    private void LoadDataTemp() {
        consolidatedList = new ArrayList<>();
        lstNotificationModelList = new ArrayList<>();
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("April 05 16:08:28 GMT+05:30 2024")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("April 04 16:08:28 GMT+05:30 2024")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("April 03 16:08:28 GMT+05:30 2024")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("Oct 19 16:08:28 GMT+05:30 2023")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("Oct 20 16:08:28 GMT+05:30 2023")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("Oct 20 16:08:28 GMT+05:30 2023")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("Oct 19 16:08:28 GMT+05:30 2023")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("Oct 20 16:08:28 GMT+05:30 2023")), "11:25 A.M"));
        lstNotificationModelList.add(new NotificationModel("Education kits & good foods", "Thank you! Your Transaction is complete", getMyPrettyDate(getDataTimemilisecond("Oct 20 16:08:28 GMT+05:30 2023")), "11:25 A.M"));

        HashMap<String, List<NotificationModel>> groupedHashMap = groupDataIntoHashMap(lstNotificationModelList);

        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);

            for (NotificationModel pojoOfJsonArray : groupedHashMap.get(date)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setPojoOfJsonArray(pojoOfJsonArray);//setBookingDataTabs(bookingDataTabs);
                consolidatedList.add(generalItem);
            }
        }

        binding.rvNotifiction.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvNotifiction.setHasFixedSize(true);

        notificationAdapter = new NotificationAdapter(svContext, consolidatedList);
        binding.rvNotifiction.setAdapter(notificationAdapter);

        notificationAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NotificationModel obj, int position) {
                Toast.makeText(getApplicationContext(), obj.getStrTitle(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private HashMap<String, List<NotificationModel>> groupDataIntoHashMap(List<NotificationModel> listOfPojosOfJsonArray) {

        LinkedHashMap<String, List<NotificationModel>> groupedHashMap = new LinkedHashMap<>();

        for (NotificationModel pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getStrDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<NotificationModel> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    private void initToolbar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.layActionbar.tvTitle.setText("Notification");
    }

    private long getDataTimemilisecond(String givenDateString) {
        long timeInMilliseconds = 0;
        //String givenDateString = "Sat Oct 21 16:08:28 GMT+05:30 2023";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss z yyyy");
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static String getMyPrettyDate(long neededTimeMilis) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(neededTimeMilis);

        if ((neededTime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR))) {

            if ((neededTime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH))) {

                if (neededTime.get(Calendar.DATE) - nowTime.get(Calendar.DATE) == 1) {
                    //here return like "Tomorrow at 12:00"
                    return "Tomorrow at " + DateFormat.format("HH:mm", neededTime);

                } else if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
                    //here return like "Today at 12:00"
                    //return "Today at " + DateFormat.format("HH:mm", neededTime);
                    return "Today";

                } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
                    //here return like "Yesterday at 12:00"
                    //return "Yesterday at " + DateFormat.format("HH:mm", neededTime);
                    return "Yesterday";

                } else {
                    //here return like "May 31, 12:00"
                    //return DateFormat.format("MMMM d, HH:mm", neededTime).toString();
                    return DateFormat.format("MMMM d", neededTime).toString();
                }

            } else {
                //here return like "May 31, 12:00"
                return DateFormat.format("MMMM d", neededTime).toString();
                //return DateFormat.format("MMMM d, HH:mm", neededTime).toString();
            }

        } else {
            //here return like "May 31 2010, 12:00" - it's a different year we need to show it
            return DateFormat.format("MMMM dd yyyy", neededTime).toString();
            //return DateFormat.format("MMMM dd yyyy, HH:mm", neededTime).toString();
        }
    }

}
