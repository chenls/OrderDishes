package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chenls.orderdishes.R;
import chenls.orderdishes.content.DishContent;
import chenls.orderdishes.fragment.CategoryFragment;
import chenls.orderdishes.fragment.DishFragment;

public class OrderDishActivity extends AppCompatActivity implements
        DishFragment.OnListFragmentInteractionListener,
        CategoryFragment.OnListFragmentInteractionListener,
        SwipeRefreshLayout.OnRefreshListener {
    public static final String ORDER_NUM = "order_num";
    public static final String DISH_ITEM = "dish_item";
    private SwipeRefreshLayout swipeRefreshLayout;
    private DishFragment dishFragment;
    private CategoryFragment categoryFragment;
    private TextView dish_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);
        Button bt_compute = (Button) findViewById(R.id.bt_compute);
        bt_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDishActivity.this, AckOrderActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dish_category = (TextView) findViewById(R.id.dish_category);
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
        Intent intent = new Intent(this, DishDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DISH_ITEM, item);
        bundle.putString(ORDER_NUM, num);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String num = data.getStringExtra(ORDER_NUM);
            //TODO 有可能传回值是0
//            tv_order_num.setText(num);
        }
    }

    @Override
    public void onDishListButtonClick(int type, int num) {
        categoryFragment.setDishNum(type, num);
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
