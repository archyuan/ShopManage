package com.jyy.service;

import com.jyy.dto.ImageHolder;
import com.jyy.dto.ProductExecution;
import com.jyy.entity.Product;

import java.util.List;

public interface ProductService {

    ProductExecution addProduct(
            Product product,
            ImageHolder thumbnail,
            List<ImageHolder> productImageList
    ) throws RuntimeException;

    Product getProductById(long productId);

    ProductExecution modifyProduct(Product product,
                                   ImageHolder thumbnail,
                                   List<ImageHolder> productImageList) throws RuntimeException;

    ProductExecution getProductList(
            Product productCondition,
            int pageIndex,
            int pageSize
    );


}
