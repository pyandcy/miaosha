package com.miaosha.dao;

import com.miaosha.dataobject.ItemStockDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface ItemStockDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Mar 10 15:21:47 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Mar 10 15:21:47 CST 2020
     */
    int insert(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Mar 10 15:21:47 CST 2020
     */
    int insertSelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Mar 10 15:21:47 CST 2020
     */
    ItemStockDO selectByPrimaryKey(Integer id);

    ItemStockDO selectByItem_id(Integer item_id);
    int decreaseStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
//    int decreaseStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Mar 10 15:21:47 CST 2020
     */
    int updateByPrimaryKeySelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Mar 10 15:21:47 CST 2020
     */
    int updateByPrimaryKey(ItemStockDO record);
}