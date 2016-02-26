package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.DishBean;
import chenls.orderdishes.content.DishContent;
import chenls.orderdishes.fragment.CategoryFragment;
import chenls.orderdishes.fragment.DishFragment;
import chenls.orderdishes.utils.SerializableMap;

public class OrderDishActivity extends AppCompatActivity implements
        DishFragment.OnListFragmentInteractionListener,
        CategoryFragment.OnListFragmentInteractionListener,
        SwipeRefreshLayout.OnRefreshListener {
    public static final String ORDER_NUM = "order_num";
    public static final String DISH_ITEM = "dish_item";
    public static final String DISH_BEAN_MAP = "dish_bean_map";
    public static final String TOTAL_PRICE = "total_price";
    public static final String TOTAL_NUM = "total_num";
    private SwipeRefreshLayout swipeRefreshLayout;
    private DishFragment dishFragment;
    private CategoryFragment categoryFragment;
    private TextView dish_category, tv_total_num, tv_total_price;
    private Map<Integer, DishBean> dishBeanMap;
    private DishContent.DishItem item;
    private String last_num;
    private Button bt_compute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);
        if (dishBeanMap == null) {
            dishBeanMap = new HashMap<>();
        }
        bt_compute = (Button) findViewById(R.id.bt_compute);
        bt_compute.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDishActivity.this, AckOrderActivity.class);
                Bundle bundle = new Bundle();
                SerializableMap map = new SerializableMap(dishBeanMap);
                bundle.putSerializable(DISH_BEAN_MAP, (Serializable) map.getMap());
                bundle.putString(TOTAL_PRICE, tv_total_price.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dish_category = (TextView) findViewById(R.id.dish_category);
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        dishFragment = DishFragment.newInstance();
        categoryFragment = CategoryFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linear2, dishFragment);
        fragmentTransaction.add(R.id.linear1, categoryFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //取消刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onDishListFragmentClick(DishContent.DishItem item, String num) {
        this.item = item;
        last_num = num;
        Intent intent = new Intent(this, DishDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DISH_ITEM, item);
        bundle.putString(ORDER_NUM, num);
        SerializableMap map = new SerializableMap(dishBeanMap);
        bundle.putSerializable(DISH_BEAN_MAP, (Serializable) map.getMap());
        bundle.putString(TOTAL_PRICE, tv_total_price.getText().toString());
        bundle.putString(TOTAL_NUM, tv_total_num.getText().toString());
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String num = data.getStringExtra(ORDER_NUM);
            if (TextUtils.isEmpty(num))
                return;
            //修改dishFragment的UI
            dishFragment.setBookNum(item.position, num);
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
                onDishListButtonClick(item.type, t, item.tv_price, item.tv_dish_name, item.position,
                        item.iv_dish);
            }
        }
    }

    @Override
    public void onDishListButtonClick(int type, int num, int price, String name,
                                      int position, String image) {
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
        if (dishBeanMap.get(position) == null)
            dishBeanMap.put(position, new DishBean(num, price, name, image));
        else {
            int k = dishBeanMap.get(position).getNum() + num;
            if (k == 0)
                dishBeanMap.remove(position);
            else
                dishBeanMap.put(position, new DishBean(k, price, name, image));
        }
    }


    @Override
    public void onDishListScroll(int index) {
        dish_category.setText(DishContent.category_names[index]);
        categoryFragment.setPosition(index);
    }


    @Override
    public void onCategoryListFragmentClick(int category_position, int dish_position) {
        dishFragment.setPosition(dish_position);
        dish_category.setText(DishContent.category_names[category_position]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
