package chenls.orderdishes.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.DishBean;
import chenls.orderdishes.utils.ConsigneeMessage.ConsigneeMessageUtil;

public class AckOrderRecyclerViewAdapter extends RecyclerView.Adapter<AckOrderRecyclerViewAdapter.ViewHolder> {

    public static final int BUTTON_NATIVE = 0;
    public static final int BUTTON_OUTER = 1;
    public static final int BUTTON_ONLINE = 2;
    public static final int BUTTON_CASH = 3;
    public static final int BUTTON_MARK = 4;
    private final Map<Integer, DishBean> dishBeanMap;
    private Context context;
    private OnClickListenerInterface mListener;

    public AckOrderRecyclerViewAdapter(Context context, Map<Integer, DishBean> dishBeanMap) {
        this.context = context;
        this.dishBeanMap = dishBeanMap;

        if (context instanceof OnClickListenerInterface) {
            mListener = (OnClickListenerInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickListenerInterface");
        }
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
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.activity_ack_order_item_first, parent, false);
            return new ViewHolder(view, viewType);
        }
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_ack_order_item_other, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Map<String, String> map = ConsigneeMessageUtil.GetMessage(context);
            holder.consignee_name.setText(map.get("name"));
            holder.consignee_tel.setText(map.get("tel"));
            holder.consignee_address.setText(map.get("address"));
            holder.consignee_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.mipmap.select);
                    builder.setTitle(context.getString(R.string.choose));
                    final String s[] = new String[]{context.getString(R.string._native), context.getString(R.string.outer)};
                    builder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    mListener.OnClickListener(BUTTON_NATIVE);
                                    break;
                                case 1:
                                    mListener.OnClickListener(BUTTON_OUTER);
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            holder.rl_play_online.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.iv_play_online.setBackgroundResource(R.mipmap.check_on);
                    holder.iv_play_cash.setBackgroundResource(R.mipmap.check_off);
                    mListener.OnClickListener(BUTTON_ONLINE);
                }
            });
            holder.rl_play_cash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.iv_play_online.setBackgroundResource(R.mipmap.check_off);
                    holder.iv_play_cash.setBackgroundResource(R.mipmap.check_on);
                    mListener.OnClickListener(BUTTON_CASH);
                }
            });
            holder.mark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnClickListener(BUTTON_MARK);
                }
            });
            return;
        }
        holder.mItem = dishBeanMap.get(getKey(dishBeanMap)[position - 1]);
        holder.iv_dish.setImageResource(R.mipmap.loading);
        Glide.with(context)
                .load(holder.mItem.getImage())
                .crossFade()
                .placeholder(R.mipmap.loading)
                .into(holder.iv_dish);
        holder.tv_dish_name.setText(holder.mItem.getName());
        int num = holder.mItem.getNum();
        holder.tv_dish_num.setText(context.getString(R.string.product_sign, num));
        holder.tv_dish_price.setText(context.getString(R.string.rmb, holder.mItem.getPrice() * num));
    }

    @Override
    public int getItemCount() {
        return dishBeanMap.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_dish;
        public TextView tv_dish_name;
        public TextView tv_dish_num;
        public TextView tv_dish_price;
        public DishBean mItem;
        public RelativeLayout consignee_message, rl_play_online, rl_play_cash, mark;
        public TextView consignee_name, consignee_tel, consignee_address;
        public ImageView iv_play_online, iv_play_cash;

        public ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == 1) {
                consignee_message = (RelativeLayout) view.findViewById(R.id.consignee_message);
                consignee_name = (TextView) view.findViewById(R.id.order_name);
                consignee_tel = (TextView) view.findViewById(R.id.pay_num);
                consignee_address = (TextView) view.findViewById(R.id.consignee_address);
                rl_play_online = (RelativeLayout) view.findViewById(R.id.rl_play_online);
                rl_play_cash = (RelativeLayout) view.findViewById(R.id.rl_play_cash);
                iv_play_online = (ImageView) view.findViewById(R.id.iv_play_online);
                iv_play_cash = (ImageView) view.findViewById(R.id.iv_play_cash);
                mark = (RelativeLayout) view.findViewById(R.id.mark);
                return;
            }
            iv_dish = (ImageView) view.findViewById(R.id.iv_dish);
            tv_dish_name = (TextView) view.findViewById(R.id.tv_dish_name);
            tv_dish_num = (TextView) view.findViewById(R.id.tv_dish_num);
            tv_dish_price = (TextView) view.findViewById(R.id.tv_dish_price);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        return 0;
    }

    public interface OnClickListenerInterface {
        void OnClickListener(int id);
    }
}
