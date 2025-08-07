package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.ItemAnimation;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.model.DonationHeaderItem;
import com.vhkfoundation.model.DonationListItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DonationListHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DonationHeaderItem> items;
    private List<DonationListItem> donationListItems;
    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnViewAllClickListener mOnViewAllClickListener;

    private OnLikeClickListener mOnLikeClickListener;

    private OnDonateClickListener mOnDonateClickListener;

    DonationListItemAdapter donationListItemAdapter;

    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, DonationListItem obj, int position,int headerPosition);
    }

    public interface OnViewAllClickListener {
        void onViewAllClick(View view, DonationHeaderItem obj, int position);
    }

    public interface OnLikeClickListener {
        void onLikeClick(View view, DonationListItem obj, int position,int headerPosition);
    }

    public interface OnDonateClickListener {
        void onDonateClick(View view, DonationListItem obj, int position,int headerPosition);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnViewAllClickListener(final OnViewAllClickListener mItemClickListener) {
        this.mOnViewAllClickListener = mItemClickListener;
    }

    public void setOnLikeClickListener(final OnLikeClickListener mItemClickListener) {
        this.mOnLikeClickListener = mItemClickListener;
    }

    public void setOnDonateClickListener(final OnDonateClickListener mDonateClickListener) {
        this.mOnDonateClickListener = mDonateClickListener;
    }

    public DonationListHeaderAdapter(Context context, List<DonationHeaderItem> consolidatedList) {
        this.items = consolidatedList;
        ctx = context;
    }



    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_header,tv_view_all;
        public ImageView img_cat;
        public LinearLayout lyt_parent;
        RecyclerView rv_donate_item;

        public OriginalViewHolder(View v) {
            super(v);
            rv_donate_item = v.findViewById(R.id.rv_donate_item);
            tv_header = v.findViewById(R.id.tv_header);
            tv_view_all = v.findViewById(R.id.tv_view_all);
            lyt_parent = v.findViewById(R.id.root_layout);
//
//            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
//                Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
//                FontUtils.setFont(lyt_parent, font);
//            }
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_header, parent, false);
        vh = new DonationListHeaderAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DonationListHeaderAdapter.OriginalViewHolder) {
            DonationListHeaderAdapter.OriginalViewHolder view = (DonationListHeaderAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            donationListItems = items.get(position).getDonationListItemList();
            //donationListItemAdapter = new DonationListItemAdapter(ctx,items.get(position).getDonationListItemList());
            donationListItemAdapter = new DonationListItemAdapter(ctx,donationListItems);
            view.rv_donate_item.setAdapter(donationListItemAdapter);
            //view.rv_donate_item.setAdapter(new DonationListItemAdapter(ctx,items.get(position).getDonationListItemList()));
            view.rv_donate_item.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
            view.rv_donate_item.setHasFixedSize(true);
            view.tv_header.setText(items.get(position).getTitle());
            //view.tv_cat_name.setText(items.get(position).getStrCatName());
            //ImageLoading.loadLocalImages( Integer.parseInt(items.get(position).getStrCatImage()), view.img_cat);

            //GlobalData.setRandomColor(ctx, view.imgDrawable);
            //GlobalData.setRandomColor(ctx, view.imgDrawable, position);

            int finalPosition = position;
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.tv_view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnViewAllClickListener != null) {
                        mOnViewAllClickListener.onViewAllClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            donationListItemAdapter.setOnItemClickListener(new DonationListItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, DonationListItem obj, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, obj, position,finalPosition);

                    }
                }

            });

            donationListItemAdapter.setOnItemLikeClickListener(new DonationListItemAdapter.OnItemLikeClickListener() {
                @Override
                public void onItemLikeClick(View view, DonationListItem obj, int position) {
                    if (mOnLikeClickListener != null) {
                        mOnLikeClickListener.onLikeClick(view, obj, position,finalPosition);

                    }
                }
            });

            donationListItemAdapter.setOnItemDonateClickListener(new DonationListItemAdapter.OnItemDonateClickListener() {
                @Override
                public void onItemDonateClick(View view, DonationListItem obj, int position) {
                    if (mOnDonateClickListener != null) {
                        mOnDonateClickListener.onDonateClick(view, obj, position,finalPosition);

                    }
                }


            });

            setAnimation(view.itemView, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

    public void setItems(List<DonationHeaderItem> items) {
        //donationListItemAdapter.setItems(items);
        this.items = items;
        //notifyDataSetChanged();
        //donationListItemAdapter.notifyDataSetChanged();
        //this.items = items;
    }

}

