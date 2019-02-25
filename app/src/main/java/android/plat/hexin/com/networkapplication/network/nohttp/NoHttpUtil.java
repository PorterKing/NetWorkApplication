package android.plat.hexin.com.networkapplication.network.nohttp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Author: jcb.
 * on 2019/1/17 0017.
 */
public class NoHttpUtil {

    public static void addHeader(HashMap<String, String> header, NoHttpRequest<String> request) {
        if (header == null || header.size() < 1 || request == null) return;
        Iterator<Map.Entry<String, String>> iterator = header.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            request.addHeader(next.getKey(), next.getValue());
        }
    }
}
