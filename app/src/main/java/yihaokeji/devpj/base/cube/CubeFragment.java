package yihaokeji.devpj.base.cube;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import org.json.JSONObject;

import yihaokeji.devpj.base.cube.lifecycle.IComponentContainer;
import yihaokeji.devpj.base.cube.lifecycle.LifeCycleComponent;
import yihaokeji.devpj.base.cube.lifecycle.LifeCycleComponentManager;
import yihaokeji.devpj.log.Logger;


/**
 * Implement {@link ICubeFragment}, {@link IComponentContainer}
 * <p/>
 * Ignore {@link LifeCycleComponentManager#onBecomesPartiallyInvisible}
 */
public  class CubeFragment extends Fragment implements ICubeFragment, IComponentContainer {

    private static final boolean DEBUG =false;
    protected Object mDataIn;
    private boolean mFirstResume = true;

    public static  final String TAG="API";

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

//    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public CubeFragmentActivity getContext() {
        return (CubeFragmentActivity) getActivity();
    }

    /**
     * ===========================================================
     * Implements {@link ICubeFragment}
     * ===========================================================
     */
    @Override
    public void onEnter(Object data) {
        mDataIn = data;
        if (DEBUG) {
            showStatus("onEnter");
        }
    }

    @Override
    public void onLeave() {
        if (DEBUG) {
            showStatus("onLeave");
        }
        mComponentContainer.onBecomesTotallyInvisible();
    }

    @Override
    public void onBackWithData(Object data) {
        if (DEBUG) {
            showStatus("onBackWithData");
        }
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public boolean processBackPressed() {
        return false;
    }

    @Override
    public void onBack() {
        if (DEBUG) {
            showStatus("onBack");
        }
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    /**
     * ===========================================================
     * Implements {@link IComponentContainer}
     * ===========================================================
     */
    @Override
    public void addComponent(LifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }

    /**
     * Not add self to back stack when removed, so only when Activity stop
     */
    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG) {
            showStatus("onStop");
        }
        onLeave();
    }

    /**
     * Only when Activity resume, not very precise.
     * When activity recover from partly invisible, onBecomesPartiallyInvisible will be triggered.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!mFirstResume) {
            onBack();
        }
        if (mFirstResume) {
            mFirstResume = false;
        }
        if (DEBUG) {
            showStatus("onResume");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (DEBUG) {
            showStatus("onAttach");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            showStatus("onCreate");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG) {
            showStatus("onActivityCreated");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) {
            showStatus("onStart");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (DEBUG) {
            showStatus("onPause");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (DEBUG) {
            showStatus("onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            showStatus("onDestroy");
        }
        mComponentContainer.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (DEBUG) {
            showStatus("onDetach");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DEBUG) {
            showStatus("onCreateView");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void showStatus(String status) {
        final String[] className = ((Object) this).getClass().getName().split("\\.");
        Logger.d("cube-lifecycle", className[className.length - 1] + "");
    }



    /*** 显示键盘 */
    public void showSoftInput() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_SHOWN, 0);
    }

    /*** 隐藏键盘 */
    public void hideSoftInput(EditText editText) {
        InputMethodManager inputmangers = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputmangers.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }


    /**
     * DIP转换成PX
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * PX转换成DIP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * PX转换SP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * SP转换PX
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }


    /**
     * Debug输出Log日志
     **/
    protected void logj(JSONObject msg) {
        Logger.i("json", msg.toString());
    }

    /**
     * Debug输出Log日志
     **/
    protected void logi(String msg) {
        Logger.i("info", msg);
    }

    protected void logi(String... msg) {
        StringBuffer sb = new StringBuffer();
        for (String string : msg) {
            sb.append(string);
        }
        Logger.i("info", sb.toString());
    }


    public void log(String tag, Object obj) {
        Logger.i(tag, obj.toString());
    }

    public void log(Object obj) {
        Logger.i("info", obj.toString());
    }

    /**
     * Debug输出Log日志
     **/
    protected void logi(String tag, String msg) {
        Logger.i(tag, msg);
    }

    /**
     * Debug输出Log日志
     **/
    protected void logd(String tag, String msg) {
        Logger.d("debug", msg);
    }

    /**
     * Error输出Log日志
     **/
    protected void loge(String msg) {
        Logger.e("error", msg);
    }

    /**
     * Error输出Log日志
     **/
    protected void loge(String msg, Exception exception) {
        Logger.e("app_error", msg, exception);
    }

    /**
     * Error输出Log日志
     **/
    protected void loge(String tag, String msg) {
        Logger.e(tag, msg);
    }

}
