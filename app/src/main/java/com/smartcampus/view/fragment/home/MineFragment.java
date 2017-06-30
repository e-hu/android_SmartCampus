package com.smartcampus.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartcampus.R;
import com.smartcampus.activity.LoginActivity;
import com.smartcampus.activity.SettingActivity;
import com.smartcampus.activity.UserInfoActivity;
import com.smartcampus.constant.Constant;
import com.smartcampus.manager.UserManager;
import com.smartcampus.module.update.UpdateModel;
import com.smartcampus.module.user.User;
import com.smartcampus.network.http.RequestCenter;
import com.smartcampus.okhttp.listener.DisposeDataListener;
import com.smartcampus.service.update.UpdateService;
import com.smartcampus.share.ShareDialog;
import com.smartcampus.util.ImageLoaderManager;
import com.smartcampus.util.Util;
import com.smartcampus.view.CommonDialog;
import com.smartcampus.view.MyQrCodeDialog;
import com.smartcampus.view.fragment.BaseFragment;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.smartcampus.activity.LoginActivity.LOGIN_ACTION;
import static com.smartcampus.activity.UserInfoActivity.LOGOUT_ACTION;

/**
 * @author: vision
 * @function: 个人信息fragment
 * @date: 16/7/14
 */
public class MineFragment extends BaseFragment
        implements OnClickListener {

    /**
     * UI
     */
    private View mContentView;
    private RelativeLayout mLoginLayout;
    private CircleImageView mPhotoView;
    private CircleImageView userPhotoView;
    private TextView mLoginInfoView;
    private TextView mLoginView;
    private RelativeLayout mLoginedLayout;
    private TextView mUserNameView;
    private TextView mTickView;
    private TextView mVideoPlayerView;
    private TextView mShareView;
    private TextView mQrCodeView;
    private TextView mUpdateView;

    //自定义了一个广播接收器
    private LoginBroadcastReceiver receiver =
            new LoginBroadcastReceiver();

    public MineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        registerBroadcast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_mine_layout, null, false);
        initView();
        return mContentView;
    }


    private void initView() {
        //未登录的View
        mLoginLayout = (RelativeLayout) mContentView.findViewById(R.id.login_layout);
        mLoginLayout.setOnClickListener(this);
        mPhotoView = (CircleImageView) mContentView.findViewById(R.id.photo_view);
        mPhotoView.setOnClickListener(this);
        mLoginView = (TextView) mContentView.findViewById(R.id.login_view);
        mLoginView.setOnClickListener(this);
        //已登录的View
        mLoginedLayout = (RelativeLayout) mContentView.findViewById(R.id.logined_layout);
        mLoginedLayout.setOnClickListener(this);
        mLoginInfoView = (TextView) mContentView.findViewById(R.id.login_info_view);
        mLoginInfoView.setOnClickListener(this);
        mUserNameView = (TextView) mContentView.findViewById(R.id.username_view);
        mUserNameView.setOnClickListener(this);
        mTickView = (TextView) mContentView.findViewById(R.id.tick_view);
        mTickView.setOnClickListener(this);
        userPhotoView = (CircleImageView) mContentView.findViewById(R.id.user_photo_view);
        //公共View
        mVideoPlayerView = (TextView) mContentView.findViewById(R.id.video_setting_view);
        mVideoPlayerView.setOnClickListener(this);
        mShareView = (TextView) mContentView.findViewById(R.id.share_imooc_view);
        mShareView.setOnClickListener(this);
        mQrCodeView = (TextView) mContentView.findViewById(R.id.my_qrcode_view);
        mQrCodeView.setOnClickListener(this);
        mUpdateView = (TextView) mContentView.findViewById(R.id.update_view);
        mUpdateView.setOnClickListener(this);
        if (UserManager.getInstance().hasLogined()) {
            User user = UserManager.getInstance().getUser();
            ImageLoaderManager.getInstance(mContext).displayImage(userPhotoView, user.getPicture().getFileUrl());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //根据用户信息更新我们的fragment
        if (UserManager.getInstance().hasLogined()) {
            if (mLoginedLayout.getVisibility() == View.GONE) {
                mLoginLayout.setVisibility(View.GONE);
                mLoginedLayout.setVisibility(View.VISIBLE);
                mUserNameView.setText(UserManager.getInstance().getUser().getUsername());
                mTickView.setText(UserManager.getInstance().getUser().getEmail());
                ImageLoaderManager.getInstance(mContext).displayImage(userPhotoView, UserManager.getInstance().getUser().getPicture().getFileUrl());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_imooc_view:
                //分享Imooc课网址
                shareFriend();
                break;
            case R.id.login_layout:
            case R.id.login_view:
                //未登陆，则跳轉到登陸页面
                if (!UserManager.getInstance().hasLogined()) {
                    toLogin();
                }
                break;
            case R.id.logined_layout:
            case R.id.login_info_view:
            case R.id.username_view:
            case R.id.tick_view:
                //已登录，查看用户信息
                if (UserManager.getInstance().hasLogined()) {
                    startActivity(UserInfoActivity.class);
                }
                break;
            case R.id.my_qrcode_view:
                if (!UserManager.getInstance().hasLogined()) {
                    //未登陆，去登陆。
                    toLogin();
                } else {
                    //已登陆根据用户ID生成二维码显示
                    MyQrCodeDialog dialog = new MyQrCodeDialog(mContext);
                    dialog.show();
                }
                break;
            case R.id.video_setting_view:
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.update_view:
                if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
                    checkVersion();
                } else {
                    requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
                }
                break;
        }
    }

    @Override
    public void doWriteSDCard() {
        checkVersion();
    }

    /**
     * 去登陆页面
     */
    private void toLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 分享慕课网给好友
     */
    private void shareFriend() {
        ShareDialog dialog = new ShareDialog(mContext, false);
        dialog.setShareType(Platform.SHARE_IMAGE);
        dialog.setShareTitle("慕课网");
        dialog.setShareTitleUrl("http://www.imooc.com");
        dialog.setShareText("慕课网");
        dialog.setShareSite("imooc");
        dialog.setShareSiteUrl("http://www.imooc.com");
        dialog.setImagePhoto(Environment.getExternalStorageDirectory() + "/test2.jpg");
        dialog.show();
    }

    private void registerBroadcast() {

        IntentFilter filter =new IntentFilter();
        filter.addAction(LOGIN_ACTION);
        filter.addAction(LOGOUT_ACTION);
        LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(receiver, filter);
    }

    private void unregisterBroadcast() {
        LocalBroadcastManager.getInstance(mContext)
                .unregisterReceiver(receiver);
    }

    //发送版本检查更新请求
    private void checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                final UpdateModel updateModel = (UpdateModel) responseObj;
                if (Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
                    //说明有新版本,开始下载
                    CommonDialog dialog = new CommonDialog(mContext, getString(R.string.update_new_version),
                            getString(R.string.update_title), getString(R.string.update_install),
                            getString(R.string.cancel), new CommonDialog.DialogClickListener() {
                        @Override
                        public void onDialogClick() {
                            Intent intent = new Intent(mContext, UpdateService.class);
                            mContext.startService(intent);
                        }
                    });
                    dialog.show();
                } else {
                    //弹出一个toast提示当前已经是最新版本等处理
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 接收mina发送来的消息，并更新UI
     */
    private class LoginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case LOGIN_ACTION:
                    if (UserManager.getInstance().hasLogined()) {
                        //更新我们的fragment
                        if (mLoginedLayout.getVisibility() == View.GONE) {
                            mLoginLayout.setVisibility(View.GONE);
                            mLoginedLayout.setVisibility(View.VISIBLE);
                            mUserNameView.setText(UserManager.getInstance().getUser().getUsername());
                            mTickView.setText(UserManager.getInstance().getUser().getEmail());
                            User user = UserManager.getInstance().getUser();
                            ImageLoaderManager.getInstance(mContext).displayImage(userPhotoView, user.getPicture().getFileUrl());
                        }
                    }
                    break;
                case LOGOUT_ACTION:
                    if (!UserManager.getInstance().hasLogined()){
                        //更新我们的fragment
                        if (mLoginLayout.getVisibility() == View.GONE) {
                            mLoginedLayout.setVisibility(View.GONE);
                            mLoginLayout.setVisibility(View.VISIBLE);
                            mUserNameView.setText(null);
                            mTickView.setText(null);
                        }
                    }
                    break;
            }
        }
    }
}
