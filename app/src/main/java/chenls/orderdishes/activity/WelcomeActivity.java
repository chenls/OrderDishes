package chenls.orderdishes.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import chenls.orderdishes.R;
import chenls.orderdishes.fragment.LoginDialogFragment;
import chenls.orderdishes.fragment.RegisterDialogFragment;


public class WelcomeActivity extends AppCompatActivity implements
        LoginDialogFragment.OnLoginFragmentInteractionListener,
        RegisterDialogFragment.OnRegisterFragmentInteractionListener {
    Intent intent;
    //圆点图标数组
    private ImageView[] img;
    //视图列表
    ArrayList<View> viewList;
    private SharedPreferences sharedPreferences;
    public static final String IS_NOT_FIRST_OPEN = "is_not_first_open";
    private LoginDialogFragment loginDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        sharedPreferences = this.getSharedPreferences(IS_NOT_FIRST_OPEN, Context.MODE_PRIVATE);
        //是否进入滑动欢迎页
        if (sharedPreferences.getBoolean(IS_NOT_FIRST_OPEN, false)) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewGroup);
        //定义选项卡、
        LayoutInflater lf = LayoutInflater.from(WelcomeActivity.this);
        @SuppressLint("InflateParams") View view1 = lf.inflate(R.layout.layout_welcome1, null);
        @SuppressLint("InflateParams") View view2 = lf.inflate(R.layout.layout_welcome2, null);
        @SuppressLint("InflateParams") View view3 = lf.inflate(R.layout.layout_welcome3, null);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        img = new ImageView[viewList.size()];
        for (int i = 0; i < viewList.size(); i++) {
            img[i] = new ImageView(WelcomeActivity.this);
            if (0 == i) {
                img[i].setBackgroundResource(R.mipmap.dot_blue);
            } else {
                img[i].setBackgroundResource(R.mipmap.dot_write);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 10, 0);
            img[i].setLayoutParams(params);
            layout.addView(img[i]);
        }
        //定义适配器
        PagerAdapter pager = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < viewList.size(); i++) {
                    if (arg0 == i) {
                        img[i].setBackgroundResource(R.mipmap.dot_blue);
                    } else {
                        img[i].setBackgroundResource(R.mipmap.dot_write);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setAdapter(pager);
    }

    public void login(View view) {
        loginDialogFragment = new LoginDialogFragment();
        loginDialogFragment.show(getSupportFragmentManager(), "loginDialogFragment");

    }

    public void register(View view) {
        onRegisterButtonPressed();
    }

    @Override
    public void onRegisterButtonPressed() {
        loginDialogFragment.dismiss();
        RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment();
        registerDialogFragment.show(getSupportFragmentManager(), "registerDialogFragment");

    }

    @Override
    public void onLoginSuccess() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_NOT_FIRST_OPEN, true);
        editor.commit();
        intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterSuccess() {
        onLoginSuccess();
    }
}
