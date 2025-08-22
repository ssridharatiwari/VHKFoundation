package com.vhkfoundation.activity;

import static com.vhkfoundation.retrofit.WebServiceRetroFit.callWebService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.DonationHistoryAdapter;
import com.vhkfoundation.adapter.EventHistoryAdapter;
import com.vhkfoundation.adapter.SpinnerPopulateAdapter;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActDonationHistoryListBinding;
import com.vhkfoundation.model.ActivityModel;
import com.vhkfoundation.model.Data;
import com.vhkfoundation.model.EventCatData;
import com.vhkfoundation.model.User;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityDonationHistory extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;

    ActDonationHistoryListBinding binding;
    private List<String> listSpinnerCatEvent;
    List<Data> lstDonationData;
    DonationHistoryAdapter donationHistoryAdapter;
    private LinkedList<String> lstUploadData = new LinkedList<>();

    Type listType = new TypeToken<ArrayList<EventCatData>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActDonationHistoryListBinding.inflate(getLayoutInflater());
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
        getCategoryList();
        getDonationList();
        loadToolBar();
    }

    private void getCategoryList() {
        lstUploadData = new LinkedList<>();
        lstUploadData.clear();
        callWebService(this ,ApiInterface.GETCATEGORYLIST, lstUploadData, this);
    }

    private void getDonationList() {

        lstUploadData = new LinkedList<>();
        lstUploadData.clear();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add("0");
        lstUploadData.add("");
        callWebService(this ,ApiInterface.GETDONATIONPOSTLIST, lstUploadData, this);
    }


    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.layActionbar.tvTitle.setText("Donation History");
    }

    private void LoadActivityData(String result) {
        lstDonationData = new ArrayList<>();

       /* try {
            JSONObject json = new JSONObject(result);
            JSONArray dataArray = json.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject mainData = dataArray.getJSONObject(i);
                JSONObject userData = mainData.getJSONObject("user");

                // Create User object
                User user = new User();
                user.setId(userData.getInt("id"));
                user.setName(userData.getString("name"));
                user.setEmail(userData.getString("email"));
                user.setMobile(userData.getString("mobile"));
                user.setProfile_img(userData.getString("profile_img"));
                user.setOther_img(userData.getString("other_img"));
                user.setCcode(userData.getString("ccode"));
                user.setRole(userData.getString("role"));
                user.setDecrypt_password(userData.getString("decrypt_password"));
                user.setUser_type(userData.getString("user_type"));
                user.setVerified(userData.getString("verified"));
                user.setVerified_by(userData.getInt("verified_by"));
                user.setStatus(userData.getInt("status"));
                user.setState_id(userData.getString("state_id"));
                user.setDistrict_id(userData.getString("district_id"));
                user.setCity_name(userData.getString("city_name"));
                user.setPincode(userData.getString("pincode"));
                user.setTrash(userData.getString("trash"));
                user.setCreated_at(userData.getString("created_at"));
                user.setUpdated_at(userData.getString("updated_at"));
                // If email_verified_at is nullable, use optString or opt to handle nulls
                user.setEmail_verified_at(userData.opt("email_verified_at"));

                // Create Data object
                Data data = new Data();
                data.setId(mainData.getInt("id"));
                data.setUser_id(mainData.getInt("user_id"));
                data.setTitle(mainData.getString("title"));
                data.setSlug(mainData.getString("slug"));
                data.setType(mainData.getString("type"));
                data.setShort_desc(mainData.getString("short_desc"));
                data.setContent(mainData.getString("content"));
                data.setTags(mainData.getString("tags"));
                data.setBlog_category_id(mainData.getInt("blog_category_id"));
                data.setIs_emergency(mainData.getString("is_emergency"));
                data.setIs_donate(mainData.getString("is_donate"));
                data.setFeatured_image(mainData.getString("featured_image"));
                data.setDetails_img(mainData.getString("details_img"));
                data.setMeta_title(mainData.getString("meta_title"));
                data.setMeta_description(mainData.getString("meta_description"));
                data.setMeta_keywords(mainData.getString("meta_keywords"));
                data.setStatus(mainData.getString("status"));
                data.setTrash(mainData.getString("trash"));
                data.setCreated_at(mainData.getString("created_at"));
                data.setUpdated_at(mainData.getString("updated_at"));
                data.setUser(user);

                // Add to list
                lstDonationData.add(data);
            }

        } catch (Exception e) {
            Log.d("Donation History :: ", e.toString());
        }*/

        try {
            JSONObject json = new JSONObject(result);
            JSONArray dataArray = json.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject mainData = dataArray.getJSONObject(i);

                Data data = new Data();
                data.setId(mainData.getInt("id"));
                data.setTitle(mainData.getString("title"));
                data.setContent(mainData.getString("description")); // mapping to 'description'
                data.setBlog_category_id(mainData.getInt("category_id"));
                data.setIs_emergency(mainData.getString("is_emergency"));
                data.setIs_donate(mainData.getString("is_donate"));
                data.setFeed_created_by(mainData.getString("feed_created_by")); // Add this field in your model
                data.setFeatured_image(mainData.getString("images"));
                data.setTotal_like(mainData.getString("total_like")); // Add this field in your model
                data.setIs_like(mainData.getString("is_like"));       // Add this field in your model
                data.setDatetime(mainData.getString("datetime"));     // Add this field in your model

                lstDonationData.add(data);
            }

        } catch (Exception e) {
            Log.d("Donation History :: ", e.toString());
        }



       /* JSONObject json = null;
        try {
            json = new JSONObject(result);

            JSONArray data_emergency_data = json.getJSONArray("data");
            if (data_emergency_data != null && data_emergency_data.length() > 0) {
                lstActivityModels = new ArrayList<>();
                for (int i = 0; i < data_emergency_data.length(); i++) {
                    JSONObject jsonObj = data_emergency_data.getJSONObject(i);
                    lstActivityModels.add(new ActivityModel(
                            jsonObj.getString("icon_image"),
                            jsonObj.getString("title"),
                            jsonObj.getString("created"),
                            jsonObj.getString("icon_image"),
                            jsonObj.getString("total_like"),
                            jsonObj.getString("description")));
                }
                // bindEmergencyData(lstActivityModels);
            }

        } catch (JSONException e) {
            customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
            e.printStackTrace();
        }
*/

       /* lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));*/




        binding.rvActivities.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvActivities.setHasFixedSize(true);
        donationHistoryAdapter = new DonationHistoryAdapter(svContext,lstDonationData,0);
        binding.rvActivities.setAdapter(donationHistoryAdapter);

    }


/*    donationHistoryAdapter.setOnItemClickListener(new EventHistoryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, ActivityModel obj, int position) {
            Intent intent = new Intent(ActivityEvent.this, ActivityEventDetails.class);
            Bundle bundle = new Bundle();

            bundle.putString("title",lstActivityModels.get(position).getStrTitle());
            bundle.putString("icon_image",lstActivityModels.get(position).getStrUserImageUrl());
            bundle.putString("description",lstActivityModels.get(position).getStrComment());
            bundle.putString("date",lstActivityModels.get(position).getStrDate());

            intent.putExtras(bundle);

            startActivity(intent);
        }
    });*/




    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.GETDONATIONPOSTLIST)) {
            try {
                json = new JSONObject(result);

                Log.d("Log Response","GETDONATIONPOSTLIST True");

                LoadActivityData(result);

            } catch (JSONException e) {
                Log.d("Log Response","GETDONATIONPOSTLIST False");
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }else if (url.contains(ApiInterface.GETCATEGORYLIST)) {
            try {
                json = new JSONObject(result);
                String str_status = json.getString("status");
                Log.d("Log Response","GETCATEGORYLIST True");
                if (!result.isEmpty()  && str_status.equalsIgnoreCase("1")) {
                    PopulateSpinnerCatEvent(result);
                }
            } catch (JSONException e) {
                Log.d("Log Response","GETCATEGORYLIST False");
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {}


    private void PopulateSpinnerCatEvent(String result) {
        listSpinnerCatEvent = new ArrayList<>();

        List<EventCatData> list = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(result);
            JSONArray dataArray = json.getJSONArray("data");
            list = new Gson().fromJson(dataArray.toString(), listType);
            // listSpinnerCatEvent.add("0" + "#:#" + "Select Category");
            if (!list.isEmpty()) {
                for (int i=0;i<list.size();i++){
                    listSpinnerCatEvent.add(String.valueOf(list.get(i).getId()) + "#:#" + list.get(i).getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SpinnerPopulateAdapter LegAdapter3 = new SpinnerPopulateAdapter(svContext, listSpinnerCatEvent, true);
        binding.spinnerEventCatList.setAdapter(LegAdapter3);
        binding.spinnerEventCatList.setOnItemSelectedListener(onItemSelectedMaritialListener);
    }

    AdapterView.OnItemSelectedListener onItemSelectedMaritialListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (view != null) {
                TextView textView = view.findViewById(R.id.txtitem);
                textView.setBackgroundColor(getResources().getColor(R.color.light_orange));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);


                lstUploadData.clear();

                try {
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
                    lstUploadData.add("0");
                    lstUploadData.add(listSpinnerCatEvent.get(position).split("#")[0]);
                    callWebService(ActivityDonationHistory.this,ApiInterface.GETDONATIONPOSTLIST, lstUploadData, ActivityDonationHistory.this);
                }catch (Exception  e){}
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
