package lwq.com.recyclerviewdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lwq.com.recyclerviewdemo.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.linear_layout_manager)
    Button linearLayoutManager;
    @BindView(R.id.grid_layout_manager)
    Button gridLayoutManager;
    @BindView(R.id.staggered_layout_manager)
    Button staggeredLayoutManager;
    @BindView(R.id.multi_type)
    Button multiType;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext=this;
    }

    @OnClick({R.id.linear_layout_manager, R.id.grid_layout_manager, R.id.staggered_layout_manager, R.id.multi_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_manager:
                startActivity(new Intent(mContext, LinearLayoutDemoActivity.class));
                break;
            case R.id.grid_layout_manager:
                startActivity(new Intent(mContext, GridLayoutDemoActivity.class));
                break;
            case R.id.staggered_layout_manager:
                startActivity(new Intent(mContext, StaggeredLayoutActivity.class));
                break;
            case R.id.multi_type:
                startActivity(new Intent(mContext, MultiTypeActivity.class));
                break;
        }
    }
}
