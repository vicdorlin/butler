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
    public static final String POLYMERIZE_KEY = "a382d0159bf16e60c90a00471123087e";
    //聚合数据访问url
    public static String POLYMERIZE_URL = "http://v.juhe.cn/exp/index?key=" + POLYMERIZE_KEY + "&com={com}&no={no}";
    //聚合数据查询快递公司
    public static final String POLYMERIZE_EXPRESS_COM_QUERY_URL = "http://v.juhe.cn/exp/com?key=" + POLYMERIZE_KEY;

}
