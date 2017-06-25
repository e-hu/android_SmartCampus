package com.smartcampus.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartcampus.R;
import com.smartcampus.activity.base.BaseActivity;
import com.smartcampus.service.UpdateProductService;
import com.smartcampus.view.fragment.home.BaiduMapFragment;
import com.smartcampus.view.fragment.home.HomeFragment;
import com.smartcampus.view.fragment.home.MessageFragment;
import com.smartcampus.view.fragment.home.MineFragment;

/**
 * @function创建首页所有的fragment
 */
public class HomeActivity extends BaseActivity implements OnClickListener {

    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private BaiduMapFragment mBaiduMapFragmentOne;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;
    private Fragment mCurrent;

    /**
     * UI
     */
    private RelativeLayout mHomeLayout;
    private RelativeLayout mPondLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mMineLayout;
    private TextView mHomeView;
    private TextView mPondView;
    private TextView mMessageView;
    private TextView mMineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor(R.color.color_fed952);
        setContentView(R.layout.activity_home_layout);
        //启动后台产品服务更新
        startAllService();
        //初始化页面中的所有控件
        initView();

        //添加默认显示的Fragment
        //事务模式添加Fragment
        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //Fragment切换方式 : replace = 先remove在add //Fragment栈中只有一个，每次需要重新创建Fragment
        fragmentTransaction.replace(R.id.content_layout, mHomeFragment);
        fragmentTransaction.commit();
    }

    private void startAllService() {
        Intent intent = new Intent(this, UpdateProductService.class);
        startService(intent);
    }

    /**
     * 初始化view  ->findViewById+设置监听器
     */
    private void initView() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mPondLayout = (RelativeLayout) findViewById(R.id.pond_layout_view);
        mPondLayout.setOnClickListener(this);
        mMessageLayout = (RelativeLayout) findViewById(R.id.message_layout_view);
        mMessageLayout.setOnClickListener(this);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);
        mMineLayout.setOnClickListener(this);

        mHomeView = (TextView) findViewById(R.id.home_image_view);
        mPondView = (TextView) findViewById(R.id.fish_image_view);
        mMessageView = (TextView) findViewById(R.id.message_image_view);
        mMineView = (TextView) findViewById(R.id.mine_image_view);
        mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 隐藏具体的Fragment
     * @param fragment
     * @param ft
     */
    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    /**
     * Fragment切换逻辑
     * @param v
     */
    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();//开启事务
        switch (v.getId()) {
            case R.id.home_layout_view:
                //底端UI实现，当前点击的为黄色，其他为透明
                changeStatusBarColor(R.color.color_fed952);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
                mPondView.setBackgroundResource(R.drawable.comui_tab_pond);
                mMessageView.setBackgroundResource(R.drawable.comui_tab_message);
                mMineView.setBackgroundResource(R.drawable.comui_tab_person);
                //将HomeFragment显示出来，隐藏其他Fragment
                hideFragment(mBaiduMapFragmentOne, fragmentTransaction);
                hideFragment(mMessageFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                } else {
                    mCurrent = mHomeFragment;
                    //显示Fragment
                    fragmentTransaction.show(mHomeFragment);
                }
                break;
            case R.id.pond_layout_view:
                //底端UI实现，当前点击的为黄色，其他为透明
                changeStatusBarColor(R.color.color_e3e3e3);
                mMessageView.setBackgroundResource(R.drawable.comui_tab_message);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
                mPondView.setBackgroundResource(R.drawable.comui_tab_pond_selected);
                mMineView.setBackgroundResource(R.drawable.comui_tab_person);

                //将BaiduMapFragment显示出来，隐藏其他Fragment
                hideFragment(mMessageFragment, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mBaiduMapFragmentOne == null) {
                    mBaiduMapFragmentOne = new BaiduMapFragment();
                    fragmentTransaction.add(R.id.content_layout, mBaiduMapFragmentOne);
                } else {
                    mCurrent = mBaiduMapFragmentOne;
                    fragmentTransaction.show(mBaiduMapFragmentOne);
                }
                break;
            case R.id.message_layout_view:
                //底端UI实现，当前点击的为黄色，其他为透明
                changeStatusBarColor(R.color.color_e3e3e3);
                mMessageView.setBackgroundResource(R.drawable.comui_tab_message_selected);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
                mPondView.setBackgroundResource(R.drawable.comui_tab_pond);
                mMineView.setBackgroundResource(R.drawable.comui_tab_person);

                //将MessageFragment显示出来，隐藏其他Fragment
                hideFragment(mBaiduMapFragmentOne, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.content_layout, mMessageFragment);
                } else {
                    mCurrent = mMessageFragment;
                    fragmentTransaction.show(mMessageFragment);
                }
                break;
            case R.id.mine_layout_view:
                //底端UI实现，当前点击的为黄色，其他为透明
                changeStatusBarColor(R.color.white);
                mMineView.setBackgroundResource(R.drawable.comui_tab_person_selected);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
                mPondView.setBackgroundResource(R.drawable.comui_tab_pond);
                mMessageView.setBackgroundResource(R.drawable.comui_tab_message);

                //将MineFragment显示出来，隐藏其他Fragment
                hideFragment(mBaiduMapFragmentOne, fragmentTransaction);
                hideFragment(mMessageFragment, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mMineFragment);
                } else {
                    mCurrent = mMineFragment;
                    fragmentTransaction.show(mMineFragment);
                }
                break;
        }

        fragmentTransaction.commit();//提交事务
    }
}
