package android.plat.hexin.com.networkapplication.network.okhttp;

import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.executor.IExecutor;
import android.plat.hexin.com.networkapplication.network.okhttp.help.ProgressRequestBody;
import android.plat.hexin.com.networkapplication.network.util.HttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Author: jcb.
 * on 2019/2/20 0020.
 */
public class OkHttpExecutor implements IExecutor {
    public static OkHttpClient mClient;
    public static final Map<Object, ArrayList<Call>> calls = new HashMap<>();

    public OkHttpExecutor(OkHttpConfig config) {

        //配置okhttpclient，全局唯一
        OkHttpClient.Builder builder = new OkHttpClient.Builder().retryOnConnectionFailure(config
                .retryOnConnectionFailure).connectTimeout(config.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(config.readTimeout, TimeUnit.MILLISECONDS).writeTimeout(config.writeTimeout, TimeUnit
                        .MILLISECONDS);
        if (config.cache != null) {
            builder.cache(config.cache);
        }

        if (config.x509TrustManager != null && config.sslSocketFactory != null) {
            builder.sslSocketFactory(config.sslSocketFactory, config.x509TrustManager);
        }
        if (config.interceptors != null && config.interceptors.size() > 0) {
            for (int i = 0; i < config.interceptors.size(); i++) {
                builder.addInterceptor(config.interceptors.get(i));
            }
        }

        if (config.networkInterceptors != null && config.networkInterceptors.size() > 0) {
            for (int i = 0; i < config.networkInterceptors.size(); i++) {
                builder.addNetworkInterceptor(config.networkInterceptors.get(i));
            }
        }

        mClient = builder.build();
    }

    @Override
    public void doGet(RequestParams requestParams) {
        Request request = new Request.Builder()
                .url(HttpUtils.getUrlWithParams(requestParams))
                .get()
                .headers(OkHttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag)
                .build();
        okhttp3.Call call = mClient.newCall(request);
        call.enqueue(new OkHttpCallback(requestParams));
        cacheCall(requestParams, call);
    }


    @Override
    public void doPost(RequestParams requestParams) {
        Request request = new Request.Builder()
                .url(requestParams.url)
                .post(OkHttpUtils.getPostBody(requestParams))
                .headers(OkHttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new OkHttpCallback(requestParams));
        cacheCall(requestParams, call);
    }

    @Override
    public void doPostJson(RequestParams requestParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                requestParams.body);
        Request request = new Request.Builder()
                .url(requestParams.url)
                .post(requestBody)
                .headers(OkHttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new OkHttpCallback(requestParams));
        cacheCall(requestParams, call);
    }

    @Override
    public void doUploadFile(RequestParams requestParams) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (requestParams.files != null && requestParams.files.size() > 0) {
            Iterator<Map.Entry<String, File>> iterator = requestParams.files.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, File> next = iterator.next();
                String key = next.getKey();
                File value = next.getValue();
                builder.addFormDataPart(key, value.getName(), RequestBody.create(MediaType.parse("image/png"), value));
            }
        }
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestParams, builder.build());
        Request request = new Request.Builder()
                .url(requestParams.url)
                .post(progressRequestBody)
                .headers(OkHttpUtils.getHeaders(requestParams)).tag(requestParams.tag).build();
        Call call = mClient.newCall(request);
        call.enqueue(new OkHttpCallback(requestParams));
        cacheCall(requestParams, call);
    }

    @Override
    public void doDownLoad(RequestParams requestParams) {
        Request request = new Request.Builder()
                .url(HttpUtils.getUrlWithParams(requestParams))
                .get()
                .headers (OkHttpUtils.getHeaders(requestParams))
                .tag(requestParams.tag)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new OkHttpCallback(requestParams));
        cacheCall(requestParams, call);
    }

    @Override
    public void cancel(Object tag) {
        ArrayList<Call> values = calls.get(tag);
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                Call call = values.get(i);
                if (!call.isCanceled()) {
                    call.cancel();
                }
                values.remove(call);
            }
        }
    }

    @Override
    public void cancelAll() {
        Iterator<Map.Entry<Object, ArrayList<Call>>> iterator = calls.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, ArrayList<Call>> next = iterator.next();
            Object key = next.getKey();
            cancel(key);
        }
    }

    public void cacheCall(RequestParams requestParams, okhttp3.Call call) {
        ArrayList<Call> values = calls.get(requestParams.tag);
        if (values == null) {
            values = new ArrayList<>();
            values.add(call);
            calls.put(requestParams.tag, values);
        } else {
            values.add(call);
        }
    }
}
