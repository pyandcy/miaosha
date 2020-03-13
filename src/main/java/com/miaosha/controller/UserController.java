package com.miaosha.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaosha.controller.viewobject.UserVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.UserService;
import com.miaosha.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
//    用户登录
@RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
@ResponseBody
public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                              @RequestParam(name = "password")String  password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
//    入参检验
    if (org.apache.commons.lang3.StringUtils.isEmpty(telphone)|| org.apache.commons.lang3.StringUtils.isEmpty(password)){
        throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
    }
//    检验登录查数据库比较
    UserModel userModel = userService.validateLogin(telphone, this.enCodeMd5(password));
//    将登录凭证添加到成功登录的用户session中
    this.httpServletRequest.getSession().setAttribute("Is_Login",true);
    this.httpServletRequest.getSession().setAttribute("Login_User",userModel);
    return CommonReturnType.create(null);
}
//    用户注册接口
@RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
@ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
//验证手机号对应的otp
        String optCodeInSession = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if (!StringUtils.equals(optCodeInSession,otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合要求");
        }
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisitMode("byPhone");
        userModel.setEncrptPassword(this.enCodeMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }
//    重写md5方法
    public String enCodeMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }
//    用户获取短信otp接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone) throws BusinessException {
//        通过一定的规则生成otp验证码
        Random random = new Random();
//        [0-99999)
//        int randomInt = random.nextInt(99999);
////        [10000-109999)
//        randomInt=randomInt+10000;
//        String otpCode = String.valueOf(randomInt);

        StringBuffer otpCodeBuffer = new StringBuffer();
        for (int i=0;i<6;i++){
            otpCodeBuffer.append(random.nextInt(10));
        }
        String otpCode = otpCodeBuffer.toString();

//        将otp验证码和对应的tel关联，使用httpsession的方式绑定手机号和otpCode
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

//        用redis方式将手机号和验证码缓存在内存中
//        try {
//            stringRedisTemplate.opsForValue().set(telphone, otpCode, 5, TimeUnit.MINUTES);
//        } catch (Exception e) {
//            throw new BusinessException(EmBusinessError.REDIS_KEY_SAVE_FAILED,"redis缓存失败-存失败");
//        }
//
//        String randomCodeFromRedis = "";
//        try {
//            // 从redis取值
//            randomCodeFromRedis = stringRedisTemplate.opsForValue().get(telphone);
//        } catch (Exception e) {
//            throw new BusinessException(EmBusinessError.REDIS_KEY_SAVE_FAILED,"redis缓存失败-取失败");
//        }
////        从redis中通过telphone获取的otpCode
//        System.out.println("redis的otpCode:"+randomCodeFromRedis);

//        将otp通过手机短信通告发送给用户
        System.out.println("otpCode:"+otpCode);
        System.out.println("telphone:"+telphone);
        return CommonReturnType.create(null);
    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
//        调用service获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        if (userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
//            userModel.setEncrptPassword("123");  为了演示空指针异常，未知错误
        }
//        将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        UserVO userVO = new UserVO();
        if (userModel==null){
            return null;
        }
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

}
