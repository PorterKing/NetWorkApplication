package android.plat.hexin.com.networkapplication.network.executor;

import android.plat.hexin.com.networkapplication.network.RequestParams;

/**
 * 代理 HttpExecutor
 * Created by Author: jcb.
 * on 2019/2/20 0020.
 */
public class HttpExecutor implements IExecutor {
    private IExecutor mExecutor;

    public HttpExecutor(IExecutor executor) {
        mExecutor = executor;
    }

    @Override
    public void doGet(RequestParams requestParams) {
        mExecutor.doGet(requestParams);
    }

    @Override
    public void doPost(RequestParams requestParams) {
        mExecutor.doPost(requestParams);
    }

    @Override
    public void doPostJson(RequestParams requestParams) {
        mExecutor.doPostJson(requestParams);
    }

    @Override
    public void doUploadFile(RequestParams requestParams) {
        mExecutor.doUploadFile(requestParams);
    }

    @Override
    public void doDownLoad(RequestParams requestParams) {
        mExecutor.doDownLoad(requestParams);
    }

    @Override
    public void cancel(Object tag) {
        mExecutor.cancel(tag);
    }

    @Override
    public void cancelAll() {
        mExecutor.cancelAll();
    }
}
