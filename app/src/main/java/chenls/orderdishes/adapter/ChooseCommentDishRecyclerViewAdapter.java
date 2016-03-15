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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.activity.CommentActivity;
import chenls.orderdishes.bean.Dish;

public class ChooseCommentDishRecyclerViewAdapter extends
        RecyclerView.Adapter<ChooseCommentDishRecyclerViewAdapter.ViewHolder> {

    public static final String DISH = "dish";
    private final Map<Integer, Dish> dishMap;
    private final List<Dish> list;
    private Context context;

    public ChooseCommentDishRecyclerViewAdapter(Context context, Map<Integer, Dish> dishMap) {
        this.context = context;
        this.dishMap = dishMap;
        list = mapTransitionList(dishMap);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_choose_comment_dish_item, parent, false);
        return new ViewHolder(view);
    }

    //map转换成list
    public static List<Dish> mapTransitionList(Map<Integer, Dish> map) {
        List<Dish> list = new ArrayList<>();
        for (Map.Entry<Integer, Dish> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Dish dish = list.get(position);
        Glide.with(context)
                .load(dish.getPic().getFileUrl(context))
                .placeholder(R.mipmap.loading)
                .into(holder.iv_dish);
        holder.tv_dish_name.setText(dish.getName());
        holder.tv_price.setText(context.getString(R.string.rmb, dish.getPrice()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra(DISH, (Serializable) dish);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView iv_dish;
        public TextView tv_dish_name, tv_price;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            iv_dish = (ImageView) view.findViewById(R.id.iv_dish);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }
    }
}
