package com.atguigu.android.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.android.base.BaseFragment;


public class ThirdPartyFragment extends BaseFragment {

    private static final String TAG = ThirdPartyFragment.class.getSimpleName();
    private TextView textView;
    @Override
    protected View initView() {
        Log.e(TAG,"ThirdPartyFragment页面被初始化了...");
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"第三方框架数据被初始化了...");
        textView.setText("第三方框架");
    }
}
