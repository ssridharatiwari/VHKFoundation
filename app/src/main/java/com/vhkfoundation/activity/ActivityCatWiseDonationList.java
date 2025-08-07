package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.CatWiseDonationListAdapter;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActCatWiseDonationListBinding;
import com.vhkfoundation.model.DonationListItem;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityCatWiseDonationList extends AppCompatActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;
    private ActCatWiseDonationListBinding binding;
    private LinkedList<String> lstUploadData = new LinkedList<>();
    private List<DonationListItem> lstDonationitmList;
    private CatWiseDonationListAdapter catWiseDonationListAdapter;
    private int itemPosition = 0;
    private String CatId="",CatName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActCatWiseDonationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StartApp();
    }

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);

        ViewGroup root = findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        resumeApp();
    }

    private void resumeApp() {

        if(getIntent().getExtras()!=null){
            CatId = getIntent().getStringExtra("CatId");
            CatName= getIntent().getStringExtra("CatName");
            getData();
            loadToolBar();
        }

        binding.refreshLayout.setOnRefreshListener(() -> {
            getData();
            binding.refreshLayout.setRefreshing(false);
        });
    }


    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
        binding.layActionbar.tvTitle.setText(CatName);
    }

    private void getData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        //lstUploadData.add(CatId);
        callWebService(ApiInterface.GETDONATIONLISTCATWISE, lstUploadData);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json;
        if (url.contains(ApiInterface.GETDONATIONLISTCATWISE)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray donation_data = json.getJSONArray("data");
                    if (donation_data != null && donation_data.length() > 0) {
                        lstDonationitmList = new ArrayList<>();
                        for (int i = 0; i < donation_data.length(); i++) {
                            JSONObject obj_post = donation_data.getJSONObject(i);
                            String str_post_id = obj_post.getString("id");
                            String str_post_title = obj_post.getString("title");
                            String str_post_desc = obj_post.getString("description");
                            String str_post_image = obj_post.getString("icon_image");
                            String is_like = obj_post.getString("is_like");
                            String total_like = obj_post.getString("total_like");
                            String str_datetime = obj_post.getString("created");
                            String strCatid =  obj_post.getString("category_id");
                            String strCatName =  obj_post.getString("category_name");
                            lstDonationitmList.add(new DonationListItem(str_post_id,"",str_post_title,str_datetime,str_post_image,is_like,"","500",str_post_desc,strCatid,"",total_like,strCatName));
                        }
                        bindFeedData(lstDonationitmList);
                    } else {
                        customToast.showCustomToast(svContext, "No records found", customToast.ToastyError);
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GETDONATIONLISTCATLIKE)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    if (lstDonationitmList.get(itemPosition).getStrLike().equals("No")) {
                        String strCount = lstDonationitmList.get(itemPosition).getStrTotalLike();
                        int likecount = Integer.valueOf(strCount) + 1;
                        lstDonationitmList.get(itemPosition).setStrTotalLike(String.valueOf(likecount));
                        lstDonationitmList.get(itemPosition).setStrLike("Yes");
                    } else {
                        String strCount = lstDonationitmList.get(itemPosition).getStrTotalLike();
                        int likecount = 0;
                        if (!strCount.equals("0")) {
                            likecount = Integer.valueOf(strCount) - 1;
                        }
                        lstDonationitmList.get(itemPosition).setStrTotalLike(String.valueOf(likecount));
                        lstDonationitmList.get(itemPosition).setStrLike("No");
                    }
                    catWiseDonationListAdapter.setItems(lstDonationitmList);
                    catWiseDonationListAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }

    private void bindFeedData(List<DonationListItem> lstdonationlist) {
        binding.rvDonation.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvDonation.setHasFixedSize(true);

        catWiseDonationListAdapter = new CatWiseDonationListAdapter(svContext, lstdonationlist, 0);
        binding.rvDonation.setAdapter(catWiseDonationListAdapter);

        catWiseDonationListAdapter.setOnItemClickListener((view, obj, position) -> {
            Intent intent = new Intent(svContext, ActivityDonationDetails.class);
            intent.putExtra("event_id", obj.getStrId());
            svContext.startActivity(intent);
        });

        catWiseDonationListAdapter.setOnItemLikeClickListener((view, obj, position) -> {
            itemPosition = position;
            updateLike(obj);
        });

        catWiseDonationListAdapter.setOnDonateClickListener((view, obj, position) -> {
            Intent intent = new Intent(svContext, ActivityDonate.class);
            intent.putExtra("isMenu", "1");
            svContext.startActivity(intent);
        });
    }

    private void updateLike(DonationListItem obj) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add(obj.getStrId());
        if (obj.getStrLike().equals("No")) {
            lstUploadData.add("1");
        } else {
            lstUploadData.add("0");
        }
        callWebService(ApiInterface.GETDONATIONLISTCATLIKE, lstUploadData);
    }
}
