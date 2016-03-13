package chenls.orderdishes.adapter;

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

    public CategoryRecyclerViewAdapter(List<Category> categoryList, CategoryFragment.OnListFragmentInteractionListener listener) {
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

    private View last_view;
    private View last_background_left;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == current_position) {
            //设置为选中
            holder.mView.setBackgroundResource(R.color.colorWhite);
            holder.background_left.setVisibility(View.VISIBLE);
        } else {
            //设置为非选中
            holder.mView.setBackgroundResource(R.color.colorBackground);
            holder.background_left.setVisibility(View.GONE);
        }
        if (position == 0) {
            last_view = holder.mView;
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
                    last_view.setBackgroundResource(R.color.colorBackground);
                    last_background_left.setVisibility(View.GONE);
                    holder.mView.setBackgroundResource(R.color.colorWhite);
                    holder.background_left.setVisibility(View.VISIBLE);
                    last_view = holder.mView;
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

    public void changBackground(View view, View background_left) {
        last_view.setBackgroundResource(R.color.colorBackground);
        last_background_left.setVisibility(View.GONE);
        view.setBackgroundResource(R.color.colorWhite);
        background_left.setVisibility(View.VISIBLE);
        last_view = view;
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
