package android.plat.hexin.com.networkapplication.network.nohttp;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public class NoHttpResult<Entity> {

    private boolean isSucceed;       // 业务是否成功。
    private Entity t;                // 结果
    private int code;                // 业务代码，目前和Http相同。
    private String message;          // 错误消息。
    private boolean isFromCache;     // 是否从缓存获取

    public NoHttpResult(boolean isSucceed, Entity t, int code, String message) {
        this.isSucceed = isSucceed;
        this.t = t;
        this.code = code;
        this.message = message;
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public Entity get() {
        return t;
    }

    public int getCode() {
        return code;
    }

    public String error() {
        return message;
    }

    public void setFromCache(boolean isFromCache) {
        this.isFromCache = isFromCache;
    }


}
