package com.miaosha.service.impl;

import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import com.miaosha.validator.ValidationImpl;
import com.miaosha.validator.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidationImpl validatior;
    @Override
    public UserModel getUserById(Integer id) {
//        调用userDOMapper得到对应id用户的dataobject
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
//        通过用户id得到加了密的用户密码
        if (userDO==null){
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if (StringUtils.isEmpty(userModel.getName())
//                ||StringUtils.isEmpty(userModel.getTelphone())
//                ||userModel.getAge()==null
//                ||userModel.getGender()==null){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validatior.validate(userModel);
        if (result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }
        UserDO userDO =convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已经注册");
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertFromPasswordModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
//        通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (userDO==null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);
//        通过获得的用户信息中的加了密的密码和前端传过来的用户密码比较
        if (!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO convertFromPasswordModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    public UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO!=null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
