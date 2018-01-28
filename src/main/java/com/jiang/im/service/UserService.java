package com.jiang.im.service;

import com.jiang.im.dataobject.UserProfile;
import com.jiang.im.exception.LiveException;

public interface UserService {

    UserProfile findUserByIDAndPwd(String account, String password) throws LiveException;


    /**
     * 添加账户信息
     * @param userProfile
     * @return
     */
    UserProfile reigisterUserProfile(UserProfile userProfile) throws LiveException;

    /**
     * 查找账户信息
     * @param account
     * @return
     */
    UserProfile findOne(String account);

    /**
     * 保存或则更新用户
     * @param userProfile
     * @return
     */
    UserProfile save(UserProfile userProfile);
}
