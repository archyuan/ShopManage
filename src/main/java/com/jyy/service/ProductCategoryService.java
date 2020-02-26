package com.jyy.service;

import com.jyy.dto.ProductCategoryExecution;
import com.jyy.entity.Product;
import com.jyy.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> getProductCategoryList(long shopId);

    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
            throws RuntimeException;

    ProductCategoryExecution deleteProductCategory(
            long productCategoryId,
            long shopId
    )throws RuntimeException;
}
