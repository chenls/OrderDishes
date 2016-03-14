package chenls.orderdishes.fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.activity.AckOrderActivity;
import chenls.orderdishes.adapter.OrderRecyclerViewAdapter;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.bean.Order;
import chenls.orderdishes.utils.CommonUtil;
import chenls.orderdishes.utils.serializable.MapSerializable;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ONLY_PAY = "onlyPay";
    public static final String OBJECT_ID = "objectId";
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private ProgressDialog progressDialog;

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view;
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 40);
        swipeRefreshLayout.setRefreshing(true);
        queryDish(false);
        return view;
    }

    //TODO 提交订单后 刷新订单
    @Override
    public void onRefresh() {
        queryDish(true);
    }

    private void queryDish(boolean refresh) {
        if (!CommonUtil.checkNetState(getContext())) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        final BmobQuery<Order> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("username", MyUser.getObjectByKey(getContext(), "username"));
        bmobQuery.setLimit(100);
        bmobQuery.order("-updatedAt");
        //先判断是否强制刷新
        if (refresh) {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);        // 强制在从网络中获取
        } else {
            //先判断是否有缓存
            boolean isCache = bmobQuery.hasCachedResult(getActivity(), Order.class);
            if (isCache) {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
            } else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
            }
        }
        bmobQuery.findObjects(getContext(), new FindListener<Order>() {

            @Override
            public void onSuccess(List<Order> orderList) {
                swipeRefreshLayout.setRefreshing(false);
                initiateView(orderList);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }

            @Override
            public void onError(int code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
    }

    void initiateView(List<Order> orderList) {
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new OrderRecyclerViewAdapter(getContext(), orderList, mListener));
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

    public void onDeleteButtonClick(String objectId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("请稍等...");

        }
        progressDialog.show();
        Order order = new Order();
        order.setObjectId(objectId);
        order.delete(getContext(), objectId, new DeleteListener() {
            @Override
            public void onSuccess() {
                queryDish(true);
            }

            @Override
            public void onFailure(int i, String s) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Toast.makeText(getContext(), "删除失败，请刷新后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onOrderListButtonClick(Order order) {
        Intent intent = new Intent(getActivity(), AckOrderActivity.class);
        Bundle bundle = new Bundle();
        MapSerializable map = new MapSerializable(order.getDishMap());
        bundle.putSerializable(CategoryAndDishFragment.DISH_BEAN_MAP, (Serializable) map.getMap());
        bundle.putString(CategoryAndDishFragment.TOTAL_PRICE, order.getPrice() + "");
        bundle.putString(OBJECT_ID, order.getObjectId());
        bundle.putBoolean(ONLY_PAY, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public interface OnListFragmentInteractionListener {
        void onOrderListButtonClick(Order order);

        void onDeleteButtonClick(String objectId);
    }
}
