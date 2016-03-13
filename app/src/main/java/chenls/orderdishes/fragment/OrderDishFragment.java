package chenls.orderdishes.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.activity.AckOrderActivity;
import chenls.orderdishes.activity.DishDetailActivity;
import chenls.orderdishes.bean.Category;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.utils.CommonUtil;
import chenls.orderdishes.utils.serializable.SerializableMap;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class OrderDishFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    public static final String ORDER_NUM = "order_num";
    public static final String DISH_ITEM = "dish_item";
    public static final String DISH_BEAN_MAP = "dish_bean_map";
    public static final String TOTAL_PRICE = "total_price";
    public static final String TOTAL_NUM = "total_num";
    public static final String POSITION = "position";
    private DishFragment dishFragment;
    private CategoryFragment categoryFragment;
    private TextView dish_category, tv_total_num, tv_total_price;
    private Map<Integer, Dish> dishMap;
    private Dish dish;
    private String last_num;
    private Button bt_compute;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private int position;
    private List<Category> categoryList;

    public OrderDishFragment() {
    }

    public static OrderDishFragment newInstance() {
        return new OrderDishFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_dish, container, false);
        view.findViewById(R.id.fragment_dish_category).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.include_bottom).setVisibility(View.INVISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 40);
        swipeRefreshLayout.setRefreshing(true);
        queryDish(false);
        return view;
    }

    @Override
    public void onRefresh() {
        queryDish(true);
    }

    private void queryDish(boolean refresh) {
        if (!CommonUtil.checkNetState(getContext())) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        final BmobQuery<Dish> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(100);
        bmobQuery.order("category,-updatedAt");
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
            public void onSuccess(List<Dish> dishList) {
                swipeRefreshLayout.setRefreshing(false);
                categoryList = new ArrayList<>();
                categoryList.add(new Category(0, "招牌"));

                List<String> positionList = new ArrayList<>();
                String oldCategory = null;
                for (int i = 0; i < dishList.size(); i++) {
                    Dish dish = dishList.get(i);
                    String newCategory = dish.getCategory();
                    if (oldCategory != null) {
                        if (!newCategory.equals(oldCategory)) {
                            positionList.add("" + i);
                            dishList.add(i, new Dish("0", dish.getCategoryName()));
                            categoryList.add(new Category(i, dish.getCategoryName()));
                        }
                    }
                    oldCategory = newCategory;
                }
                final int size = positionList.size();
                String[] positionArray = positionList.toArray(new String[size]);
                initiateView(dishList, positionArray);
            }

            @Override
            public void onError(int code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initiateView(List<Dish> dishList, String[] positionArray) {
        if (dishMap == null) {
            dishMap = new HashMap<>();
        }
        bt_compute = (Button) view.findViewById(R.id.bt_compute);
        bt_compute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AckOrderActivity.class);
                Bundle bundle = new Bundle();
                SerializableMap map = new SerializableMap(dishMap);
                bundle.putSerializable(DISH_BEAN_MAP, (Serializable) map.getMap());
                bundle.putString(TOTAL_PRICE, tv_total_price.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dish_category = (TextView) view.findViewById(R.id.dish_category);
        tv_total_num = (TextView) view.findViewById(R.id.tv_total_num);
        tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
        dishFragment = DishFragment.newInstance(dishList, positionArray);
        categoryFragment = CategoryFragment.newInstance(categoryList);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linear2, dishFragment);
        fragmentTransaction.replace(R.id.linear1, categoryFragment);
        fragmentTransaction.commit();
        view.findViewById(R.id.fragment_dish_category).setVisibility(View.VISIBLE);
        view.findViewById(R.id.include_bottom).setVisibility(View.VISIBLE);
    }

    public void onDishListFragmentClick(int position, Dish dish, String num) {
        this.position = position;
        this.dish = dish;
        last_num = num;
        Intent intent = new Intent(getActivity(), DishDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putParcelable(DISH_ITEM, dish);
        bundle.putString(ORDER_NUM, num);
        SerializableMap map = new SerializableMap(dishMap);
        bundle.putSerializable(DISH_BEAN_MAP, (Serializable) map.getMap());
        bundle.putString(TOTAL_PRICE, tv_total_price.getText().toString());
        bundle.putString(TOTAL_NUM, tv_total_num.getText().toString());
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, 1);
    }

    public void onDishListButtonClick(int type, int position, Dish dish) {
        int num = dish.getNumber();
        int price = Integer.parseInt(dish.getPrice());
        categoryFragment.setDishNum(type, num);
        String n = tv_total_num.getText().toString();
        if (TextUtils.isEmpty(n) || "0".equals(n)) {
            tv_total_num.setText("1");
            bt_compute.setVisibility(View.VISIBLE);
            tv_total_num.setVisibility(View.VISIBLE);
            tv_total_price.setText(getString(R.string.rmb, price));
        } else {
            int m = Integer.parseInt(n) + num;
            tv_total_num.setText(String.valueOf(m));
            String str = tv_total_price.getText().toString().substring(1);
            if (num < 0) {
                price = -price;
            }
            int p = Integer.parseInt(str) + price;
            tv_total_price.setText(getString(R.string.rmb, p));
            if (m == 0) {
                tv_total_num.setVisibility(View.GONE);
                bt_compute.setVisibility(View.GONE);
            }
        }
        if (dishMap.get(position) == null)
            dishMap.put(position, new Dish(num, price + "", dish.getName(), dish.getPic()));
        else {
            int k = dishMap.get(position).getNumber() + num;
            if (k == 0)
                dishMap.remove(position);
            else
                dishMap.put(position, new Dish(k, price + "", dish.getName(), dish.getPic()));
        }
    }

    public void onDishListScroll(int index) {
        dish_category.setText(categoryList.get(index).getCategoryName());
        categoryFragment.setPosition(index);
    }

    public void onCategoryListFragmentClick(int category_position, int dish_position) {
        dishFragment.setPosition(dish_position);
        dish_category.setText(categoryList.get(category_position).getCategoryName());
    }

    public void myActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String num = data.getStringExtra(ORDER_NUM);
            if (TextUtils.isEmpty(num))
                return;
            //修改dishFragment的UI
            dishFragment.setBookNum(position, num);
            //模拟点击
            int int_last_num;
            if (TextUtils.isEmpty(last_num))
                int_last_num = 0;
            else
                int_last_num = Integer.parseInt(last_num);
            int int_num = Integer.parseInt(num);
            int i = int_num - int_last_num;
            int t;
            if (i > 0)
                t = 1;
            else
                t = -1;
            for (int k = 0; k < Math.abs(i); k++) {
                onDishListButtonClick(t, position, dish);
            }
        }
    }

    public void mySwipeRefreshLayout(boolean b) {
        swipeRefreshLayout.setEnabled(b);
    }
}
