package android.plat.hexin.com.networkapplication.network.nohttp;

import android.content.Context;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public class CallServer {

    private static CallServer sInstance;

    public static CallServer getInstance() {
        if (sInstance == null) synchronized (CallServer.class) {
            if (sInstance == null) sInstance = new CallServer();
        }
        return sInstance;
    }

    private RequestQueue mRequestQueue;

    private CallServer() {
        mRequestQueue = NoHttp.newRequestQueue(3);
    }

    public <T> void request(Context context, NoHttpRequest<T> request, boolean dialog, ICallBack<T> callback) {
        mRequestQueue.add(request.mRequestParams.tag, request, new DefaultResponseListener<>(context, callback, dialog));
    }

    public void cancelBySign(Object sign) {
        mRequestQueue.cancelBySign(sign);
    }

    public void cancelAll() {
        mRequestQueue.cancelAll();
    }
}