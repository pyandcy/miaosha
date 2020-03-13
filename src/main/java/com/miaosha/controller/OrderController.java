package com.miaosha.controller;

import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.OrderService;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOder(@RequestParam(name ="itemId" ) Integer itemId,
                                       @RequestParam(name ="amount" ) Integer amount,
                                       @RequestParam(name ="promoId" ,required = false) Integer promoId ) throws BusinessException {
//        this.httpServletRequest.getSession().setAttribute("Is_Login",true);
//        this.httpServletRequest.getSession().setAttribute("Login_User",userModel);
        Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("Is_Login");
        if (isLogin==null||!isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户名未登录");
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("Login_User");
        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId,promoId, amount);
        return CommonReturnType.create(orderModel);
    }
}
