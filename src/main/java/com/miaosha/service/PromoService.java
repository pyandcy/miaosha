package com.miaosha.service;

import com.miaosha.dataobject.PromoDO;
import com.miaosha.service.model.PromoModel;

public interface PromoService {
//    根据itemId找到要开启秒杀或正在秒杀的活动
    PromoModel getPromoByItem(Integer itemId);
}
