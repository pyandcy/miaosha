package com.miaosha.controller;

import com.miaosha.controller.viewobject.ItemVO;
import com.miaosha.error.BusinessException;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        ItemModel itemModel = new ItemModel();
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        itemModel.setTitle(title);
        ItemModel returnItem = itemService.createItem(itemModel);
        ItemVO itemVO = convertItemVOFromReturnItem(returnItem);
        return CommonReturnType.create(itemVO);
    }

//    商品信息浏览
@RequestMapping(value = "/get",method = {RequestMethod.GET})
@ResponseBody
public CommonReturnType createItem(@RequestParam(name = "id") Integer id){
    ItemModel itemById = itemService.getItemById(id);
    ItemVO itemVO = convertItemVOFromReturnItem(itemById);
    return CommonReturnType.create(itemVO);
}
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModels = itemService.listItem();
        List<ItemVO> itemVOList = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = convertItemVOFromReturnItem(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }
    public ItemVO convertItemVOFromReturnItem(ItemModel itemModel){
        if (itemModel==null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if (itemModel.getPromoModel()!=null){
//            说明有正在进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoId(itemModel.getPromoModel().getId());
        }else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
