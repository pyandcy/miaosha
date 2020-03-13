package com.miaosha.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {
    private Integer id;
//    名称
    @NotBlank(message = "商品名称不能为空")
    private String title;
//    价格
    @NotNull(message = "价格不能为空")
    @Min(value = 0 ,message = "最低价格为0元")
    private BigDecimal price;
//    库存
    @NotNull(message = "库存不能不填")
    private Integer stock;
//    描述
    @NotBlank(message = "描述不能为空")
    private String description;
//    销量
    private Integer sales;
//    图片地址
@NotBlank(message = "图片不能为空")
    private String imgUrl;
//聚合的方式，如果promoModel不为空，说明其有还未结束的秒杀活动
private PromoModel promoModel;

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

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

    @Override
    public String toString() {
        return "ItemModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", descreption='" + description + '\'' +
                ", sales=" + sales +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
