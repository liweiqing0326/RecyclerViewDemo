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

public class LinearLayoutDemoActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_demo);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3,R.id.btn4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(new Intent(mContext, LinearLayoutDemo1Activity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(mContext, LinearLayoutDemo2Activity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(mContext, LinearLayoutDemo3Activity.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(mContext, LinearLayoutDemo4Activity.class));
                break;
        }
    }
}
