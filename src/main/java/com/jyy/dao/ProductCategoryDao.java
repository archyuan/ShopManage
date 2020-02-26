package com.jyy.dao;

import com.jyy.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {

    List<ProductCategory> queryProductCategoryLIst(long shopId);

    //批量新增商品类别
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    int deleteProductCategory(@Param("productCategoryId")
                              long productCategoryId,
                              @Param("shopId")long shopId);
}
