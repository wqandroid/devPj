package yihaokeji.devpj.base.basefm;

import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import yihaokeji.devpj.base.MyApplication;
import yihaokeji.devpj.base.cube.CubeFragment;
import yihaokeji.devpj.network.APIClient;
import yihaokeji.devpj.network.ApiUrlinterface;
import yihaokeji.devpj.network.CallbackForRequest;
import yihaokeji.devpj.util.JsonUtil;


/**
 * Created by wangqiong on 15/3/3.
 */
public class ModelAPIFragment<T> extends CubeFragment {


    public static final int SORT_CITY=3;
    public static final int SORT_TIME=1;
    public static final int SORT_HOT=2;


    private final static int CACHA_TIME = 7 * 24 * 60 * 60;
    /**
     * 是否是加载更多
     */
    public boolean mLoadMore = false;
    /**
     * 是否需要读取缓存
     */
    public boolean isAwaysLoadFromcache = false;
    private int currentpage = 1;//翻页用的ID

    public int getModelPageSize() {
        return 20;
    }

    /**
     * 网络数据的list集合
     */
    public ArrayList<T> mListItems = new ArrayList<>();


    /**
     * api 请求类型
     */
    public static enum API_MENU {
        /**聊天订单列表*/
        API_CHAT_HOSTORY,
        /**用户列表*/
        API_CHAT_LIST,
        /**to列表*/
        API_TOSHOW_LIST,
        /**游戏列表*/
        API_GAME_LIST,
        /**to秀赞赏*/
        API_TOSHOW_AWARD_LIST,
        /**to秀赞赏我的*/
        API_TOSHOW_AWARD_ME_LIST,
        /**to秀赞赏我赞赏的*/
        API_TOSHOW_AWARD_SEND_LIST,
        /**to秀评论*/
        API_TOSHOW_COMMENT_LIST,
        /**to秀动态列表*/
        API_TOSHOW_SELF_COMMENT_LIST,
        API_NULL //空请求
    }

    /**
     * 返回之类网络请求的url
     */
    protected API_MENU getModelAPi() {
        return API_MENU.API_NULL;
    }

    /**
     * 加载数据
     *
     * @param loadMore
     */
    protected void loadData(boolean loadMore) {
        API_MENU api = getModelAPi();
        if (api == API_MENU.API_NULL) return;
        mLoadMore = loadMore;
        if (!loadMore) {
            currentpage =1;
        }
        ApiUrlinterface apiClient = getRequstApi();
        switch (api) {
            case API_NULL:
                break;
            case API_TOSHOW_LIST:
                apiClient.getAllShowList(MyApplication.getInstance().getUid(), getTargetid(), currentpage, getModelPageSize());
                break;
            default:
        }
    }
    private APIClient getRequstApi() {
        return new APIClient(getActivity(), new CallbackForRequest() {
            @Override
            public void requestSuccess(JSONObject json) throws JSONException {
                super.requestSuccess(json);
                dealRequestSucess(json, false);
            }

            @Override
            public void requestFailure(int errorcode, JSONObject json) {
                super.requestFailure(errorcode, json);
                didFailure(errorcode, json);
            }
        });
    }


    /**
     * 获取完数据之后的回掉,里面换回完整未解析的jsonobject数据
     * @param obj
     * @param cache 是否来自于缓存
     */
    public void dealRequestSucess(JSONObject obj, final Boolean cache) {
        logi(TAG, "success:" + cache + "&&" + getModelAPi() + "---" + obj.toString());
        final ArrayList<T> tmpList = new ArrayList<T>();
        try {
            if ((obj.get("data") instanceof JSONArray) && obj.getJSONArray("data") != null) {
                JSONArray ja = obj.getJSONArray("data");
                for (int index = 0; index < ja.length(); index++) {
                    T a= (T) ja.get(index);
                    tmpList.add(a);
                }
                currentpage++;
                //不是来自于cache and
                if (!cache && !mLoadMore) {
//                    //写入缓存
                    dealCache(obj);
                }
                didFinished(tmpList, cache);
            }else {
                didFinished(tmpList, cache);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            didFinished(tmpList, cache);
        }
    }

    /**
     * 请求完成并且解析成list之后的回调
     * @param list  解析之后的list集合
     * @param cache 是否是从缓存来的数据
     */
    public synchronized void didFinished(ArrayList<T> list, Boolean cache) {
        //缓存请求数据
        if (mListItems.isEmpty()
                || cache || !mLoadMore) {
            mListItems.clear();
            mListItems.addAll(list);
        } else if (!mListItems.containsAll(list) || mLoadMore) {//加载更多
            mListItems.addAll(list);
        }
    }

    /**
     * 请求出错的回调
     * 系统错误
     * 请求错误
     * 网络错误
     * @param json      错误信息
     * @param errorcode 错误代码
     */
    public void didFailure(int errorcode, JSONObject json) {
        logi(TAG, "error:" + "&&" + getModelAPi() + "---" + json.toString());
        showErrorToast(json);
    }
    /**
     * 搜索本地缓存
     */
    public void searchCacheDta() {
        JSONObject object = MyApplication.getInstance().getCache().getAsJSONObject(getCacheKey());
        if (object != null) {
            dealRequestSucess(object, true);
        }
    }


    protected int getSortType(){
        return SORT_TIME;
    }
    protected String getSortValue(){
        return "";
    }
    protected String getUser_Gender(){
        return "";
    }
    protected String getSort(){
        return "new";
    }

    protected String getLeave(){
        return 1+"";
    }

    protected String getTargetid(){
        return "";
    }
    protected String getShowID(){
        return "";
    }
    /**
     * 将数据写入到本地缓存
     *
     * @param object
     */
    public void dealCache(JSONObject object) {
        MyApplication.getInstance().getCache().put(getCacheKey(), object, CACHA_TIME);
    }

    /**
     * 清除当前列表得缓存
     */
    public void deleteListCache() {
        MyApplication.getInstance().getCache().remove(getCacheKey());
    }

    /**
     * 缓存key 默认是根据api类型返回key
     *
     * @return
     */
    public String getCacheKey() {
        API_MENU api = getModelAPi();
        String cacheKey = String.format("API_MOTHED_%s", api.toString());
        return cacheKey;
    }

    /**
     * toast字符串
     */
    public void t(String msg) {
        if(getActivity() == null){return;}
        /** 如果当前程序在后台则不toast */
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 现实请求错误的toast
     *
     * @param object
     */
    public void showErrorToast(JSONObject object) {
        String str = "";
        try {
            str = object.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
            str = "请求错误";
        }
        t(str);
    }
    public int getErrorCode(JSONObject object){
        return  JsonUtil.getInt(object, "code", -100);
    }

    public boolean isSuccess(JSONObject object){
        int code= JsonUtil.getInt(object, "code", -100);
        if(code==0){
            return true;
        }
        showErrorToast(object);
        return false;
    }

}
