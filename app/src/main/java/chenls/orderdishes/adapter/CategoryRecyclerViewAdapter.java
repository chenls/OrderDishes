package chenls.orderdishes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.content.CategoryContent.CategoryItem;
import chenls.orderdishes.content.DishContent;
import chenls.orderdishes.fragment.CategoryFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CategoryItem} and makes a call to the
 * specified {@link CategoryFragment.OnListFragmentInteractionListener}.
 */
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<CategoryItem> mValues;
    private final CategoryFragment.OnListFragmentInteractionListener mListener;
    private boolean isFirstItem = true;


    public CategoryRecyclerViewAdapter(List<CategoryItem> items, CategoryFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.mItem = mValues.get(position);
        holder.category_name.setText(mValues.get(position).category_name);
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
                    if (holder.getAdapterPosition() == 0) {
                        mListener.onCategoryListFragmentClick(holder.getAdapterPosition(), 0);
                    } else {
                        mListener.onCategoryListFragmentClick(holder.getAdapterPosition(),
                                DishContent.title_position[holder.getAdapterPosition() - 1] + 1);
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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView category_name;
        public final TextView category_num;
        public final View background_left;
        public CategoryItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            category_name = (TextView) view.findViewById(R.id.category_name);
            category_num = (TextView) view.findViewById(R.id.category_num);
            background_left = view.findViewById(R.id.background_left);
            if (isFirstItem) {
                last_view = mView;
                last_background_left = background_left;
                mView.setBackgroundResource(R.color.colorWhite);
                background_left.setVisibility(View.VISIBLE);
                isFirstItem = false;
            }
        }
    }
}
