package android.plat.hexin.com.networkapplication.network.nohttp;

import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.R;
import android.plat.hexin.com.networkapplication.network.HttpEntity;
import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.util.HttpUtils;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.tools.HeaderUtils;
import com.yanzhenjie.nohttp.tools.IOUtils;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public  class NoHttpRequest<Entity> extends Request<NoHttpResult<Entity>> {

    public RequestParams mRequestParams;

    public NoHttpRequest(RequestParams requestParams, RequestMethod requestMethod) {
        super(requestParams.url, requestMethod);
        setAccept(Headers.HEAD_VALUE_CONTENT_TYPE_JSON);
        mRequestParams = requestParams;
    }

    @Override
    public void onPreExecute() {
        // TODO 此方法在子线程中被调用，所以很合适用来做加密。并且是在真正发起请求前被调用。
    }

    @Override
    public final NoHttpResult<Entity> parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        String body = "";
        if (responseBody != null && responseBody.length > 0) {
            body = parseResponseString(responseHeaders, responseBody);
        }
        Logger.i("body：" + body);

        int resCode = responseHeaders.getResponseCode();
        if (resCode >= 200 && resCode < 300) {
            HttpEntity httpEntity;
            try {
                httpEntity = JSON.parseObject(body, HttpEntity.class);
            } catch (Exception e) {
                httpEntity = new HttpEntity();
                httpEntity.setFlag(false);
                httpEntity.setMsg(App.get().getString(R.string.http_server_data_format_error));
            }
            // 业务成功，解析data。
            if (httpEntity.isFlag()) {
                try {
                    Entity result = (Entity) HttpUtils.parseEntity(httpEntity.getData(), mRequestParams.clazz);
                    return new NoHttpResult<>(true, result, resCode, null);
                } catch (Throwable throwable) {
                    return new NoHttpResult<>(false, null, resCode, App.get().getString(R.string
                            .http_server_data_format_error));
                }
            } else {
                return new NoHttpResult<>(false, null, resCode, httpEntity.getMsg());
            }
        } else if (resCode >= 400 && resCode < 500) {
            return new NoHttpResult<>(false, null, resCode, App.get().getString(R.string.http_unknow_error));
        }
        return new NoHttpResult<>(false, null, resCode, App.get().getString(R.string.http_server_error));
    }


    private static String parseResponseString(Headers responseHeaders, byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) return "";
        String charset = HeaderUtils.parseHeadValue(responseHeaders.getContentType(), "charset", "");
        return IOUtils.toString(responseBody, charset);
    }
}