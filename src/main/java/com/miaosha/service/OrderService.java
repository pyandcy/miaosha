package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.OrderModel;

public interface OrderService {
//    通过前端url传过来的秒杀活动id,然后下单接口内校验对应id是否属于对应商品且活动已开始
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
