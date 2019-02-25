package android.plat.hexin.com.networkapplication.network.okhttp;

import android.plat.hexin.com.networkapplication.network.HttpConstant;
import android.plat.hexin.com.networkapplication.network.executor.ExecutorFactory;
import android.plat.hexin.com.networkapplication.network.executor.HttpExecutor;

/**
 * Created by Author: jcb.
 * on 2019/2/20 0020.
 */
public class OKHttpExecutorFactory  implements ExecutorFactory {
    @Override
    public HttpExecutor create() {
        //建造者模式，生成配置对象
        OkHttpConfig config = new OkHttpConfig.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(HttpConstant.CONNECTTIMEOUT)
                .readTimeout(HttpConstant.READTIMEOUT)
                .writeTimeout(HttpConstant.WRITETIMEOUT)
                .build();
        return new HttpExecutor(new OkHttpExecutor(config)) ;
    }
}
