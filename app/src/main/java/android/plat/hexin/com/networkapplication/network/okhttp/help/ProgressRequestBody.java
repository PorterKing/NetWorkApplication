package android.plat.hexin.com.networkapplication.network.okhttp.help;

import android.plat.hexin.com.networkapplication.network.RequestParams;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Author: jcb.
 * on 2019/2/21 0021.
 */
public class ProgressRequestBody extends RequestBody {

    private final RequestBody body;
    private final RequestParams params;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestParams params, RequestBody body) {
        this.body = body;
        this.params = params;
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return body.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        body.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            private long current;
            private long total;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (total == 0) {
                    total = contentLength();
                }
                current += byteCount;
                float progress = current * 1.0f / total * 100;
                params.callBack.onProgress(params.tag, (int) progress, total, 0);
            }
        };
    }

}