package lwq.com.recyclerviewdemo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.MyItemAnimator;
import lwq.com.recyclerviewdemo.adapter.StaggeredHomeAdapter;

public class StaggeredLayoutActivity extends AppCompatActivity {

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

    private ArrayList<String> mDatas;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    private MyItemAnimator myItemAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_layout);
        ButterKnife.bind(this);
        initData();
        mStaggeredHomeAdapter = new StaggeredHomeAdapter(this, mDatas);
        myRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        myRecyclerView.setAdapter(mStaggeredHomeAdapter);
        // 设置item动画
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick({R.id.btn_add, R.id.btn_del, R.id.btn_change, R.id.btn_delanim, R.id.btn_addanim, R.id.btn_moveanim, R.id.btn_changeanim})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                mStaggeredHomeAdapter.addData(1);
                myRecyclerView.scrollToPosition(1);
                break;
            case R.id.btn_del:
                mStaggeredHomeAdapter.removeData(1);
                break;
            case R.id.btn_change:
                mStaggeredHomeAdapter.Change(1);
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

    protected void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }
}
