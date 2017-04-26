package lwq.com.recyclerviewdemo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.DividerGridItemDecoration;
import lwq.com.recyclerviewdemo.adapter.LinearLayoutAdapter;
import lwq.com.recyclerviewdemo.adapter.MyItemAnimator;

public class GridLayoutDemo1Activity extends AppCompatActivity {

    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_del)
    Button btnDel;
    @BindView(R.id.btn_change)
    Button btnChange;
    @BindView(R.id.btn_delanim)
    Button btnDelanim;
    @BindView(R.id.btn_addanim)
    Button btnAddanim;
    @BindView(R.id.btn_moveanim)
    Button btnMoveanim;
    @BindView(R.id.btn_changeanim)
    Button btnChangeanim;
    @BindView(R.id.myRecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private Context mContext;
    private ArrayList<String> mDatas;
    private LinearLayoutAdapter adapter;
    private MyItemAnimator myItemAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_layout_demo1);
        ButterKnife.bind(this);
        mContext = this;
        initData();
        //设置布局管理器
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        //设置Item增加，删除动画
        //添加分割线
        myRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置adapter
        adapter = new LinearLayoutAdapter(mDatas, this);
        myRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    @OnClick({R.id.btn_add, R.id.btn_del, R.id.btn_change, R.id.btn_delanim, R.id.btn_addanim, R.id.btn_moveanim, R.id.btn_changeanim})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                adapter.Add(0, "新添加的数据");
                myRecyclerView.scrollToPosition(0);
                break;
            case R.id.btn_del:
                adapter.Remove(0);
                break;
            case R.id.btn_change:
                adapter.Change(0, "更改后的数据");
                break;
            case R.id.btn_delanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setRemoveDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
            case R.id.btn_addanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setAddDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
            case R.id.btn_moveanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setMoveDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
            case R.id.btn_changeanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setChangeDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
        }
    }
}
