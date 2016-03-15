package chenls.orderdishes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Map;

import chenls.orderdishes.R;
import chenls.orderdishes.adapter.ChooseCommentDishRecyclerViewAdapter;
import chenls.orderdishes.bean.Dish;
import chenls.orderdishes.fragment.CategoryAndDishFragment;

public class ChooseCommentDishActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_comment_dish);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Map<Integer, Dish> dishMap = (Map<Integer, Dish>)
                bundle.getSerializable(CategoryAndDishFragment.DISH_BEAN_MAP);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        //如果每个item大小固定，设置这个属性可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChooseCommentDishActivity.this));
        recyclerView.setAdapter(new ChooseCommentDishRecyclerViewAdapter(ChooseCommentDishActivity.this, dishMap));
    }
}
