package com.smartcampus.module.user;


import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 李志鹏 on 15/11/20.
 */
public class User extends BmobUser {

    private BmobFile picture;//头像
    private String name;//姓名
    private String sex;//性别
    private String address;//家庭住址
    private Date birthday;//生日
    private Integer age;//年龄

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
