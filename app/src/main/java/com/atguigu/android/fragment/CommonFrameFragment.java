package com.atguigu.android.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.atguigu.android.R;
import com.atguigu.android.activity.OKHttpActivity;
import com.atguigu.android.base.BaseFragment;

import Adapter.CommonFrameFragmentAdapter;

public class CommonFrameFragment extends BaseFragment {

    private static final String TAG = CommonFrameFragment.class.getSimpleName();
    private ListView mListView;

    private String[] datas;
    private CommonFrameFragmentAdapter adapter;

    @Override
    protected View initView() {
        Log.e(TAG,"常用框架Fragment页面被初始化了...");
        View view = View.inflate(mContext,R.layout.fragment_common_frame,null);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = datas[position];
                if (data.toLowerCase().equals("okhttp")) {
                    Intent intent = new Intent(mContext, OKHttpActivity.class);
                    mContext.startActivity(intent);
                }
                Toast.makeText(mContext, "data=="+data, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"常用框架数据被初始化了...");
        datas = new String[]{"OKHttp", "xUtils3","Retrofit2","Fresco","Glide","greenDao","RxJava","volley","Gson","FastJson","picasso","evenBus","jcvideoplayer","pulltorefresh","Expandablelistview","UniversalVideoView","....."};
        adapter = new CommonFrameFragmentAdapter(mContext,datas);
        mListView.setAdapter(adapter);

    }
}
