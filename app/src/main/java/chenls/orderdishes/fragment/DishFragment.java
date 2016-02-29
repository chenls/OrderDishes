package chenls.orderdishes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.DishRecyclerViewAdapter;
import chenls.orderdishes.content.DishContent;
import chenls.orderdishes.image.ImageLoader;
import chenls.orderdishes.utils.CommonUtil;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DishFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private boolean move;
    private int position;
    private DishRecyclerViewAdapter dishRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DishFragment() {
    }

    public static DishFragment newInstance() {
        DishFragment fragment = new DishFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
            ImageLoader imageLoader = new ImageLoader(getActivity());

            //如果每个item大小固定，设置这个属性可以提高性能
            recyclerView.setHasFixedSize(true);
            dishRecyclerViewAdapter = new DishRecyclerViewAdapter(getActivity(), imageLoader, mListener);
            recyclerView.setAdapter(dishRecyclerViewAdapter);
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    secondScroll();
                    scrollEvent(dy);
                }
            });

        }
        return view;
    }

    //滚动事件处理
    private void scrollEvent(int dy) {
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        //上拉
        if (dy > 0) {
            int index = Arrays.binarySearch(DishContent.title_position, firstItem);
            if (index >= 0) {
                mListener.onDishListScroll(index + 1);
            }
        }
        //下拉
        else {
            int index = Arrays.binarySearch(DishContent.title_position, firstItem + 1);
            if (index >= 0) {
                mListener.onDishListScroll(index);
            }
        }
    }

    private void secondScroll() {
        //在这里进行第二次滚动（最后的100米！）
        if (move) {
            move = false;
            //获取要置顶的项在当前屏幕的位置，position是记录的要置顶项在RecyclerView中的位置
            int n = position - linearLayoutManager.findFirstVisibleItemPosition();
            if (0 <= n && n < recyclerView.getChildCount()) {
                //获取要置顶的项顶部离RecyclerView顶部的距离
                int top = recyclerView.getChildAt(n).getTop();
                //最后的移动
                recyclerView.scrollBy(0, top - 20);
            }
        }
    }

    public void setPosition(int n) {
        this.position = n;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            // 当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
            recyclerView.scrollToPosition(position);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(n);
            // 这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    public void setBookNum(final int position, final String num) {
        //保存数据 便于恢复界面
        dishRecyclerViewAdapter.saveData(position, num);
        ViewGroup view = (ViewGroup) recyclerView.findViewWithTag(position);
        ImageView iv_minus = (ImageView)  CommonUtil.findViewInViewGroupById(view, R.id.iv_minus);
        TextView tv_order_num = (TextView)  CommonUtil.findViewInViewGroupById(view, R.id.tv_order_num);
        assert tv_order_num != null;
        assert iv_minus != null;
        if ("0".equals(num)) {
            tv_order_num.setText("0");
            tv_order_num.setVisibility(View.GONE);
            iv_minus.setVisibility(View.GONE);
        } else {
            tv_order_num.setText(num);
            tv_order_num.setVisibility(View.VISIBLE);
            iv_minus.setVisibility(View.VISIBLE);
        }
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
        void onDishListFragmentClick(DishContent.DishItem item, String num);

        void onDishListButtonClick(int pint, int num, int price, String name, int position, String image);

        void onDishListScroll(int index);
    }
}
