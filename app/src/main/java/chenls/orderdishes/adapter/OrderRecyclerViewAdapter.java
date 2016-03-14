package chenls.orderdishes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Order;
import chenls.orderdishes.fragment.OrderFragment;


public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final List<Order> orderList;
    private final OrderFragment.OnListFragmentInteractionListener mListener;

    public OrderRecyclerViewAdapter(Context context, List<Order> orderList, OrderFragment.OnListFragmentInteractionListener listener) {
        this.context = context;
        this.orderList = orderList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order order = orderList.get(position);
//        Glide.with(context)
//                .load(order.getDishMap())
//        iv_dish
//        tv_dish_name
        holder.tv_time.setText(order.getCreatedAt());
        holder.tv_state.setText(order.getState()+"");
        holder.tv_price.setText(order.getPrice()+"");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOrderListFragmentInteraction(order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final ImageView iv_dish;
        private final TextView tv_dish_name, tv_time, tv_state, tv_price;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            iv_dish = (ImageView) view.findViewById(R.id.iv_dish);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
        }
    }
}
