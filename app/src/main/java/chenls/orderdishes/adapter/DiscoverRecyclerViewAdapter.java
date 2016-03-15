package chenls.orderdishes.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.activity.CommentActivity;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.fragment.DiscoverFragment;

public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.ViewHolder> {

    private final List<Dish> mValues;
    private final DiscoverFragment.OnListFragmentInteractionListener mListener;
    private Context context;
    private boolean isPraise;

    public DiscoverRecyclerViewAdapter(Context context, List<Dish> items, DiscoverFragment.OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_discover, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Dish dish = mValues.get(position);
        holder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
        holder.iv_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPraise) {
                    isPraise = false;
                    holder.iv_favour.setImageDrawable(context.getResources()
                            .getDrawable(R.mipmap.praise_not));
                } else {
                    isPraise = true;
                    holder.iv_favour.setImageDrawable(context.getResources()
                            .getDrawable(R.mipmap.praise));
                }
            }
        });
        Glide.with(context)
                .load(dish.getPic().getFileUrl(context))
                .placeholder(R.mipmap.loading)
                .into(holder.iv_dish_pic);
        holder.tv_dish_name.setText(dish.getName());
        holder.tv_dish_summarize.setText(dish.getSummarize());
        holder.tv_comment.setText(dish.getCommentNumber());
//        holder.tv_all_comment.setText(dish.getAllComment());
        holder.tv_name1.setText(dish.getName());
        holder.tv_name2.setText(dish.getSummarize());
        holder.tv_name3.setText(dish.getSummarize());
        holder.tv_comment1.setText(dish.getSummarize());
        holder.tv_comment2.setText(dish.getSummarize());
        holder.tv_comment3.setText(dish.getSummarize());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDiscoverListFragmentInteraction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView iv_dish_pic, iv_comment, iv_favour;
        public TextView tv_dish_name, tv_dish_summarize, tv_comment, tv_all_comment, tv_name1, tv_name2,
                tv_name3, tv_comment1, tv_comment2, tv_comment3;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            iv_dish_pic = (ImageView) view.findViewById(R.id.iv_dish_pic);
            iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
            iv_favour = (ImageView) view.findViewById(R.id.iv_favour);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_dish_summarize = (TextView) view.findViewById(R.id.tv_dish_summarize);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_all_comment = (TextView) view.findViewById(R.id.tv_all_comment);
            tv_name1 = (TextView) view.findViewById(R.id.tv_name1);
            tv_name2 = (TextView) view.findViewById(R.id.tv_name2);
            tv_name3 = (TextView) view.findViewById(R.id.tv_name3);
            tv_comment1 = (TextView) view.findViewById(R.id.tv_comment1);
            tv_comment2 = (TextView) view.findViewById(R.id.tv_comment2);
            tv_comment3 = (TextView) view.findViewById(R.id.tv_comment3);
        }
    }
}
