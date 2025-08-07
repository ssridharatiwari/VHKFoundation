package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.bumptech.glide.load.engine.GlideException;

import com.bumptech.glide.request.RequestOptions;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.ItemAnimation;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.model.DonationListItem;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class DonationListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DonationListItem> items;
    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemDonateClickListener mOnItemDonateClickListener;
    private DonationListItemAdapter.OnItemLikeClickListener monItemLikeClickListener;

    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, DonationListItem obj, int position);
    }

    public interface OnItemDonateClickListener {
        void onItemDonateClick(View view, DonationListItem obj, int position);
    }

    public interface OnItemLikeClickListener {
        void onItemLikeClick(View view, DonationListItem obj, int position);
    }


    public void setOnItemDonateClickListener(final OnItemDonateClickListener mItemClickListener) {
        this.mOnItemDonateClickListener = mItemClickListener;
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public DonationListItemAdapter(Context context, List<DonationListItem> consolidatedList) {
        this.items = consolidatedList;
        ctx = context;
    }

    public void setOnItemLikeClickListener(final DonationListItemAdapter.OnItemLikeClickListener mItemClickListener){
        this.monItemLikeClickListener = mItemClickListener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_amount,tv_count_like,tv_donate,tv_desc;
        public ImageView img_cat,iv_like;
        public LinearLayout lyt_parent;
        public CardView cv_image;
        public ProgressBar progress_bar;

        public OriginalViewHolder(View v) {
            super(v);
            tv_title = v.findViewById(R.id.tv_title);
            tv_amount = v.findViewById(R.id.tv_amount);
            tv_count_like = v.findViewById(R.id.tv_count_like);
            tv_donate = v.findViewById(R.id.tv_donate);
            img_cat = v.findViewById(R.id.img_cat);
            lyt_parent = v.findViewById(R.id.root_layout);
            progress_bar = v.findViewById(R.id.progress_bar);
            iv_like = v.findViewById(R.id.iv_like);
            tv_desc = v.findViewById(R.id.tv_desc);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) lyt_parent.getLayoutParams();

            DisplayMetrics lDisplayMetrics = ctx.getResources().getDisplayMetrics();
            int widthPixels = lDisplayMetrics.widthPixels;
            int heightPixels = lDisplayMetrics.heightPixels;

            if(items!=null && items.size()>1){
                params.width=widthPixels/2+100;
                params.height=RecyclerView.LayoutParams.WRAP_CONTENT;
                //params.height=heightPixels*28/100;
            } else {
                params.width=widthPixels-75;
                params.height=RecyclerView.LayoutParams.WRAP_CONTENT;
            }

            lyt_parent.setLayoutParams(params);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donation_list, parent, false);
        vh = new DonationListItemAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DonationListItemAdapter.OriginalViewHolder) {
            DonationListItemAdapter.OriginalViewHolder view = (DonationListItemAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            view.tv_title.setText(items.get(position).getStrTitle());
            view.tv_amount.setText(items.get(position).getStrTarget());
            view.tv_count_like.setText(items.get(position).getStrTotalLike());
            view.tv_desc.setText(items.get(position).getStrDesc());

            if(items.get(position).getStrLike().equals("No")){
                view.iv_like.setImageResource(R.drawable.ic_like);

            } else {
                view.iv_like.setImageResource(R.drawable.ic_like_select);
                //view.iv_like.setColorFilter(ContextCompat.getColor(ctx,R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

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
            //ImageLoading.loadLocalImages( Integer.parseInt(items.get(position).getStrCatImage()), view.img_cat);

            int finalPosition = position;
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
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

            view.tv_donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemDonateClickListener != null) {
                        //((OriginalViewHolder) holder).tv_like_count.setText("250");
                        mOnItemDonateClickListener.onItemDonateClick(view, items.get(finalPosition), finalPosition);
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

    public void setItems(List<DonationListItem> items) {
        this.items = items;
    }

}

