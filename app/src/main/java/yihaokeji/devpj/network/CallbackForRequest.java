package yihaokeji.devpj.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangqiong on 15/3/3.
 */
public class CallbackForRequest {
    /**
     * 请求成功的回调
     * @param json
     * @throws JSONException
     */
    public void requestSuccess (JSONObject json) throws JSONException {}
    /**
     * 请求失败的回掉 (包括网络错误 和 服务器错误)
     * @param json
     */
    public void requestFailure(int errorcode,JSONObject json){}

    /**
     * 请求进度
     * @param bytesWritten 进度值
     * @param totalSize 总大小
     */
    public void onProgress(long bytesWritten, long totalSize){}


    public void onFinish(){}
}
