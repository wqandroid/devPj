package yihaokeji.devpj.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import yihaokeji.devpj.adapter.ToShowAdapter;
import yihaokeji.devpj.adapter.base.BaseRecycleAdapter;
import yihaokeji.devpj.base.basefm.BaseListFragment;

/**
 * Created by wangqiong on 15/11/20.
 */
public class ToshowFragment extends BaseListFragment {





    @Override
    protected API_MENU getModelAPi() {
        return API_MENU.API_TOSHOW_LIST;
    }

    @Override
    public BaseRecycleAdapter getmAdapter() {
        return new ToShowAdapter(mListItems, this);
    }

    @Override
    protected String getTargetid() {
        return "";
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData(false);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        return layoutManager;
    }


}
