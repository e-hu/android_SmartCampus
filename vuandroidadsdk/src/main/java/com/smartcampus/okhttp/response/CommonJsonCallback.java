package com.smartcampus.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.smartcampus.okhttp.exception.OkHttpException;
import com.smartcampus.okhttp.listener.DisposeDataHandle;
import com.smartcampus.okhttp.listener.DisposeDataListener;
import com.smartcampus.okhttp.listener.DisposeHandleCookieListener;
import com.smartcampus.adutil.ResponseEntityToModule;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author vision
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback implements Callback {

    /**
     * 与服务器返回的字段的一个对应关系
     */
    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it
    // can has the value of
    // set-cookie2

    /**
     * 自定义异常类型
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 服务器无响应
     * @param call
     * @param ioexception
     */
    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));//应用程序实现此方法
            }
        });
    }

    /**
     * 服务器有响应
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        final ArrayList<String> cookieLists = handleCookie(response.headers());
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
                /**
                 * handle the cookie
                 */
                if (mListener instanceof DisposeHandleCookieListener) {
                    ((DisposeHandleCookieListener) mListener).onCookie(cookieLists);
                }
            }
        });
    }

    private ArrayList<String> handleCookie(Headers headers) {
        ArrayList<String> tempList = new ArrayList<String>();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).equalsIgnoreCase(COOKIE_STORE)) {
                tempList.add(headers.value(i));
            }
        }
        return tempList;
    }

    /**
     * 处理服务器返回的数据
     * @param responseObj
     */
    private void handleResponse(Object responseObj) {
        //为了保证代码健壮性
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));//应用程序实现此方法
            return;
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            JSONObject result = new JSONObject(responseObj.toString());
            if (mClass == null) {
                mListener.onSuccess(result);//应用程序实现此方法
            } else {
                //将Json对象转化为Object
                Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);//应用程序实现此方法
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));//应用程序实现此方法
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));//应用程序实现此方法
            e.printStackTrace();
        }
    }
}