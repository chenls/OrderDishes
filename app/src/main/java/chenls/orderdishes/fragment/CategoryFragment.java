package chenls.orderdishes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.CategoryRecyclerViewAdapter;
import chenls.orderdishes.bean.Category;
import chenls.orderdishes.utils.CommonUtil;
import chenls.orderdishes.utils.serializable.SerializableCategoryList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CategoryFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String CATEGORY_LIST = "category_list";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CategoryRecyclerViewAdapter myCategoryRecyclerViewAdapter;
    private List<Category> categoryList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(List<Category> categoryList) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        SerializableCategoryList serializableCategoryList =
                new SerializableCategoryList(categoryList);
        args.putSerializable(CATEGORY_LIST, (Serializable) serializableCategoryList.getCategoryList());
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            categoryList = (List<Category>) getArguments().getSerializable(CATEGORY_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
            //如果每个item大小固定，设置这个属性可以提高性能
            recyclerView.setHasFixedSize(true);
            myCategoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(categoryList, mListener);
            recyclerView.setAdapter(myCategoryRecyclerViewAdapter);
        }
        return view;
    }
    //TODO 将来再更新此处理方式

    /**
     * 由于scrollToPosition()是异步的，滚动后不能立即生效
     * 所以延时100ms后获取view对象
     * 处理方式不够完美，暂且这样吧
     *
     * @param position 位置
     */
    public void setPosition(final int position) {
        recyclerView.scrollToPosition(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewGroup view = (ViewGroup) recyclerView.findViewWithTag(position);
                View background_left = CommonUtil.findViewInViewGroupById(view, R.id.background_left);
                myCategoryRecyclerViewAdapter.changBackground(view, background_left);
            }
        }, 100);
    }

    public void setDishNum(final int position, final int num) {
        setPosition(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewGroup view = (ViewGroup) recyclerView.findViewWithTag(position);
                TextView category_num = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.category_num);
                assert category_num != null;
                int old_num = 0;
                String value = category_num.getText().toString();
                if (!TextUtils.isEmpty(value)) {
                    old_num = Integer.parseInt(value);
                }
                int new_num = old_num + num;
                category_num.setText(String.valueOf(new_num));
                if (new_num == 0) {
                    category_num.setVisibility(View.GONE);
                } else {
                    category_num.setVisibility(View.VISIBLE);
                }
                myCategoryRecyclerViewAdapter.storeBookNum(position, Integer.toString(new_num));
            }
        }, 100);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onCategoryListFragmentClick(int category_position, int dish_position);
    }
}
