package com.miaosha.service.impl;

import com.miaosha.dao.ItemDOMapper;
import com.miaosha.dao.ItemStockDOMapper;
import com.miaosha.dao.PromoDOMapper;
import com.miaosha.dataobject.ItemDO;
import com.miaosha.dataobject.ItemStockDO;
import com.miaosha.dataobject.PromoDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.service.ItemService;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.PromoModel;
import com.miaosha.validator.ValidationImpl;
import com.miaosha.validator.ValidationResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidationImpl validator;
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private PromoService promoService;
    private ItemDO convertItemDoFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemDO itemDO=new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }
    private ItemStockDO convertItemStockDoFromItemModel(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }
    private ItemModel convertItemModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
//        校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }
//        转化itemModel>>dataObject
        ItemDO itemDO = this.convertItemDoFromItemModel(itemModel);
//        存入数据库
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDoFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
//        返回创建完的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.selectAll();
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItem_id(itemDO.getId());
            ItemModel itemModel = convertItemModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO==null){
            return null;
        }
//        获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItem_id(itemDO.getId());
//        通过俩object返回model
        ItemModel itemModel = this.convertItemModelFromDataObject(itemDO, itemStockDO);

//        通过itemId找的相关的活动信息
        PromoModel promoModel = promoService.getPromoByItem(itemModel.getId());
        if (promoModel!=null&&promoModel.getStatus()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);
        if (affectedRow>0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void updateSales(Integer itemId, Integer amount) {
        itemDOMapper.updateSales(itemId,amount);
    }
}
