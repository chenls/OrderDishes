package chenls.orderdishes.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import chenls.orderdishes.R;

public class OrderMarkActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_no_capsicum, tv_little_capsicum, tv_much_capsicum, tv_no_green_onion,
            tv_no_onion, tv_no_caraway, tv_much_vinegar, tv_much_green_onion;
    private EditText et_other_mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_mark);
        Button bt_sure = (Button) findViewById(R.id.bt_sure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String other_mark = et_other_mark.getText().toString();
                String mark = toString(quick_mark);
                if (TextUtils.isEmpty(other_mark))
                    mark = mark.substring(0, mark.length() - 1);
                else
                    mark = mark + other_mark;
                Intent intent = new Intent(OrderMarkActivity.this, AckOrderActivity.class);
                intent.putExtra(AckOrderActivity.CONSIGNEE_MARK, mark);
                setResult(RESULT_OK, intent);
                finish();
            }


            private String toString(String[] array) {
                if (array == null) {
                    return "";
                }
                StringBuilder sb = new StringBuilder(array.length * 7);
                for (String anArray : array) {
                    if (!TextUtils.isEmpty(anArray)) {
                        sb.append(anArray);
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
        });
        tv_no_capsicum = (TextView) findViewById(R.id.tv_no_capsicum);
        tv_no_capsicum.setOnClickListener(this);
        tv_little_capsicum = (TextView) findViewById(R.id.tv_little_capsicum);
        tv_little_capsicum.setOnClickListener(this);
        tv_much_capsicum = (TextView) findViewById(R.id.tv_much_capsicum);
        tv_much_capsicum.setOnClickListener(this);
        tv_no_green_onion = (TextView) findViewById(R.id.tv_no_green_onion);
        tv_no_green_onion.setOnClickListener(this);
        tv_no_onion = (TextView) findViewById(R.id.tv_no_onion);
        tv_no_onion.setOnClickListener(this);
        tv_no_caraway = (TextView) findViewById(R.id.tv_no_caraway);
        tv_no_caraway.setOnClickListener(this);
        tv_much_vinegar = (TextView) findViewById(R.id.tv_much_vinegar);
        tv_much_vinegar.setOnClickListener(this);
        tv_much_green_onion = (TextView) findViewById(R.id.tv_much_green_onion);
        tv_much_green_onion.setOnClickListener(this);
        et_other_mark = (EditText) findViewById(R.id.et_other_mark);
    }

    int color;
    String[] quick_mark = new String[8];

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no_capsicum:
                clickHandleCapsicum();
                clickHandle(0, tv_no_capsicum);
                break;
            case R.id.tv_little_capsicum:
                clickHandleCapsicum();
                clickHandle(1, tv_little_capsicum);
                break;
            case R.id.tv_much_capsicum:
                clickHandleCapsicum();
                clickHandle(2, tv_much_capsicum);
                break;
            case R.id.tv_no_green_onion:
                clickHandleGreenOnion();
                clickHandle(3, tv_no_green_onion);
                break;
            case R.id.tv_no_onion:
                clickHandle(4, tv_no_onion);
                break;
            case R.id.tv_no_caraway:
                clickHandle(5, tv_no_caraway);
                break;
            case R.id.tv_much_vinegar:
                clickHandle(6, tv_much_vinegar);
                break;
            case R.id.tv_much_green_onion:
                clickHandleGreenOnion();
                clickHandle(7, tv_much_green_onion);
                break;
        }
    }

    private void clickHandle(int n, TextView textView) {
        if (TextUtils.isEmpty(quick_mark[n])) {
            ColorStateList colorStateList = textView.getTextColors();
            color = colorStateList.getDefaultColor();
            quick_mark[n] = textView.getText().toString();
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
            textView.setBackgroundResource(R.drawable.button_select_blue_background);
        } else {
            quick_mark[n] = "";
            textView.setTextColor(color);
            textView.setBackgroundResource(R.drawable.button_not_select_blue_background);
        }
    }

    private void clickHandleCapsicum() {
        if (!TextUtils.isEmpty(quick_mark[0])) {
            quick_mark[0] = "";
            tv_no_capsicum.setTextColor(color);
            tv_no_capsicum.setBackgroundResource(R.drawable.button_not_select_blue_background);
        } else if (!TextUtils.isEmpty(quick_mark[1])) {
            quick_mark[1] = "";
            tv_little_capsicum.setTextColor(color);
            tv_little_capsicum.setBackgroundResource(R.drawable.button_not_select_blue_background);
        } else if (!TextUtils.isEmpty(quick_mark[2])) {
            quick_mark[2] = "";
            tv_much_capsicum.setTextColor(color);
            tv_much_capsicum.setBackgroundResource(R.drawable.button_not_select_blue_background);
        }
    }

    private void clickHandleGreenOnion() {
        if (!TextUtils.isEmpty(quick_mark[3])) {
            quick_mark[3] = "";
            tv_no_green_onion.setTextColor(color);
            tv_no_green_onion.setBackgroundResource(R.drawable.button_not_select_blue_background);
        } else if (!TextUtils.isEmpty(quick_mark[7])) {
            quick_mark[7] = "";
            tv_much_green_onion.setTextColor(color);
            tv_much_green_onion.setBackgroundResource(R.drawable.button_not_select_blue_background);
        }
    }
}
