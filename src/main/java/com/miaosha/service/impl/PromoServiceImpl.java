package com.miaosha.service.impl;

import com.miaosha.dao.PromoDOMapper;
import com.miaosha.dataobject.PromoDO;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Override
    public PromoModel getPromoByItem(Integer itemId) {
//        通过商品id得到秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
//        转为model
        PromoModel promoModel = convertPromoModelFromPromoDO(promoDO);
        if (promoModel==null){
            return null;
        }
        if (promoModel.getStartDate().isAfterNow()){
//            活动还未开始
            promoModel.setStatus(1);
        }else if (promoModel.getEndDate().isBeforeNow()){
//            活动已经结束
            promoModel.setStatus(3);
        }else {
//            活动在进行中
            promoModel.setStatus(2);
        }
        return promoModel;
    }
    public PromoModel convertPromoModelFromPromoDO(PromoDO promoDO){
        if (promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
