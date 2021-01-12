package com.atguigu.android;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioGroup;


import java.util.ArrayList;
import java.util.List;

import com.atguigu.android.base.BaseFragment;
import com.atguigu.android.fragment.CommonFrameFragment;
import com.atguigu.android.fragment.CustomFragment;
import com.atguigu.android.fragment.OtherFragment;
import com.atguigu.android.fragment.ThirdPartyFragment;


//主页面

public class MainActivity extends FragmentActivity {

    private List<BaseFragment> mBaseFragment;
    private RadioGroup mRg_main;

    /**
     * 选中的Fragment的对应的位置
     */
    private int position;

    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化view
        initView();
        //初始化Fragment
        initFragment();
//        //设置RadioGroup的监听
        setListener();
    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_common_frame);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_common_frame://常用框架
                    position = 0;
                    break;
                case R.id.rb_thirdparty://第三方
                    position = 1;
                    break;
                case R.id.rb_custom://自定义
                    position = 2;
                    break;
                case R.id.rb_other://其他
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();

            //switch
            switchFragment(mContent,to);

        }
    }

    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to){
            mContent = to;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (!to.isAdded()){
                if (from !=null){
                    fragmentTransaction.hide(from);
                }
                if (to != null){
                    fragmentTransaction.add(R.id.fl_content,to).commit();
                }
            }else {
                if (from != null){
                    fragmentTransaction.hide(from);
                }

                if(to != null){
                    fragmentTransaction.show(to).commit();
                }


            }
        }
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new CommonFrameFragment());
        mBaseFragment.add(new ThirdPartyFragment());
        mBaseFragment.add(new CustomFragment());
        mBaseFragment.add(new OtherFragment());

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);

    }
}