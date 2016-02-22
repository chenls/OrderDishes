package chenls.orderdishes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.content.AckOrderContent.AckOrderItem;
import chenls.orderdishes.fragment.DishFragment.OnListFragmentInteractionListener;
import chenls.orderdishes.image.ImageLoader;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AckOrderItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class AckOrderRecyclerViewAdapter extends RecyclerView.Adapter<AckOrderRecyclerViewAdapter.ViewHolder> {

    private final List<AckOrderItem> mValues;
    private final ImageLoader imageLoader;

    public AckOrderRecyclerViewAdapter(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        mValues = chenls.orderdishes.content.AckOrderContent.ITEMS;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_ack_order_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.iv_dish.setImageResource(R.mipmap.address_icon_reserve);
        imageLoader.DisplayImage(holder.mItem.iv_dish, holder.iv_dish, false);
        holder.tv_dish_name.setText(holder.mItem.tv_dish_name);
        holder.tv_dish_num.setText(holder.mItem.tv_dish_num);
        holder.tv_dish_price.setText(holder.mItem.tv_dish_price);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView iv_dish;
        public final TextView tv_dish_name;
        public final TextView tv_dish_num;
        public final TextView tv_dish_price;
        public AckOrderItem mItem;

        public ViewHolder(View view) {
            super(view);
            iv_dish = (ImageView) view.findViewById(R.id.iv_dish);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_dish_num = (TextView) view.findViewById(R.id.tv_dish_num);
            tv_dish_price = (TextView) view.findViewById(R.id.tv_dish_price);
        }
    }
}
