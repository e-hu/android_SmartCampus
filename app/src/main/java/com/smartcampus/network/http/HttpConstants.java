package com.smartcampus.network.http;

/**
 * @author: 李志鹏
 * @function: 所有请求相关地址
 * @date: 16/8/12
 */
/**
 * @function: 所有请求相关地址
 */
public class HttpConstants {
    /**
     * 方便我们更改地址
     */
    private static final String ROOT_URL = "http://yuexibo.top/yxbApp/";

    /**
     * 请求本地产品列表
     */
    public static String PRODUCT_LIST = ROOT_URL + "search.json";
    /**
     * 本地产品列表更新时间措请求
     */
    public static String PRODUCT_LATESAT_UPDATE = ROOT_URL + "splash_ad_data.json";

    /**
     * 登陆接口
     */
    public static String LOGIN = ROOT_URL + "user_info.json";

    /**
     * 检查更新接口
     */
    public static String CHECK_UPDATE = ROOT_URL + "update.json";

    /**
     * 首页产品请求接口
     */
    public static String HOME_RECOMMAND = ROOT_URL + "home_data.json";
    /**
     * 课程详情接口
     */
    public static String COURSE_DETAIL = ROOT_URL + "course_detail.json";

}
