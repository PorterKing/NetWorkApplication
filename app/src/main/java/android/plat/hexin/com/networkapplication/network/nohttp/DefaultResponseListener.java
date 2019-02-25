package android.plat.hexin.com.networkapplication.network.nohttp;

import android.app.Dialog;
import android.content.Context;
import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.R;
import android.plat.hexin.com.networkapplication.network.ProgressDialog;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;
import android.support.annotation.NonNull;

import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public class DefaultResponseListener<T> implements OnResponseListener<NoHttpResult<T>> {

    private Context mContext;
    private ICallBack<T> callBack;
    private Dialog mDialog;

    public DefaultResponseListener(@NonNull Context context, ICallBack<T> callback, boolean dialog) {
        this.mContext = context;
        this.callBack = callback;
        if (dialog) {
            mDialog = new ProgressDialog(mContext);
        }
    }

    @Override
    public void onStart(int what) {
        if (callBack != null) callBack.onStart(what);
        if (mDialog != null && !mDialog.isShowing()) mDialog.show();
    }

    @Override
    public void onFinish(int what) {
        if (callBack != null) callBack.onFinish(what);
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    public void onSucceed(int what, Response<NoHttpResult<T>> response) {
        NoHttpResult<T> tResult = response.get();
        tResult.setFromCache(response.isFromCache());
        if (callBack != null) callBack.onSuccess(what, tResult);
    }

    @Override
    public void onFailed(int what, Response<NoHttpResult<T>> response) {
        Exception exception = response.getException();

        int stringRes = R.string.http_exception_unknow_error;
        if (exception instanceof NetworkError) {
            stringRes = R.string.http_exception_network;
        } else if (exception instanceof TimeoutError) {
            stringRes = R.string.http_exception_connect_timeout;
        } else if (exception instanceof UnKnownHostError) {
            stringRes = R.string.http_exception_host;
        } else if (exception instanceof URLError) {
            stringRes = R.string.http_exception_url;
        }
        if (callBack != null) {
            callBack.onError(what, App.get().getString(stringRes));
        }
    }
}