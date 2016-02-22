package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import chenls.orderdishes.R;
import chenls.orderdishes.content.DishContent;
import chenls.orderdishes.image.ImageLoader;

public class DishDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_order_num;
    private ImageView iv_minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        Button bt_compute = (Button) findViewById(R.id.bt_compute);
        bt_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishDetailActivity.this, AckOrderActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        ImageView iv_dish = (ImageView) findViewById(R.id.iv_dish);
        TextView tv_dish_name = (TextView) findViewById(R.id.tv_dish_name);
        TextView tv_signboard = (TextView) findViewById(R.id.tv_signboard);
        TextView tv_dish_summarize = (TextView) findViewById(R.id.tv_dish_summarize);
        TextView tv_comment = (TextView) findViewById(R.id.tv_comment);
        TextView tv_sell_num = (TextView) findViewById(R.id.tv_sell_num);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String order_num = bundle.getString(OrderDishActivity.ORDER_NUM);
        DishContent.DishItem item = bundle.getParcelable(OrderDishActivity.DISH_ITEM);
        assert item != null;
        tv_dish_name.setText(item.tv_dish_name);
        tv_signboard.setVisibility(item.tv_signboard);
        tv_dish_summarize.setText(item.tv_dish_summarize);
        tv_comment.setText(getString(R.string.comment, item.tv_comment));
        tv_sell_num.setText(getString(R.string.sell_num, item.tv_sell_num));
        tv_price.setText(String.valueOf(item.tv_price));
        ratingBar.setRating(item.ratingBar);
        new ImageLoader(DishDetailActivity.this).DisplayImage(item.iv_dish, iv_dish, false, 2000);
        if (!TextUtils.isEmpty(order_num)) {
            iv_minus.setVisibility(View.VISIBLE);
            tv_order_num.setVisibility(View.VISIBLE);
            tv_order_num.setText(order_num);
        }
        iv_add.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                if (iv_minus.getVisibility() == View.GONE) {
                    iv_minus.setVisibility(View.VISIBLE);
                    tv_order_num.setVisibility(View.VISIBLE);
                }
                String order_num = tv_order_num.getText().toString();
                if (TextUtils.isEmpty(order_num))
                    tv_order_num.setText(String.valueOf(1));
                else
                    tv_order_num.setText(String.valueOf(Integer.parseInt(order_num) + 1));
                break;
            case R.id.iv_minus:
                tv_order_num.setText(String.valueOf(Integer.parseInt(
                        tv_order_num.getText().toString()) - 1));
                if (Integer.parseInt(tv_order_num.getText().toString()) == 0) {
                    iv_minus.setVisibility(View.GONE);
                    tv_order_num.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            returnData();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            returnData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnData() {
        Intent intent = new Intent(this, OrderDishActivity.class);
        String order_num = tv_order_num.getText().toString();
        intent.putExtra(OrderDishActivity.ORDER_NUM, order_num);
        setResult(RESULT_OK, intent);
        finish();
    }
}