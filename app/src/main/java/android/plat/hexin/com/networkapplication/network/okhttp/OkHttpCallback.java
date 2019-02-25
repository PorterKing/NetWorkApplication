package android.plat.hexin.com.networkapplication.network.okhttp;

import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.R;
import android.plat.hexin.com.networkapplication.network.HttpEntity;
import android.plat.hexin.com.networkapplication.network.util.HttpUtils;
import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;
import android.plat.hexin.com.networkapplication.network.nohttp.NoHttpResult;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Author: jcb.
 * on 2019/2/20 0020.
 */
public class OkHttpCallback<Entity> implements Callback {

    int requestTag;
    ICallBack<Entity> callBack;
    RequestParams requestParams;

    public OkHttpCallback(RequestParams requestParams) {
        this.callBack = requestParams.callBack;
        this.requestTag = requestParams.tag;
        this.requestParams = requestParams;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("OkHttp ", "onFailure:" + e.toString());
        e.printStackTrace();
        callBack.onError(requestTag, App.get().getString(R.string.http_server_error));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d("OkHttp ", "response.code:" + response.code() + "\n" + "response:" + response.body().toString());
        int requestTag = this.requestTag;
        if (!response.isSuccessful()) {
            callBack.onError(requestTag, App.get().getString(R.string.http_server_error));
            callBack.onFinish(requestTag);
            return;
        }
        //TODO 下载逻辑
        HttpEntity httpEntity;
        try {
            httpEntity = JSON.parseObject(response.body().string(), HttpEntity.class);
        } catch (Exception e) {
            httpEntity = new HttpEntity();
            httpEntity.setFlag(false);
            httpEntity.setMsg(App.get().getString(R.string.http_server_data_format_error));
        }

        //业务返回是否成功
        if (!httpEntity.isFlag()) {
            callBack.onError(requestTag, httpEntity.getMsg());
            return;
        }

        try {
            Entity content = (Entity) HttpUtils.parseEntity(httpEntity.getData(), requestParams.clazz);
            callBack.onSuccess(requestTag, new NoHttpResult<>(true, content, response.code(), null));
        } catch (Throwable throwable) {
            callBack.onError(requestTag, App.get().getString(R.string.http_server_data_format_error));
        }

        callBack.onFinish(requestTag);
    }


}