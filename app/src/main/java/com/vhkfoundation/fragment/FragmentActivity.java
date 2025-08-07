package com.vhkfoundation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vhkfoundation.R;
import com.vhkfoundation.activity.ActivityActivitiesDetails;
import com.vhkfoundation.activity.ActivityMain;
import com.vhkfoundation.adapter.ActivityAdapter;
import com.vhkfoundation.adapter.FeedsActivityAdapter;
import com.vhkfoundation.model.ActivityModel;
import com.vhkfoundation.model.FeedsModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends Fragment {

    List<ActivityModel> lstActivityModels;
    ActivityAdapter activityAdapter;
    private View aiView = null;

    RecyclerView rv_activities,rv_feeds;


    FeedsActivityAdapter feedsActivityAdapter;
    List<FeedsModel> lstfeedsmodel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_activity, container, false);
        }
        return aiView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startApp();
    }

    private void startApp() {
        rv_activities = aiView.findViewById(R.id.rv_activities);
        rv_feeds = aiView.findViewById(R.id.rv_feeds);
        LoadFeedsData();
        LoadActivityData();
        ImageView iv_create_post = ((ActivityMain)getActivity()).findViewById(R.id.iv_create_post);
        iv_create_post.setVisibility(View.GONE);


    }

    private void LoadActivityData() {
        lstActivityModels = new ArrayList<>();

        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));
        lstActivityModels.add(new ActivityModel("","","","","",""));

        rv_activities.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_activities.setHasFixedSize(true);
        activityAdapter = new ActivityAdapter(getActivity(),lstActivityModels,0);
        rv_activities.setAdapter(activityAdapter);

        activityAdapter.setOnItemClickListener(new ActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ActivityModel obj, int position) {
                Intent intent = new Intent(getActivity(), ActivityActivitiesDetails.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void LoadFeedsData() {
        lstfeedsmodel=new ArrayList<>();
        lstfeedsmodel.add(new FeedsModel("","","","","",""));
        lstfeedsmodel.add(new FeedsModel("","","","","",""));
        lstfeedsmodel.add(new FeedsModel("","","","","",""));
        lstfeedsmodel.add(new FeedsModel("","","","","",""));
        rv_feeds.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        //rv_feeds.setHasFixedSize(true);

        feedsActivityAdapter = new FeedsActivityAdapter(getActivity(), lstfeedsmodel,0);
        rv_feeds.setAdapter(feedsActivityAdapter);

    }
}
