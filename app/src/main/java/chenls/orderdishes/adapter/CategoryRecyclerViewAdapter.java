package chenls.orderdishes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Category;
import chenls.orderdishes.fragment.CategoryFragment;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Category> categoryList;
    private final CategoryFragment.OnListFragmentInteractionListener mListener;
    private int current_position = 0; //被选中的条目 默认为第0个
    private Map<Integer, String> bookNumMap;
    private Context context;

    public CategoryRecyclerViewAdapter(Context context, List<Category> categoryList, CategoryFragment.OnListFragmentInteractionListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        mListener = listener;
        bookNumMap = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    private TextView last_category_name;
    private View last_background_left;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == current_position) {
            //设置为选中
            holder.category_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.background_left.setVisibility(View.VISIBLE);
        } else {
            //设置为非选中
            holder.category_name.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.background_left.setVisibility(View.GONE);
        }
        if (position == 0) {
            last_category_name = holder.category_name;
            last_background_left = holder.background_left;
        }

        String num = bookNumMap.get(position);
        if (TextUtils.isEmpty(num)) {
            holder.category_num.setVisibility(View.GONE);
            holder.category_num.setText("0");
        } else {
            holder.category_num.setText(num);
            holder.category_num.setVisibility(View.VISIBLE);
        }

        holder.category_name.setText(categoryList.get(position).getCategoryName());
        //设置tag，方便CategoryFragment通过tag找到View
        holder.mView.setTag(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    last_category_name.setTextColor(context.getResources().getColor(R.color.colorGrey));
                    holder.category_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    last_category_name = holder.category_name;

                    last_background_left.setVisibility(View.GONE);
                    holder.background_left.setVisibility(View.VISIBLE);
                    last_background_left = holder.background_left;
                    current_position = holder.getAdapterPosition();
                    //通知处理点击事件
                    if (current_position == 0) {
                        mListener.onCategoryListFragmentClick(current_position, 0);
                    } else {
                        mListener.onCategoryListFragmentClick(current_position,
                                categoryList.get(current_position).getPosition() + 1);
                    }
                }
            }
        });
    }

    public void changBackground(TextView category_name, View background_left) {
        last_category_name.setTextColor(context.getResources().getColor(R.color.colorGrey));
        category_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        last_category_name = category_name;

        last_background_left.setVisibility(View.GONE);
        background_left.setVisibility(View.VISIBLE);
        last_background_left = background_left;
    }

    public void storeBookNum(int position, String num) {
        if ("0".equals(num))
            bookNumMap.remove(position);
        else
            bookNumMap.put(position, num);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView category_name;
        public final TextView category_num;
        public final View background_left;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            category_name = (TextView) view.findViewById(R.id.category_name);
            category_num = (TextView) view.findViewById(R.id.category_num);
            background_left = view.findViewById(R.id.background_left);
        }
    }
}
