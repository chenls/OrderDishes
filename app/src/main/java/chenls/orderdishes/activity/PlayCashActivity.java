package chenls.orderdishes.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import chenls.orderdishes.BmobApplication;
import chenls.orderdishes.R;
import chenls.orderdishes.bean.Order;
import chenls.orderdishes.fragment.CategoryAndDishFragment;
import chenls.orderdishes.fragment.OrderFragment;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.listener.UpdateListener;

public class PlayCashActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ACTION = "com.chenls.doNotCareName";
    private String price;
    private String goods_message;
    private ProgressDialog progressDialog;
    private ImageView iv_weixinpay;
    private ImageView iv_alipay;
    private boolean isAliPay = true;
    private String objectId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cash);
        //初始化Bmob支付
        BP.init(this, BmobApplication.APP_ID);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        price = bundle.getString(CategoryAndDishFragment.TOTAL_PRICE);
        objectId = bundle.getString(OrderFragment.OBJECT_ID);
        TextView product_price = (TextView) findViewById(R.id.product_price);
        product_price.setText(getString(R.string.rmb, price));
        TextView goods_num = (TextView) findViewById(R.id.goods_num);
        goods_message = getString(R.string.goods_num,
                String.valueOf(System.currentTimeMillis()));
        goods_num.setText(goods_message);
        iv_alipay = (ImageView) findViewById(R.id.iv_alipay);
        findViewById(R.id.rl_alipay).setOnClickListener(this);
        iv_weixinpay = (ImageView) findViewById(R.id.iv_weixinpay);
        findViewById(R.id.rl_weixinpay).setOnClickListener(this);
        Button bt_pay = (Button) findViewById(R.id.bt_pay);
        bt_pay.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        if (!CommonUtil.checkNetState(PlayCashActivity.this)) {
            return;
        }
        switch (v.getId()) {
            case R.id.rl_alipay:
                iv_alipay.setBackgroundResource(R.mipmap.check_on);
                iv_weixinpay.setBackgroundResource(R.mipmap.check_off);
                isAliPay = true;
                break;
            case R.id.rl_weixinpay:
                iv_alipay.setBackgroundResource(R.mipmap.check_off);
                iv_weixinpay.setBackgroundResource(R.mipmap.check_on);
                isAliPay = false;
                break;
            case R.id.bt_pay:
                //开始支付
                if (isAliPay) {
                    payByAli();
                } else {
                    payByWeiXin();
                }
                break;
        }
    }

    // 调用支付宝支付
    void payByAli() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(PlayCashActivity.this);
            progressDialog.setMessage("请稍等...");
        }
        progressDialog.show();
        BP.pay(this, goods_message, "", Double.parseDouble(price), true, new PListener() {
            String orderId;

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PlayCashActivity.this, "支付结果未知,请稍后手动查询",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                query(orderId);
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                this.orderId = orderId;
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                Toast.makeText(PlayCashActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                        .show();
                progressDialog.dismiss();
            }
        });
    }

    // 调用微信支付
    void payByWeiXin() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(PlayCashActivity.this);
            progressDialog.setMessage("请稍等...");
        }
        progressDialog.show();
        BP.pay(this, goods_message, "", Double.parseDouble(price), false, new PListener() {
            String orderId;

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PlayCashActivity.this, "支付结果未知,请稍后手动查询",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                query(orderId);
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                this.orderId = orderId;
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    new AlertDialog.Builder(PlayCashActivity.this)
                            .setMessage(
                                    "监测到你尚未安装微信支付插件,无法进行支付。请安装插件(已打包在本地,无流量消耗)。")
                            .setPositiveButton("安装",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int which) {
                                            installBmobPayPlugin("bp_wx.db");
                                        }
                                    })
                            .setNegativeButton("支付宝支付",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int which) {
                                            payByAli();
                                        }
                                    }).create().show();
                } else {
                    Toast.makeText(PlayCashActivity.this, "支付中断!",
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    // 执行订单查询
    void query(String orderId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(PlayCashActivity.this);
            progressDialog.setMessage("请稍等...");
        }
        progressDialog.show();
        BP.query(this, orderId, new QListener() {

            @Override
            public void succeed(String status) {
                final Order order = new Order();
                order.setObjectId(objectId);
                order.setState(2);
                order.update(PlayCashActivity.this, objectId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(PlayCashActivity.this, "支付成功！", Toast.LENGTH_SHORT)
                                .show();
                        //发送广播
                        Intent mIntent = new Intent(ACTION);
                        LocalBroadcastManager.getInstance(PlayCashActivity.this)
                                .sendBroadcast(mIntent);

                        progressDialog.dismiss();
                        Intent intent = new Intent(PlayCashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(PlayCashActivity.this, "支付成功,但是订单提交失败！", Toast.LENGTH_SHORT)
                                .show();
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(PlayCashActivity.this, "订单提交失败！", Toast.LENGTH_SHORT)
                        .show();
                progressDialog.dismiss();
            }
        });
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

