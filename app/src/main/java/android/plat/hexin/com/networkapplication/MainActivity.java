package android.plat.hexin.com.networkapplication;

import android.os.Bundle;
import android.plat.hexin.com.networkapplication.network.Method;
import android.plat.hexin.com.networkapplication.network.NetWork;
import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;
import android.plat.hexin.com.networkapplication.network.nohttp.NoHttpResult;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button get, post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get = (Button) findViewById(R.id.get);
        post = (Button) findViewById(R.id.post);

        get.setOnClickListener(this);
        post.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                doGet();
                break;
            case R.id.post:
                break;
        }

    }

    private void doGet() {
        String url = "https://mall.stocke.com.cn/hexinifs/rs/cms/discussion/getDiscussionList";
        Disposable disposable = NetWork.getInstance().RxRequest(new RequestParams.Builder()
                .url(url)
                .method(Method.GET)
                .params("page.page", "1")
                .params("page.size", "20")
                .build(), String.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String bean) throws Exception {
                        Log.e("onSuccess", bean == null ? "null" :bean.toString());
                    }

                });

    }

    private void doPost() {
        String url = "http://127.0.0.1:8080/test/test";
        NetWork.getInstance().doRequest(new RequestParams.Builder()
                .url(url)
                .method(Method.POST)
                .params("key1", "hello")
                .params("key2", "doGet")
                .setCallBack(new ICallBack<String>() {
                    @Override
                    public void onSuccess(int what, NoHttpResult<String> response) {

                    }

                    @Override
                    public void onError(int what, String error) {

                    }
                })
                .build());
    }
}
