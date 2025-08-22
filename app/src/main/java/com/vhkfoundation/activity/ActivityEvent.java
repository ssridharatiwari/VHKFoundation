package com.vhkfoundation.activity;

import static com.vhkfoundation.retrofit.WebServiceRetroFit.callWebService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.EventHistoryAdapter;
import com.vhkfoundation.adapter.SpinnerPopulateAdapter;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActDonationHistoryListBinding;
import com.vhkfoundation.model.ActivityModel;

import com.vhkfoundation.model.EventCatData;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityEvent extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;


    private LinkedList<String> lstUploadData = new LinkedList<>();
    private List<String> listSpinnerCatEvent;

    ActDonationHistoryListBinding binding;

    List<ActivityModel> lstActivityModels;
    EventHistoryAdapter eventHistoryAdapter;

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
        callEvetntDataApi();
        callEvetntCatDataApi();
        loadToolBar();
    }

    private void callEvetntCatDataApi() {
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        //lstUploadData.add(CatId);
        callWebService(this,ApiInterface.GETDONATIONLISTCATWISE, lstUploadData,this);
    }

    private void callEvetntDataApi() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add("");
        callWebService(this,ApiInterface.GETEVENTLIST, lstUploadData, this);
    }


    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.layActionbar.tvTitle.setText("Events");
    }

    private void LoadActivityData(String result) {
        JSONObject json = null;
        lstActivityModels = new ArrayList<>();


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



      /*  lstActivityModels.add(new ActivityModel("","","","","",""));
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
        eventHistoryAdapter = new EventHistoryAdapter(svContext,lstActivityModels,0);
        binding.rvActivities.setAdapter(eventHistoryAdapter);

        eventHistoryAdapter.setOnItemClickListener(new EventHistoryAdapter.OnItemClickListener() {
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
        });

    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.GETEVENTLIST)) {
            try {
                json = new JSONObject(result);
                String str_status = json.getString("status");

                if (!result.isEmpty()  && str_status.equalsIgnoreCase("1")) {
                    LoadActivityData(result);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }else if (url.contains(ApiInterface.GETDONATIONLISTCATWISE)) {
            try {
                json = new JSONObject(result);
                String str_status = json.getString("status");

                if (!result.isEmpty()  && str_status.equalsIgnoreCase("1")) {
                    PopulateSpinnerCatEvent(result);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {}

    Type listType = new TypeToken<ArrayList<EventCatData>>() {}.getType();

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
                    lstUploadData.add(listSpinnerCatEvent.get(position).split("#")[0]);
                    callWebService(ActivityEvent.this,ApiInterface.GETEVENTLIST, lstUploadData, ActivityEvent.this);
                }catch (Exception  e){}
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

}
