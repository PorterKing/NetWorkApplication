package android.plat.hexin.com.networkapplication.network.nohttp.download;

import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.R;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;

/**
 * Created by Author: jcb.
 * on 2019/1/17 0017.
 */
public class DownloadCallback implements DownloadListener {

    private ICallBack callBack;

    public DownloadCallback(ICallBack callback) {
        this.callBack = callback;
    }

    @Override
    public void onDownloadError(int what, Exception e) {
        String message;
        if (e instanceof NetworkError) {
            message = App.get().getString(R.string.http_exception_network);
        } else if (e instanceof URLError) {
            message = App.get().getString(R.string.http_exception_url);
        } else if (e instanceof UnKnownHostError) {
            message = App.get().getString(R.string.http_exception_host);
        } else if (e instanceof TimeoutError) {
            message = App.get().getString(R.string.http_exception_connect_timeout);
        } else {
            message = App.get().getString(R.string.http_exception_unknow_error);
        }
        Logger.e(e);
        if (callBack != null) callBack.onError(what, message);
    }

    @Override
    public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
        if (callBack != null) callBack.onStart(what);
    }

    @Override
    public void onProgress(int what, int progress, long fileCount, long speed) {
        if (callBack != null) callBack.onProgress(what, progress, fileCount, speed);
    }

    @Override
    public void onFinish(int what, String filePath) {
        if (callBack != null) callBack.onFinish(what);
    }

    @Override
    public void onCancel(int what) {
        if (callBack != null) callBack.onCancel(what);

    }
}