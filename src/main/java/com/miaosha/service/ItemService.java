package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.ItemModel;

import java.util.List;

public interface ItemService {
//    创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
//    商品列表浏览
    List<ItemModel> listItem();
//商品详情浏览
    ItemModel getItemById(Integer id);
//    落单减库存
    boolean decreaseStock(Integer itemId,Integer amount);
    void updateSales(Integer itemId,Integer amount);
}
