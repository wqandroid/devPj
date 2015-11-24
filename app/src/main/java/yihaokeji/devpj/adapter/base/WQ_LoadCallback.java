package yihaokeji.devpj.adapter.base;

/**
 * Created by wangqiong on 15/11/23.
 */
public interface WQ_LoadCallback {
    /**上拉加载更多是否可以使用*/
    boolean loadMoreEnable();
    /**是否可以显示在家更多*/
    boolean isCanShowloadMore();
    void onloadMore();
    boolean isloading();

}
