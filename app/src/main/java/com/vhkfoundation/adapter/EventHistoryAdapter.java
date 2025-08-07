package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.ImageLoading;
import com.vhkfoundation.commonutility.ItemAnimation;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.model.ActivityModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ActivityModel> items;
    private final Context ctx;
    private EventHistoryAdapter.OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, ActivityModel obj, int position);
    }

    public void setOnItemClickListener(final EventHistoryAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public EventHistoryAdapter(Context context, List<ActivityModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_cat_name,tvTitle,description,tvMoney;
        public ImageView img_cat,imageSlide;
        public LinearLayout lyt_parent;
        RelativeLayout rl_amount;

        public OriginalViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_title);
            description = v.findViewById(R.id.tv_description);
            tvMoney = v.findViewById(R.id.tv_money);
            imageSlide = v.findViewById(R.id.imageSlide);



            tv_cat_name = v.findViewById(R.id.tv_cat_name);
            img_cat = v.findViewById(R.id.img_cat);
            lyt_parent = v.findViewById(R.id.root_layout);
            rl_amount = v.findViewById(R.id.rl_amount);
            rl_amount.setVisibility(View.GONE);
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setFont(lyt_parent, font);
            }
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_history, parent, false);
        vh = new EventHistoryAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHistoryAdapter.OriginalViewHolder) {
            EventHistoryAdapter.OriginalViewHolder view = (EventHistoryAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            view.tvTitle.setText(items.get(position).getStrTitle());
            view.description.setText(items.get(position).getStrComment());
            Glide.with(ctx)
                    .load(items.get(position).getStrUserImageUrl())
                    .error(es.dmoral.toasty.R.drawable.ic_error_outline_white_24dp)
                    .into(view.imageSlide);


            //view.tv_cat_name.setText(items.get(position).getStrCatName());
            //ImageLoading.loadLocalImages( Integer.parseInt(items.get(position).getStrCatImage()), view.img_cat);

            //GlobalData.setRandomColor(ctx, view.imgDrawable);
            //GlobalData.setRandomColor(ctx, view.imgDrawable, position);

            int finalPosition = position;
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
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


}
