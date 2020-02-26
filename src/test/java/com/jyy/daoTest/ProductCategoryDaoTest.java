package com.jyy.daoTest;

import com.jyy.dao.ProductCategoryDao;
import com.jyy.entity.ProductCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductCategoryDaoTest extends BaseTest {

    @Autowired
    ProductCategoryDao productCategoryDao;

    @Test
    @Ignore
    public void testQueryByShopId(){
        long shopId = 1;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryLIst(
          shopId
        );
        System.out.println("该店铺自定义类别数为:"+productCategoryList.size());
    }

    @Test
    public void delete(){
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryLIst(1L);
        for(ProductCategory pc:productCategoryList){
            if("商品类别1".equals(pc.getProductCategoryName())||
            "商品类别2".equals(pc.getProductCategoryName())){
                int eff = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(),1);
                assertEquals(1,eff);
            }
        }
    }
}
