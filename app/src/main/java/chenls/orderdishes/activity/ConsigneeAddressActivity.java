package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import chenls.orderdishes.R;

public class ConsigneeAddressActivity extends AppCompatActivity {
    private EditText et_consignee_name, et_consignee_tel, et_consignee_address;
    private Button bt_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignee_address);
        et_consignee_name = (EditText) findViewById(R.id.et_consignee_name);
        et_consignee_tel = (EditText) findViewById(R.id.et_consignee_tel);
        et_consignee_address = (EditText) findViewById(R.id.et_consignee_address);
        bt_sure = (Button) findViewById(R.id.bt_sure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsigneeAddressActivity.this, AckOrderActivity.class);
                String consignee_name = et_consignee_name.getText().toString();
                intent.putExtra(AckOrderActivity.CONSIGNEE_NAME, consignee_name);
                String consignee_tel = et_consignee_tel.getText().toString();
                intent.putExtra(AckOrderActivity.CONSIGNEE_TEL, consignee_tel);
                String consignee_address = et_consignee_address.getText().toString();
                intent.putExtra(AckOrderActivity.CONSIGNEE_ADDRESS, consignee_address);
                setResult(RESULT_OK, intent);
                finish();
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
}
