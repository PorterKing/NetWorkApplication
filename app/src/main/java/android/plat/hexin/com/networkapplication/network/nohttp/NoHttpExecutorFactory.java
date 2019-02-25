package android.plat.hexin.com.networkapplication.network.nohttp;

import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.network.HttpConstant;
import android.plat.hexin.com.networkapplication.network.executor.ExecutorFactory;
import android.plat.hexin.com.networkapplication.network.executor.HttpExecutor;

import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;

/**
 * 初始化创建 Nohttp
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public class NoHttpExecutorFactory implements ExecutorFactory {

    @Override
    public HttpExecutor create() {
        InitializationConfig config = InitializationConfig.newBuilder(App.get())
                .connectionTimeout(HttpConstant.CONNECTTIMEOUT)
                .readTimeout(HttpConstant.READTIMEOUT)
                .networkExecutor(new URLConnectionNetworkExecutor())
                .cacheStore( new DBCacheStore(App.get()).setEnable(true))
                .build();
        return new HttpExecutor(new NoHttpExecutor(config));
    }
}
