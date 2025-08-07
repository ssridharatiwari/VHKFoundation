package com.vhkfoundation.fragment;

import static com.vhkfoundation.retrofit.WebServiceRetroFit.callWebService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vhkfoundation.R;
import com.vhkfoundation.activity.ActivityCatWiseDonationList;
import com.vhkfoundation.activity.ActivityDonate;
import com.vhkfoundation.activity.ActivityDonationDetails;
import com.vhkfoundation.activity.ActivityMain;
import com.vhkfoundation.activity.ActivitySplash;
import com.vhkfoundation.adapter.DonationAllItemAdapter;
import com.vhkfoundation.adapter.DonationHistoryAdapter;
import com.vhkfoundation.adapter.DonationListHeaderAdapter;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.databinding.FragDonationBinding;
import com.vhkfoundation.model.Data;
import com.vhkfoundation.model.DonationHeader;
import com.vhkfoundation.model.DonationHeaderItem;
import com.vhkfoundation.model.DonationItem;
import com.vhkfoundation.model.DonationListItem;
import com.vhkfoundation.model.DonationModel;
import com.vhkfoundation.model.ListItem;
import com.vhkfoundation.model.User;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class FragmentDonation extends Fragment implements WebServiceListenerRetroFit {
    private View aiView = null;
   // RecyclerView rv_donate_list;

    List<DonationHeaderItem> lstdonationheaderitem;

    List<DonationListItem> lstitem;
    DonationListHeaderAdapter donationAllItemAdapter;
    private Context svContext;
    private ShowCustomToast customToast;
    int itemPosition = 0;
    int headerPosition = 0;
    LinkedList<String> lstUploadData = new LinkedList<>();
    //SwipeRefreshLayout swipeRefreshLayout;
    List<Data> lstDonationData;

    FragDonationBinding binding;

    DonationHistoryAdapter donationHistoryAdapter;



    //https://medium.com/@ashishkudale/android-list-inside-list-using-recyclerview-73cff2c4ea95
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragDonationBinding.inflate(getLayoutInflater());

        if (aiView == null) {

            aiView = inflater.inflate(R.layout.frag_donation, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startApp();
    }

    private void startApp() {
        svContext=getActivity();
        customToast = new ShowCustomToast(svContext);
      //  rv_donate_list = aiView.findViewById(R.id.rv_donate_list);
      //  swipeRefreshLayout = aiView.findViewById(R.id.refreshLayout);
        //LoadDataTemp();
        ImageView iv_create_post = ((ActivityMain)getActivity()).findViewById(R.id.iv_create_post);
        iv_create_post.setVisibility(View.GONE);

        getDonationEventList();

        binding.refreshLayout.setOnRefreshListener(() -> {
            getDonationEventList();
            binding.refreshLayout.setRefreshing(false);
        });
    }

    private void getDonationEventList(){
        lstUploadData = new LinkedList<>();
        lstUploadData.clear();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add("0");
        lstUploadData.add("");
        callWebService(ApiInterface.GETDONATIONPOSTLIST, lstUploadData, true);
    }

    private void LoadDataTemp() {


//        lstdonationheaderitem = new ArrayList<>();
//        lstitem = new ArrayList<>();
//        lstitem.add(new DonationListItem("1","","","","","","","","","","",""));
//        lstitem.add(new DonationListItem("2","","","","","","","","","","",""));
//        lstitem.add(new DonationListItem("3","","","","","","","","","","",""));
//        lstdonationheaderitem.add(new DonationHeaderItem("Food Donation",1,lstitem));
//        lstdonationheaderitem.add(new DonationHeaderItem("Emergency Help",1,lstitem));
//        lstdonationheaderitem.add(new DonationHeaderItem("Food Donation",1,lstitem));
//        //rv_donate_list.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
//        rv_donate_list.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv_donate_list.setHasFixedSize(true);
//
//        donationAllItemAdapter = new DonationListHeaderAdapter(getActivity(), lstdonationheaderitem);
//        rv_donate_list.setAdapter(donationAllItemAdapter);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData, boolean isDialogShow) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this,isDialogShow);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.GETDONATIONPOSTLIST)) {
            try {
                json = new JSONObject(result);
                LoadActivityData(result);

            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

        }

       /* if (url.contains(ApiInterface.GETDONATIONEVENTLIST)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if(str_status.equalsIgnoreCase("1")){
                    JSONArray data = json.getJSONArray("data");
                    if(data!=null && data.length()>0){
                        lstdonationheaderitem = new ArrayList<>();
                        for(int i=0;i<data.length();i++){
                            JSONObject obj_cat = data.getJSONObject(i);
                            String str_cat_id = obj_cat.getString("category_id");
                            String str_cat_name = obj_cat.getString("category_title");
                            JSONArray arr_post = obj_cat.getJSONArray("donations");
                            if(arr_post!=null && arr_post.length()>0){
                                lstitem = new ArrayList<>();
                                for(int j=0;j<arr_post.length();j++){
                                    JSONObject obj_post = arr_post.getJSONObject(j);
                                    String str_post_id = obj_post.getString("id");
                                    String str_post_title = obj_post.getString("title");
                                    String str_post_desc = obj_post.getString("description");
                                    String str_post_image = obj_post.getString("image");
                                    String str_don_amount = obj_post.getString("don_amount");
                                    String str_datetime = obj_post.getString("datetime");
                                    String is_like = obj_post.getString("is_like");
                                    String total_like = obj_post.getString("total_like");
                                    //String str_datetime = obj_post.getString("created");
                                    DonationListItem donationListItem = new DonationListItem(str_post_id,"",str_post_title,str_datetime,str_post_image,is_like,"","500",str_post_desc,str_cat_id,"",total_like,str_cat_name);
                                    lstitem.add(donationListItem);
                                }
                                DonationHeaderItem donationHeaderItem = new DonationHeaderItem(str_cat_name,Integer.valueOf(str_cat_id),lstitem);
                                lstdonationheaderitem.add(donationHeaderItem);
                            }
                        }
                        rv_donate_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_donate_list.setHasFixedSize(true);
                        donationAllItemAdapter = new DonationListHeaderAdapter(getActivity(), lstdonationheaderitem);
                        rv_donate_list.setAdapter(donationAllItemAdapter);

                        donationAllItemAdapter.setOnViewAllClickListener(new DonationListHeaderAdapter.OnViewAllClickListener() {
                            @Override
                            public void onViewAllClick(View view, DonationHeaderItem obj, int position) {
                                Intent intent = new Intent(svContext, ActivityCatWiseDonationList.class);
                                intent.putExtra("CatId",String.valueOf(obj.getId()));
                                intent.putExtra("CatName",obj.getTitle());
                                svContext.startActivity(intent);
                                //customToast.showCustomToast(svContext, "Id " + obj.getId(), customToast.ToastyError);
                            }
                        });

                        donationAllItemAdapter.setOnLikeClickListener(new DonationListHeaderAdapter.OnLikeClickListener() {
                            @Override
                            public void onLikeClick(View view, DonationListItem obj, int position,int hederPos) {
                                //customToast.showCustomToast(svContext, "Id " + obj.getStrId() + " " + obj.getStrTitle(), customToast.ToastyError);
                                itemPosition = position;
                                headerPosition = hederPos;
                                updateLike(obj);
                            }
                        });

                        donationAllItemAdapter.setOnDonateClickListener(new DonationListHeaderAdapter.OnDonateClickListener() {
                            @Override
                            public void onDonateClick(View view, DonationListItem obj, int position, int headerPosition) {
                                //customToast.showCustomToast(svContext, "Id " + obj.getStrId() + " " + obj.getStrTitle(), customToast.ToastyError);
                                Intent intent = new Intent(svContext, ActivityDonate.class);
                                intent.putExtra("isMenu", "1");
                                svContext.startActivity(intent);
                            }
                        });

                        donationAllItemAdapter.setOnItemClickListener(new DonationListHeaderAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, DonationListItem obj, int position, int headerPosition) {
                                Intent intent = new Intent(svContext, ActivityDonationDetails.class);
                                intent.putExtra("event_id", obj.getStrId());
                                svContext.startActivity(intent);
                            }
                        });
                    }

                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
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
                    if (lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).getStrLike().equals("No")) {
                        String strCount = lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).getStrTotalLike();
                        int likecount = Integer.valueOf(strCount) + 1;
                        lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).setStrLike("Yes");
                        lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).setStrTotalLike(String.valueOf(likecount));
                        //lstitem.get(itemPosition).setStrTotalLike(String.valueOf(likecount));
                        //lstitem.get(itemPosition).setStrLike("Yes");
                    } else {
                        String strCount = lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).getStrTotalLike();
                        int likecount = 0;
                        if (!strCount.equals("0")) {
                            likecount = Integer.valueOf(strCount) - 1;
                        }
                        lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).setStrLike("No");
                        lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).setStrTotalLike(String.valueOf(likecount));
                    }

//                    DonationHeaderItem donationHeaderItem = new DonationHeaderItem(str_cat_name,Integer.valueOf(str_cat_id),lstitem);
//                    lstdonationheaderitem.add(donationHeaderItem);

                    //lstdonationheaderitem.get(headerPosition).getDonationListItemList().get(itemPosition).setDonationListItemList(itemPosition,lstitem);

                    donationAllItemAdapter.setItems(lstdonationheaderitem);
                    donationAllItemAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }*/
    }

    @Override
    public void onWebServiceRetroError(String result, String url) {}

    private void updateLike(DonationListItem obj) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add(obj.getStrId());
        //lstUploadData.add(obj.getStrId());
        if (obj.getStrLike().equals("No")) {
            lstUploadData.add("1");
        } else {
            lstUploadData.add("0");
        }
        callWebService(ApiInterface.GETDONATIONLISTCATLIKE, lstUploadData,true);
    }

    private void LoadActivityData(String result) {
        lstDonationData = new ArrayList<>();

/*        try {
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

        binding.rvDonateList.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvDonateList.setHasFixedSize(true);
        donationHistoryAdapter = new DonationHistoryAdapter(svContext,lstDonationData,0);
        binding.rvDonateList.setAdapter(donationHistoryAdapter);

    }

}
