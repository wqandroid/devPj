package yihaokeji.devpj.adapter.base;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import yihaokeji.devpj.base.basefm.BaseListFragment;

/**
 * Created by wangqiong on 15/11/24.
 */
public abstract class HeadRecycleAdapter extends BaseRecycleAdapter {
    private View view;
    public HeadRecycleAdapter(ArrayList list, BaseListFragment fragment,View view) {
        super(list, fragment);
        this.view=view;
        if (view==null){
            throw new IllegalArgumentException("view must not be null");
        }
    }
    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);
    protected abstract void onBindItemViewHolder(VH holder, int position);
    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position >0){
            onBindItemViewHolder(holder,position);
        }

    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD){
            return new HeadHolder(view);
        }else {
            return onCreateItemViewHolder(parent, viewType);
        }
    }



    @Override
    public int getItemViewType(int position) {
        if (position == 0)return  TYPE_HEAD;
        return  TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public Object getitemdata(int pos) {
        return super.getitemdata(pos-1);
    }

    public  static  class HeadHolder extends VH{
        public HeadHolder(View itemView) {
            super(itemView);
        }
    }





}
