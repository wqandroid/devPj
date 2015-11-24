package yihaokeji.devpj.base.basefm;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import yihaokeji.devpj.R;
import yihaokeji.devpj.adapter.base.BaseRecycleAdapter;
import yihaokeji.devpj.adapter.base.decoration.GridSpacingItemDecoration;

/**
 * Created by wangqiong on 15/11/20.
 */
public abstract   class BaseListFragment<T> extends BaseFragment implements Paginate.Callbacks {

    //loding view
    private View progress_bar_loading_layout;
    //下啦刷新的view
    public SwipeRefreshLayout swipe_container;
    /***/
    public RecyclerView recyclerView;

    //之类公用的适配器
    protected BaseRecycleAdapter mAdapter;
    private View empty_layout;

    private Paginate paginate;

    private boolean isloading=false;




    /**
     * 子类必须返回adapter类型
     */
    public abstract BaseRecycleAdapter getmAdapter();


    /**
     * 当前加载的布局文件
     *
     * @return
     */
    public int getRootView() {
        return R.layout.base_listframent_layout;
    }


    @Override
    public void viewInit(View rootview) {
        super.viewInit(rootview);
        swipe_container = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        swipe_container.setColorSchemeResources(R.color.pink);
        empty_layout = rootview.findViewById(R.id.empty_view);
        empty_layout.setVisibility(View.GONE);
        progress_bar_loading_layout = rootview.findViewById(R.id.progress_bar_loading_layout);

        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(false);
            }
        });
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = getmAdapter();
        if (mAdapter == null) {
            return;
        }
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addItemDecoration(recyclerView);
        recyclerView.setAdapter(mAdapter);
        paginate = Paginate.with(recyclerView, this)
                .addLoadingListItem(true)
                .setLoadingTriggerThreshold(2)
                .setLoadingListItemCreator(null)
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 2;
                    }
                })
                .build();
        paginate.setHasMoreDataToLoad(true);
    }

    protected RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(getActivity());
    }

    protected void addItemDecoration(RecyclerView recyclerView){
        if (recyclerView == null)return;
        GridSpacingItemDecoration decoration=new GridSpacingItemDecoration(2,0,true);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void loadData(boolean loadMore) {
        super.loadData(loadMore);
        isloading=true;
    }
    @Override
    public synchronized void onLoadMore() {
        loadData(true);
    }
    @Override
    public synchronized boolean isLoading() {
        return isloading;
    }
    @Override
    public boolean hasLoadedAllItems() {
        return  currentsize < getCanLoadMoreSize() ;
    }

    private int currentsize=0;
    @Override
    public synchronized void didFinished(ArrayList list, Boolean cache) {
        super.didFinished(list, cache);
        currentsize=list.size();
        refreshComplete();
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }
    /**
     * 刷新完成
     */
    public void refreshComplete() {
        isloading = false;
        if (swipe_container == null) {
            if (getContinerView() == null) {
                return;
            }
            swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            return;
        }
        swipe_container.setRefreshing(false);
        hidenProgressView();
//        if (mListItems.size() == 0) {
//            showEmptyView();
//        } else {
//            hideEmptyView();
//        }
    }


    public int getCanLoadMoreSize() {
        return getModelPageSize();
    }

    public void hidenProgressView() {
        if (progress_bar_loading_layout !=null){
            progress_bar_loading_layout.setVisibility(View.GONE);
        }
    }

    public void showProgressView() {
        if (progress_bar_loading_layout !=null){
            progress_bar_loading_layout.setVisibility(View.VISIBLE);
        }
    }
}
