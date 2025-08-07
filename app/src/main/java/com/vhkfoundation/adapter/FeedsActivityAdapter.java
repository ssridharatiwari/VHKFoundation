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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedsActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FeedsModel> items;
    private final Context ctx;
    private FeedsActivityAdapter.OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, FeedsModel obj, int position);
    }

    public void setOnItemClickListener(final FeedsActivityAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public FeedsActivityAdapter(Context context, List<FeedsModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_desc;
        ImageView iv_view;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
//            iv_view = v.findViewById(R.id.iv_view);
//            tv_title = v.findViewById(R.id.tv_title);
//            tv_desc = v.findViewById(R.id.tv_desc);
            lyt_parent = v.findViewById(R.id.root_layout);

        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds_activity, parent, false);
        vh = new FeedsActivityAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedsActivityAdapter.OriginalViewHolder) {
            FeedsActivityAdapter.OriginalViewHolder view = (FeedsActivityAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
//            view.tv_title.setText(items.get(position).getStrTitle().trim());
//            view.tv_desc.setText(items.get(position).getStrDesc().trim());
//            int finalPosition = position;
//            view.iv_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
//                    }
//                }
//            });
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
