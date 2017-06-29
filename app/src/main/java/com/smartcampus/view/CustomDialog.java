package com.smartcampus.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.smartcampus.R;

import java.io.File;

import static com.smartcampus.activity.UserInfoActivity.CAMERA_REQUEST_CODE;
import static com.smartcampus.activity.UserInfoActivity.IMAGE_REQUEST_CODE;
import static com.smartcampus.activity.UserInfoActivity.PHOTO_IMAGE_FILE_NAME;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private final Activity activity;
    private TextView camera;
    private TextView picture;
    private TextView cancel;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.pop_anim_style);
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_heardicon_layout);
        setCancelable(false);//提示框以外点击无效
        initView();
    }

    private void initView() {
        /**
         * 通过获取到dialog的window来控制dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT; //设置宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度
        dialogWindow.setAttributes(lp);


        camera = (TextView) findViewById(R.id.camera_view);
        picture = (TextView) findViewById(R.id.picture_view);
        cancel = (TextView) findViewById(R.id.cancel_view);
        picture.setOnClickListener(this);
        camera.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_view:
                toCamera();
                break;
            case R.id.picture_view:
                toPicture();
                break;
            case R.id.cancel_view:
                dismiss();
                break;
        }
    }

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dismiss();
    }


}
