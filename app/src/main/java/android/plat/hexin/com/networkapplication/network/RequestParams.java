package android.plat.hexin.com.networkapplication.network;


import android.plat.hexin.com.networkapplication.network.executor.ICallBack;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * 请求数据结构
 * Created by Author: jcb.
 * on 2019/1/15 0015.
 */
public class RequestParams {
    public String url;                                             // 请求链接
    public HashMap<String, String> header = new HashMap<>();       // 请求头
    public HashMap<String, Object> params = new HashMap<>();       // 请求参数
    public HashMap<String, File> files = new HashMap<>();          // 请求文件
    public Charset charset;                                        // 编码
    public int tag;                                                // 请求标识
    public String body;                                            // PostJson body参数
    public Method method;                                          // 请求方法
    public String downLoadFilePath;                                // 下载文件路径
    public String downLoadFileName;                                // 下载文件名称
    public ICallBack callBack;                                     // 请求返回
    public Class clazz;                                            // 请求返回的数据结构class


    public static class Builder {
        private String url;
        private HashMap<String, String> header = new HashMap<>();
        private HashMap<String, Object> params = new HashMap<>();
        private HashMap<String, File> files = new HashMap<>();
        private Charset charset = Charset.forName("UTF-8");
        private int tag = 0;
        public String downLoadFilePath;
        public String downLoadFileName;
        private Method method = Method.GET;
        private String body;
        public ICallBack callBack;
        public Class clazz = java.lang.String.class;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder header(String key, String value) {
            this.header.put(key, value);
            return this;
        }

        public Builder header(HashMap<String, String> header) {
            this.header.putAll(header);
            return this;
        }

        public Builder params(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder params(HashMap<String, Object> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder files(String key, File file) {
            this.files.put(key, file);
            return this;
        }

        public Builder defaultCharset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder tag(int tag) {
            this.tag = tag;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder downLoadFilePath(String downLoadFilePath) {
            this.downLoadFilePath = downLoadFilePath;
            return this;
        }

        public Builder downLoadFileName(String downLoadFileName) {
            this.downLoadFileName = downLoadFileName;
            return this;
        }

        public Builder setCallBack(ICallBack<String> callBack) {
            this.callBack = callBack;
            return this;
        }

        public <Entity> Builder setCallBack(Class<Entity> clazz, ICallBack<Entity> callBack) {
            this.callBack = callBack;
            this.clazz = clazz;
            return this;
        }


        public RequestParams build() {
            RequestParams obj = new RequestParams();
            obj.url = this.url;
            obj.header = this.header;
            obj.params = this.params;
            obj.files = this.files;
            obj.charset = this.charset;
            obj.tag = this.tag;
            obj.method = this.method;
            obj.body = this.body;
            obj.downLoadFilePath = this.downLoadFilePath;
            obj.downLoadFileName = this.downLoadFileName;
            obj.clazz = this.clazz;
            obj.callBack = this.callBack;
            return obj;
        }
    }
}
