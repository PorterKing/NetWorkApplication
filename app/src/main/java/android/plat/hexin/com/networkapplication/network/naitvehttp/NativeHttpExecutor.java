package android.plat.hexin.com.networkapplication.network.naitvehttp;

import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.executor.IExecutor;

/**
 * Created by Author: jcb.
 * on 2019/2/21 0021.
 */
public class NativeHttpExecutor implements IExecutor {
    @Override
    public void doGet(RequestParams requestParams) {
        try {
            NativeHttp.get(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(RequestParams requestParams) {
        try {
            NativeHttp.post(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPostJson(RequestParams requestParams) {

    }

    @Override
    public void doUploadFile(RequestParams requestParams) {

    }

    @Override
    public void doDownLoad(RequestParams requestParams) {

    }

    @Override
    public void cancel(Object tag) {

    }

    @Override
    public void cancelAll() {

    }
}
