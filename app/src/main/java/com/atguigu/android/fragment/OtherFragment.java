package fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import base.BaseFragment;

public class OtherFragment extends BaseFragment {

    private static final String TAG = OtherFragment.class.getSimpleName();
    private TextView textView;
    @Override
    protected View initView() {
        Log.e(TAG,"OtherFragment页面被初始化了...");
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"其他数据被初始化了...");
        textView.setText("其他");
    }
}
