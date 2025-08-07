package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.vhkfoundation.R;
import com.vhkfoundation.model.DonationListItem;
import com.vhkfoundation.model.FeedsDashBoardModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CatWiseDonationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DonationListItem> items;
    private final Context ctx;
    private CatWiseDonationListAdapter.OnItemClickListener mOnItemClickListener;
    private CatWiseDonationListAdapter.OnItemLikeClickListener monItemLikeClickListener;
    private CatWiseDonationListAdapter.OnItemCommetsClickListener monItemCommetsClickListener;
    private CatWiseDonationListAdapter.OnItemShareClickListener monItemShareClickListener;

    private CatWiseDonationListAdapter.OnDonateClickListener mOnDonateClickListener;

    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, DonationListItem obj, int position);
    }

    public interface OnItemLikeClickListener {
        void onItemLikeClick(View view, DonationListItem obj, int position);
    }

    public interface OnItemCommetsClickListener {
        void onItemCommetsClick(View view, DonationListItem obj, int position);
    }

    public interface OnItemShareClickListener {
        void onItemShareClick(View view, DonationListItem obj, int position,LinearLayout linearLayout);
    }

    public interface OnDonateClickListener {
        void onDonateClick(View view, DonationListItem obj, int position);
    }

    public void setOnDonateClickListener(final CatWiseDonationListAdapter.OnDonateClickListener mItemClickListener) {
        this.mOnDonateClickListener = mItemClickListener;
    }

    public void setOnItemClickListener(final CatWiseDonationListAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemLikeClickListener(final CatWiseDonationListAdapter.OnItemLikeClickListener mItemClickListener){
        this.monItemLikeClickListener = mItemClickListener;
    }

    public void setOnItemCommentsClickListener(final CatWiseDonationListAdapter.OnItemCommetsClickListener mItemClickListener){
        this.monItemCommetsClickListener = mItemClickListener;
    }

    public void setOnItemShareClickListener(final CatWiseDonationListAdapter.OnItemShareClickListener mItemClickListener){
        this.monItemShareClickListener = mItemClickListener;
    }

    public CatWiseDonationListAdapter(Context context, List<DonationListItem> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_amount,tv_count_like,tv_donate,tv_desc;
        public ImageView img_cat,iv_like,iv_share;
        public LinearLayout lyt_parent;
        public CardView cv_image;
        public ProgressBar progress_bar;

        public OriginalViewHolder(View v) {
            super(v);
            iv_like= v.findViewById(R.id.iv_like);
            iv_share= v.findViewById(R.id.iv_share);
            tv_title = v.findViewById(R.id.tv_title);
            tv_desc= v.findViewById(R.id.tv_desc);
            tv_amount = v.findViewById(R.id.tv_amount);
            tv_count_like = v.findViewById(R.id.tv_count_like);
            tv_donate = v.findViewById(R.id.tv_donate);
            img_cat = v.findViewById(R.id.img_cat);
            lyt_parent = v.findViewById(R.id.root_layout);
            progress_bar = v.findViewById(R.id.progress_bar);
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_list_cat_wise, parent, false);
        vh = new CatWiseDonationListAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CatWiseDonationListAdapter.OriginalViewHolder) {
            CatWiseDonationListAdapter.OriginalViewHolder view = (CatWiseDonationListAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            view.tv_title.setText(items.get(position).getStrTitle().trim());
            view.tv_desc.setText(items.get(position).getStrDesc().trim());
            view.tv_count_like.setText(items.get(position).getStrTotalLike());


            if(items.get(position).getStrLike().equals("No")){
                view.iv_like.setImageResource(R.drawable.ic_like);

            } else {
                view.iv_like.setImageResource(R.drawable.ic_like_select);
                //view.iv_like.setColorFilter(ContextCompat.getColor(ctx,R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

//            if(items.get(position).getStrDonate().equals("Yes")){
//                view.tv_donate.setVisibility(View.VISIBLE);
//            } else {
//                view.tv_donate.setVisibility(View.GONE);
//            }


            if(!TextUtils.isEmpty(items.get(position).getStrFeedImageUrl())){
                RequestOptions myOptions = new RequestOptions()
                        .override(600, 200);
                Glide.with(ctx)
                        .load(items.get(position).getStrFeedImageUrl())
                        .placeholder(R.drawable.no_image_icon)
                        .apply(new RequestOptions()
                                .override(Target.SIZE_ORIGINAL)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        //.apply(myOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                view.progress_bar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                view.progress_bar.setVisibility(View.GONE);
                                return false;
                            }

                        })
                        .into(view.img_cat);

//                Glide.with(ctx)
//                        .load(items.get(position).getStrFeedImageUrl())
//                        .thumbnail(0.1f)
//                        .into(view.img_cat);
            }

            int finalPosition = position;



            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //((OriginalViewHolder) holder).tv_like_count.setText("250");
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

    public void setItems(List<DonationListItem> items) {
        this.items = items;
    }

}
