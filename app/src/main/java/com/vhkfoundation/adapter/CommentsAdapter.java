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
import com.vhkfoundation.model.CommentsModel;
import com.vhkfoundation.model.FeedsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommentsModel> items;
    private final Context ctx;
    private CommentsAdapter.OnItemClickListener mOnItemClickListener;
    private CommentsAdapter.OnItemLikeClickListener monItemLikeClickListener;
    private CommentsAdapter.OnItemCommetsClickListener monItemCommetsClickListener;
    private CommentsAdapter.OnItemShareClickListener monItemShareClickListener;
    private CommentsAdapter.OnItemActionClickListener monItemActionClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, CommentsModel obj, int position);
    }

    public interface OnItemLikeClickListener {
        void onItemLikeClick(View view, CommentsModel obj, int position);
    }

    public interface OnItemCommetsClickListener {
        void onItemCommetsClick(View view, CommentsModel obj, int position);
    }

    public interface OnItemShareClickListener {
        void onItemShareClick(View view, CommentsModel obj, int position);
    }

    public interface OnItemActionClickListener {
        void onItemActionClick(View view, CommentsModel obj, int position);
    }

    public void setOnItemClickListener(final CommentsAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemLikeClickListener(final CommentsAdapter.OnItemLikeClickListener mItemClickListener){
        this.monItemLikeClickListener = mItemClickListener;
    }

    public void setOnItemCommentsClickListener(final CommentsAdapter.OnItemCommetsClickListener mItemClickListener){
        this.monItemCommetsClickListener = mItemClickListener;
    }

    public void setOnItemShareClickListener(final CommentsAdapter.OnItemShareClickListener mItemClickListener){
        this.monItemShareClickListener = mItemClickListener;
    }

    public void setOnItemActionClickListener(final CommentsAdapter.OnItemActionClickListener mItemClickListener){
        this.monItemActionClickListener = mItemClickListener;
    }

    public void setItems(List<CommentsModel> persons) {
        this.items = persons;
    }

    public CommentsAdapter(Context context, List<CommentsModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_name,tv_date_comment,tv_desc_comment,tv_like_count,tv_comments_count,tv_share_count;
        ImageView iv_like_comments,iv_comments_comments,iv_share_comments,iv_comment_action;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
//            iv_view = v.findViewById(R.id.iv_view);
            tv_user_name = v.findViewById(R.id.tv_user_name);
            tv_date_comment = v.findViewById(R.id.tv_date_comment);
            tv_desc_comment = v.findViewById(R.id.tv_desc_comment);

            tv_like_count = v.findViewById(R.id.tv_like_count);
            tv_comments_count = v.findViewById(R.id.tv_comments_count);
            tv_share_count = v.findViewById(R.id.tv_share_count);

            iv_like_comments=v.findViewById(R.id.iv_like_comments);
            iv_comments_comments=v.findViewById(R.id.iv_comments_comments);
            iv_share_comments=v.findViewById(R.id.iv_share_comments);
            iv_comment_action = v.findViewById(R.id.iv_comment_action);


            lyt_parent = v.findViewById(R.id.root_layout);

        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commets, parent, false);
        vh = new CommentsAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentsAdapter.OriginalViewHolder) {
            CommentsAdapter.OriginalViewHolder view = (CommentsAdapter.OriginalViewHolder) holder;
            position = holder.getAdapterPosition();
            view.tv_user_name.setText(items.get(position).getSteUserName().trim());
            view.tv_date_comment.setText(items.get(position).getStrDate().trim());
            view.tv_desc_comment.setText(items.get(position).getStrComment().trim());

            view.tv_like_count.setText(items.get(position).getStrLike().trim());
            view.tv_comments_count.setText(items.get(position).getStrComments().trim());

            view.iv_share_comments.setVisibility(View.GONE);
            view.iv_comments_comments.setVisibility(View.GONE);
            view.tv_comments_count.setVisibility(View.GONE);

            if(items.get(position).getStrIsLike().equals("No")){
                view.iv_like_comments.setImageResource(R.drawable.ic_like);

            } else {
                view.iv_like_comments.setImageResource(R.drawable.ic_like_select);
                //view.iv_like.setColorFilter(ContextCompat.getColor(ctx,R.color.orange), android.graphics.PorterDuff.Mode.MULTIPLY);
            }


            int finalPosition = position;
//            view.iv_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view, items.get(finalPosition), finalPosition);
//                    }
//                }
//            });


            view.iv_like_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemLikeClickListener != null) {
                        monItemLikeClickListener.onItemLikeClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.iv_share_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemShareClickListener != null) {
                        monItemShareClickListener.onItemShareClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.iv_comments_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemCommetsClickListener != null) {
                        monItemCommetsClickListener.onItemCommetsClick(view, items.get(finalPosition), finalPosition);
                    }
                }
            });

            view.iv_comment_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (monItemActionClickListener != null) {
                        monItemActionClickListener.onItemActionClick(view, items.get(finalPosition), finalPosition);
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
        if(items.size()>0){
            return items.size();
        } else {
            return 0;
        }

    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    @Override
    public int getItemViewType(int position){
        return position;
    }

}
