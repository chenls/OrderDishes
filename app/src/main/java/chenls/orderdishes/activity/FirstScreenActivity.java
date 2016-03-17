package chenls.orderdishes.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import chenls.orderdishes.R;

public class FirstScreenActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        new Handler() {
            public void handleMessage(Message msg) {
                startActivity(new Intent(FirstScreenActivity.this,
                        WelcomeActivity.class));
                finish();
            }
        }.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
