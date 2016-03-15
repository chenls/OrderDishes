package chenls.orderdishes.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.views.PgyerDialog;

import chenls.orderdishes.R;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.bean.Order;
import chenls.orderdishes.fragment.CategoryAndDishFragment;
import chenls.orderdishes.fragment.CategoryFragment;
import chenls.orderdishes.fragment.DiscoverFragment;
import chenls.orderdishes.fragment.DishFragment;
import chenls.orderdishes.fragment.OrderFragment;
import cn.bmob.v3.datatype.BmobFile;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , DishFragment.OnListFragmentInteractionListener
        , CategoryFragment.OnListFragmentInteractionListener
        , DiscoverFragment.OnListFragmentInteractionListener
        , OrderFragment.OnListFragmentInteractionListener {

    private static final int SET_INFORMATION = 1;
    public static final String PIC = "pic";
    private TextView tv_name;
    private TextView tv_phone_num;
    private ImageView iv_pic;
    private Fragment from;
    private DiscoverFragment discoverFragment;
    private CategoryAndDishFragment categoryAndDishFragment;
    private OrderFragment orderFragment;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置抽屉DrawerLayout
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        Intent intent1 = getIntent();
        if (intent1.getBooleanExtra(WelcomeActivity.IS_FIRST_OPEN, false)) {
            drawer.openDrawer(GravityCompat.START); //如果第一次打开，则打开抽屉
        }
        toggle.syncState();
        //设置导航栏NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        iv_pic = (ImageView) headerView.findViewById(R.id.iv_ali);
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, SetInformationActivity.class);
                startActivityForResult(intent2, SET_INFORMATION);
                drawer.closeDrawer(GravityCompat.START);//关闭抽屉
            }
        });
        tv_name = (TextView) headerView.findViewById(R.id.tv_name);
        tv_phone_num = (TextView) headerView.findViewById(R.id.tv_phone_num);
        changeInformation(null);
        final ImageView iv_logout = (ImageView) headerView.findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.logout));
                builder.setMessage(getString(R.string.sure_logout));
                builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyUser.logOut(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.discover));
        }
        discoverFragment = new DiscoverFragment();
        from = discoverFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linear_layout, discoverFragment);
        fragmentTransaction.commit();
        //支付成功后 刷新界面
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (from instanceof OrderFragment)
                    orderFragment.myRefresh(true);
                else if (from instanceof CategoryAndDishFragment) {
                    categoryAndDishFragment.myRefresh(true);
                    //同时需要刷新订单
                    if (orderFragment != null)
                        orderFragment.myRefresh(true);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(PlayCashActivity.ACTION);
        LocalBroadcastManager.getInstance(MainActivity.this)
                .registerReceiver(broadcastReceiver, intentFilter);
    }

    private void changeInformation(Bitmap bitmap) {
        if (bitmap != null) {
            iv_pic.setImageBitmap(bitmap);
        }
        MyUser myUser = MyUser.getCurrentUser(MainActivity.this, MyUser.class);
        BmobFile pic = myUser.getPic();
        if (pic != null) {
            String url = pic.getFileUrl(MainActivity.this);
            setFaceImage(url);
        }
        tv_name.setText((String) MyUser.getObjectByKey(this, "username"));
        tv_phone_num.setText((String) MyUser.getObjectByKey(this, "mobilePhoneNumber"));
    }

    private void setFaceImage(String url) {
        Glide.with(MainActivity.this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.loading)
                .into(iv_pic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SET_INFORMATION) {
                Bitmap photo = data.getParcelableExtra(PIC);
                changeInformation(photo);
            }
        }
        if (categoryAndDishFragment != null) {
            categoryAndDishFragment.myActivityResult(resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ActionBar actionBar = getSupportActionBar();
        switch (item.getItemId()) {
            case R.id.nav_discover:
                if (actionBar != null) {
                    actionBar.setTitle(getString(R.string.discover));
                }
                switchContent(discoverFragment);
                break;
            case R.id.nav_dish:
                if (actionBar != null) {
                    actionBar.setTitle(getString(R.string.order_dish_activity));
                }
                if (categoryAndDishFragment == null) {
                    categoryAndDishFragment = CategoryAndDishFragment.newInstance();
                }
                switchContent(categoryAndDishFragment);
                break;
            case R.id.nav_order:
                if (actionBar != null) {
                    actionBar.setTitle(getString(R.string.order));
                }
                if (orderFragment == null) {
                    orderFragment = OrderFragment.newInstance();
                }
                switchContent(orderFragment);
//                //打开订单时是否需要刷新
//                if (isNeedRefreshOrder) {
//                    categoryAndDishFragment.myRefresh(true);
//                    isNeedRefreshOrder = false;
//                }
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_about:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(Fragment to) {
        if (from != to) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (!to.isAdded()) {    // 先判断是否被add过
                // 隐藏当前的fragment，add下一个到Activity中
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(from).add(R.id.linear_layout, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(from).show(to).commit();
            }
            from = to;
        }
    }

    @Override
    public void onDishListFragmentClick(int itemPosition, Dish item, String num) {
        categoryAndDishFragment.onDishListFragmentClick(itemPosition, item, num);

    }

    @Override
    public void onDishListButtonClick(int type, Dish dish, int position) {
        categoryAndDishFragment.onDishListButtonClick(type, dish, position);
    }

    @Override
    public void onDishListScroll(int index) {
        categoryAndDishFragment.onDishListScroll(index);
    }

    @Override
    public void mySwipeRefreshLayout(boolean b) {
        categoryAndDishFragment.mySwipeRefreshLayout(b);
    }

    @Override
    public void onCategoryListFragmentClick(int category_position, int dish_position) {
        categoryAndDishFragment.onCategoryListFragmentClick(category_position, dish_position);
    }

    @Override
    public void onDiscoverListFragmentInteraction() {

    }

    @Override
    public void onOrderListButtonClick(Order order) {
        orderFragment.onOrderListButtonClick(order);
    }

    @Override
    public void onDeleteButtonClick(String objectId) {
        orderFragment.onDeleteButtonClick(objectId);
    }

    //蒲公英 反馈相关
    @Override
    protected void onResume() {
        super.onResume();
        // 摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(800);
        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(MainActivity.this);
        //noinspection ResourceType
        PgyerDialog.setDialogTitleBackgroundColor(getString(R.color.colorPrimary));
    }

    @Override
    protected void onPause() {
        super.onPause();
        PgyFeedbackShakeManager.unregister();
    }
}
