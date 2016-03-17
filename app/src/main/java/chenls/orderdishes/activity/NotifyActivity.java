package chenls.orderdishes.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.NotifyRecyclerViewAdapter;
import chenls.orderdishes.bean.MyUser;
import chenls.orderdishes.bean.Notify;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class NotifyActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 40);
        swipeRefreshLayout.setRefreshing(true);
        queryNotify();
    }

    @Override
    public void onRefresh() {
        queryNotify();
    }

    private void queryNotify() {
        if (!CommonUtil.checkNetState(NotifyActivity.this)) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        final BmobQuery<Notify> query1 = new BmobQuery<>();
        query1.addWhereEndsWith("userId", (String)
                MyUser.getObjectByKey(NotifyActivity.this, "objectId"));
        final BmobQuery<Notify> query2 = new BmobQuery<>();
        query2.addWhereContains("platform", "Android");
        List<BmobQuery<Notify>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<Notify> or = new BmobQuery<Notify>().or(queries);
        or.setLimit(10);
        or.order("-updatedAt");
        or.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);  // 强制在从网络中获取
        or.findObjects(NotifyActivity.this, new FindListener<Notify>() {
            public void onSuccess(List<Notify> notifyList) {
                swipeRefreshLayout.setRefreshing(false);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotifyActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new NotifyRecyclerViewAdapter(NotifyActivity.this, notifyList));
            }

            @Override
            public void onError(int code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
