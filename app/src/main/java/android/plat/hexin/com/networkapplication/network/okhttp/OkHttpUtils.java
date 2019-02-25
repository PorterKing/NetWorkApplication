package android.plat.hexin.com.networkapplication.network.okhttp;

import android.plat.hexin.com.networkapplication.network.RequestParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;

/**
 * Created by Author: jcb.
 * on 2019/2/21 0021.
 */
public class OkHttpUtils {

    public static FormBody getPostBody(RequestParams requestParams) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        HashMap<String, Object> params = requestParams.params;
        if (params == null) return bodyBuilder.build();

        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            if(value instanceof  String){
                bodyBuilder.addEncoded(key, value.toString());
            }
        }
        return bodyBuilder.build();
    }

    public static Headers getHeaders(RequestParams requestParams) {
        Headers.Builder builder = new Headers.Builder();
        HashMap<String, String> params = requestParams.header;
        if (params == null) return builder.build();

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.add(next.getKey(), next.getValue());
        }
        return builder.build();
    }
}
