package lwq.com.recyclerviewdemo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.DividerGridItemDecoration;
import lwq.com.recyclerviewdemo.adapter.GridLayoutAdapter;
import lwq.com.recyclerviewdemo.adapter.MyItemAnimator;

public class GridLayoutDemo2Activity extends AppCompatActivity {

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
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    private Context mContext;
    private ArrayList<String> mDatas;
    private MyItemAnimator myItemAnimator;
    private GridLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private int PAGE_COUNT = 10;
    private GridLayoutAdapter adapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_layout_demo2);
        ButterKnife.bind(this);
        mContext = this;

        initData();

        initRefreshLayout();
        //设置布局管理器
        mLayoutManager = new GridLayoutManager(this, 1);
        myRecyclerView.setLayoutManager(mLayoutManager);
        //设置Item增加，删除动画
        //添加分割线
        myRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置adapter
        adapter = new GridLayoutAdapter(getDatas(0, PAGE_COUNT), mContext, getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<String> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final String newData = "新增第" + 1 + "个数据";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.Add(0, newData);
                                swipeContainer.setRefreshing(false);
                            }
                        });
                    }
                }, 1000);
            }
        });
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    private List<String> getDatas(final int firstIndex, final int lastIndex) {
        List<String> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < mDatas.size()) {
                resList.add(mDatas.get(i));
            }
        }
        return resList;
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
