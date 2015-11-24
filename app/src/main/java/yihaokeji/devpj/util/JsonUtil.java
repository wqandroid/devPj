package yihaokeji.devpj.util;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangqiong on 15/5/25.
 */
public class JsonUtil {

    public static final String getString(JSONObject object, String key) {
        String value = null;
        if (object.has(key)) {
            try {
                value = object.getString(key);
                if(value!=null && value.equals("null")){
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static final String getString(JSONObject object, String key,String defaultvalue) {
        String value = getString(object,key);
        if(TextUtils.isEmpty(value)){
            return defaultvalue;
        }
        return value;
    }

    public static final int getInt(JSONObject object, String key) {
        return getInt(object,key,0);
    }
    public static final int getInt(JSONObject object, String key,int defaultcode) {
        int value = defaultcode;
        if (object.has(key)) {
            try {
                value = object.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
    public static final float getFloat(JSONObject object, String key,float defaultcode) {
        float value = defaultcode;
        if (object.has(key)) {
            try {
                value = (float)object.getDouble(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }



    public static final long getLong(JSONObject object, String key) {
        long value = 0l;
        if (object.has(key)) {
            try {
                value = object.getLong(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    public static final boolean getBloolean(JSONObject object, String key) {
        boolean value = false;
        if (object.has(key)) {
            try {
                value = object.getBoolean(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

}
