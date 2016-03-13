package chenls.orderdishes.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.AckOrderRecyclerViewAdapter;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.bean.Order;
import chenls.orderdishes.fragment.CategoryAndDishFragment;
import chenls.orderdishes.utils.CommonUtil;
import chenls.orderdishes.utils.serializable.MapSerializable;
import cn.bmob.v3.listener.SaveListener;

public class AckOrderActivity extends AppCompatActivity implements AckOrderRecyclerViewAdapter.OnClickListenerInterface {
    public static final String CONSIGNEE_NAME = "consignee_name";
    public static final String CONSIGNEE_TEL = "consignee_tel";
    public static final String CONSIGNEE_ADDRESS = "consignee_address";
    public static final String CONSIGNEE_MARK = "consigneeMark";
    public static final String CONSIGNEE_MESSAGE = "consignee_message";
    private RecyclerView recyclerView;
    private String consigneeMark;
    private String consigneeMessage;
    private Boolean isOnlinePay = true;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ack_order);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Map<Integer, Dish> dishMap = (Map<Integer, Dish>)
                bundle.getSerializable(CategoryAndDishFragment.DISH_BEAN_MAP);
        final TextView tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        final String total_price = bundle.getString(CategoryAndDishFragment.TOTAL_PRICE);
        tv_total_price.setText(total_price);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        //如果每个item大小固定，设置这个属性可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AckOrderActivity.this));
        recyclerView.setAdapter(new AckOrderRecyclerViewAdapter(AckOrderActivity.this, dishMap));
        Button ack_button = (Button) findViewById(R.id.ack_button);
        ack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(consigneeMessage)) {
                    consigneeMessage = MyUser.getObjectByKey(AckOrderActivity.this, "username")
                            + "," + MyUser.getObjectByKey(AckOrderActivity.this, "address") + ","
                            + MyUser.getObjectByKey(AckOrderActivity.this, "mobilePhoneNumber");
                }
                if (isOnlinePay) {
                    Intent intent = new Intent(AckOrderActivity.this, PlayCashActivity.class);
                    Bundle bundle = new Bundle();
                    MapSerializable map = new MapSerializable(dishMap);
                    bundle.putSerializable(CategoryAndDishFragment.DISH_BEAN_MAP, (Serializable) map.getMap());
                    bundle.putString(CategoryAndDishFragment.TOTAL_PRICE, total_price);
                    bundle.putString(CONSIGNEE_MESSAGE, consigneeMessage);
                    bundle.putString(CONSIGNEE_MARK, consigneeMark);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    assert total_price != null;
                    final Order order = new Order(false, consigneeMessage, consigneeMark
                            , Double.parseDouble(total_price.substring(1, total_price.length())), dishMap);
                    order.save(AckOrderActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AckOrderActivity.this, "提交订单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AckOrderActivity.this, "提交订单失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
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

    @Override
    public void OnClickListener(int id, String name, String tel, String address) {
        switch (id) {
            case AckOrderRecyclerViewAdapter.BUTTON_NATIVE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.table));
                builder.setIcon(R.mipmap.select);
                int total = 25;
                final String s[] = new String[total];
                for (int i = 0; i < total; i++) {
                    s[i] = getString(R.string.number, i + 1);
                }
                builder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeConsigneeMessage(true, getString(R.string._native), "", getString(R.string.table_num, s[which]));
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_OUTER:
                Intent intent2 = new Intent(AckOrderActivity.this, ConsigneeAddressActivity.class);
                intent2.putExtra(CONSIGNEE_NAME, name);
                intent2.putExtra(CONSIGNEE_TEL, tel);
                intent2.putExtra(CONSIGNEE_ADDRESS, address);
                startActivityForResult(intent2, AckOrderRecyclerViewAdapter.BUTTON_OUTER);
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_ONLINE:
                isOnlinePay = true;
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_CASH:
                isOnlinePay = false;
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_MARK:
                Intent intent3 = new Intent(AckOrderActivity.this, OrderMarkActivity.class);
                startActivityForResult(intent3, AckOrderRecyclerViewAdapter.BUTTON_MARK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case AckOrderRecyclerViewAdapter.BUTTON_OUTER:
                String name = data.getStringExtra(CONSIGNEE_NAME);
                String tel = data.getStringExtra(CONSIGNEE_TEL);
                String address = data.getStringExtra(CONSIGNEE_ADDRESS);
                changeConsigneeMessage(false, name, tel, address);
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_MARK:
                consigneeMark = data.getStringExtra(CONSIGNEE_MARK);
                ViewGroup view = (ViewGroup) recyclerView.getChildAt(0);
                TextView tv_mark = (TextView) CommonUtil.findViewInViewGroupById((ViewGroup) view.getChildAt(2), R.id.tv_mark);
                assert tv_mark != null;
                tv_mark.setText(consigneeMark);
                break;
        }
    }

    private void changeConsigneeMessage(boolean isNative, String name, String tel, String address) {
        ViewGroup view = (ViewGroup) recyclerView.getChildAt(0);
        TextView consignee_name = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.order_name);
        assert consignee_name != null;
        consignee_name.setText(name);
        TextView consignee_tel = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.pay_num);
        assert consignee_tel != null;
        consignee_tel.setText(tel);
        TextView consignee_address = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.consignee_address);
        assert consignee_address != null;
        consignee_address.setText(address);
        if (isNative)
            consigneeMessage = address;
        else
            consigneeMessage = name + "," + tel + "," + address;
    }
}
