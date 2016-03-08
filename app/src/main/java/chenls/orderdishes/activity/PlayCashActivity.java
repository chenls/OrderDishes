package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.DishBean;
import chenls.orderdishes.utils.aliplay.PayResult;
import chenls.orderdishes.utils.aliplay.SignUtils;

public class PlayCashActivity extends AppCompatActivity {
    // 商户PID
    public static final String PARTNER = "2016030101173294";
    // 商户收款账号
    public static final String SELLER = "18883288008";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANgaNgw3Q+JyVCZq\n" +
            "tb8G78O1nUOAGAE8JhUJDkvXe4z3EexZ50BoZHn7XjEAatzwidJJJ6LZ7CjJe8S9\n" +
            "cy3obZIQMyOKLVqNPuq4FHEWSGrwEIU9aJMnQy1oxa0peZT3djjoB6huZhfxCwZi\n" +
            "mJCmE1mY9fkCzeKWrkHM6cywJwntAgMBAAECgYB311FRxPQoiC6Sf2r63OXqKrVj\n" +
            "0Y1+YdTcpYEAHieuLPYLlG/iEAJogzXhBtI8ZEiKDXeloCSPGe1BVa6iLttTt2K4\n" +
            "4+Xj6MgxO9ZGgZKtuV9rMrKTmNrSjtBvm62kjSV1jw0whgcaV6vfnxVIJB0m7SDj\n" +
            "/P33udahcPsTxppvIQJBAP7Gz9akFkXaxBcZmu+vELnaeEZ/hwYLweQkpUfpSYqP\n" +
            "sXbiNLPU9bvwOTeUuvPY3Jq/IZTledq5cIpZNbyMPq8CQQDZI9u9sBfwwHBOiuh3\n" +
            "Omgoe1RYd/bOHFkGC3zkWByUGzKaUbCtYjDsbNpG37g3EMR8U+kU+RypmP/dU93i\n" +
            "iwgjAkAqrV7vU3kvpl+mhvF/KNIib8gp9v7FMn7+Gm1dSGS7Qyq1AVtWrFulaa4L\n" +
            "82wFoNQDT/TuHip1Sw7g/eSUeuGdAkAodUD1OAFN7109lS45QN21ZT1S+o822htQ\n" +
            "QAW2OiChC+xCyqPDun7pHmMRP5AAWpRxoeTU7pGDnClYYM7/7e3HAkEAr12a32iL\n" +
            "ApMT2AhdVApEhSmhD6ItDsQjoWjmvj13YG8u7aZbIBbwvV+3Tnf+i+ejI8osDX8q\n" +
            "9oBNbKMexmAXlg==\n";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQK" +
            "BgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoL" +
            "PXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2" +
            "AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PlayCashActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PlayCashActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PlayCashActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PlayCashActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };
    private String price;
    private String order_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_cash);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Map<Integer, DishBean> dishBeanMap = (Map<Integer, DishBean>)
                bundle.getSerializable(OrderDishActivity.DISH_BEAN_MAP);
        final String consigneeMessage = bundle.getString(AckOrderActivity.CONSIGNEE_MESSAGE);
        final String consigneeMark = bundle.getString(AckOrderActivity.CONSIGNEE_MARK);
        TextView product_price = (TextView) findViewById(R.id.product_price);
        price = bundle.getString(OrderDishActivity.TOTAL_PRICE);
        product_price.setText(price);
        TextView order_num = (TextView) findViewById(R.id.order_num);
        order_message = getString(R.string.order_num, getOutTradeNo());
        order_num.setText(order_message);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        // 订单
        String orderInfo = getOrderInfo(order_message, "外卖订单", price.substring(1, price.length()));

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PlayCashActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PlayCashActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
//                Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}

