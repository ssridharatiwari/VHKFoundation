package com.vhkfoundation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.ImageLoading;
import com.vhkfoundation.model.FeedsModel;
import com.vhkfoundation.model.SliderModel;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderModel> sliderItems;
    private ViewPager2 viewPager2;
    private SliderAdapter.OnDonateClickListener mOnDonateClickListener;
    public SliderAdapter(List<SliderModel> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    public interface OnDonateClickListener {
        void onDonateClick(View view, SliderModel obj, int position);
    }

    public void setOnDonateClickListener(final SliderAdapter.OnDonateClickListener mItemClickListener) {
        this.mOnDonateClickListener = mItemClickListener;
    }



    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container, parent, false
                ) );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position),position);
        if (position == sliderItems.size()- 2){
            viewPager2.post(runnable);
        }
        int finalPosition = position;
        holder.tv_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDonateClickListener != null) {
                    mOnDonateClickListener.onDonateClick(view, sliderItems.get(finalPosition), finalPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }


    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tv_donate;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            tv_donate = itemView.findViewById(R.id.tv_donate);
        }

        void setImage(SliderModel sliderItems,int pos) {
            ImageLoading.loadImages(sliderItems.getBanner_img(), imageView, 0);
            //use glide or picasso in case you get image from internet
//            Glide.with(imageView.getContext())
//                    .load(Uri.parse(sliderItems.getBanner_img()))
//                    .placeholder(R.drawable.loader)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .dontAnimate()
//                    .into(imageView);
            //imageView.setImageURI(Uri.parse(sliderItems.getBanner_img()));
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}

