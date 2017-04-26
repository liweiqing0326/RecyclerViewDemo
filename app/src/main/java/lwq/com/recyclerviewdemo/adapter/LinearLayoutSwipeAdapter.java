package lwq.com.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lwq.com.recyclerviewdemo.R;
import lwq.com.recyclerviewdemo.UI.SwipeLayout;


public class LinearLayoutSwipeAdapter extends RecyclerView.Adapter<LinearLayoutSwipeAdapter.MyViewHolder> {
    private Context context;
    public List<String> list;

    public LinearLayoutSwipeAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
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
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_list, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 用于适配渲染数据到View中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.i("liweiqing", "position" + position);
        holder.tv_name.setText(list.get(position));
        holder.swipeLayout.setOnSwipeStateChangeListener(new SwipeLayout.OnSwipeStateChangeListener() {
            @Override
            public void onOpen(Object tag) {
                Toast.makeText(context, "第" + position + "个打开", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose(Object tag) {
                Toast.makeText(context, "第" + position + "个关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_delete;
        SwipeLayout swipeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_delete= (TextView) itemView.findViewById(R.id.tv_delete);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            swipeLayout= (SwipeLayout) itemView.findViewById(R.id.swipeLayout);
        }
    }
}
