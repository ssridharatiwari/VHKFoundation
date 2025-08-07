package com.vhkfoundation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vhkfoundation.R;
import com.vhkfoundation.model.DateItem;
import com.vhkfoundation.model.DonationHeader;
import com.vhkfoundation.model.DonationItem;
import com.vhkfoundation.model.DonationModel;
import com.vhkfoundation.model.GeneralItem;
import com.vhkfoundation.model.ListItem;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DonationAllItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DonationModel> items;
    List<ListItem> consolidatedList = new ArrayList<>();
    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DonationModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public DonationAllItemAdapter(Context context, List<ListItem> consolidatedList) {
        this.consolidatedList = consolidatedList;
        ctx = context;
    }



    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItem.TYPE_ITEM:
                View v1 = inflater.inflate(R.layout.item_donation_all, parent,
                        false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case ListItem.TYPE_HEADER:
                View v2 = inflater.inflate(R.layout.item_header_donate, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        if (holder instanceof NotificationAdapter.OriginalViewHolder) {
//            NotificationAdapter.OriginalViewHolder view = (NotificationAdapter.OriginalViewHolder) holder;
//            position = holder.getAdapterPosition();
//            view.tv_title.setText(items.get(position).getStrTitle());
//            view.tv_desc.setText(items.get(position).getStrDesc());
//            view.tv_time.setText(items.get(position).getStrTime());
//            int finalPosition = position;
//            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
//                    }
//                }
//            });
//        }

        switch (viewHolder.getItemViewType()) {

            case ListItem.TYPE_ITEM:

                DonationItem generalItem   = (DonationItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) viewHolder;
                //generalViewHolder.tv_title.setText(generalItem.getPojoOfJsonArray().getStrTitle());
                //generalViewHolder.tv_desc.setText(generalItem.getPojoOfJsonArray().getStrDesc());
                //generalViewHolder.tv_time.setText(generalItem.getPojoOfJsonArray().getStrTime());
//                generalViewHolder.root_layout.setOnClickListener(view1 -> {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view1, generalItem.getPojoOfJsonArray(), position);
//                    }
//                });
                break;

            case ListItem.TYPE_HEADER:
                try {
                    DonationHeader dateItem = (DonationHeader) consolidatedList.get(position);
                    DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                    dateViewHolder.tv_header1.setText(dateItem.getTitle());
                } catch (Exception ex){
                    Log.d("error ",ex.getMessage());
                }

                // Populate date item data here

                break;
        }

    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_header1;

        public DateViewHolder(View v) {
            super(v);
            this.tv_header1 = (TextView) v.findViewById(R.id.tv_header1);

        }
    }

    // View holder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_title,tv_desc,tv_time;
        LinearLayout root_layout;

        public GeneralViewHolder(View v) {
            super(v);
            //this.tv_title = (TextView) v.findViewById(R.id.tv_title);
            //this.tv_desc = (TextView) v.findViewById(R.id.tv_desc);
            //this.tv_time = (TextView) v.findViewById(R.id.tv_time);
            //this.root_layout= (LinearLayout) v.findViewById(R.id.root_layout);
        }
    }


    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }

    @Override
    public int getItemViewType(int position){
        return consolidatedList.get(position).getType();
    }



}

