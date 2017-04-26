package lwq.com.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lwq.com.recyclerviewdemo.R;

public class StaggeredHomeAdapter extends RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder> {

    private Context context;
    private List<String> list;
    private List<Integer> mHeights;

    public StaggeredHomeAdapter(Context context, List<String> datas) {
        this.list = datas;
        this.context = context;
        mHeights = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mHeights.add((int) (100 + Math.random() * 300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staggered_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.tv.getLayoutParams();
        params.height = mHeights.get(position);
        holder.tv.setLayoutParams(params);
        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_num);
        }
    }

    public void addData(int position) {
        list.add(position, "Insert One");
        mHeights.add(position,(int) (100 + Math.random() * 300));
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void Change(int position) {
        list.remove(position);
        list.add(position, "Insert Two");
        mHeights.add(position,(int) (100 + Math.random() * 300));
        notifyItemChanged(position);
    }
}
