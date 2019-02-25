package android.plat.hexin.com.networkapplication.network.executor;

import android.plat.hexin.com.networkapplication.network.nohttp.NoHttpResult;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public abstract class ICallBack<Entity> {

    /**
     * 成功回调
     */
    public abstract void onSuccess(int what, NoHttpResult<Entity> response);

    /**
     * 失败回调
     */
    public abstract void onError(int what, String error);

    /**
     * 开始回调
     */
    public void onStart(int what) {
    }

    /**
     * 结束回调
     */
    public void onFinish(int what) {
    }

    /**
     * 取消回调
     */
    public void onCancel(int what) {
    }

    /**
     * 进度过程回调
     */
    public void onProgress(int what, int progress, long fileCount, long speed) {
    }
}
