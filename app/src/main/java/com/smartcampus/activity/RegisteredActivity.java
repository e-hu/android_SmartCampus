package com.smartcampus.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcampus.R;
import com.smartcampus.activity.base.BaseActivity;
import com.smartcampus.module.user.User;
import com.smartcampus.view.associatemail.MailBoxAssociateTokenizer;
import com.smartcampus.view.associatemail.MailBoxAssociateView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private MailBoxAssociateView mUserName;
    private EditText mPass;
    private EditText mPassword;
    private TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        initView();
    }

    private void initView() {
        mUserName = (MailBoxAssociateView) findViewById(R.id.register_input_username);
        mPass = (EditText) findViewById(R.id.register_input_pass);
        mPassword = (EditText) findViewById(R.id.register_input_password);
        mRegister = (TextView) findViewById(R.id.register_button);
        mRegister.setOnClickListener(this);
        findViewById(R.id.other_type_register_button).setOnClickListener(this);

        //邮箱后缀
        String[] recommendMailBox = getResources().getStringArray(R.array.recommend_mailbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_associate_mail_list,
                R.id.tv_recommend_mail, recommendMailBox);
        mUserName.setAdapter(adapter);
        mUserName.setTokenizer(new MailBoxAssociateTokenizer());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                //获取到输入框的值
                String username = mUserName.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //判断是否为空
                if (!TextUtils.isEmpty(username) &&
                        !TextUtils.isEmpty(pass) &&
                        !TextUtils.isEmpty(password)) {

                    //判断两次输入的密码是否一致
                    if (pass.equals(password)){
                        //注册
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setEmail(username);//默认用户名为邮箱
                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if(e==null){
                                    Toast.makeText(RegisteredActivity.this,
                                            getString(R.string.text_registered_successful)+" "+getString(R.string.goto_email)
                                            , Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(RegisteredActivity.this, getString(R.string.text_registered_failure) + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.text_two_input_not_consistent, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.other_type_register_button:
                break;
        }
    }
}
