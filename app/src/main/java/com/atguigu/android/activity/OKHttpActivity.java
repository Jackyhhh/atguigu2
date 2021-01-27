package com.atguigu.android.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.atguigu.android.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpActivity extends Activity implements View.OnClickListener {

    //OKHTTP MediaType
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * GET请求
     */
    private static final int GET = 1;

    /**
     * post请求
     */
    private static final int POST = 2;

    private Button btn_get_post;
    private TextView tv_result;
    private Button btn_get_okhttputils;
    private Button btn_downloadfile;
    private ProgressBar mProgressBar;
    private Button btn_uploadfile;
    private Button btn_image;
    private Button btn_image_list;
    private ImageView iv_icon;
    LoggerInterceptor loggerInterceptor = new LoggerInterceptor(TAG, true);
    private final OkHttpClient client = new OkHttpClient.Builder()
            //添加拦截器
            .addInterceptor(new LoggerInterceptor(TAG, true))
            .build();


    private static final String TAG = OKHttpActivity.class.getSimpleName();

    //添加Handler回调
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GET:
                    tv_result.setText((String) msg.obj);
                    break;

                case POST:
                    tv_result.setText((String) msg.obj);
                    break;

                case 3:
                    mProgressBar.setProgress((int) msg.obj);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        btn_get_post = (Button) findViewById(R.id.btn_get_post);
        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_get_okhttputils = (Button) findViewById(R.id.btn_get_okhttputils);
        btn_downloadfile = (Button) findViewById(R.id.btn_downloadfile);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_uploadfile = (Button) findViewById(R.id.btn_uploadfile);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        btn_image = (Button) findViewById(R.id.btn_image);
        btn_image_list = (Button) findViewById(R.id.btn_image_list);

        //设置点击事件
        btn_get_post.setOnClickListener(this);
        btn_get_okhttputils.setOnClickListener(this);
        btn_downloadfile.setOnClickListener(this);
        btn_uploadfile.setOnClickListener(this);
        btn_image.setOnClickListener(this);
        btn_image_list.setOnClickListener(this);

        //初始化OkHttpClient
        OkHttpUtils.initClient(client);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_post:
                tv_result.setText("");
//                getDataFormGet();
                getDataFromPost();
                break;

            case R.id.btn_get_okhttputils:
//                getDataGetByOkhttpUtils();
                getDataPostByOkhttpUtils();
                break;

            case R.id.btn_downloadfile://下载文件
                /**
                 *为api29+添加动态权限请求
                 */
                if (ContextCompat.checkSelfPermission(OKHttpActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OKHttpActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    downloadFile();
                }
                break;

            case R.id.btn_uploadfile://文件上传
                multiFileUpload();
                break;

            case R.id.btn_image://请求单张图片
                getImage();
                break;

            case R.id.btn_image_list://请求列表中的图片
                Intent intent = new Intent(OKHttpActivity.this, OKHttpListActivity.class);
                startActivity(intent);
                break;


        }
    }

    private void getImage() {
        tv_result.setText("");
        String url = "https://images.csdn.net/20150817/1.jpg";
        OkHttpUtils
                .get()
                .url(url)
                .tag(this)
                .build()
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tv_result.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        iv_icon.setImageBitmap(bitmap);
                    }
                });
    }

    private void getDataPostByOkhttpUtils() {

        //链接已经失效了，暂时没有找到新的代替
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .post()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    /**
     * 使用okhttp-utils上传多个或者单个文件
     */
    private void multiFileUpload() {
        String mBaseUrl = "http://192.168.39.84:8080/FileUpload/FileUploadServlet";
        File file = new File(Environment.getExternalStorageDirectory(), "afu.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test.txt");
        if (!file.exists() || !file2.exists()) {
            Toast.makeText(OKHttpActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "Jackyhhh");
        params.put("password", "123");

        String url = mBaseUrl;
        OkHttpUtils.post()
                .addFile("mFile", "server_afu.png", file)
                .addFile("mFile", "server_test.txt", file2)
                .url(url)
                .params(params)
                .build()
                .execute(new MyStringCallback());
    }


    /**
     * 使用okhttp-utils下载大文件
     */
    private void downloadFile() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //尝试用自己封装的类下载;
//                    String url = "https://vfx.mtime.cn/Video/2021/01/25/mp4/210125124902223191.mp4";
//                    String fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    DownloadUtil.get().download(url, fileUrl, "电影.mp4", new DownloadUtil.OnDownloadListener(){
//                                @Override
//                                public void onDownloadSuccess(File file) {
//                                    Message msg = Message.obtain();
//                                    msg.what = GET;
//                                    msg.obj = "下载完成";
//                                    handler.sendMessage(msg);
//                                    Log.e(TAG, "onDownloadSuccess :" + file.getAbsolutePath());
//                                }
//
//                                @Override
//                                public void onDownloading(int progress) {
//                                    Message msg = Message.obtain();
//                                    msg.what = 3;
//                                    msg.obj = progress;
//                                    handler.sendMessage(msg);
//                                    Log.e(TAG, "onDownloading :" + (int) (progress));
//                                }
//
//                                @Override
//                                public void onDownloadFailed(Exception e) {
//                                    e.printStackTrace();
//                                    Log.e(TAG, "onDownloadFailed :" + e.getMessage());
//                                }
//                            }
//                    );
                String url = "https://vfx.mtime.cn/Video/2021/01/25/mp4/210125124902223191.mp4";
                OkHttpUtils
                        .get()
                        .url(url)
                        .build()
                        .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "okhttp-utils-test.mp4") {
                            @Override
                            public void inProgress(float progress, long total, int id) {
                                mProgressBar.setProgress((int) (100 * progress));
                                Log.e(TAG, "inProgress :" + (int) (100 * progress));
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e(TAG, "onError :" + e.getMessage());
                            }

                            @Override
                            public void onResponse(File file, int id) {
                                Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                            }
                        });
            }
        }.start();


    }

    private void getDataGetByOkhttpUtils() {
        String url = "https://www.555x.org/home/down/txt/id/54158";
        OkHttpUtils
                .post()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {

        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id) {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tv_result.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            tv_result.setText("onResponse:" + response);
            switch (id) {
                case 100:
                    Toast.makeText(OKHttpActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(OKHttpActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }
    }


    /**
     * 使用post请求网络数据
     */
    private void getDataFromPost() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = post("https://www.lagou.com/jobs/positionAjax.json", "");
                    Log.e(TAG, result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 使用get请求网络数据
     */
    private void getDataFormGet() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //原url失效，采用新的代替;
                    String result = get("https://www.it610.com/article/1282460251861303296.htm");
                    Log.e(TAG, result);
                    Message message = Message.obtain();
                    message.what = GET;
                    message.obj = result;
                    handler.sendMessage(message);
                    ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * okhttp3的get请求
     *
     * @param url 网络连接
     * @return
     * @throws IOException
     */
    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * okhttp3的post请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}

