package com.smartcampus.manager;


import com.smartcampus.module.user.User;

import cn.bmob.v3.BmobUser;

/**
 * @description 单例管理登陆用户信息
 * @author 李志鹏
 * @date 2015年8月19日
 */
public class UserManager {

	private static UserManager userManager = null;
	private User user = null;

	private UserManager(){
		user = BmobUser.getCurrentUser(User.class);
	}

	public static UserManager getInstance() {

		if (userManager == null) {

			synchronized (UserManager.class) {

				if (userManager == null) {

					userManager = new UserManager();
				}
				return userManager;
			}
		} else {

			return userManager;
		}
	}

	/**
	 * init the user
	 * 
	 * @param user
	 */
	public void setUser(User user) {

		this.user = user;
	}

	public boolean hasLogined() {

		return user != null;
	}

	/**
	 * has user info
	 * 
	 * @return
	 */
	public User getUser() {

		return this.user;
	}

	/**
	 * remove the user info
	 */
	public void removeUser() {

		this.user = null;
	}
}
