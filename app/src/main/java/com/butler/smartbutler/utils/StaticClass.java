package com.butler.smartbutler.utils;

public class StaticClass {
    //闪屏页延时
    public static final int HANDLER_SPLASH = 1001;
    //判断程序是否是第一次运行
    public static final String SHARE_IS_FIRST = "isFirst";
    //bugly app id
    public static final String BUGLY_APP_ID = "bd7f2fd4a8";
    //Bmob app id
    public static final String BMOB_APP_ID = "b0cbe301cdff37c2337fb38055de62fa";
    //聚合数据key
    public static final String EXPRESS_QUERY_KEY = "a382d0159bf16e60c90a00471123087e";
    //聚合数据访问url
    public static String EXPRESS_QUERY_URL = "http://v.juhe.cn/exp/index?key=" + EXPRESS_QUERY_KEY + "&com={com}&no={no}";
    //聚合数据查询快递公司
    public static final String POLYMERIZE_EXPRESS_COM_QUERY_URL = "http://v.juhe.cn/exp/com?key=" + EXPRESS_QUERY_KEY;
    //归属地查询接口key
    public static final String PHONE_LOCATION_QUERY_KEY = "4ac6ae91ace21f7b4e2008da2e295c51";
    //归属地查询URL
    public static String PHONE_LOCATION_QUERY_URL = "http://apis.juhe.cn/mobile/get?phone={phone}&key=" + PHONE_LOCATION_QUERY_KEY;
    //HaoService问答机器人KEY
    public static final String ANSWER_ROBOT_KEY = "1237bbc304ea44f0b18a75ec49369927";
    //HaoService问答机器人URL
    public static String ANSWER_ROBOT_URL = "http://apis.haoservice.com/efficient/robot?info={info}&key=" + ANSWER_ROBOT_KEY;
    //微信精选appkey
    public static final String WECHAT_SELECTION_KEY = "c6bbce3ab04ab6d3327f5e3d25faa5c0";
    //微信精选URL
    public static String WECHAT_SELECTION_URL = "http://v.juhe.cn/weixin/query?key=" + WECHAT_SELECTION_KEY;
    //语音key
    public static final String VOICE_KEY = "5c1780b0";
    //妹纸接口
    public static String GIRL_URL = "http://image.baidu.com/data/imgs?col=%E7%BE%8E%E5%A5%B3&tag=%E7%BE%8E%E5%A5%B3&sort=0&pn={start}&rn={size}&p=channel&from=1";
    //短信Action
    public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    //版本更新
    public static final String CHECK_UPDATE_URL = "http://192.168.3.9:8080/smartBoy/config.json";

}
