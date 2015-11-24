package yihaokeji.devpj.base.cube;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import yihaokeji.devpj.R;
import yihaokeji.devpj.log.Logger;


public class CubeFragmentActivity extends AppCompatActivity {

    private final static String LOG_TAG = "cube-fragment";

    public static boolean DEBUG = true;
    protected CubeFragment currentFragment;
    private boolean mCloseWarned;

    /**
     * return the string id of close warning
     * <p/>
     * return value which lower than 1 will exit instantly when press back key
     *
     * @return
     */
    public String getCloseWarning() {
        return "";
    }

    public int getAppTheme() {
        return R.style.AppTheme;
    }

    public int getFragmentContainerId() {
        return 0;
    }

    public View getFragmentContainerView() {
        return null;
    }



    /**
     * 获取当前节目的fragemnt
     *
     * @param c
     * @return
     */
    public CubeFragment getFragment(Class<?> c) {
        return (CubeFragment) getSupportFragmentManager().findFragmentByTag(c.toString());
    }


    /**
     * 如果要用这个方法 引入Fragment 必须要实现 getFragmentContainerId()
     *
     * @param cls
     * @param data
     */
    public void pushFragmentToBackStack(Class<?> cls, Object data) {
        FragmentParam param = new FragmentParam();
        param.cls = cls;
        param.data = data;
        goToThisFragment(param);
    }

    protected String getFragmentTag(FragmentParam param) {
        StringBuilder sb = new StringBuilder(param.cls.toString());
        return sb.toString();
    }

    private void goToThisFragment(FragmentParam param) {
        int containerId = getFragmentContainerId();
        if (containerId == 0) {
            throw new IllegalStateException("必须给fragment一个可以依赖的布局getFragmentContainerId...");
        }
        Class<?> cls = param.cls;
        if (cls == null) {
            return;
        }
        try {
            String fragmentTag = getFragmentTag(param);
            FragmentManager fm = getSupportFragmentManager();
            if (DEBUG) {
                Logger.d(LOG_TAG, "before operate, stack entry count: %s" + fm.getBackStackEntryCount());
            }
            CubeFragment fragment = (CubeFragment) fm.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                fragment = (CubeFragment) cls.newInstance();
            }
            if (currentFragment != null && currentFragment != fragment) {
                currentFragment.onLeave();
            }
            fragment.onEnter(param.data);
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment.isAdded()) {
                if (DEBUG) {
                    Logger.d(LOG_TAG, "%s has been added, will be shown again." + fragmentTag);
                }
                ft.show(fragment);
            } else {
                if (DEBUG) {
                    Logger.d(LOG_TAG, "%s is added.+fragmentTag");
                }
                ft.add(containerId, fragment, fragmentTag);
            }
            currentFragment = fragment;

            ft.addToBackStack(fragmentTag);
            ft.commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mCloseWarned = false;
    }

    public void goToFragment(Class<?> cls, Object data) {
        if (cls == null) {
            return;
        }
        CubeFragment fragment = (CubeFragment) getSupportFragmentManager().findFragmentByTag(cls.toString());
        if (fragment != null) {
            currentFragment = fragment;
            fragment.onBackWithData(data);
        }
        getSupportFragmentManager().popBackStackImmediate(cls.toString(), 0);
    }

    public void popTopFragment(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
        if (tryToUpdateCurrentAfterPop() && currentFragment != null) {
            currentFragment.onBackWithData(data);
        }
    }

    public void popToRoot(Object data) {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        }
        popTopFragment(data);
    }

    /**
     * process the return back logic
     * return true if back pressed event has been processed and should stay in current view
     *
     * @return
     */
    protected boolean processBackPressed() {
        return false;
    }

    /**
     * process back pressed
     */
    @Override
    public void onBackPressed() {
        // process back for fragment
        if (processBackPressed()) {
            return;
        }
        // process back for fragment
        boolean enableBackPressed = true;
        if (currentFragment != null) {
            enableBackPressed = !currentFragment.processBackPressed();
        }
        if (enableBackPressed) {
            int cnt = getSupportFragmentManager().getBackStackEntryCount();
            if (cnt <= 1 && isTaskRoot()) {
                String closeWarningHint = getCloseWarning();
                if (!mCloseWarned && !TextUtils.isEmpty(closeWarningHint)) {
                    Toast toast = Toast.makeText(this, closeWarningHint, Toast.LENGTH_SHORT);
                    toast.show();
                    mCloseWarned = true;
                } else {
                    doReturnBack();
                }
            } else {
                mCloseWarned = false;
                doReturnBack();
            }
        }
    }

    private boolean tryToUpdateCurrentAfterPop() {
        FragmentManager fm = getSupportFragmentManager();
        int cnt = fm.getBackStackEntryCount();
        if (cnt > 0) {
            String name = fm.getBackStackEntryAt(cnt - 1).getName();
            Fragment fragment = fm.findFragmentByTag(name);
            if (fragment != null && fragment instanceof CubeFragment) {
                currentFragment = (CubeFragment) fragment;
            }
            return true;
        }
        return false;
    }

    protected void doReturnBack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            if (tryToUpdateCurrentAfterPop() && currentFragment != null) {
                currentFragment.onBack();
            }
        }
    }


    public void hideKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void hideKeyboardForCurrentFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyboardAtView(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected void exitFullScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
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
