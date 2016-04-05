package chenls.orderdishes.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import chenls.orderdishes.R;

public class FirstScreenActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(
                FirstScreenActivity.this, R.anim.animation);
        imageView.startAnimation(animation);

        new Handler() {
            public void handleMessage(Message msg) {
                startActivity(new Intent(FirstScreenActivity.this,
                        WelcomeActivity.class));
                finish();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
