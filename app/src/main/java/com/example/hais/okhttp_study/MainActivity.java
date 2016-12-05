package com.example.hais.okhttp_study;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Bind(R.id.tv_content)
    TextView tvContent;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_main_synchronous_get, R.id.btn_mian_asynchronous_get, R.id.btn_main_post, R.id.button4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_synchronous_get:
                synchronousGet();
                break;
            case R.id.btn_mian_asynchronous_get:
                asynchronousGet();
                break;
            case R.id.btn_main_post:
                post();
                break;
            case R.id.button4:
                break;

        }
    }

    private void post() {
        OkHttpClient okHttpClient =new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, "在此传入需要的json数据");//此处暂时没有服务器可测试，大致流程是这样

        Request request = new Request.Builder()
                .post(body)
                .url("http://www.baidu.com")
                .build();
        //3.开始请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText("请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.e("tag",response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText("请求成功"+response.body().toString());                    }
                });
            }
        });
    }

    /***
     * 异步Get
     */
    private void asynchronousGet() {
        //1.创建OkHttpClient 客户端
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建请求参数
        Request request =  new Request.Builder().url("http://www.baidu.com").build();

        //3.开始请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText("请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.e("tag",response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText("请求成功"+response.body().toString());                    }
                });
            }
        });
    }

    /**
     * 同步get
     */
    private void synchronousGet() {
        new Thread(new Runnable() {//同步操作，需要把网络请求放到子线程中
            @Override
            public void run() {
                try {
                    //1.新建OkHttpClient客户端
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //2.新建一个Request 对象
                    Request request = new Request.Builder().url("http://www.jianshu.com/p/27c1554b7fee").build();
                    //3.响应
                    final Response response = okHttpClient.newCall(request).execute();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.isSuccessful()){
                                tvContent.setText("请求成功"+response.body().toString());
                            }else {
                                tvContent.setText("请求失败"+response.message().toString());
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
