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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Integer, String> bookDishMap;

    public DishRecyclerViewAdapter(Context context, ImageLoader imageLoader, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = DishContent.ITEMS;
        mListener = listener;
        this.imageLoader = imageLoader;
        bookDishMap = new HashMap<>();
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.getItemViewType() == IS_TITLE) {
            for (int i = 0; i < DishContent.title_position.length; i++) {
                if (DishContent.title_position[i] == holder.getAdapterPosition()) {
                    holder.dish_category.setText(DishContent.category_names[i + 1]);
                    return;
                }
            }
            return;
        }
        holder.mItem = mValues.get(holder.getAdapterPosition());
        holder.iv_dish.setImageResource(R.mipmap.address_icon_reserve);
        imageLoader.DisplayImage(holder.mItem.iv_dish, holder.iv_dish, false);
        holder.tv_dish_name.setText(holder.mItem.tv_dish_name);
        holder.tv_signboard.setVisibility(holder.mItem.tv_signboard);
        holder.tv_dish_summarize.setText(holder.mItem.tv_dish_summarize);
        holder.tv_comment.setText(context.getString(R.string.comment, holder.mItem.tv_comment));
        holder.tv_sell_num.setText(context.getString(R.string.sell_num, holder.mItem.tv_sell_num));
        holder.tv_price.setText(String.valueOf(holder.mItem.tv_price));
        //TODO 重写RatingBar
        holder.ratingBar.setRating(holder.mItem.ratingBar);
        String num = bookDishMap.get(position);
        if (TextUtils.isEmpty(num)) {
            holder.tv_order_num.setText("0");
            holder.iv_minus.setVisibility(View.GONE);
            holder.tv_order_num.setVisibility(View.GONE);
        } else {
            holder.tv_order_num.setText(num);
            holder.iv_minus.setVisibility(View.VISIBLE);
            holder.tv_order_num.setVisibility(View.VISIBLE);
        }
        //设置tag，方便DishFragment通过tag找到View
        holder.mView.setTag(position);
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
                addClick(holder);
            }
        });
        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusClick(holder);
            }
        });
    }


    private void addClick(ViewHolder holder) {
        if (holder.iv_minus.getVisibility() == View.GONE) {
            holder.iv_minus.setVisibility(View.VISIBLE);
            holder.tv_order_num.setVisibility(View.VISIBLE);
        }
        String s = holder.tv_order_num.getText().toString();
        if (TextUtils.isEmpty(s))
            holder.tv_order_num.setText(String.valueOf(1));
        else
            holder.tv_order_num.setText(String.valueOf(Integer.parseInt(s) + 1));
        mListener.onDishListButtonClick(holder.mItem.type, 1, holder.mItem.tv_price,
                holder.mItem.tv_dish_name, holder.getAdapterPosition(), holder.mItem.iv_dish);
        saveData(holder.getAdapterPosition(), holder.tv_order_num.getText().toString());
    }

    private void minusClick(ViewHolder holder) {
        String num = holder.tv_order_num.getText().toString();
        holder.tv_order_num.setText(String.valueOf(Integer.parseInt(num) - 1));
        if ("1".equals(num)) {
            holder.iv_minus.setVisibility(View.GONE);
            holder.tv_order_num.setVisibility(View.GONE);
        }
        mListener.onDishListButtonClick(holder.mItem.type, -1, holder.mItem.tv_price,
                holder.mItem.tv_dish_name, holder.getAdapterPosition(), holder.mItem.iv_dish);
        saveData(holder.getAdapterPosition(), holder.tv_order_num.getText().toString());
    }

    //保存数据
    public void saveData(int position, String num) {
        if ("0".equals(num))
            bookDishMap.remove(position);
        else
            bookDishMap.put(position, num);
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
