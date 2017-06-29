package com.smartcampus.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.euler.andfix.util.FileUtil;
import com.github.mikephil.charting.utils.FileUtils;
import com.smartcampus.R;
import com.smartcampus.activity.base.BaseActivity;
import com.smartcampus.manager.UserManager;
import com.smartcampus.module.user.User;
import com.smartcampus.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户信息
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final String LOGOUT_ACTION = "UserInfoActivity_logout_action";
    private User user;
    private TextView mLogout;
    private CircleImageView heardIcon;
    private CustomDialog dialog;
    private Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_layout);
        initData();
        initView();
    }

    private void initView() {
        heardIcon = (CircleImageView) findViewById(R.id.photo_view);
        heardIcon.setOnClickListener(this);
        mLogout = (TextView) findViewById(R.id.logout_button);
        mLogout.setOnClickListener(this);
        LinearLayout mobile = (LinearLayout) findViewById(R.id.mobile);
        ((TextView) mobile.findViewById(R.id.left)).setText(R.string.mobile);
        ((TextView) mobile.findViewById(R.id.right)).setText(user.getMobilePhoneNumber());
        LinearLayout email = (LinearLayout) findViewById(R.id.email);
        ((TextView) email.findViewById(R.id.left)).setText(R.string.email);
        ((TextView) email.findViewById(R.id.right)).setText(user.getEmail());
        if (picture != null){
            heardIcon.setImageBitmap(picture);
        }

        //初始化dialog
        dialog = new CustomDialog(this);
    }

    private void initData() {
        user = UserManager.getInstance().getUser();
        if (TextUtils.isEmpty(user.getMobilePhoneNumber()))
            user.setMobilePhoneNumber(getString(R.string.bind_mobile));
        if (TextUtils.isEmpty(user.getEmail()))
            user.setEmail(getString(R.string.bind_email));
        if (user.getPicture() != null){
            picture = BitmapFactory.decodeFile(user.getPicture().getFilename());
        }

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
            case R.id.photo_view:
                dialog.show();
                break;
        }
    }

    //向整个应用发送退出登陆广播事件
    private void sendLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGOUT_ACTION));
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;



    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.e(getClass().getName(),"uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            new SaveAndUpload(bitmap, "test.jpg").start();//启动线程
            heardIcon.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    private class SaveAndUpload extends Thread{

        private final Bitmap bitmap;
        private final String path;
        private final File saveFile;

        public SaveAndUpload(Bitmap bitmap, String path){
            this.bitmap = bitmap;
            this.path = path;
            this.saveFile = new File(Environment.getExternalStorageDirectory(), path);
            if (!saveFile.exists()){
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void run() {
            save();
            //upload();
        }

        private void upload() {
            final BmobFile picture = new BmobFile(saveFile);
            picture.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if(e==null){
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.e("BMOBUPDATE", "上传文件成功:" + picture.getFileUrl());
                    }else{
                        Log.e("BMOBUPDATE", "上传文件失败：" + e.getMessage());
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
            User user = BmobUser.getCurrentUser(User.class);
            user.setPicture(picture);
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null){
                        Log.e("BMOBUPDATE", e.getMessage());
                        Log.e("BMOBUPDATE", e.getErrorCode()+"");
                        Log.e("BMOBUPDATE", e+"");
                    }
                }
            });
            if (saveFile != null) {
                if (saveFile.exists()){
                    saveFile.delete();
                }
            }
        }

        private void save() {
            try {
                FileOutputStream out = new FileOutputStream(saveFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
