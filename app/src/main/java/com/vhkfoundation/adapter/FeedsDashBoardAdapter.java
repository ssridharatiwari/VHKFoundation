package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.model.CommentsModel;
import com.vhkfoundation.model.FeedsDashBoardModel;
import com.vhkfoundation.model.FeedsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedsDashBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FeedsDashBoardModel> items;
    private final Context ctx;
    private FeedsDashBoardAdapter.OnItemClickListener mOnItemClickListener;
    private FeedsDashBoardAdapter.OnItemLikeClickListener monItemLikeClickListener;
    private FeedsDashBoardAdapter.OnItemCommetsClickListener monItemCommetsClickListener;
    private FeedsDashBoardAdapter.OnItemShareClickListener monItemShareClickListener;

    private FeedsDashBoardAdapter.OnDonateClickListener mOnDonateClickListener;

    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, FeedsDashBoardModel obj, int position);
    }

    public interface OnItemLikeClickListener {
        void onItemLikeClick(View view, FeedsDashBoardModel obj, int position);
    }

    public interface OnItemCommetsClickListener {
        void onItemCommetsClick(View view, FeedsDashBoardModel obj, int position);
    }

    public interface OnItemShareClickListener {
        void onItemShareClick(View view, FeedsDashBoardModel obj, int position,LinearLayout linearLayout);
    }

    public interface OnDonateClickListener {
        void onDonateClick(View view, FeedsDashBoardModel obj, int position);
    }

    public void setOnDonateClickListener(final FeedsDashBoardAdapter.OnDonateClickListener mItemClickListener) {
        this.mOnDonateClickListener = mItemClickListener;
    }

    public void setOnItemClickListener(final FeedsDashBoardAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemLikeClickListener(final FeedsDashBoardAdapter.OnItemLikeClickListener mItemClickListener){
        this.monItemLikeClickListener = mItemClickListener;
    }

    public void setOnItemCommentsClickListener(final FeedsDashBoardAdapter.OnItemCommetsClickListener mItemClickListener){
        this.monItemCommetsClickListener = mItemClickListener;
    }

    public void setOnItemShareClickListener(final FeedsDashBoardAdapter.OnItemShareClickListener mItemClickListener){
        this.monItemShareClickListener = mItemClickListener;
    }

    public FeedsDashBoardAdapter(Context context, List<FeedsDashBoardModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_desc,tv_date,tv_donate,tv_like_count,tv_user_name;
        ImageView imageSlide,iv_like,iv_comments,iv_share;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
//            iv_view = v.findViewById(R.id.iv_view);
            tv_title = v.findViewById(R.id.tv_title);
            tv_desc = v.findViewById(R.id.tv_desc);
            iv_like=v.findViewById(R.id.iv_like);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_share=v.findViewById(R.id.iv_share);
            lyt_parent = v.findViewById(R.id.root_layout);
            tv_donate= v.findViewById(R.id.tv_donate);
            imageSlide=v.findViewById(R.id.imageSlide);
            tv_date=v.findViewById(R.id.tv_date);
            tv_like_count=v.findViewById(R.id.tv_like_count);
            tv_user_name=v.findViewById(R.id.tv_user_name);
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds, parent, false);
        vh = new FeedsDashBoardAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedsDashBoardAdapter.OriginalViewHolder) {
            FeedsDashBoardAdapter.OriginalViewHolder view = (FeedsDashBoardAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            view.tv_title.setText(items.get(position).getStrTitle().trim());
            view.tv_desc.setText(items.get(position).getStrDesc().trim());
            view.tv_date.setText(items.get(position).getStrDate().trim());
            view.tv_like_count.setText(items.get(position).getStrLike());
            view.tv_user_name.setText(items.get(position).getStrCreatedBy());

            if(items.get(position).getStrisLike().equals("No")){
                view.iv_like.setImageResource(R.drawable.ic_like);

            } else {
                view.iv_like.setImageResource(R.drawable.ic_like_select);
                //view.iv_like.setColorFilter(ContextCompat.getColor(ctx,R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            if(items.get(position).getStrDonate().equals("Yes")){
                view.tv_donate.setVisibility(View.VISIBLE);
            } else {
                view.tv_donate.setVisibility(View.GONE);
            }


            if(items.get(position).getStrFeedImageUrl()!=null){

//                Glide.with(ctx)
//                        .load(Uri.parse(items.get(position).getStrFeedImageUrl()))
//                        .placeholder(R.drawable.loader)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .dontAnimate()
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                view.imageSlide.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });


                Glide.with(view.imageSlide.getContext())
                        .load(Uri.parse(items.get(position).getStrFeedImageUrl()))
                        .placeholder(R.drawable.loader)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(view.imageSlide);
            }

            int finalPosition = position;
            view.imageSlide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });


            view.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemLikeClickListener != null) {
                        //((OriginalViewHolder) holder).tv_like_count.setText("250");
                        monItemLikeClickListener.onItemLikeClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemShareClickListener != null) {
                        monItemShareClickListener.onItemShareClick(view, items.get(finalPosition), finalPosition,((OriginalViewHolder) holder).lyt_parent);
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

    public void setItems(List<FeedsDashBoardModel> items) {
        this.items = items;
    }

}
