package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
//encrptPassword为用户加密后的密码
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
