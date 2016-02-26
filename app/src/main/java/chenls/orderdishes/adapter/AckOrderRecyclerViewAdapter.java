package chenls.orderdishes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.DishBean;
import chenls.orderdishes.content.AckOrderContent.AckOrderItem;
import chenls.orderdishes.fragment.DishFragment.OnListFragmentInteractionListener;
import chenls.orderdishes.image.ImageLoader;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AckOrderItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AckOrderRecyclerViewAdapter extends RecyclerView.Adapter<AckOrderRecyclerViewAdapter.ViewHolder> {

    private final ImageLoader imageLoader;
    private final Map<Integer, DishBean> dishBeanMap;
    private Context context;

    public AckOrderRecyclerViewAdapter(Context context, ImageLoader imageLoader, Map<Integer, DishBean> dishBeanMap) {
        this.context = context;
        this.imageLoader = imageLoader;
        this.dishBeanMap = dishBeanMap;
    }

    private int[] getKey(Map<Integer, DishBean> map) {
        int[] re = new int[map.size()];
        int index = 0;
        for (Integer i : map.keySet()) {
            re[index++] = i;
        }
        Arrays.sort(re);
        return re;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_ack_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = dishBeanMap.get(getKey(dishBeanMap)[position]);
        holder.iv_dish.setImageResource(R.mipmap.address_icon_reserve);
        imageLoader.DisplayImage(holder.mItem.getImage(), holder.iv_dish, false);
        holder.tv_dish_name.setText(holder.mItem.getName());
        int num = holder.mItem.getNum();
        holder.tv_dish_num.setText(context.getString(R.string.product_sign, num));
        holder.tv_dish_price.setText(context.getString(R.string.rmb, holder.mItem.getPrice() * num));
    }

    @Override
    public int getItemCount() {
        return dishBeanMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView iv_dish;
        public final TextView tv_dish_name;
        public final TextView tv_dish_num;
        public final TextView tv_dish_price;
        public DishBean mItem;

        public ViewHolder(View view) {
            super(view);
            iv_dish = (ImageView) view.findViewById(R.id.iv_dish);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_dish_num = (TextView) view.findViewById(R.id.tv_dish_num);
            tv_dish_price = (TextView) view.findViewById(R.id.tv_dish_price);
        }
    }
}
