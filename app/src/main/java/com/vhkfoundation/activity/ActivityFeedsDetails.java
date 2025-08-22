package com.vhkfoundation.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.PreferenceConnector;
import com.vhkfoundation.commonutility.ShowCustomToast;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.databinding.ActFeedsDetailsBinding;
import com.vhkfoundation.retrofit.ApiInterface;
import com.vhkfoundation.retrofit.WebServiceListenerRetroFit;
import com.vhkfoundation.retrofit.WebServiceRetroFit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ActivityFeedsDetails extends BaseActivity implements WebServiceListenerRetroFit {
    private Context svContext;
    private ShowCustomToast customToast;
    private ActFeedsDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActFeedsDetailsBinding.inflate(getLayoutInflater());
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
        if(getIntent().getExtras()!=null){
            String feed_id = getIntent().getStringExtra("feed_id");
            loadData(feed_id);
        }

        binding.llOurServices.setVisibility(View.VISIBLE);
    }

    private void loadToolBar() {
        binding.layActionbar.llBack.setOnClickListener(view -> finish());
        binding.layActionbar.tvTitle.setText("Feed Details");
    }

    private void loadData(String feed_id){
        LinkedList<String> lstUploadData = new LinkedList<>();
        lstUploadData.add(PreferenceConnector.readString(svContext, PreferenceConnector.USERID, ""));
        lstUploadData.add(feed_id);
        callWebService(ApiInterface.GETFEEDDETAILS, lstUploadData);
    }

    private void callWebService(String postUrl, LinkedList<String> lstUploadData) {
        WebServiceRetroFit webService = new WebServiceRetroFit(svContext, postUrl, lstUploadData, this);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    @Override
    public void onWebServiceRetroActionComplete(String result, String url) throws JSONException {
        JSONObject json;
        if (url.contains(ApiInterface.GETFEEDDETAILS)) {
            try {
                json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("1")) {
                    JSONObject jsonObject = new JSONObject(json.getString("data"));
                    binding.tvTitle.setText(jsonObject.getString("title"));
                    binding.tvDate.setText(jsonObject.getString("datetime"));
                    binding.tvDesc.setText(jsonObject.getString("description"));
                    binding.tvUserName.setText(jsonObject.getString("feed_created_by"));

                    Glide.with(svContext)
                            .load(Uri.parse(jsonObject.getString("images")))
                            .placeholder(R.drawable.loader)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(binding.ivPost);
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
}
