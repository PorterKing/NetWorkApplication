package android.plat.hexin.com.networkapplication.network.naitvehttp;

import android.plat.hexin.com.networkapplication.network.executor.ExecutorFactory;
import android.plat.hexin.com.networkapplication.network.executor.HttpExecutor;
import android.plat.hexin.com.networkapplication.network.nohttp.NoHttpExecutor;

/**
 * Created by Author: jcb.
 * on 2019/2/21 0021.
 */
public class NativeHttpExecutorFactory implements ExecutorFactory {
    @Override
    public HttpExecutor create() {
        return new HttpExecutor(new NativeHttpExecutor());
    }
}
