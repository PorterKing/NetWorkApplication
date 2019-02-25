package android.plat.hexin.com.networkapplication.network;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Author: jcb.
 * on 2019/2/20 0020.
 */
public class TestBean implements Parcelable {

    @JSONField(name = "firstPage")
    private boolean firstPage;

    @JSONField(name = "lastPage")
    private boolean lastPage;

    @JSONField(name = "number")
    private int number;

    @JSONField(name = "numberOfElements")
    private int numberOfElements;

    @JSONField(name = "size")
    private int size;

    @JSONField(name = "totalElements")
    private int totalElements;

    @JSONField(name = "totalPages")
    private int totalPages;

    @JSONField(name = "content")
    private String content;

    public TestBean() {
    }


    protected TestBean(Parcel in) {
        firstPage = in.readByte() != 0;
        lastPage = in.readByte() != 0;
        number = in.readInt();
        numberOfElements = in.readInt();
        size = in.readInt();
        totalElements = in.readInt();
        totalPages = in.readInt();
        content = in.readString();
    }

    public static final Creator<TestBean> CREATOR = new Creator<TestBean>() {
        @Override
        public TestBean createFromParcel(Parcel in) {
            return new TestBean(in);
        }

        @Override
        public TestBean[] newArray(int size) {
            return new TestBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (firstPage ? 1 : 0));
        dest.writeByte((byte) (lastPage ? 1 : 0));
        dest.writeInt(number);
        dest.writeInt(numberOfElements);
        dest.writeInt(size);
        dest.writeInt(totalElements);
        dest.writeInt(totalPages);
        dest.writeString(content);
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Creator<TestBean> getCREATOR() {
        return CREATOR;
    }
}
