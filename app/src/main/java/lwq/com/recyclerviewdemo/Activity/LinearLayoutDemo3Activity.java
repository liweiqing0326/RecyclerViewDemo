package lwq.com.recyclerviewdemo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.DividerListItemDecoration;
import lwq.com.recyclerviewdemo.adapter.ItemTouchHelper.SimpleItemTouchHelperCallback;
import lwq.com.recyclerviewdemo.adapter.LinearLayout1Adapter;
import lwq.com.recyclerviewdemo.adapter.MyItemAnimator;

public class LinearLayoutDemo3Activity extends AppCompatActivity {

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
    @BindView(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private Context mContext;
    private ArrayList<String> mDatas;
    private DividerListItemDecoration dividerListItemDecoration;
    private LinearLayout1Adapter adapter;
    private MyItemAnimator myItemAnimator;

    private boolean isLoading;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_demo3);
        ButterKnife.bind(this);
        mContext = this;
        //初始化数据
        initData();
        //设置布局管理器
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);
        //添加分割线
        dividerListItemDecoration = new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST);
        myRecyclerView.addItemDecoration(dividerListItemDecoration);
        //设置adapter
        adapter = new LinearLayout1Adapter(mDatas, this);
        myRecyclerView.setAdapter(adapter);
        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(myRecyclerView);
        //设置下拉进度的主题颜色
        demoSwiperefreshlayout.setColorSchemeResources(R.color.colorPrimary);
        // 设置下拉进度的背景颜色，默认就是白色的
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        //添加点击事件
        adapter.setOnItemClickListener(new LinearLayout1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext,"item position = " + position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(mContext,"item position = " + position,Toast.LENGTH_SHORT).show();
            }
        });
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("liweiqing","StateCanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("liweiqing","onScrolled");
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                Log.i("liweiqing","lastVisibleItemPosition"+lastVisibleItemPosition);
                Log.i("liweiqing","adapter.getItemCount()"+adapter.getItemCount());
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    Log.i("liweiqing","loading executed");
                    boolean isRefreshing = demoSwiperefreshlayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String newData = "新增第" + 1 + "个数据";
                                adapter.Add(layoutManager.findLastVisibleItemPosition(), newData);
                                Toast.makeText(mContext,"load more completed",Toast.LENGTH_SHORT).show();
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
    }


    @OnClick({R.id.btn_add, R.id.btn_del, R.id.btn_change, R.id.btn_delanim, R.id.btn_addanim, R.id.btn_moveanim, R.id.btn_changeanim})
    public void onViewClicked(View view) {
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
                myItemAnimator.setMoveDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
            case R.id.btn_addanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setAddDuration(2000);
                //设置Item增加，删除动画
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
            case R.id.btn_moveanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setMoveDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
            case R.id.btn_changeanim:
                myItemAnimator = new MyItemAnimator();
                myItemAnimator.setMoveDuration(2000);
                myRecyclerView.setItemAnimator(myItemAnimator);
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    /**
     * 数据刷新
     */
    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final String newData = "新增第" + 1 + "个数据";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.Add(0, newData);
                        demoSwiperefreshlayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }
}
