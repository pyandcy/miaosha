package com.miaosha.service.impl;

import com.miaosha.dao.OrderDOMapper;
import com.miaosha.dao.SequenceDOMapper;
import com.miaosha.dataobject.OrderDO;
import com.miaosha.dataobject.SequenceDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.OrderService;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {
//        1.校验下单状态，用户是否合法，购买商品是否存在，数量是否合理
        UserModel userModel = userService.getUserById(userId);
        if (userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        if (amount<=0||amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"下单数量不合理");
        }

//        校验活动id
        if (promoId!=null){
//            1.校验前端传过来的promoId是否有对应商品
            if (promoId!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }else  if (itemModel.getPromoModel().getStatus()!=2){
//                2.校验活动是否在进行中
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"不在活动时间内");
            }
        }


        //2.落单减库存decreaseStock
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setAmount(amount);
        if (promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
//        生成id
        orderModel.setId(generateOrderId());
        OrderDO orderDO = convertOrderDOFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
//        4.更新销量sales
        itemService.updateSales(itemId,amount);
        //5.返回前端
        return orderModel;
    }
@Transactional(propagation = Propagation.REQUIRES_NEW)
public String generateOrderId(){
//        id的前8位是时间
        StringBuilder stringBuilder = new StringBuilder();

        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
//        中间6位是自增序列
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        Integer currentValue = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String currentValueStr= String.valueOf(currentValue);
        for (int i=0;i<6-currentValueStr.length();i++){
            stringBuilder.append("0");
        }
        stringBuilder.append(currentValueStr);
//        后2位是分库分表，暂时写死00
        stringBuilder.append("00");
        return stringBuilder.toString();
    }
    public OrderDO convertOrderDOFromOrderModel(OrderModel orderModel){
        if (orderModel==null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
