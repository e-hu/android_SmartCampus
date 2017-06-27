package com.smartcampus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.smartcampus.R;
import com.smartcampus.activity.base.BaseActivity;
import com.smartcampus.manager.UserManager;
import com.smartcampus.module.user.User;

import cn.bmob.v3.BmobUser;

/**
 * 用户信息
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final String LOGOUT_ACTION = "UserInfoActivity_logout_action";
    private User user;
    private TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_layout);
        initData();
        initView();
    }

    private void initView() {
        mLogout = (TextView) findViewById(R.id.logout_button);
        mLogout.setOnClickListener(this);
    }

    private void initData() {
        user = UserManager.getInstance().getUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_button:
                BmobUser.logOut();
                UserManager.getInstance().removeUser();
                sendLoginBroadcast();
                finish();
                break;
        }
    }

    //向整个应用发送退出登陆广播事件
    private void sendLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGOUT_ACTION));
    }
}
