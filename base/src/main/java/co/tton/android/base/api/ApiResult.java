package co.tton.android.base.api;

import com.google.gson.annotations.SerializedName;

public class ApiResult<T> {

    @SerializedName("result")
    public int mResult;

    @SerializedName("msg")
    public String mMsg;

    @SerializedName("data")
    public T mData;

    public boolean isOk() {
        return mResult == 200;
    }

}
