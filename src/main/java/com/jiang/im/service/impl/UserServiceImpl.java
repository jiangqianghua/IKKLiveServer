package com.jiang.im.service.impl;

import com.jiang.im.dataobject.UserProfile;
import com.jiang.im.enums.ResultEnum;
import com.jiang.im.exception.LiveException;
import com.jiang.im.repository.UserProfileRepository;
import com.jiang.im.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileRepository repository ;
    @Override
    public UserProfile findUserByIDAndPwd(String account, String password) throws LiveException {
        UserProfile userProfile =  repository.findOne(account);
        if(userProfile == null){
            throw new LiveException(ResultEnum.USER_NOT_FOUND);
        }
        if(!userProfile.getUserPassword().equals(password)){
            throw new LiveException(ResultEnum.USER_NOT_PASSWORD);
        }

        return userProfile ;

    }

    @Override
    public UserProfile reigisterUserProfile(UserProfile userProfile)throws LiveException {
        if(userProfile == null || StringUtils.isEmpty(userProfile.getUserAccount())
                || StringUtils.isEmpty(userProfile.getUserPassword()))
        {
            throw new LiveException(ResultEnum.USER_REGISTER_ERROR);
        }
        // 判断用户是否存在
        UserProfile userProfile1 = repository.findOne(userProfile.getUserAccount());
        if(userProfile1 != null){
            throw new LiveException(ResultEnum.USER_EXISTS);
        }

        repository.save(userProfile);
        return userProfile;
    }

    @Override
    public UserProfile findOne(String account) {
        return repository.findOne(account);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return repository.save(userProfile);
    }
}
