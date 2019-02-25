package android.plat.hexin.com.networkapplication.network.naitvehttp;

import android.plat.hexin.com.networkapplication.App;
import android.plat.hexin.com.networkapplication.R;
import android.plat.hexin.com.networkapplication.network.HttpEntity;
import android.plat.hexin.com.networkapplication.network.RequestParams;
import android.plat.hexin.com.networkapplication.network.executor.ICallBack;
import android.plat.hexin.com.networkapplication.network.nohttp.NoHttpResult;
import android.plat.hexin.com.networkapplication.network.util.HttpUtils;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Author: jcb.
 * on 2019/2/21 0021.
 */
public class NativeHttp {
    /**
     * 发送GET请求
     *
     * @param requestParams
     * @throws Exception
     */
    public static void get(RequestParams requestParams) {
        HttpURLConnection conn;
        try {
            conn = createConnection(requestParams.url, requestParams.params, requestParams.header, "GET");
        } catch (Exception e) {
            e.printStackTrace();
            requestParams.callBack.onError(requestParams.tag, App.get().getString(R.string.http_exception_error));
            return;
        }
        analysisResult(conn, requestParams.tag, requestParams.callBack, requestParams.clazz);
    }

    public static void post(RequestParams requestParams) {
        HttpURLConnection conn;
        try {
            conn = createConnection(requestParams.url, requestParams.params, requestParams.header, "POST");
        } catch (Exception e) {
            e.printStackTrace();
            requestParams.callBack.onError(requestParams.tag, App.get().getString(R.string.http_exception_error));
            return;
        }
        analysisResult(conn, requestParams.tag, requestParams.callBack, requestParams.clazz);
    }

    private static <Entity> void analysisResult(HttpURLConnection conn, int requestTag, ICallBack<Entity> callBack,
                                                Class<Entity> clazz) {
        if (conn == null) return;
        int code = 0;
        try {
            code = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            callBack.onError(requestTag, App.get().getString(R.string.http_exception_error));
        }

        if (code < 200 || code >= 300) {
            callBack.onError(requestTag, App.get().getString(R.string.http_server_error));
            callBack.onFinish(requestTag);
            return;
        }
        HttpEntity httpEntity;
        try {
            httpEntity = JSON.parseObject(read2String(conn.getInputStream()), HttpEntity.class);
        } catch (Exception e) {
            httpEntity = new HttpEntity();
            httpEntity.setFlag(false);
            httpEntity.setMsg(App.get().getString(R.string.http_server_data_format_error));
        }

        //业务返回是否成功
        if (!httpEntity.isFlag()) {
            callBack.onError(requestTag, httpEntity.getMsg());
            return;
        }
        try {
            Entity content = HttpUtils.parseEntity(httpEntity.getData(), clazz);
            callBack.onSuccess(requestTag, new NoHttpResult<>(true, content, code, null));
        } catch (Throwable throwable) {
            callBack.onError(requestTag, App.get().getString(R.string.http_server_data_format_error));
        }

        callBack.onFinish(requestTag);
    }

    private static HttpURLConnection createConnection(String urlStr, HashMap<String, Object> params, Map<String, String> headers, String method) throws Exception {
        String paramsStr = getParam(params, method);
        if ("GET".equals(method)) {
            urlStr = urlStr + paramsStr;
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        if ("POST".equals(method)) {
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            out.write(paramsStr.getBytes("UTF-8"));
        }

        // 设置请求头
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> entryHeaders = headers.entrySet();
            for (Map.Entry<String, String> entryHeader : entryHeaders) {
                conn.setRequestProperty(entryHeader.getKey(), entryHeader.getValue());
            }
        }
        return conn;
    }

    private static String getParam(HashMap<String, Object> params, String method) throws
            UnsupportedEncodingException {
        StringBuilder buf = new StringBuilder();

        Set<Map.Entry<String, Object>> entrys;
        if (params != null && !params.isEmpty()) {
            if ("GET".equals(method)) {
                buf.append("?");
            }
            entrys = params.entrySet();
            for (Map.Entry<String, Object> entry : entrys) {
                if (entry.getValue() instanceof String) {
                    buf.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode((String) entry.getValue(), "UTF-8"))
                            .append("&");
                }

            }
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();

    }


    /**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     * <FORM METHOD=POST ACTION="http://192.168.0.200:8080/ssi/fileload/test.do" enctype="multipart/form-data">
     * <INPUT TYPE="text" NAME="name">
     * <INPUT TYPE="text" NAME="id">
     * <input type="file" name="imagefile"/>
     * <input type="file" name="zip"/>
     * </FORM>
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast
     *               .cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param files  上传文件
     */
    public static boolean uploadFiles(String path, Map<String, String> params, FormFile[] files) throws Exception {
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        if (files != null && files.length != 0) {
            for (FormFile uploadFile : files) {//得到文件类型数据的总长度
                StringBuilder fileExplain = new StringBuilder();
                fileExplain.append("--");
                fileExplain.append(BOUNDARY);
                fileExplain.append("\r\n");
                fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";" +
                        "filename=\"" + uploadFile.getFilname() + "\"\r\n");
                fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                fileExplain.append("\r\n");
                fileDataLength += fileExplain.length();
                if (uploadFile.getInStream() != null) {
                    fileDataLength += uploadFile.getFile().length();
                } else {
                    fileDataLength += uploadFile.getData().length;
                }
            }
        }
        StringBuilder textEntity = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, " +
                "application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, " +
                "application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, " +
                "application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: " + dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        if (files != null && files.length != 0) {
            for (FormFile uploadFile : files) {
                StringBuilder fileEntity = new StringBuilder();
                fileEntity.append("--");
                fileEntity.append(BOUNDARY);
                fileEntity.append("\r\n");
                fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";" +
                        "filename=\"" + uploadFile.getFilname() + "\"\r\n");
                fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                outStream.write(fileEntity.toString().getBytes());
                if (uploadFile.getInStream() != null) {
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    uploadFile.getInStream().close();
                } else {
                    outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
                }
                outStream.write("\r\n".getBytes());
            }
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if (reader.readLine().indexOf("200") == -1) {//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return false;
        }
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return true;
    }

    /**
     * 提交数据到服务器
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast
     *               .cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
    public static boolean uploadFile(String path, Map<String, String> params, FormFile file) throws Exception {
        return uploadFiles(path, params, new FormFile[]{file});
    }

    /**
     * 将输入流转为字节数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] read2Byte(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 将输入流转为字符串
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String read2String(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return new String(outSteam.toByteArray(), "UTF-8");
    }

    /**
     * 发送xml数据
     *
     * @param path     请求地址
     * @param xml      xml数据
     * @param encoding 编码
     * @return
     * @throws Exception
     */
    public static byte[] postXml(String path, String xml, String encoding) throws Exception {
        byte[] data = xml.getBytes(encoding);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=" + encoding);
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        conn.setConnectTimeout(5 * 1000);
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return read2Byte(conn.getInputStream());
        }
        return null;
    }
}
