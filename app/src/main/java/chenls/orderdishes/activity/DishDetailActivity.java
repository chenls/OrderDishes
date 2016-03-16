package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.bean.MyComment;
import chenls.orderdishes.fragment.CategoryAndDishFragment;
import chenls.orderdishes.utils.CommonUtil;
import chenls.orderdishes.utils.MyScrollView;
import chenls.orderdishes.utils.serializable.MapSerializable;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class DishDetailActivity extends AppCompatActivity
        implements View.OnClickListener
        , SwipeRefreshLayout.OnRefreshListener
        , MyScrollView.ScrollChangedInterface {

    private TextView tv_order_num, tv_total_num;
    private ImageView iv_minus;
    private Map<Integer, Dish> dishBeanMap;
    private TextView tv_total_price;
    private Button bt_compute;
    private Dish dish;
    private int position;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        bt_compute = (Button) findViewById(R.id.bt_compute);
        bt_compute.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishDetailActivity.this, AckOrderActivity.class);
                Bundle bundle = new Bundle();
                MapSerializable map = new MapSerializable(dishBeanMap);
                bundle.putSerializable(CategoryAndDishFragment.DISH_BEAN_MAP, (Serializable) map.getMap());
                bundle.putString(CategoryAndDishFragment.TOTAL_PRICE, tv_total_price.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        ImageView iv_dish = (ImageView) findViewById(R.id.iv_dish);
        TextView tv_dish_name = (TextView) findViewById(R.id.tv_dish_name);
        TextView tv_dish_summarize = (TextView) findViewById(R.id.tv_dish_summarize);
        TextView tv_comment = (TextView) findViewById(R.id.tv_comment);
        TextView tv_sell_num = (TextView) findViewById(R.id.tv_sell_num);
        TextView tv_price = (TextView) findViewById(R.id.tv_price);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String p = bundle.getString(CategoryAndDishFragment.TOTAL_PRICE);
        if (TextUtils.isEmpty(p))
            tv_total_price.setText(getString(R.string.rmb_0));
        else
            tv_total_price.setText(p);
        String total_num = bundle.getString(CategoryAndDishFragment.TOTAL_NUM);
        if (!TextUtils.isEmpty(total_num) && !"0".equals(total_num)) {
            tv_total_num.setText(total_num);
            tv_total_num.setVisibility(View.VISIBLE);
            bt_compute.setVisibility(View.VISIBLE);
        }
        String order_num = bundle.getString(CategoryAndDishFragment.ORDER_NUM);
        if (!(TextUtils.isEmpty(order_num) || "0".equals(order_num))) {
            iv_minus.setVisibility(View.VISIBLE);
            tv_order_num.setVisibility(View.VISIBLE);
            tv_order_num.setText(order_num);
        }
        position = bundle.getInt(CategoryAndDishFragment.POSITION);
        dish = bundle.getParcelable(CategoryAndDishFragment.DISH_ITEM);
        assert tv_dish_name != null;
        assert dish != null;
        tv_dish_name.setText(dish.getName());
        tv_dish_summarize.setText(dish.getSummarize());
        tv_comment.setText(getString(R.string.comment, Integer.parseInt(dish.getCommentNumber())));
        tv_sell_num.setText(getString(R.string.sell_num, Integer.parseInt(dish.getSellNumber())));
        tv_price.setText(String.valueOf(dish.getPrice()));
        ratingBar.setRating(Integer.valueOf(dish.getStar()));
        Glide.with(DishDetailActivity.this)
                .load(dish.getPic().getFileUrl(DishDetailActivity.this))
                .crossFade()
                .placeholder(R.mipmap.loading)
                .into(iv_dish);
        iv_add.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        dishBeanMap = (Map<Integer, Dish>)
                bundle.getSerializable(CategoryAndDishFragment.DISH_BEAN_MAP);
        if (dishBeanMap == null) {
            dishBeanMap = new HashMap<>();
        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        TextView comment = (TextView) findViewById(R.id.comment);
        comment.setText(R.string.loading);
        queryMyComment(false);
    }

    @Override
    public void onScrollChanged() {
        MyScrollView view = (MyScrollView) findViewById(R.id.scrollView);
        if (view.getScrollY() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onRefresh() {
        queryMyComment(true);
    }

    private void queryMyComment(boolean refresh) {
        if (!CommonUtil.checkNetState(DishDetailActivity.this)) {
            return;
        }
        final BmobQuery<MyComment> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(100);
        bmobQuery.addWhereEqualTo("dishObjectId", dish.getId());
        bmobQuery.order("-updatedAt");
        //先判断是否强制刷新
        if (refresh) {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);        // 强制在从网络中获取
        } else {
            //先判断是否有缓存
            boolean isCache = bmobQuery.hasCachedResult(DishDetailActivity.this, MyComment.class);
            if (isCache) {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
            } else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
            }
        }
        bmobQuery.findObjects(DishDetailActivity.this, new FindListener<MyComment>() {
            @Override
            public void onSuccess(List<MyComment> myCommentList) {
                TextView comment = (TextView) findViewById(R.id.comment);
                SpannableStringBuilder commentValue = new SpannableStringBuilder();
                for (MyComment myComment : myCommentList) {
                    int commentValueLength = commentValue.length();
                    String name = myComment.getName();
                    commentValue.append(String.format("%s：%s分  %s",
                            name, myComment.getStart(), myComment.getComment())
                            + "\n\n");
                    int endLength = commentValueLength + name.length() + 1;
                    commentValue.setSpan(new ForegroundColorSpan(
                                    getResources().getColor(R.color.colorAccent))
                            , commentValueLength
                            , endLength
                            //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
                            , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    commentValue.setSpan(new ForegroundColorSpan(
                                    getResources().getColor(R.color.colorPrimary))
                            , endLength
                            , endLength + 4
                            //setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
                            , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                int length = commentValue.length();
                commentValue.delete(length - 2, length);//删除最后两个\n\n字符
                comment.setText(commentValue);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String order_num = tv_order_num.getText().toString();
        String total_num = tv_total_num.getText().toString();
        String total_price = tv_total_price.getText().toString().substring(1);
        DecimalFormat df = new DecimalFormat("######0.00");
        switch (v.getId()) {
            case R.id.iv_add:
                //预定数量
                if (TextUtils.isEmpty(order_num) || "0".equals(order_num)) {
                    tv_order_num.setText("1");
                    iv_minus.setVisibility(View.VISIBLE);
                    tv_order_num.setVisibility(View.VISIBLE);
                } else
                    tv_order_num.setText(String.valueOf(Integer.parseInt(order_num) + 1));
                //总数量
                if (TextUtils.isEmpty(total_num) || "0".equals(total_num)) {
                    tv_total_num.setText("1");
                    tv_total_num.setVisibility(View.VISIBLE);
                    bt_compute.setVisibility(View.VISIBLE);
                } else {
                    tv_total_num.setText(String.valueOf(Integer.valueOf(total_num) + 1));
                }
                //总价
                Double p1 = Double.parseDouble(total_price) + Double.parseDouble(dish.getPrice());
                tv_total_price.setText(getString(R.string.rmb, df.format(p1)));
                break;
            case R.id.iv_minus:
                //预定数量
                tv_order_num.setText(String.valueOf(Integer.parseInt(order_num) - 1));
                if ("1".equals(order_num)) {
                    iv_minus.setVisibility(View.GONE);
                    tv_order_num.setVisibility(View.GONE);
                }
                //总数量
                if ("1".equals(total_num)) {
                    tv_total_num.setText("0");
                    tv_total_num.setVisibility(View.GONE);
                    bt_compute.setVisibility(View.GONE);
                } else {
                    tv_total_num.setText(String.valueOf(Integer.valueOf(total_num) - 1));
                }
                //总价
                Double p2 = Double.parseDouble(total_price) - Double.parseDouble(dish.getPrice());
                tv_total_price.setText(getString(R.string.rmb, df.format(p2)));
                break;
        }
        //修改map中点菜的数量
        String tv_order_num_value = tv_order_num.getText().toString();
        if ("0".equals(tv_order_num_value))
            dishBeanMap.remove(position);
        else
            dishBeanMap.put(position, new Dish(dish.getObjectId(), Integer.parseInt(tv_order_num_value),
                    dish.getPrice(), dish.getName(), dish.getPic()));
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
        Intent intent = new Intent(this, MainActivity.class);
        String order_num = tv_order_num.getText().toString();
        intent.putExtra(CategoryAndDishFragment.ORDER_NUM, order_num);
        setResult(RESULT_OK, intent);
        finish();
    }
}