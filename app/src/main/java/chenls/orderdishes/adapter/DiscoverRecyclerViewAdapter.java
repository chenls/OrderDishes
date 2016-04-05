package chenls.orderdishes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.fragment.DiscoverFragment;

public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.ViewHolder> {

    private final List<Dish> dishList;
    private final DiscoverFragment.OnListFragmentInteractionListener mListener;
    private Context context;

    public DiscoverRecyclerViewAdapter(Context context, List<Dish> dishList, DiscoverFragment.OnListFragmentInteractionListener listener) {
        this.context = context;
        this.dishList = dishList;
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
        final Dish dish = dishList.get(position);
        Glide.with(context)
                .load(dish.getPic().getFileUrl(context))
                .placeholder(R.mipmap.loading)
                .into(holder.iv_dish);
        holder.tv_dish_name.setText(dish.getName());
        holder.ratingBar.setRating(Integer.parseInt(dish.getStar()));
        holder.tv_comment.setText(context.getString(R.string.comment
                , Integer.parseInt(dish.getCommentNumber())));
        holder.tv_sell_num.setText(context.getString(R.string.sell_num
                , Integer.parseInt(dish.getSellNumber())));
        holder.tv_price.setText(String.valueOf(dish.getPrice()));
        holder.tv_dish_summarize.setText(context.getString(
                R.string.dish_summarize, dish.getSummarize()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    dish.setId(dish.getObjectId());
                    mListener.onDiscoverListItemClick(dish);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView iv_dish;
        public TextView tv_dish_name, tv_dish_summarize, tv_comment, tv_sell_num, tv_price;
        public RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            iv_dish = (ImageView) view.findViewById(R.id.image);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_title);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_sell_num = (TextView) view.findViewById(R.id.tv_sell_num);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_dish_summarize = (TextView) view.findViewById(R.id.tv_dish_summarize);
//            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
        }
    }
}
