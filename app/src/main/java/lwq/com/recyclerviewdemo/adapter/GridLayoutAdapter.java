package lwq.com.recyclerviewdemo.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.ItemTouchHelper.ItemTouchHelperAdapter;



public class GridLayoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private List<String> list;
    private boolean hasMore = true;
    private int normalType = 0;
    private int footType = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean fadeTips = false;

    public GridLayoutAdapter(List<String> list, Context context, boolean hasMore) {
        this.list = list;
        this.context = context;
        this.hasMore = hasMore;
    }

    /**
     * 这个方法主要生成为每个Item inflater出一个View，但是该方法返回的是一个ViewHolder。
     * 方法是把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, parent, false));
        }
    }

    /**
     * 用于适配渲染数据到View中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).tv.setText(list.get(position));
        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (list.size() > 0) {
                    ((FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (list.size() > 0) {
                    ((FootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }
    }

    /**
     * 获取子条目的类型，如果是最后一个子条目是底部类型，则显示加载更多布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    /**
     * 当连接到RecyclerView后，提供数据的时候调用这个方法
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    /**
     * 更新数据列表
     *
     * @param newDatas
     * @param hasMore
     */
    public void updateList(List<String> newDatas, boolean hasMore) {
        if (newDatas != null) {
            list.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    /**
     * 获取实际最后一个子条目的位置
     *
     * @return
     */
    public int getRealLastPosition() {
        return list.size();
    }

    /**
     * 数据交换
     *
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 数据删除
     *
     * @param position
     */
    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 数据增加
     *
     * @param position
     * @param data
     */
    public void Add(int position, String data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * 数据删除
     *
     * @param position
     */
    public void Remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 数据刷新
     *
     * @param position
     * @param data
     */
    public void Change(int position, String data) {
        list.remove(position);
        list.add(position, data);
        notifyItemChanged(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_num);
        }
    }

    private class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }
}
