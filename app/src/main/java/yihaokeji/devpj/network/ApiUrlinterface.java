package yihaokeji.devpj.network;


/**
 * Created by wangqiong on 15/11/20.
 */
public interface ApiUrlinterface {


    void getAllShowList(String user_id, String target_id, int currentpage, int perpage);
    /**
     * 获取目标用户信息
     * @param user_id
     */
     void getTargetUserInfo(String user_id) ;



}
