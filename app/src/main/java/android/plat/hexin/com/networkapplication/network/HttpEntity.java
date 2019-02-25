package android.plat.hexin.com.networkapplication.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Author: jcb.
 * on 2019/1/16 0016.
 */
public class HttpEntity implements Parcelable {

    @JSONField(name = "flag")
    private boolean flag;

    @JSONField(name = "msg")
    private String msg;

    @JSONField(name = "data")
    private String data;

    public HttpEntity() {
    }

    protected HttpEntity(Parcel in) {
        flag = in.readByte() != 0;
        msg = in.readString();
        data = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (flag ? 1 : 0));
        dest.writeString(msg);
        dest.writeString(data);
    }

    public static final Creator<HttpEntity> CREATOR = new Creator<HttpEntity>() {
        @Override
        public HttpEntity createFromParcel(Parcel in) {
            return new HttpEntity(in);
        }

        @Override
        public HttpEntity[] newArray(int size) {
            return new HttpEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static Creator<HttpEntity> getCREATOR() {
        return CREATOR;
    }
}