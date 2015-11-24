package yihaokeji.devpj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

import yihaokeji.devpj.R;
import yihaokeji.devpj.adapter.base.BaseRecycleAdapter;
import yihaokeji.devpj.adapter.base.VH;
import yihaokeji.devpj.base.basefm.BaseListFragment;
import yihaokeji.devpj.util.JsonUtil;

/**
 * Created by wangqiong on 15/11/20.
 */
public class ToShowAdapter extends BaseRecycleAdapter<JSONObject,VH> {


    public static RecyclerView.LayoutParams params;

    public ToShowAdapter(ArrayList<JSONObject> list, BaseListFragment fragment) {
        super(list, fragment);
        params = new RecyclerView.LayoutParams(sc_width / 2, sc_width / 2);
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ToshowHolder(View.inflate(context, R.layout.item_test, null));
    }
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    @Override
    public void onBindViewHolder(VH holder, int position) {
            JSONObject object=getitemdata(position);
            ToshowHolder holder1= (ToshowHolder) holder;
            String content = JsonUtil.getString(object, "content");
            holder1.tvcontent.setText(content);
            String thumb_url=JsonUtil.getString(object,"thumb_url");
            Glide.with(context).load(thumb_url).into(holder1.iv_thumb);
    }

    public static class ToshowHolder extends VH {

        TextView tvcontent;
        FrameLayout item_root;
        ImageView iv_thumb;
        public ToshowHolder(View itemView) {
            super(itemView);
            iv_thumb= (ImageView) itemView.findViewById(R.id.iv_thumb);
            item_root = (FrameLayout) itemView.findViewById(R.id.item_root);
            tvcontent = (TextView) itemView.findViewById(R.id.tv_content);
            itemView.setLayoutParams(params);
        }
    }




}
