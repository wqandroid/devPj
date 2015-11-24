package yihaokeji.devpj.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import yihaokeji.devpj.R;
import yihaokeji.devpj.base.cube.CubeFragmentActivity;

/**
 * Created by wangqiong on 15/11/3.
 */
public  class BaseActivity extends CubeFragmentActivity {

    /**
     * 禁止截屏
     */
    public void enableSnap() {
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    protected int getToolbarElevation(){
        return 6;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
        setContentView(R.layout.activity_base_layout);
        if (getRootView() != 0){
            View rootview=getLayoutInflater().inflate(getRootView(),null);
            getFragmentContainerView().addView(rootview);
        }
        initView();
        setToolbarElevation();
    }

    protected void initView(){}


    protected int getRootView(){
        return 0;
    }

    @Override
    public FrameLayout getFragmentContainerView() {
        return (FrameLayout)findViewById(getFragmentContainerId());
    }



    @Override
    public int getFragmentContainerId() {
        return R.id.container;
    }


    /**
     * 设置toolbar的阴影
     */
    protected void setToolbarElevation(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(getToolbarElevation());
        }
    }


    public void showBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    public void setStatusbarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public Context getThis() {
        return BaseActivity.this;
    }

    public void hidenActionbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setTitle(title);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isSDK21() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setElevation(View view, int elevation) {
        try {
            if (isSDK21()) {
                view.setElevation(elevation);
            } else {
                ViewCompat.setElevation(view, elevation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScreenWindth() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }
    /**
     * 短暂显示Toast提示(来自String) *
     */
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast提示(来自String) *
     */
    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    protected void startActivityForResult(Class<?> cls,int reqcode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, reqcode);
    }
    /**
     * toast资源字符串
     *
     * @param resId
     */
    public void t(int resId) {
        try {
            t(this.getString(resId));
        } catch (Exception e) {
            t("" + resId);
        }
    }
    /**
     * toast字符串
     */
    public void t(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast 在非UI线程
     *
     * @param msg
     */
    public void t_inthread(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public android.support.v7.app.AlertDialog alertDialog;

    public void showProgrssLoading(boolean cancleable) {
        if (alertDialog == null) {
            alertDialog = getProgressDialog(this, cancleable);
        }
        alertDialog.show();
    }

    public void showProgrssLoading() {
        if (alertDialog == null) {
            alertDialog = getProgressDialog(this, true);
        }
        alertDialog.show();
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
     * 提示loading
     *
     * @param txt
     */
    public void showProgrssLoading(String txt) {
        showProgrssLoading();
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

    public android.support.v7.app.AlertDialog getProgressDialog(Activity activity, boolean canclealble) {
        android.support.v7.app.AlertDialog.Builder
                builder = new android.support.v7.app.AlertDialog.Builder(activity);
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(
                    R.layout.progress_dialog, null);
        }
        tv_loading = (TextView) view.findViewById(R.id.tv_loading);
        builder.setCancelable(canclealble);
        builder.setView(view);
        android.support.v7.app.AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        Window window = ad.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        ad.getWindow().setLayout(dip2px(activity, 160), dip2px(activity, 100));
        return ad;
    }



}
