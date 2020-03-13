package com.miaosha.service.model;

import java.math.BigDecimal;

public class OrderModel {
    private String id;
    private Integer userId;
    private Integer itemId;
//    一件商品的价格,若promoId存在说明有活动，此时的商品价格为活动价
    private BigDecimal itemPrice;
    private Integer amount;
//    订单价格
    private Integer promoId;
//    若有活动，此时的订单价格为活动单价和购买数量的乘积
    private BigDecimal orderPrice;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
