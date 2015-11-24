package yihaokeji.devpj.base;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import yihaokeji.devpj.util.ACache;


/**
 * Created by wangqiong on 15/7/23.
 */
public class MyApplication extends Application {

    public static MyApplication instance = null;
    public int sWidthPix;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
    }



    public String getUid(){
        return "19063472";
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    private ACache aCache;
    /**
     * 获取cache对象,用来保存网络请求数据做临时缓存
     *
     * @return
     */
    public ACache getCache() {
        if (aCache == null) {
            try {
                aCache = ACache.get(getInstance(), 60 * 1024 * 1024, 60);//60M 缓存大小    最多可以缓存60条数据
            } catch (Exception ex) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "qqh", "qqhhttpcache");
                if (!file.exists()) {
                    file.mkdirs();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                aCache = ACache.get(file, 60 * 1024 * 1024, 60);
            }
        }
        return aCache;
    }
}
