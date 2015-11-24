package yihaokeji.devpj.base.basefm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import yihaokeji.devpj.R;


/**
 * Created by wangqiong on 15/3/4.
 */
public class BaseFragment extends ModelAPIFragment {
    private View rootview;

    /**
     * 返回当前页面指定布局 layout
     *
     * @return
     */
    public int getRootView() {
        return R.layout.activity_base_layout;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootview == null) {
            rootview = inflater.inflate(getRootView(), null, false);
        }
        viewInit(rootview);
        return rootview;
    }

    public View findViewById(int id) {
        return rootview.findViewById(id);
    }

    public View getContinerView() {
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewData(view);
    }

    public View getBaseRootView() {
        return rootview;
    }

    /**
     * 当前activity 可用
     * @return
     */
    public boolean isAvailable() {
        if (getActivity() != null) {
            return true;
        }
        return false;
    }


    public void autoload() {
        loadData(false);
    }


    /**
     * 初始化rootview中子控件
     *
     * @param rootview
     */
    public void viewInit(View rootview) {
        if (rootview == null) return;
    }

    /**
     * View 初始化赋值 以及绑定监听器
     *
     * @param rootview 这个v是当前页面指定的layout == getRootView()
     */
    public void initViewData(View rootview) {

    }

    public void backResult(Intent intent) {
    }


    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        hidenProgressLoading();
    }




    public AlertDialog alertDialog;

    public void showProgrssLoading(boolean cancleable) {
        if (!isAvailable()) {
            return;
        }
        if (alertDialog == null) {
            alertDialog = getProgressDialog(getActivity(), cancleable);
        }
        if (alertDialog == null) {
            return;
        }
        alertDialog.show();
    }

    public void showProgrssLoading() {
        if (!isAvailable()) {
            return;
        }
        if (alertDialog == null) {
            alertDialog = getProgressDialog(getActivity(), true);
        }
        alertDialog.show();
    }

    /**
     * 提示loading
     *
     * @param txt
     */
    public void showProgrssLoading(String txt) {
        showProgrssLoading();
        updateloadingtxt(txt);
    }

    /**
     * 提示loading
     * @param txt
     */
    public void showProgrssLoading(String txt,boolean cancleable) {
        showProgrssLoading(cancleable);
        updateloadingtxt(txt);
    }


    /**
     * 更新loading文字
     *
     * @param txt
     */
    public void updateloadingtxt(String txt) {
        if (alertDialog != null) {
            if (tv_loading == null) {
                View view = alertDialog.findViewById(R.id.tv_loading);
                if (view != null) {
                    tv_loading = (TextView) view;
                }
            }
            if (tv_loading == null) {
                return;
            }
            tv_loading.setText(txt);
        }
    }

    public void hidenProgressLoading() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    TextView tv_loading;
    View view = null;

    public AlertDialog getProgressDialog(Activity activity, boolean canclealble) {
        AlertDialog.Builder
                builder = new AlertDialog.Builder(activity,R.style.DialogProgressbar);
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(
                    R.layout.progress_dialog, null);
        }
        tv_loading = (TextView) view.findViewById(R.id.tv_loading);
        builder.setCancelable(canclealble);
        builder.setView(view);
        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        Window window = ad.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        ad.getWindow().setLayout(dip2px(activity, 160), dip2px(activity, 100));
        return ad;
    }

}
