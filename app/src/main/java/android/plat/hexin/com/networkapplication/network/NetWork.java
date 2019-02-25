package android.plat.hexin.com.networkapplication.network;

import android.plat.hexin.com.networkapplication.network.executor.ExecutorFactory;
import android.plat.hexin.com.networkapplication.network.executor.HttpExecutor;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;
import android.plat.hexin.com.networkapplication.network.nohttp.NoHttpResult;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public class NetWork {

    public static HttpExecutor executor;
    public static NetWork mInstance;

    private NetWork(ExecutorFactory factory) {
        executor = factory.create();
    }

    /**
     * 设置底层使用哪种网络实现，目前只实现了OkHttpExecutorFactory
     * 如果想扩展的话请继承ExecutorFactory自行实现
     *
     * @param factory
     */
    public static void init(ExecutorFactory factory) {
        if (mInstance == null) {
            synchronized (NetWork.class) {
                if (mInstance == null) {
                    mInstance = new NetWork(factory);
                }
            }
        }
    }

    public static NetWork getInstance() {
        if (mInstance == null || executor == null) {
            throw new RuntimeException("on load application not HXNetWork.init()");
        }
        return mInstance;
    }

    public <Entity> Flowable<Entity> RxRequest(final RequestParams requestParams, final Class<Entity> clazz) {

        return Flowable.create(new FlowableOnSubscribe<Entity>() {
            @Override
            public void subscribe(final FlowableEmitter<Entity> emitter) throws Exception {

                doRequest(requestParams, clazz, new ICallBack<Entity>() {
                    @Override
                    public void onSuccess(int what, NoHttpResult<Entity> response) {
                        emitter.onNext(response.get());
                    }

                    @Override
                    public void onError(int what, String error) {
                        emitter.onError(new Exception(error));
                    }
                });
            }
        }, BackpressureStrategy.BUFFER);
    }

//    public <Entity> Flowable<Entity> RxRequest(ExtraRequest extraRequest ) {
//
//        return Flowable.create(new FlowableOnSubscribe<Entity>() {
//            @Override
//            public void subscribe(final FlowableEmitter<Entity> emitter) throws Exception {
//
//                doRequest(requestParams, clazz, new ICallBack<Entity>() {
//                    @Override
//                    public void onSuccess(int what, NoHttpResult<Entity> response) {
//                        emitter.onNext(response.get());
//                    }
//
//                    @Override
//                    public void onError(int what, String error) {
//                        emitter.onError(new Exception(error));
//                    }
//                });
//            }
//        }, BackpressureStrategy.BUFFER);
//    }


    public void doRequest(RequestParams params, Class clazz, ICallBack callBack) {
        if (params.callBack == null) {
            params.callBack = callBack;
            params.clazz = clazz;
        }
        doRequest(params);
    }

    /**
     * start request
     * 开始网络请求，根据不同的请求方法选择相应的具体实现
     *
     * @param params
     */
    public void doRequest(final RequestParams params) {
        if (params == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (params.method) {
                    case GET:
                        executor.doGet(params);
                        break;
                    case POST:
                        executor.doPost(params);
                        break;
                    case POST_JSON:
                        executor.doPostJson(params);
                        break;
                    case UPLOAD:
                        executor.doUploadFile(params);
                        break;
                    case DOWNLOAD:
                        executor.doDownLoad(params);
                        break;
                }
            }
        }).start();

    }

    /**
     * 取消某个请求,一般在页面销毁的时候取消
     *
     * @param tag
     */
    public void cancleRequest(Object tag) {
        executor.cancel(tag);
    }

    /**
     * 取消所有的请求
     */
    public void cancleAllRequest() {
        executor.cancelAll();
    }
}
