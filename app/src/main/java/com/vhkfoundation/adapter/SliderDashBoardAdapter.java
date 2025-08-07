package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.ImageLoading;
import com.vhkfoundation.model.FeedsDashBoardModel;
import com.vhkfoundation.model.SliderModel;

import java.util.List;

public class SliderDashBoardAdapter extends RecyclerView.Adapter<SliderDashBoardAdapter.SliderViewHolder> {
    private List<FeedsDashBoardModel> sliderItems;
    private ViewPager2 viewPager2;
    private final Context ctx;
    private SliderDashBoardAdapter.OnDonateClickListener mOnDonateClickListener;
    private SliderDashBoardAdapter.OnItemLikeClickListener monItemLikeClickListener;
    private SliderDashBoardAdapter.OnItemCommetsClickListener monItemCommetsClickListener;
    public SliderDashBoardAdapter(Context context,List<FeedsDashBoardModel> sliderItems, ViewPager2 viewPager2) {
        this.ctx = context;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    public interface OnDonateClickListener {
        void onDonateClick(View view, FeedsDashBoardModel obj, int position);
    }

    public interface OnItemLikeClickListener {
        void onItemLikeClick(View view, FeedsDashBoardModel obj, int position);
    }

    public interface OnItemCommetsClickListener {
        void onItemCommetsClick(View view, FeedsDashBoardModel obj, int position);
    }

    public void setOnDonateClickListener(final SliderDashBoardAdapter.OnDonateClickListener mItemClickListener) {
        this.mOnDonateClickListener = mItemClickListener;
    }

    public void setOnItemLikeClickListener(final SliderDashBoardAdapter.OnItemLikeClickListener mItemClickListener){
        this.monItemLikeClickListener = mItemClickListener;
    }

    public void setOnItemCommentsClickListener(final SliderDashBoardAdapter.OnItemCommetsClickListener mItemClickListener){
        this.monItemCommetsClickListener = mItemClickListener;
    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new SliderViewHolder(
//                LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.slide_item_container, parent, false
//                ) );
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_emergency, parent, false
                ) );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position),position);
        if (position == sliderItems.size()-3){
            viewPager2.post(runnable);
        }
        int finalPosition = position;
        holder.tv_title.setText(sliderItems.get(position).getStrTitle());
        holder.tv_desc.setText(sliderItems.get(position).getStrDesc());
        holder.tv_like_count.setText(sliderItems.get(position).getStrLike());
        holder.tv_user_name.setText(sliderItems.get(position).getStrCreatedBy());

        if(sliderItems.get(position).getStrisLike().equals("No")){
            holder.iv_like.setImageResource(R.drawable.ic_like);
        } else {
            holder.iv_like.setImageResource(R.drawable.ic_like_select);
            //view.iv_like.setColorFilter(ContextCompat.getColor(ctx,R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        if(sliderItems.get(position).getStrDonate().equals("Yes")){
            holder.tv_donate.setVisibility(View.VISIBLE);
        } else {
            holder.tv_donate.setVisibility(View.GONE);
        }



        holder.tv_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mOnDonateClickListener != null) {
                    mOnDonateClickListener.onDonateClick(view, sliderItems.get(finalPosition), finalPosition);
                }
            }
        });

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monItemLikeClickListener != null) {
                    //((OriginalViewHolder) holder).tv_like_count.setText("250");
                    monItemLikeClickListener.onItemLikeClick(view, sliderItems.get(finalPosition), finalPosition);
                }
            }
        });

        holder.iv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monItemCommetsClickListener != null) {
                    monItemCommetsClickListener.onItemCommetsClick(view, sliderItems.get(finalPosition), finalPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }


    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView,iv_like,iv_comments;
        private TextView tv_title,tv_desc,tv_donate,tv_like_count,tv_user_name;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            tv_donate = itemView.findViewById(R.id.tv_donate);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_like_count= itemView.findViewById(R.id.tv_like_count);
            iv_like=itemView.findViewById(R.id.iv_like);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            iv_comments=itemView.findViewById(R.id.iv_comments);
        }

        void setImage(FeedsDashBoardModel sliderItems,int pos) {
            if(sliderItems.getStrFeedImageUrl()!=null){

//                Glide.with(ctx)
//                        .load(Uri.parse(sliderItems.getStrFeedImageUrl()))
//                        .placeholder(R.drawable.loader)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .dontAnimate()
//                        .into(new CustomTarget<Drawable>() {
//                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                imageView.setBackground(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });


            Glide.with(imageView.getContext())
                    .load(Uri.parse(sliderItems.getStrFeedImageUrl()))
                    .placeholder(R.drawable.loader)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(imageView);




                //imageView.setImageURI(Uri.parse(sliderItems.getBanner_img()));

            }
            //ImageLoading.loadImages(sliderItems.getBanner_img(), imageView, 0);
            //use glide or picasso in case you get image from internet
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public void setItems(List<FeedsDashBoardModel> items) {
        this.sliderItems = items;
    }
}

