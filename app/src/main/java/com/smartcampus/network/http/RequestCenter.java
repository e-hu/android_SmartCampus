package com.smartcampus.network.http;

import com.smartcampus.module.course.BaseCourseModel;
import com.smartcampus.module.recommand.BaseRecommandModel;
import com.smartcampus.module.update.UpdateModel;
import com.smartcampus.module.user.User;
import com.smartcampus.okhttp.CommonOkHttpClient;
import com.smartcampus.okhttp.listener.DisposeDataHandle;
import com.smartcampus.okhttp.listener.DisposeDataListener;
import com.smartcampus.okhttp.listener.DisposeDownloadListener;
import com.smartcampus.okhttp.request.CommonRequest;
import com.smartcampus.okhttp.request.RequestParams;

import cn.bmob.v3.listener.SaveListener;

/**
 * @author: vision
 * @function:
 * @date: 16/8/12
 */
public class RequestCenter {

    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    /**
     * 用户登陆请求
     *
     * @param listener
     * @param userName
     * @param password
     */
    public static void login(String userName, String password, SaveListener listener) {
        final User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(listener);
    }

    /**
     * 应用版本号请求
     *
     * @param listener
     */
    public static void checkVersion(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateModel.class);
    }

    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener, BaseRecommandModel.class);
    }

    public static void downloadFile(String url, String path, DisposeDownloadListener listener) {
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url, null),
                new DisposeDataHandle(listener, path));
    }

    /**
     * 请求课程详情
     *
     * @param listener
     */
    public static void requestCourseDetail(String courseId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("courseId", courseId);
        RequestCenter.postRequest(HttpConstants.COURSE_DETAIL, params, listener, BaseCourseModel.class);
    }
}
