package com.jyy.service;

import com.jyy.dto.ProductCategoryExecution;
import com.jyy.entity.ProductCategory;
import com.jyy.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    List<ShopCategory> getShopCategories(
            ShopCategory shopCategoryCondition
    );




}
