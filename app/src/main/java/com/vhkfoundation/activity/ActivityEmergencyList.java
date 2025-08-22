package com.vhkfoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vhkfoundation.R;
import com.vhkfoundation.adapter.AllCategoryAdapter;
import com.vhkfoundation.adapter.CommentsAdapter;
import com.vhkfoundation.adapter.EmergencyHelpAdapter;
import com.vhkfoundation.adapter.FeedsDashBoardAdapter;
import com.vhkfoundation.adapter.SliderDashBoardAdapter;
import com.vhkfoundation.commonutility.CheckInternet;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.NoInternetScreen;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActCategoryListBinding;
import com.vhkfoundation.databinding.ActEmergencyHelpListBinding;
import com.vhkfoundation.model.CategoryModel;
import com.vhkfoundation.model.CommentsModel;
import com.vhkfoundation.model.EmergencyHelpModel;
import com.vhkfoundation.model.FeedsDashBoardModel;
import com.vhkfoundation.model.SliderModel;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActivityEmergencyList extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;



    ActEmergencyHelpListBinding binding;

    LinkedList<String> lstUploadData = new LinkedList<>();
    List<FeedsDashBoardModel> lstemergencyfeedsmodel;

    FeedsDashBoardAdapter feedsDashBoardAdapter;

    int itemPosition = 0;

    String strFeedId;
    List<CommentsModel> lstCommentsModels;
    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActEmergencyHelpListBinding.inflate(getLayoutInflater());
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
        loadToolBar();
        getData();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.layActionbar.tvTitle.setText("Emergency Help List");
    }

    private void getData() {
        lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add("0");
        callWebService(ApiInterface.GETFEEDLIST, lstUploadData);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json = null;
        if (url.contains(ApiInterface.GETFEEDLIST)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    JSONArray data_emergency_data = json.getJSONArray("data");
                    if (data_emergency_data != null && data_emergency_data.length() > 0) {
                        lstemergencyfeedsmodel = new ArrayList<>();
                        for (int i = 0; i < data_emergency_data.length(); i++) {
                            JSONObject jsonObj = data_emergency_data.getJSONObject(i);
                            lstemergencyfeedsmodel.add(new FeedsDashBoardModel(jsonObj.getString("id"), jsonObj.getString("title"), jsonObj.getString("description"), jsonObj.getString("is_emergency"), jsonObj.getString("is_donate"), "", jsonObj.getString("datetime"), jsonObj.getString("images"), jsonObj.getString("total_like"), "0", jsonObj.getString("is_like"), jsonObj.getString("feed_created_by")));
                        }
                        bindEmergencyData(lstemergencyfeedsmodel);
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
                    feedsDashBoardAdapter.setItems(lstemergencyfeedsmodel);
                    feedsDashBoardAdapter.notifyDataSetChanged();
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
                } else {
                    bindCommentsData();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
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
        } else if (url.contains(ApiInterface.POSTCOMMENT)) {
            try {
                json = new JSONObject(result);
                //String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    JSONObject jsonObj = new JSONObject(json.getString("data"));

                    lstCommentsModels.add(0, new CommentsModel(jsonObj.getString("id"), jsonObj.getString("comment_by_name"), jsonObj.getString("datetime"), jsonObj.getString("comment"), jsonObj.getString("total_comment_like"), "", jsonObj.getString("user_comment_like")));
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

        binding.rvEmergencyHelp.setLayoutManager(new LinearLayoutManager(svContext));
        binding.rvEmergencyHelp.setHasFixedSize(true);

        feedsDashBoardAdapter = new FeedsDashBoardAdapter(svContext, lstfeedsmodel, 0);
        binding.rvEmergencyHelp.setAdapter(feedsDashBoardAdapter);

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
                itemPosition = position;
                updateLike(obj);
                //customToast.showCustomToast(svContext, "Like Click", customToast.ToastyError);
            }
        });

        feedsDashBoardAdapter.setOnItemCommentsClickListener(new FeedsDashBoardAdapter.OnItemCommetsClickListener() {
            @Override
            public void onItemCommetsClick(View view, FeedsDashBoardModel obj, int position) {
                strFeedId = obj.getStrId();
                loadCommentsData(obj.getStrId());
            }
        });

        feedsDashBoardAdapter.setOnItemShareClickListener(new FeedsDashBoardAdapter.OnItemShareClickListener() {
            @Override
            public void onItemShareClick(View view, FeedsDashBoardModel obj, int position, LinearLayout ll) {
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


        ViewGroup.LayoutParams params1 = rv_comments.getLayoutParams();
        params1.height = getResources().getDimensionPixelSize(R.dimen.dp_400);
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
                }
            }
        });
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
}
