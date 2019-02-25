package android.plat.hexin.com.networkapplication.network.nohttp;

import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.executor.IExecutor;
import android.plat.hexin.com.networkapplication.network.nohttp.download.Download;
import android.plat.hexin.com.networkapplication.network.nohttp.download.DownloadCallback;

import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Nohttp请求 实现方法
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public class NoHttpExecutor implements IExecutor {


    public NoHttpExecutor(InitializationConfig config) {
        NoHttp.initialize(config);
    }


    @Override
    public void doGet(RequestParams requestParams) {
        NoHttpRequest request = new NoHttpRequest(requestParams, RequestMethod.GET);
        request.add(requestParams.params);
        NoHttpUtil.addHeader(requestParams.header, request);
        request.setCancelSign(requestParams.tag);
        CallServer.getInstance().request(App.get(), request, false, requestParams.callBack);
    }

    @Override
    public void doPost(RequestParams requestParams) {
        NoHttpRequest request = new NoHttpRequest(requestParams, RequestMethod.POST);
        request.add(requestParams.params);
        NoHttpUtil.addHeader(requestParams.header, request);
        request.setCancelSign(requestParams.tag);
        CallServer.getInstance().request(App.get(), request, false, requestParams.callBack);
    }

    @Override
    public void doPostJson(RequestParams requestParams) {
        NoHttpRequest request = new NoHttpRequest(requestParams, RequestMethod.POST);
        request.setDefineRequestBody(requestParams.body, "application/json; charset=utf-8");
        NoHttpUtil.addHeader(requestParams.header, request);
        request.setCancelSign(requestParams.tag);
        CallServer.getInstance().request(App.get(), request, false, requestParams.callBack);
    }


    @Override
    public void doUploadFile(RequestParams requestParams) {
        if (requestParams.files == null && requestParams.files.size() < 1) return;

        NoHttpRequest request = new NoHttpRequest(requestParams, RequestMethod.POST);
        HashMap<String, File> files = requestParams.files;
        for (Map.Entry<String, File> next : files.entrySet()) {
            String key = next.getKey();
            File value = next.getValue();
            FileBinary binary = new FileBinary(value);
            request.add(key, binary);
        }
        NoHttpUtil.addHeader(requestParams.header, request);
        request.setCancelSign(requestParams.tag);
        CallServer.getInstance().request(App.get(), request, false, requestParams.callBack);

    }

    @Override
    public void doDownLoad(RequestParams requestParams) {
        DownloadRequest request = new DownloadRequest(requestParams.url, RequestMethod.GET, requestParams
                .downLoadFilePath, requestParams.downLoadFileName, true, true);
        request.setCancelSign(requestParams.tag);
        Download.getInstance().download(requestParams.tag, request, new DownloadCallback(requestParams.callBack));
    }

    @Override
    public void cancel(Object tag) {
        CallServer.getInstance().cancelBySign(tag);
    }

    @Override
    public void cancelAll() {
        Logger.e("onDestroy() " + getClass().getName());
        CallServer.getInstance().cancelAll();
    }


}
