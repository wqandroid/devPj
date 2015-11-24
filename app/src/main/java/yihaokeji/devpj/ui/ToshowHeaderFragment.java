package yihaokeji.devpj.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import yihaokeji.devpj.R;
import yihaokeji.devpj.adapter.ToShowAdapter;
import yihaokeji.devpj.adapter.ToshowHeadAdapter;
import yihaokeji.devpj.adapter.base.BaseRecycleAdapter;
import yihaokeji.devpj.base.basefm.BaseListFragment;

/**
 * Created by wangqiong on 15/11/20.
 */
public class ToshowHeaderFragment extends BaseListFragment {





    @Override
    protected API_MENU getModelAPi() {
        return API_MENU.API_TOSHOW_LIST;
    }

    @Override
    public BaseRecycleAdapter getmAdapter() {
        View view= getLayoutInflater().inflate(R.layout.toshow_headview,null);
        return new ToshowHeadAdapter(mListItems, this,view);
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
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? layoutManager.getSpanCount() : 1;
            }
        });
        return layoutManager;
    }


}
