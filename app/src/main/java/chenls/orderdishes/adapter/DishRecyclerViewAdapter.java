package chenls.orderdishes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.content.DishContent;
import chenls.orderdishes.content.DishContent.DishItem;
import chenls.orderdishes.fragment.DishFragment.OnListFragmentInteractionListener;
import chenls.orderdishes.image.ImageLoader;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DishItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class DishRecyclerViewAdapter extends RecyclerView.Adapter<DishRecyclerViewAdapter.ViewHolder> {

    private final List<DishItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final static int IS_TITLE = 1;
    private final ImageLoader imageLoader;
    private final Context context;

    public DishRecyclerViewAdapter(Context context, ImageLoader imageLoader, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = DishContent.ITEMS;
        mListener = listener;
        this.imageLoader = imageLoader;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == IS_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.fragment_dish_category, parent, false);
            return new ViewHolder(view, viewType);
        }
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_dish, parent, false);
        return new ViewHolder(view, viewType);
    }

//    Handler UIHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            ViewHolder holder = (ViewHolder) msg.obj;
//            if (holder.mView.getTag() == 1) {
//
//            }
//        }
//    };

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == IS_TITLE) {
            for (int i = 0; i < DishContent.title_position.length; i++) {
                if (DishContent.title_position[i] == position) {
                    holder.dish_category.setText(DishContent.category_names[i + 1]);
                    return;
                }
            }
            return;
        }
        holder.mItem = mValues.get(position);
        holder.iv_dish.setImageResource(R.mipmap.address_icon_reserve);
        imageLoader.DisplayImage(holder.mItem.iv_dish, holder.iv_dish, false);
        holder.tv_dish_name.setText(holder.mItem.tv_dish_name);
        holder.tv_signboard.setVisibility(holder.mItem.tv_signboard);
        holder.tv_dish_summarize.setText(holder.mItem.tv_dish_summarize);
        holder.tv_comment.setText(context.getString(R.string.comment, holder.mItem.tv_comment));
        holder.tv_sell_num.setText(context.getString(R.string.sell_num, holder.mItem.tv_sell_num));
        holder.tv_price.setText(String.valueOf(holder.mItem.tv_price));
        holder.ratingBar.setRating(holder.mItem.ratingBar);
        //TODO  恢复数据有问题
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDishListFragmentClick(holder.mItem, holder.tv_order_num.getText().toString());
                }
            }
        });
        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.iv_minus.getVisibility() == View.GONE) {
                    holder.iv_minus.setVisibility(View.VISIBLE);
                    holder.tv_order_num.setVisibility(View.VISIBLE);
                }
                String s = holder.tv_order_num.getText().toString();
                if (TextUtils.isEmpty(s))
                    holder.tv_order_num.setText(String.valueOf(1));
                else
                    holder.tv_order_num.setText(String.valueOf(Integer.parseInt(s) + 1));
                //通知数据
                mListener.onDishListButtonClick(holder.mItem.type, 1);
            }
        });
        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_order_num.setText(String.valueOf(Integer.parseInt(
                        holder.tv_order_num.getText().toString()) - 1));
                if (Integer.parseInt(holder.tv_order_num.getText().toString()) == 0) {
                    holder.iv_minus.setVisibility(View.GONE);
                    holder.tv_order_num.setVisibility(View.GONE);
                }
                //通知数据
                mListener.onDishListButtonClick(holder.mItem.type, -1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        for (int m : DishContent.title_position) {
            if (position == m) {
                return IS_TITLE;
            }
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView iv_dish, iv_minus, iv_add;
        public TextView tv_dish_name, tv_signboard, tv_dish_summarize, tv_comment, tv_sell_num, tv_price, tv_order_num;
        public RatingBar ratingBar;
        public DishItem mItem;
        public TextView dish_category;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == IS_TITLE) {
                dish_category = (TextView) view.findViewById(R.id.dish_category);
                return;
            }
            mView = view;
            iv_dish = (ImageView) view.findViewById(R.id.iv_dish);
            iv_minus = (ImageView) view.findViewById(R.id.iv_minus);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_signboard = (TextView) view.findViewById(R.id.tv_signboard);
            tv_dish_summarize = (TextView) view.findViewById(R.id.tv_dish_summarize);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_sell_num = (TextView) view.findViewById(R.id.tv_sell_num);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_order_num = (TextView) view.findViewById(R.id.tv_order_num);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }
}
