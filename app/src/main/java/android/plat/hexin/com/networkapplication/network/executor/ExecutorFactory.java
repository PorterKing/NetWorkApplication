package android.plat.hexin.com.networkapplication.network.executor;

/**
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public interface  ExecutorFactory {

    /**
     * 具体请求工厂类，扩展请继承
     */
    HttpExecutor create();

}
