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

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.fragment.DishFragment.OnListFragmentInteractionListener;


public class DishRecyclerViewAdapter extends RecyclerView.Adapter<DishRecyclerViewAdapter.ViewHolder> {

    private final List<Dish> dishList;
    private final OnListFragmentInteractionListener mListener;
    private final static int IS_TITLE = 1;
    private final Context context;
    private Map<Integer, String> bookDishMap;

    public DishRecyclerViewAdapter(Context context, List<Dish> dishList, OnListFragmentInteractionListener listener) {
        this.context = context;
        this.dishList = dishList;
        mListener = listener;
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
            holder.dish_category.setText(dishList.get(position).getName());
            return;
        }
        final Dish dish = dishList.get(position);
        Glide.with(context)
                .load(dish.getPic().getFileUrl(context))
                .crossFade()
                .placeholder(R.mipmap.loading)
                .into(holder.iv_dish);
        holder.tv_dish_name.setText(dish.getName());
        holder.tv_dish_summarize.setText(dish.getSummarize());
        holder.tv_comment.setText(context.getString(R.string.comment
                , Integer.parseInt(dish.getCommentNumber())));
        holder.tv_sell_num.setText(context.getString(R.string.sell_num
                , Integer.parseInt(dish.getSellNumber())));
        holder.tv_price.setText(String.valueOf(dish.getPrice()));
        holder.ratingBar.setRating(Integer.parseInt(dish.getStar()));
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
                    dish.setId(dish.getObjectId());
                    mListener.onDishListFragmentClick(holder.getAdapterPosition(), dish, holder.tv_order_num.getText().toString());
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
        Dish dish = dishList.get(holder.getAdapterPosition());
        mListener.onDishListButtonClick(1, dish, holder.getAdapterPosition());
        saveData(holder.getAdapterPosition(), holder.tv_order_num.getText().toString());
    }

    private void minusClick(ViewHolder holder) {
        String num = holder.tv_order_num.getText().toString();
        holder.tv_order_num.setText(String.valueOf(Integer.parseInt(num) - 1));
        if ("1".equals(num)) {
            holder.iv_minus.setVisibility(View.GONE);
            holder.tv_order_num.setVisibility(View.GONE);
        }
        Dish dish = dishList.get(holder.getAdapterPosition());
        mListener.onDishListButtonClick(-1, dish, holder.getAdapterPosition());
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
        return dishList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String category = dishList.get(position).getCategory();
        if ("0".equals(category)) {
            return IS_TITLE;
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView iv_dish, iv_minus, iv_add;
        public TextView tv_dish_name, tv_dish_summarize, tv_comment, tv_sell_num, tv_price, tv_order_num;
        public RatingBar ratingBar;
        public TextView dish_category;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == IS_TITLE) {
                dish_category = (TextView) view.findViewById(R.id.dish_category);
                return;
            }
            mView = view;
            iv_dish = (ImageView) view.findViewById(R.id.image);
            iv_minus = (ImageView) view.findViewById(R.id.iv_minus);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_title);
            tv_dish_summarize = (TextView) view.findViewById(R.id.tv_dish_summarize);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_sell_num = (TextView) view.findViewById(R.id.tv_sell_num);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_order_num = (TextView) view.findViewById(R.id.tv_order_num);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }
}
