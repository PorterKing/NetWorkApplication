package android.plat.hexin.com.networkapplication.network.executor;

import android.plat.hexin.com.networkapplication.network.RequestParams;

/**
 * 请求 方法接口
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public interface IExecutor {

    /**
     * get请求
     */
    void doGet(RequestParams requestParams);

    /**
     * post请求 key-value
     *
     */
    void doPost(RequestParams requestParams);

    /**
     * post请求 json
     */
    void doPostJson(RequestParams requestParams);

    /**
     * uploadFile
     */
    void doUploadFile(RequestParams requestParams);

    /**
     * downLoad
     */
    void doDownLoad(RequestParams requestParams);

    /**
     * 取消请求
     */
    void cancel(Object tag);

    /**
     * 取消所有请求
     */
    void cancelAll();
}