package com.jyy.daoTest;

import com.jyy.dao.ProductCategoryDao;
import com.jyy.dao.ShopCategoryDao;
import com.jyy.entity.Area;
import com.jyy.entity.ProductCategory;
import com.jyy.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    ShopCategoryDao shopCategoryDao;

    @Autowired
    ProductCategoryDao productCategoryDao;

    @Test
    public void testQueryshopCategory() {
        List<ShopCategory> shopCategoriesList = shopCategoryDao.
                queryShopCategory(null);
        System.out.println(shopCategoriesList.size());

    }

    @Test
    @Ignore
    public void testBatchProductCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);

        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryName("商品类别2");
        productCategory1.setPriority(2);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);
        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory1);
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(2,effectedNum);
    }
}
