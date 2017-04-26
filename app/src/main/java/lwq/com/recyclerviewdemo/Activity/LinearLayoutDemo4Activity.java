package lwq.com.recyclerviewdemo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.DividerListItemDecoration;
import lwq.com.recyclerviewdemo.adapter.LinearLayoutSwipeAdapter;


public class LinearLayoutDemo4Activity extends AppCompatActivity {

    @BindView(R.id.myRecyclerView)
    RecyclerView myRecyclerView;

    private Context mContext;
    private DividerListItemDecoration dividerListItemDecoration;
    private LinearLayoutSwipeAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_demo4);
        ButterKnife.bind(this);
        mContext = this;

        initData();

        mContext = this;
        //设置布局管理器
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        dividerListItemDecoration = new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST);
        myRecyclerView.addItemDecoration(dividerListItemDecoration);
        //设置adapter
        adapter = new LinearLayoutSwipeAdapter(mContext, list);
        myRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        //1.准备数据
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("name - " + i);
        }
    }
}
