package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.AckOrderRecyclerViewAdapter;
import chenls.orderdishes.bean.DishBean;
import chenls.orderdishes.image.ImageLoader;

public class AckOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ack_order);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Map<Integer, DishBean> dishBeanMap = (Map<Integer, DishBean>)
                bundle.getSerializable(OrderDishActivity.DISH_BEAN_MAP);
        TextView tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        String total_price = bundle.getString(OrderDishActivity.TOTAL_PRICE);
        tv_total_price.setText(total_price);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        //如果每个item大小固定，设置这个属性可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AckOrderActivity.this));
        ImageLoader imageLoader = new ImageLoader(AckOrderActivity.this);
        recyclerView.setAdapter(new AckOrderRecyclerViewAdapter(AckOrderActivity.this, imageLoader, dishBeanMap));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
