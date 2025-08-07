package com.vhkfoundation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.vhkfoundation.R;
import com.vhkfoundation.model.FeedsModel;
import com.vhkfoundation.model.SliderModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FeedsModel> items;
    private final Context ctx;
    private FeedsAdapter.OnItemClickListener mOnItemClickListener;
    private FeedsAdapter.OnItemLikeClickListener monItemLikeClickListener;
    private FeedsAdapter.OnItemCommetsClickListener monItemCommetsClickListener;
    private FeedsAdapter.OnItemShareClickListener monItemShareClickListener;

    private FeedsAdapter.OnDonateClickListener mOnDonateClickListener;

    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, FeedsModel obj, int position);
    }

    public interface OnItemLikeClickListener {
        void onItemLikeClick(View view, FeedsModel obj, int position);
    }

    public interface OnItemCommetsClickListener {
        void onItemCommetsClick(View view, FeedsModel obj, int position);
    }

    public interface OnItemShareClickListener {
        void onItemShareClick(View view, FeedsModel obj, int position);
    }

    public interface OnDonateClickListener {
        void onDonateClick(View view, FeedsModel obj, int position);
    }

    public void setOnDonateClickListener(final FeedsAdapter.OnDonateClickListener mItemClickListener) {
        this.mOnDonateClickListener = mItemClickListener;
    }

    public void setOnItemClickListener(final FeedsAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemLikeClickListener(final FeedsAdapter.OnItemLikeClickListener mItemClickListener){
        this.monItemLikeClickListener = mItemClickListener;
    }

    public void setOnItemCommentsClickListener(final FeedsAdapter.OnItemCommetsClickListener mItemClickListener){
        this.monItemCommetsClickListener = mItemClickListener;
    }

    public void setOnItemShareClickListener(final FeedsAdapter.OnItemShareClickListener mItemClickListener){
        this.monItemShareClickListener = mItemClickListener;
    }

    public FeedsAdapter(Context context, List<FeedsModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_desc,tv_donate;
        ImageView iv_like,iv_comments,iv_share;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
//            iv_view = v.findViewById(R.id.iv_view);
//            tv_title = v.findViewById(R.id.tv_title);
//            tv_desc = v.findViewById(R.id.tv_desc);
            iv_like=v.findViewById(R.id.iv_like);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_share=v.findViewById(R.id.iv_share);
            lyt_parent = v.findViewById(R.id.root_layout);
            tv_donate= v.findViewById(R.id.tv_donate);
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds, parent, false);
        vh = new FeedsAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedsAdapter.OriginalViewHolder) {
            FeedsAdapter.OriginalViewHolder view = (FeedsAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
//            view.tv_title.setText(items.get(position).getStrTitle().trim());
//            view.tv_desc.setText(items.get(position).getStrDesc().trim());
            int finalPosition = position;
//            view.iv_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
//                    }
//                }
//            });


            view.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemLikeClickListener != null) {
                        monItemLikeClickListener.onItemLikeClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemShareClickListener != null) {
                        monItemShareClickListener.onItemShareClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.iv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemCommetsClickListener != null) {
                        monItemCommetsClickListener.onItemCommetsClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.tv_donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnDonateClickListener != null) {
                        mOnDonateClickListener.onDonateClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

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

    @Override
    public int getItemViewType(int position){
        return position;
    }

}
