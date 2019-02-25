package android.plat.hexin.com.networkapplication.network;

/**
 * 请求类型
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public enum Method {

    GET(1), POST(2), POST_JSON(3), UPLOAD(4), DOWNLOAD(5);

    int value;

    Method(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
