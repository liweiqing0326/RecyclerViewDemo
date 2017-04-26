package lwq.com.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.adapter.ItemTouchHelper.ItemTouchHelperAdapter;

public class LinearLayoutAdapter extends RecyclerView.Adapter<LinearLayoutAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private List<String> list;

    public LinearLayoutAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 用于适配渲染数据到View中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(list.get(position));
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
}
