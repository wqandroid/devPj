package yihaokeji.devpj.network;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.HashMap;

/**
 * Created by wangqiong on 15/1/9.
 */
public class SelfRequstHandler extends JsonHttpResponseHandler {

    public SelfRequstHandler(){
        super();
    }
    public SelfRequstHandler(String head){
        super(head);
    }
    private HashMap<String,String> mapData=new HashMap<String, String>();
    private String url;
    public void addParmter(String key,String value){
        mapData.put(key,value);
    }
    public String getParmter(String key){
        if(mapData.containsKey(key)){
            return  mapData.get(key).toString();
        }
        return "";
    }
    public HashMap<String, String> getMapData() {
        return mapData;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
