package yihaokeji.devpj.adapter.base;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import yihaokeji.devpj.R;
import yihaokeji.devpj.base.MyApplication;
import yihaokeji.devpj.base.basefm.BaseListFragment;


/**
 * Created by wangqiong on 15/11/20.
 */
public abstract class BaseRecycleAdapter<T, V extends VH> extends RecyclerView.Adapter<V> implements RecyclerOnItemClickListener {

    public static final int TYPE_HEAD = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_LOADMORE = 2;

    public static final String TAG = "BaseRecycleAdapter";
    public static int sc_width = 0;
    public ArrayList<T> list;
    public LayoutInflater inflater;
    public Context context;


    public BaseRecycleAdapter(ArrayList<T> list, BaseListFragment fragment) {
        this.list = list;
        this.context = fragment.getActivity();
        inflater = LayoutInflater.from(context);
        sc_width = MyApplication.getInstance().sWidthPix;
    }


    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(V holder, int position) {

    }
    public T getitemdata(int pos) {
        return list.get(pos);
    }
    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public boolean isloadView(int pos) {
        return (list.size() > 0 && pos == list.size());
    }

    @Override
    public void onItemClicked(View view, int position) {

    }

    public static class VHload extends VH {

        public VHload(View itemView) {
            super(itemView);
        }
    }


}
