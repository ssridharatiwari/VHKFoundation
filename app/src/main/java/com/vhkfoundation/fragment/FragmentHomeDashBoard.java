package com.vhkfoundation.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vhkfoundation.R;
import com.vhkfoundation.activity.ActivityAddVolunteer;
import com.vhkfoundation.activity.ActivityCategoryList;
import com.vhkfoundation.activity.ActivityCreatePost;
import com.vhkfoundation.activity.ActivityDonate;
import com.vhkfoundation.activity.ActivityEmergencyList;
import com.vhkfoundation.activity.ActivityFeedsDetails;
import com.vhkfoundation.activity.ActivityFeedsList;
import com.vhkfoundation.activity.ActivityLogin;
import com.vhkfoundation.activity.ActivityMain;
import com.vhkfoundation.activity.ActivitySubCategoryList;
import com.vhkfoundation.adapter.BannerImageAdapter;
import com.vhkfoundation.adapter.CommentsAdapter;
import com.vhkfoundation.adapter.FeedsAdapter;
import com.vhkfoundation.adapter.FeedsDashBoardAdapter;
import com.vhkfoundation.adapter.SliderAdapter;
import com.vhkfoundation.adapter.SliderDashBoardAdapter;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.model.CommentsModel;
import com.vhkfoundation.model.FeedsDashBoardModel;
import com.vhkfoundation.model.FeedsModel;
import com.vhkfoundation.model.SliderModel;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FragmentHomeDashBoard extends Fragment implements WebServiceListenerRetroFit {
    private View aiView = null;
    private ViewPager2 viewPager2;
    private Context svContext;
    private ShowCustomToast customToast;
    public static List<SliderModel> lstSlider = new ArrayList<>();
    RecyclerView rv_feeds;
    private final Handler sliderHandler = new Handler();
    FeedsAdapter feedsAdapter;
    FeedsDashBoardAdapter feedsDashBoardAdapter;
    CommentsAdapter commentsAdapter;
    List<FeedsModel> lstfeedsmodel;
    List<FeedsDashBoardModel> lstemergencyfeedsmodel;
    List<FeedsDashBoardModel> lstuserfeedsmodel;
    List<CommentsModel> lstCommentsModels;
    LinearLayout ll_all_cat, ll_campaigns, ll_donate, ll_volunteer;

    SliderAdapter sliderAdapter;
    SliderDashBoardAdapter sliderDashBoardAdapter;

    LinkedList<String> lstUploadData = new LinkedList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    TextView tv_eme_feeds_all, tv_feeds_all;

    int itemPosition = 0;
    String clickAdapterName = "", strFeedId;

    private List<Integer> imageList = Arrays.asList(
            R.drawable.img_dashboard_1, R.drawable.img_dashboard_1, R.drawable.img_dashboard_1, R.drawable.img_dashboard_1
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_homedashboard, container, false);
        }

        RecyclerView recyclerView = aiView.findViewById(R.id.rv_bannerImage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new BannerImageAdapter(imageList));



        return aiView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resumeApp();
    }

    public void resumeApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        viewPager2 = aiView.findViewById(R.id.viewPagerImageSlider);
        rv_feeds = aiView.findViewById(R.id.rv_feeds);
        ll_all_cat = aiView.findViewById(R.id.ll_all_cat);
        ll_campaigns = aiView.findViewById(R.id.ll_campaigns);
        ll_donate = aiView.findViewById(R.id.ll_donate);
        ll_volunteer = aiView.findViewById(R.id.ll_volunteer);
        swipeRefreshLayout = aiView.findViewById(R.id.refreshLayout);
        tv_eme_feeds_all = aiView.findViewById(R.id.tv_eme_feeds_all);
        tv_feeds_all = aiView.findViewById(R.id.tv_feeds_all);
        //LoadSlider();
        //LoadDataTemp();

        ImageView iv_create_post = getActivity().findViewById(R.id.iv_create_post);
        iv_create_post.setVisibility(View.VISIBLE);

        iv_create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivityCreatePost.class);
                svContext.startActivity(intent);
            }
        });

        tv_eme_feeds_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivityEmergencyList.class);
                svContext.startActivity(intent);
            }
        });

        tv_feeds_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivityFeedsList.class);
                svContext.startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        setOnClickListner();
        getData();
    }

    private void LoadDataTemp() {
        lstfeedsmodel = new ArrayList<>();
        lstfeedsmodel.add(new FeedsModel("", "", "", "", "", ""));
        lstfeedsmodel.add(new FeedsModel("", "", "", "", "", ""));
        //lstfeedsmodel.add(new FeedsModel("","","","","",""));
        //lstfeedsmodel.add(new FeedsModel("","","","","",""));

        rv_feeds.setLayoutManager(new LinearLayoutManager(svContext));
        rv_feeds.setHasFixedSize(true);

        feedsAdapter = new FeedsAdapter(svContext, lstfeedsmodel, 0);
        rv_feeds.setAdapter(feedsAdapter);

        feedsAdapter.setOnItemLikeClickListener(new FeedsAdapter.OnItemLikeClickListener() {
            @Override
            public void onItemLikeClick(View view, FeedsModel obj, int position) {
                customToast.showCustomToast(svContext, "Like Click", customToast.ToastyError);
            }
        });

        feedsAdapter.setOnItemCommentsClickListener(new FeedsAdapter.OnItemCommetsClickListener() {
            @Override
            public void onItemCommetsClick(View view, FeedsModel obj, int position) {
                bindCommentsData();
                //customToast.showCustomToast(svContext, "Comments Click", customToast.ToastyError);
            }
        });

        feedsAdapter.setOnItemShareClickListener(new FeedsAdapter.OnItemShareClickListener() {
            @Override
            public void onItemShareClick(View view, FeedsModel obj, int position) {
                customToast.showCustomToast(svContext, "Share Click", customToast.ToastyError);
            }
        });

        feedsAdapter.setOnDonateClickListener(new FeedsAdapter.OnDonateClickListener() {
            @Override
            public void onDonateClick(View view, FeedsModel obj, int position) {
                Intent intent = new Intent(svContext, ActivityDonate.class);
                intent.putExtra("isMenu", "1");
                svContext.startActivity(intent);
                //customToast.showCustomToast(svContext, "Donate Click " + position, customToast.ToastyError);
            }
        });

        setOnClickListner();
    }

    private void setOnClickListner() {
        ll_all_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivityCategoryList.class);
                svContext.startActivity(intent);
            }
        });

        ll_campaigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivitySubCategoryList.class);
                svContext.startActivity(intent);
            }
        });

        ll_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivityDonate.class);
                intent.putExtra("isMenu", "0");
                svContext.startActivity(intent);
            }
        });

        ll_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(svContext, ActivityAddVolunteer.class);
                svContext.startActivity(intent);
            }
        });
    }

    private void LoadSlider() {
        String imageUri1 = "drawable://" + R.drawable.img_dashboard_1;
        String imageUri2 = "drawable://" + R.drawable.img_dashboard_1;
        //String imageUri3 = getResources().getResourceName(R.drawable.slide1);
        lstSlider.add(new SliderModel("1", imageUri1, imageUri2));
        lstSlider.add(new SliderModel("2", imageUri2, imageUri2));
        //lstSlider.add(new SliderModel("3",imageUri1,imageUri2));
        //lstSlider.add(new SliderModel("4",imageUri2,imageUri2));

        sliderAdapter = new SliderAdapter(FragmentHomeDashBoard.lstSlider, viewPager2);
        //viewPager2.setAdapter(new SliderAdapter(FragmentHomeDashBoard.lstSlider,viewPager2));
        viewPager2.setAdapter(sliderAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        //viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                //page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
            }
        });

        sliderAdapter.setOnDonateClickListener(new SliderAdapter.OnDonateClickListener() {
            @Override
            public void onDonateClick(View view, SliderModel obj, int position) {
                Intent intent = new Intent(svContext, ActivityDonate.class);
                intent.putExtra("isMenu", "1");
                svContext.startActivity(intent);
                //customToast.showCustomToast(svContext, "Donate Click " + position, customToast.ToastyError);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalVariables.isRefresh == 1) {
            getData();
            GlobalVariables.isRefresh = 0;
        }
        sliderHandler.postDelayed(sliderRunnable, 3000);
        //customToast.showCustomToast(svContext, "Donate Click ", customToast.ToastyError);
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private void loadCommentsData(String feed_id) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add(feed_id);
        callWebService(ApiInterface.GETCOMMENTSLIST, lstUploadData);
    }

    private void bindCommentsData() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(svContext, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(svContext).inflate(R.layout.bottom_sheet_dialog_comments, bottomSheetDialog.findViewById(R.id.BottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

        AppCompatTextView btn_create_comment = bottomSheetDialog.findViewById(R.id.btn_create_comment);
        EditText et_comment = bottomSheetDialog.findViewById(R.id.et_comment);
        RecyclerView rv_comments = bottomSheetDialog.findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(svContext));
        rv_comments.setHasFixedSize(true);


//        lstCommentsModels = new ArrayList<>();
//        lstCommentsModels.add(new CommentsModel("1","Codunite Pvt.Ltd.","02 January 2024","Lets help the kid for better food","50","100",""));


            ViewGroup.LayoutParams params1=rv_comments.getLayoutParams();
            params1.height=getResources().getDimensionPixelSize(R.dimen.dp_400);
            //rv_comments.setLayoutParams(params1);
            commentsAdapter = new CommentsAdapter(svContext, lstCommentsModels, 0);
            rv_comments.setAdapter(commentsAdapter);

            commentsAdapter.setOnItemActionClickListener(new CommentsAdapter.OnItemActionClickListener() {
            @Override
            public void onItemActionClick(View view, CommentsModel obj, int position) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(svContext, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(svContext).inflate(R.layout.bottom_sheet_dialog_comments_action, bottomSheetDialog.findViewById(R.id.BottomSheetContainer));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.show();

                LinearLayout ll_delete_comment = bottomSheetDialog.findViewById(R.id.ll_delete_comment);
                ll_delete_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position >= 0) {
//                            lstCommentsModels.remove(position);
//                            commentsAdapter.setItems(lstCommentsModels);
//                            commentsAdapter.notifyDataSetChanged();
//                            bottomSheetDialog.hide();
                        }
                    }
                });
            }
        });

        commentsAdapter.setOnItemLikeClickListener(new CommentsAdapter.OnItemLikeClickListener() {
            @Override
            public void onItemLikeClick(View view, CommentsModel obj, int position) {
                itemPosition = position;
                updateCommentLike(obj);
            }
        });

        btn_create_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_comment.getText().toString().isEmpty()) {

                    lstUploadData = new LinkedList<>();
                    lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
                    lstUploadData.add(strFeedId);
                    lstUploadData.add(et_comment.getText().toString().trim());
                    callWebService(ApiInterface.POSTCOMMENT, lstUploadData);
                    et_comment.setText("");

//                    lstCommentsModels.add(new CommentsModel("1", "Test Comment", "02 January 2024", et_comment.getText().toString().trim(), "0", "0",""));
//                    commentsAdapter.setItems(lstCommentsModels);
//                    commentsAdapter.notifyDataSetChanged();
//                    et_comment.setText("");
                }
            }
        });
    }

    private void getData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        callWebService(ApiInterface.GETHOMEDATA, lstUploadData);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.GETHOMEDATA)) {
            try {
                json = new JSONObject(result);
               String str_message = json.getString("message");
                String str_status = json.getString("status");
               // JSONArray jsonArray = new JSONArray(result);


                if (!result.isEmpty()  && str_status.equalsIgnoreCase("1"))
                {
                    JSONArray data_emergency_data = json.getJSONArray("emergency_data");
                    if (data_emergency_data != null && data_emergency_data.length() > 0) {
                        lstemergencyfeedsmodel = new ArrayList<>();
                        for (int i = 0; i < data_emergency_data.length(); i++) {
                            JSONObject jsonObj = data_emergency_data.getJSONObject(i);
                            lstemergencyfeedsmodel.add(new FeedsDashBoardModel(jsonObj.getString("id"), jsonObj.getString("title"), jsonObj.getString("description"), jsonObj.getString("is_emergency"), jsonObj.getString("is_donate"), "", jsonObj.getString("datetime"), jsonObj.getString("images"), jsonObj.getString("total_like"), "0", jsonObj.getString("is_like"), jsonObj.getString("feed_created_by")));
                        }
                        bindEmergencyData(lstemergencyfeedsmodel);
                    }
                    JSONArray feed_data = json.getJSONArray("feed_data");
                    if (feed_data != null && feed_data.length() > 0) {
                        lstuserfeedsmodel = new ArrayList<>();
                        for (int i = 0; i < feed_data.length(); i++) {
                            JSONObject jsonObj = feed_data.getJSONObject(i);
                            lstuserfeedsmodel.add(new FeedsDashBoardModel(jsonObj.getString("id"), jsonObj.getString("title"), jsonObj.getString("description"), jsonObj.getString("is_emergency"), jsonObj.getString("is_donate"), "", jsonObj.getString("datetime"), jsonObj.getString("images"), jsonObj.getString("total_like"), "0", jsonObj.getString("is_like"), jsonObj.getString("feed_created_by")));
                        }
                        bindFeedsData(lstuserfeedsmodel);
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.SETLIKE)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    if (clickAdapterName.equals("Feed")) {
                        if (lstuserfeedsmodel.get(itemPosition).getStrisLike().equals("No")) {
                            String strCount = lstuserfeedsmodel.get(itemPosition).getStrLike();
                            int likecount = Integer.valueOf(strCount) + 1;
                            lstuserfeedsmodel.get(itemPosition).setStrLike(String.valueOf(likecount));
                            lstuserfeedsmodel.get(itemPosition).setStrisLike("Yes");
                        } else {
                            String strCount = lstuserfeedsmodel.get(itemPosition).getStrLike();
                            int likecount = 0;
                            if (!strCount.equals("0")) {
                                likecount = Integer.valueOf(strCount) - 1;
                            }
                            lstuserfeedsmodel.get(itemPosition).setStrLike(String.valueOf(likecount));
                            lstuserfeedsmodel.get(itemPosition).setStrisLike("No");
                        }
                        feedsDashBoardAdapter.setItems(lstuserfeedsmodel);
                        feedsDashBoardAdapter.notifyDataSetChanged();
                    } else if (clickAdapterName.equals("Emergency")) {
                        if (lstemergencyfeedsmodel.get(itemPosition).getStrisLike().equals("No")) {
                            String strCount = lstemergencyfeedsmodel.get(itemPosition).getStrLike();
                            int likecount = Integer.valueOf(strCount) + 1;
                            lstemergencyfeedsmodel.get(itemPosition).setStrLike(String.valueOf(likecount));
                            lstemergencyfeedsmodel.get(itemPosition).setStrisLike("Yes");
                        } else {
                            String strCount = lstemergencyfeedsmodel.get(itemPosition).getStrLike();
                            int likecount = 0;
                            if (!strCount.equals("0")) {
                                likecount = Integer.valueOf(strCount) - 1;
                            }
                            lstemergencyfeedsmodel.get(itemPosition).setStrLike(String.valueOf(likecount));
                            lstemergencyfeedsmodel.get(itemPosition).setStrisLike("No");
                        }
                        sliderDashBoardAdapter.setItems(lstemergencyfeedsmodel);
                        sliderDashBoardAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.GETCOMMENTSLIST)) {
            try {
                json = new JSONObject(result);
                lstCommentsModels = new ArrayList<>();
                //String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray data_comments = json.getJSONArray("comment_data");

                    if (data_comments != null && data_comments.length() > 0) {
                        for (int i = 0; i < data_comments.length(); i++) {
                            JSONObject jsonObj = data_comments.getJSONObject(i);
                            lstCommentsModels.add(new CommentsModel(jsonObj.getString("id"), jsonObj.getString("comment_by_name"), jsonObj.getString("datetime"), jsonObj.getString("comment"), jsonObj.getString("total_comment_like"), "", jsonObj.getString("user_comment_like")));
                        }
                        bindCommentsData();
                    }

                    bindEmergencyData(lstemergencyfeedsmodel);

                } else {
                    bindCommentsData();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.POSTCOMMENT)) {
            try {
                json = new JSONObject(result);
                //String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    JSONObject jsonObj = new JSONObject(json.getString("data"));
                    lstCommentsModels.add(0,new CommentsModel(jsonObj.getString("id"), jsonObj.getString("comment_by_name"), jsonObj.getString("datetime"), jsonObj.getString("comment"), jsonObj.getString("total_comment_like"), "", jsonObj.getString("user_comment_like")));
                    commentsAdapter.setItems(lstCommentsModels);
                    commentsAdapter.notifyDataSetChanged();

//                    lstCommentsModels.add(new CommentsModel("1", "Test Comment", "02 January 2024", et_comment.getText().toString().trim(), "0", "0",""));
//                    commentsAdapter.setItems(lstCommentsModels);
//                    commentsAdapter.notifyDataSetChanged();
//                    et_comment.setText("");
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, e.getMessage(), customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(ApiInterface.SETCOMMENTLIKE)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {

                    if (lstCommentsModels.get(itemPosition).getStrIsLike().equals("No")) {
                        String strCount = lstCommentsModels.get(itemPosition).getStrLike();
                        int likecount = Integer.valueOf(strCount) + 1;
                        lstCommentsModels.get(itemPosition).setStrLike(String.valueOf(likecount));
                        lstCommentsModels.get(itemPosition).setStrIsLike("Yes");
                    } else {
                        String strCount = lstCommentsModels.get(itemPosition).getStrLike();
                        int likecount = 0;
                        if (!strCount.equals("0")) {
                            likecount = Integer.valueOf(strCount) - 1;
                        }
                        lstCommentsModels.get(itemPosition).setStrLike(String.valueOf(likecount));
                        lstCommentsModels.get(itemPosition).setStrIsLike("No");
                    }
                    commentsAdapter.setItems(lstCommentsModels);
                    commentsAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, e.getMessage(), customToast.ToastyError);
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onWebServiceRetroError(String result, String url) {

    }

    private void bindEmergencyData(List<FeedsDashBoardModel> lstfeedsmodel) {
        String imageUri1 = "drawable://" + R.drawable.img_dashboard_1;
        String imageUri2 = "drawable://" + R.drawable.img_dashboard_1;
        //String imageUri3 = getResources().getResourceName(R.drawable.slide1);
        lstSlider.add(new SliderModel("1", imageUri1, imageUri2));
        lstSlider.add(new SliderModel("2", imageUri2, imageUri2));
        //lstSlider.add(new SliderModel("3",imageUri1,imageUri2));
        //lstSlider.add(new SliderModel("4",imageUri2,imageUri2));

        sliderDashBoardAdapter = new SliderDashBoardAdapter(svContext, lstemergencyfeedsmodel, viewPager2);
        //viewPager2.setAdapter(new SliderAdapter(FragmentHomeDashBoard.lstSlider,viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(lstemergencyfeedsmodel.size());
        //viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                //page.setScaleY(0.85f + r * 0.15f);
//            }
//        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
            }
        });

        sliderDashBoardAdapter.setOnDonateClickListener(new SliderDashBoardAdapter.OnDonateClickListener() {
            @Override
            public void onDonateClick(View view, FeedsDashBoardModel obj, int position) {
                Intent intent = new Intent(svContext, ActivityDonate.class);
                intent.putExtra("isMenu", "1");
                svContext.startActivity(intent);
                //customToast.showCustomToast(svContext, "Donate Click " + position, customToast.ToastyError);
            }
        });

        sliderDashBoardAdapter.setOnItemLikeClickListener(new SliderDashBoardAdapter.OnItemLikeClickListener() {
            @Override
            public void onItemLikeClick(View view, FeedsDashBoardModel obj, int position) {
                itemPosition = position;
                clickAdapterName = "Emergency";
                updateLike(obj);
                //customToast.showCustomToast(svContext, "Like Click", customToast.ToastyError);
            }
        });

        sliderDashBoardAdapter.setOnItemCommentsClickListener(new SliderDashBoardAdapter.OnItemCommetsClickListener() {
            @Override
            public void onItemCommetsClick(View view, FeedsDashBoardModel obj, int position) {
                //bindCommentsData();
                strFeedId = obj.getStrId();
                loadCommentsData(obj.getStrId());
            }
        });

        viewPager2.setAdapter(sliderDashBoardAdapter);
    }

    private void bindFeedsData(List<FeedsDashBoardModel> lstfeedsmodel) {


        rv_feeds.setLayoutManager(new LinearLayoutManager(svContext));
        rv_feeds.setHasFixedSize(true);

        feedsDashBoardAdapter = new FeedsDashBoardAdapter(svContext, lstfeedsmodel, 0);
        rv_feeds.setAdapter(feedsDashBoardAdapter);


        feedsDashBoardAdapter.setOnItemClickListener(new FeedsDashBoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, FeedsDashBoardModel obj, int position) {
                Intent intent = new Intent(svContext, ActivityFeedsDetails.class);
                intent.putExtra("feed_id", obj.getStrId());
                svContext.startActivity(intent);
                //customToast.showCustomToast(svContext, "Id: " + obj.getStrId(), customToast.ToastyError);
            }
        });

        feedsDashBoardAdapter.setOnItemLikeClickListener(new FeedsDashBoardAdapter.OnItemLikeClickListener() {
            @Override
            public void onItemLikeClick(View view, FeedsDashBoardModel obj, int position) {
                //TextView tv =view.findViewById(R.id.tv_like_count);
                //tv.setText("200");
                //((TextView)view.findViewById(R.id.tv_like_count)).setText("200");
                itemPosition = position;
                clickAdapterName = "Feed";
                updateLike(obj);

                //customToast.showCustomToast(svContext, "Like Click", customToast.ToastyError);
            }
        });

        feedsDashBoardAdapter.setOnItemCommentsClickListener(new FeedsDashBoardAdapter.OnItemCommetsClickListener() {
            @Override
            public void onItemCommetsClick(View view, FeedsDashBoardModel obj, int position) {
                //bindCommentsData();
                strFeedId = obj.getStrId();
                loadCommentsData(obj.getStrId());

                //customToast.showCustomToast(svContext, "Comments Click", customToast.ToastyError);
            }
        });

        feedsDashBoardAdapter.setOnItemShareClickListener(new FeedsDashBoardAdapter.OnItemShareClickListener() {
            @Override
            public void onItemShareClick(View view, FeedsDashBoardModel obj, int position, LinearLayout ll) {
                //shareReceipt(ll);
                customToast.showCustomToast(svContext, "Share Click", customToast.ToastyError);
            }
        });

        feedsDashBoardAdapter.setOnDonateClickListener(new FeedsDashBoardAdapter.OnDonateClickListener() {
            @Override
            public void onDonateClick(View view, FeedsDashBoardModel obj, int position) {
                Intent intent = new Intent(svContext, ActivityDonate.class);
                intent.putExtra("isMenu", "1");
                svContext.startActivity(intent);
                //customToast.showCustomToast(svContext, "Donate Click " + position, customToast.ToastyError);
            }
        });
        //setOnClickListner();
    }

    private void updateLike(FeedsDashBoardModel obj) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add(obj.getStrId());
        if (obj.getStrisLike().equals("No")) {
            lstUploadData.add("1");
        } else {
            lstUploadData.add("0");
        }
        callWebService(ApiInterface.SETLIKE, lstUploadData);
    }

    private void updateCommentLike(CommentsModel obj) {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add(obj.getStrId());
        if (obj.getStrIsLike().equals("No")) {
            lstUploadData.add("1");
        } else {
            lstUploadData.add("0");
        }
        callWebService(ApiInterface.SETCOMMENTLIKE, lstUploadData);
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) bgDrawable.draw(canvas);
        else canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private void shareReceipt(LinearLayout ll) {
        //Bitmap bitmap = takeScreenshot();
        Bitmap bitmap = getBitmapFromView(ll, ll.getHeight(), ll.getWidth());
        getImageToShare(bitmap);
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File imagefolder = new File(svContext.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(svContext, "com.vhkfoundation.provider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            // putting uri of image to be shared
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            // adding text to share
            intent.putExtra(Intent.EXTRA_TEXT, "Sharing Post");
            // Add subject Here
            intent.putExtra(Intent.EXTRA_SUBJECT, "VHK Foundation");
            // setting type to image
            intent.setType("image/png");
            // calling startactivity() to share
            startActivity(Intent.createChooser(intent, "Share Via"));
        } catch (Exception ex) {
            Log.d("ActivityFeed", ex.getMessage());
            customToast.showCustomToast(svContext, ex.getMessage(), customToast.ToastyError);
        }
        return uri;
    }

}
