package chenls.orderdishes.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.activity.DishDetailActivity;
import chenls.orderdishes.adapter.DiscoverRecyclerViewAdapter;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;

    public DiscoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 40);
        swipeRefreshLayout.setRefreshing(true);
        queryDish(false);
        return view;
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

    public void onDiscoverListItemClick(Dish dish) {
        Intent intent = new Intent(getContext(), DishDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CategoryAndDishFragment.DISH_ITEM, dish);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public interface OnListFragmentInteractionListener {
        void onDiscoverListItemClick(Dish dish);
    }

    @Override
    public void onRefresh() {
        queryDish(true);
    }

    private void queryDish(boolean refresh) {
        if (!CommonUtil.checkNetState(getActivity())) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        final BmobQuery<Dish> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("category", "1");
        bmobQuery.setLimit(10);
        bmobQuery.order("-updatedAt");
        //先判断是否强制刷新
        if (refresh) {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);        // 强制在从网络中获取
        } else {
            //先判断是否有缓存
            boolean isCache = bmobQuery.hasCachedResult(getActivity(), Dish.class);
            if (isCache) {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
            } else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
            }
        }
        bmobQuery.findObjects(getContext(), new FindListener<Dish>() {

            @Override
            public void onSuccess(List<Dish> object) {
                swipeRefreshLayout.setRefreshing(false);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
//                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new DiscoverRecyclerViewAdapter(getContext(), object, mListener));
            }

            @Override
            public void onError(int code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
