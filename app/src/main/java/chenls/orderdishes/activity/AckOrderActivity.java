package chenls.orderdishes.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.AckOrderRecyclerViewAdapter;
import chenls.orderdishes.bean.DishBean;
import chenls.orderdishes.image.ImageLoader;
import chenls.orderdishes.utils.CommonUtil;

public class AckOrderActivity extends AppCompatActivity implements AckOrderRecyclerViewAdapter.OnClickListenerInterface {
    public static final String CONSIGNEE_NAME = "consignee_name";
    public static final String CONSIGNEE_TEL = "consignee_tel";
    public static final String CONSIGNEE_ADDRESS = "consignee_address";
    public static final String MARK = "mark";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ack_order);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Map<Integer, DishBean> dishBeanMap = null;
        try {
            dishBeanMap = (Map<Integer, DishBean>) bundle.getSerializable(OrderDishActivity.DISH_BEAN_MAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        String total_price = bundle.getString(OrderDishActivity.TOTAL_PRICE);
        tv_total_price.setText(total_price);
        recyclerView = (RecyclerView) findViewById(R.id.list);
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

    @Override
    public void OnClickListener(int id) {
        switch (id) {
            case AckOrderRecyclerViewAdapter.BUTTON_NATIVE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.table));
                builder.setIcon(R.mipmap.ic_launcher);
                int total = 25;
                final String s[] = new String[total];
                for (int i = 0; i < total; i++) {
                    s[i] = getString(R.string.number, i + 1);
                }
                builder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeConsigneeMessage(getString(R.string._native), "", getString(R.string.table_num, s[which]));
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_OUTER:
                Intent intent2 = new Intent(AckOrderActivity.this, ConsigneeAddressActivity.class);
                startActivityForResult(intent2, AckOrderRecyclerViewAdapter.BUTTON_OUTER);
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_ONLINE:
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_CASH:
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
                changeConsigneeMessage(name, tel, address);
                break;
            case AckOrderRecyclerViewAdapter.BUTTON_MARK:
                String mark = data.getStringExtra(MARK);
                ViewGroup view = (ViewGroup) recyclerView.getChildAt(0);
                TextView tv_mark = (TextView) CommonUtil.findViewInViewGroupById((ViewGroup) view.getChildAt(2), R.id.tv_mark);
                assert tv_mark != null;
                tv_mark.setText(mark);
                break;
        }
    }

    private void changeConsigneeMessage(String name, String tel, String address) {
        ViewGroup view = (ViewGroup) recyclerView.getChildAt(0);
        TextView consignee_name = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.consignee_name);
        assert consignee_name != null;
        consignee_name.setText(name);
        TextView consignee_tel = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.consignee_tel);
        assert consignee_tel != null;
        consignee_tel.setText(tel);
        TextView consignee_address = (TextView) CommonUtil.findViewInViewGroupById(view, R.id.consignee_address);
        assert consignee_address != null;
        consignee_address.setText(address);
    }
}
