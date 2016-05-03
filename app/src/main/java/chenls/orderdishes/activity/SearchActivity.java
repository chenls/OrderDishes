package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.DiscoverRecyclerViewAdapter;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.fragment.CategoryAndDishFragment;
import chenls.orderdishes.fragment.DiscoverFragment;
import chenls.orderdishes.utils.CommonUtil;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends AppCompatActivity implements
        DiscoverFragment.OnListFragmentInteractionListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void queryDish(String searchData) {
        if (!CommonUtil.checkNetState(SearchActivity.this)) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        final BmobQuery<Dish> query1 = new BmobQuery<>();
        query1.addWhereContains("name", searchData);
        final BmobQuery<Dish> query2 = new BmobQuery<>();
        query2.addWhereContains("summarize", searchData);
        final BmobQuery<Dish> query3 = new BmobQuery<>();
        query3.addWhereContains("categoryName", searchData);
        List<BmobQuery<Dish>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);
        BmobQuery<Dish> or = new BmobQuery<Dish>().or(queries);
        or.setLimit(10);
        or.order("-updatedAt");
        or.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);        // 强制在从网络中获取
        or.findObjects(SearchActivity.this, new FindListener<Dish>() {
            @Override
            public void onSuccess(List<Dish> object) {
                swipeRefreshLayout.setRefreshing(false);
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new DiscoverRecyclerViewAdapter(SearchActivity.this, object
                        , SearchActivity.this));
                if (object.size() == 0) {
                    Toast.makeText(SearchActivity.this, "抱歉，未搜索到内容", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        assert searchView != null;
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    queryDish(newText);
                    swipeRefreshLayout.setRefreshing(true);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onDiscoverListItemClick(Dish dish) {
        Intent intent = new Intent(SearchActivity.this, DishDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CategoryAndDishFragment.DISH_ITEM, dish);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
}
