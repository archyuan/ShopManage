package com.jyy.service;

import com.jyy.dto.ShopExecution;
import com.jyy.entity.Shop;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName);

    Shop getShopByShopId(Long shopId);

    ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream
    ,String fileName) throws RuntimeException;

    public ShopExecution getShopList(Shop shopCondition,
                                     int pageIndex,
                                     int pageSize);
}
