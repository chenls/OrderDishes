package chenls.orderdishes.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.fragment.LoginDialogFragment;
import chenls.orderdishes.fragment.RegisterDialogFragment;
import chenls.orderdishes.fragment.VerifySmsCodeDialogFragment;


public class WelcomeActivity extends AppCompatActivity implements
        LoginDialogFragment.OnLoginFragmentInteractionListener,
        RegisterDialogFragment.OnRegisterFragmentInteractionListener,
        VerifySmsCodeDialogFragment.OnVerifySmsCodeFragmentInteractionListener {
    //圆点图标数组
    private ImageView[] img;
    //视图列表
    ArrayList<View> viewList;
    private LoginDialogFragment loginDialogFragment;
    public static final String IS_FIRST_OPEN = "is_first_open";
    private RegisterDialogFragment registerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断用户名，是否进入滑动欢迎页
        String username = (String) MyUser.getObjectByKey(this, "username");
        if (!TextUtils.isEmpty(username)) {
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
        if (loginDialogFragment != null)
            loginDialogFragment.dismiss();
        registerDialogFragment = new RegisterDialogFragment();
        registerDialogFragment.show(getSupportFragmentManager(), "registerDialogFragment");

    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.putExtra(IS_FIRST_OPEN, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterSuccess(String userName, String userPwd, String phoneNum) {
        if (registerDialogFragment != null)
            registerDialogFragment.dismiss();
        VerifySmsCodeDialogFragment verifySmsCodeDialogFragment = VerifySmsCodeDialogFragment
                .newInstance(userName, userPwd, phoneNum);
        verifySmsCodeDialogFragment.show(getSupportFragmentManager(), "registerDialogFragment");
    }

    @Override
    public void onVerifySmsCodeSuccess() {
        onLoginSuccess();
    }
}
