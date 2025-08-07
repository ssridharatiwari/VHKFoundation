package com.vhkfoundation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vhkfoundation.R;
import com.vhkfoundation.commonutility.GlobalVariables;
import com.vhkfoundation.commonutility.ImageLoading;
import com.vhkfoundation.commonutility.ItemAnimation;
import com.vhkfoundation.commonutility.customfont.FontUtils;
import com.vhkfoundation.model.CategoryModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> items;
    private final Context ctx;
    private AllCategoryAdapter.OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, CategoryModel obj, int position);
    }

    public void setOnItemClickListener(final AllCategoryAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AllCategoryAdapter(Context context, List<CategoryModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_cat_name;
        public ImageView img_cat;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tv_cat_name = v.findViewById(R.id.tv_cat_name);
            img_cat = v.findViewById(R.id.img_cat);
            lyt_parent = v.findViewById(R.id.root_layout);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        vh = new AllCategoryAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AllCategoryAdapter.OriginalViewHolder) {
            AllCategoryAdapter.OriginalViewHolder view = (AllCategoryAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            view.tv_cat_name.setText(items.get(position).getStrCatName());
            ImageLoading.loadLocalImages( Integer.parseInt(items.get(position).getStrCatImage()), view.img_cat);

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
