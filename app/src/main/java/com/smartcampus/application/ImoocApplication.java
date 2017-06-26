package com.smartcampus.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.smartcampus.core.AdSDKManager;
import com.smartcampus.share.ShareManager;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;


/**
 * *******************************************************
 * @文件名称：CommonApplication.java
 * @文件作者：李志鹏
 * @创建时间：2015年11月19日 下午10:38:25
 * @文件描述：App容器
 * @修改历史：2015年11月19日创建初始版本 ********************************************************
 * 1. 整个程序的入口
 * 2. 初始化工作
 * 3. 为整个应用的其他模块提供上下文环境
 */
public class ImoocApplication extends Application {

    private static ImoocApplication mApplication = null;

    /**
     * 单例模式，onCreate()方法只调用一次，在此方法内初始化对象
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initShareSDK();
        initJPush();
        initAdSDK();
        initBaiduMapSDK();
        initBmob();
    }

    private void initBmob() {
        Bmob.initialize(this, "7fde5b16cc4a9c0d7a9f1362e4a326d4");
    }

    private void initBaiduMapSDK() {
        SDKInitializer.initialize(getApplicationContext());
    }

    public static ImoocApplication getInstance() {
        return mApplication;
    }

    public void initShareSDK() {
        ShareManager.initSDK(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initAdSDK() {
        AdSDKManager.init(this);
    }
}