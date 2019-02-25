package android.plat.hexin.com.networkapplication;

import android.app.Application;
import android.plat.hexin.com.networkapplication.network.NetWork;
import android.plat.hexin.com.networkapplication.network.naitvehttp.NativeHttpExecutorFactory;

/**
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public class App extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        NetWork.init(new NativeHttpExecutorFactory());
    }

    public static Application get() {
        return application;
    }
}
