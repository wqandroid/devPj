package yihaokeji.devpj.network;

import android.content.Context;

import com.loopj.android.http.RequestParams;

/**
 * Created by wangqiong on 15/11/20.
 */
public class APIClient extends HttpClient implements ApiUrlinterface {


    public APIClient(Context context, CallbackForRequest call) {
        super(context, call);
    }
    @Override
    public void getAllShowList(String user_id, String target_id, int currentpage, int perpage) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("target_id", target_id);
        params.put("currentpage", currentpage);
        params.put("perpage", perpage);
        requestGet("toshow/getShowListAll.php", params);
    }
    @Override
    public void getTargetUserInfo(String user_id) {
        RequestParams params = new RequestParams();
        params.put("target_id", user_id);
        requestGet("user/getTargetUserInfo.php", params);
    }


}
