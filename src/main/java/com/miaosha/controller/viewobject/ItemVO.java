package com.miaosha.controller.viewobject;

import java.math.BigDecimal;
public class ItemVO {
    private Integer id;
    //    名称
    private String title;
    //    价格
    private BigDecimal price;
    //    库存
    private Integer stock;
    //    描述
    private String description;
    //    销量
    private Integer sales;
    //    图片地址
    private String imgUrl;

//    活动状态 0：没有秒杀活动 1：活动待开始 2：活动进行中
    private Integer promoStatus;
//    活动价格
     private BigDecimal promoPrice;
//     活动开始时间
//    这里设置为String？便于前端接收
     private String startDate;
//     秒杀活动id
    private Integer promoId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
