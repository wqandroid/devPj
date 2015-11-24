package yihaokeji.devpj.network;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import yihaokeji.devpj.base.MyApplication;
import yihaokeji.devpj.log.Logger;
import yihaokeji.devpj.util.JsonUtil;
import yihaokeji.devpj.util.MD5;


/**
 * Created by wangqiong on 15/3/3.
 */
public class HttpClient {
    public static String SERVER_URL = "http://api.toyihao.com/";
    public static String KEY = "UZR$X~?1])5k3|35[";
    public static int VERSION_CODE = 210;
    Context mContext;
    public final int CODE_SUCCESS = 1;
    public final int ERROR_CODE_NEED_AUTH = 527;//需要登录
    public final int ERROR_CODE_GAG = 530;//当前用户被禁言
    /**
     * 获取当前请求的完整url
     * @param method
     * @return
     */
    public String getRequestUrl(String method) {
        return SERVER_URL + method;
    }

    private AsyncHttpClient client; // 实例话对象
    private CallbackForRequest callback;
    private SelfRequstHandler jsonHttpResponseHandler;

    public HttpClient(final Context context, final CallbackForRequest call) {
        mContext = context;
        callback = call;
        client = new AsyncHttpClient();
        client.setLoggingLevel(6);//只打印error网络请求信息
        client.setAuthenticationPreemptive(false);
        client.setMaxRetriesAndTimeout(2, 8000);
        jsonHttpResponseHandler = new SelfRequstHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                e("请求失败"+jsonHttpResponseHandler.getUrl() +"\n"+ throwable.getMessage());
                if (!activityIsFinish()) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("code", statusCode);
                        object.put("msg", "请求出错,服务器那小子要扣奖金了!");
                        callback.requestFailure(statusCode, object);
                    } catch (JSONException e) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("code", 404);
                            object.put("msg", "服务器离家出走了....");
                            callback.requestFailure(statusCode, object);
                        } catch (JSONException e1) {
                        }
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                e("请求出错:"+jsonHttpResponseHandler.getUrl()+"\n"+errorResponse);
                if (!activityIsFinish()) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("code", statusCode);
                            object.put("msg", errorResponse);
                        callback.requestFailure(statusCode, object);
                    } catch (JSONException e) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("code", 404);
                            object.put("msg", "服务器离家出走了....");
                            callback.requestFailure(statusCode, object);
                        } catch (JSONException e1) {
                        }
                    }
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                if (activityIsFinish()) {
                    client.cancelAllRequests(true);
                }
            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                if (!activityIsFinish()) {
                    callback.onProgress(bytesWritten, totalSize);
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if (!activityIsFinish()) {
                    callback.onFinish();
                }
            }
            @Override
            public void onUserException(Throwable error) {
                super.onUserException(error);
                JSONObject object = new JSONObject();
                try {
                    object.put("code", 404);
                    object.put("msg", "请求出错" + error.getMessage());
                    callback.requestFailure(404, object);
                } catch (JSONException e1) {
                }
            }


            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                i("请求成功:" + response);
                if (!activityIsFinish()) {
                    int code = JsonUtil.getInt(response, "code", 9999);
                    if (code == CODE_SUCCESS) {
                        try {
                            callback.requestSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (code == ERROR_CODE_NEED_AUTH) {
                     }else {
                        callback.requestFailure(code, response);
                    }
                }
            }
        };

    }


    public void CancleAll(){
        if(client!=null){
            client.cancelAllRequests(true);
        }
    }

    /**
     * 当前程序是否关闭
     *
     * @return true 没有 false 已销毁
     */
    public Boolean activityIsFinish() {
        if (mContext != null && callback != null)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    /**
     * 设置加密key
     * @param params
     */
    public void addPASSParams(RequestParams params) {
        if (!params.has("user_id")) {
            try {
                String uid = MyApplication.getInstance().getUid();
                if (!TextUtils.isEmpty(uid)) {
                    params.put("user_id", uid);
                }
            } catch (Exception e) {
            }
        }
        params.put("ver", "" + VERSION_CODE);
        params.put("src", "android");
        params.put("device_token", "009427F8F1D46A7AECA4F783ABB707EA");
        String time = System.currentTimeMillis() + "";
        params.put("uptime", time.substring(0, 10));
        String key = params.toString();
        String[] keys = key.split("&");
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        String keystr = "";
        for (int i = 0; i < keys.length; i++) {
            keystr += keys[i] + "&";
        }
        keystr = keystr.substring(0, keystr.length() - 1);
        String sign = MD5.getMD5(keystr + KEY);
        params.put("sign", sign);
    }

    /**
     * 发送post请求
     *
     * @param method 请求名字
     * @param params 请求参数 String File int object类型都行
     */
    public void requestPost(String method, RequestParams params) {
        addPASSParams(params);
        jsonHttpResponseHandler.setUrl(getRequestUrl(getRequestUrl(method)));
        client.post(getRequestUrl(method), params, jsonHttpResponseHandler);
        i("post:" + getRequestUrl(method) + "?" + params.toString());
    }
    /**
     * 发送post请求
     *
     * @param method 请求名字
     * @param params 请求参数 String File int object类型都行
     */
    public void requestPost(String method, RequestParams params, File file) {
        addPASSParams(params);
        if (file!=null){
            try {
                params.put("postFile", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        jsonHttpResponseHandler.setUrl(getRequestUrl(getRequestUrl(method)));
        client.post(getRequestUrl(method), params, jsonHttpResponseHandler);
        i("post:" + getRequestUrl(method) + "?" + params.toString());
    }

    /**
     * 多文件上传
     *
     * @param method
     * @param params
     * @param files
     */
    public void requestPost(String method, RequestParams params, HashMap<String, File> files) {
        addPASSParams(params);
        try {
            Set<String> keys = files.keySet();
            for (String k : keys) {
                params.put(k, files.get(k));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jsonHttpResponseHandler.setUrl(getRequestUrl(getRequestUrl(method)));
        client.post(getRequestUrl(method), params, jsonHttpResponseHandler);
        i("post:" + getRequestUrl(method) + "?" + params.toString());
    }


    /**
     * 发送get请求
     *
     * @param method
     * @param params
     */
    public void requestGet(String method, RequestParams params) {
        addPASSParams(params);
        jsonHttpResponseHandler.setUrl(getRequestUrl(getRequestUrl(method)));
        client.get(getRequestUrl(method), params, jsonHttpResponseHandler);
        i("get:" + getRequestUrl(method) + "?" + params.toString());
    }


    private void e(String msg) {
        Logger.e("http", msg);
    }
    private void i(String msg) {
        Logger.i("http", msg);
    }


}
