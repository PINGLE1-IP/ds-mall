package com.ds.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.mall.product.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    @Update("update product_sku set stock = stock + #{quantity}, updated_at = now(), version = version + 1 where id = #{skuId} and deleted = 0")
    int increaseStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("""
            update product_sku
            set stock = stock - #{quantity}, updated_at = now(), version = version + 1
            where id = #{skuId} and deleted = 0 and enabled = 1 and stock - locked_stock >= #{quantity}
            """)
    int deductAvailableStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("""
            update product_sku
            set locked_stock = locked_stock + #{quantity}, updated_at = now(), version = version + 1
            where id = #{skuId} and deleted = 0 and enabled = 1 and stock - locked_stock >= #{quantity}
            """)
    int lockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("""
            update product_sku
            set locked_stock = locked_stock - #{quantity}, updated_at = now(), version = version + 1
            where id = #{skuId} and deleted = 0 and locked_stock >= #{quantity}
            """)
    int releaseLockedStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Update("""
            update product_sku
            set stock = stock - #{quantity}, locked_stock = locked_stock - #{quantity}, updated_at = now(), version = version + 1
            where id = #{skuId} and deleted = 0 and stock >= #{quantity} and locked_stock >= #{quantity}
            """)
    int deductLockedStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
}
