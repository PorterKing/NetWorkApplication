package android.plat.hexin.com.networkapplication.network.util;

import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Author: jcb.
 * on 2019/2/21 0021.
 */
public class HttpUtils {

    public static String getUrlWithParams(RequestParams requestParams) {
        String url = requestParams.url;
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> params = requestParams.params;
        if (params == null) return url;

        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            if (value instanceof String) {
                sb.append(key + "=").append(value + "&");
            }
        }
        return url.endsWith("?") ? url + sb.toString() : url + "?" + sb.toString();
    }

    public static <Entity> Entity parseEntity(String responseBody, Class<Entity> clazz) {
        if (TextUtils.isEmpty(responseBody)) return null;
        else return JSON.parseObject(responseBody, clazz);
    }


}
