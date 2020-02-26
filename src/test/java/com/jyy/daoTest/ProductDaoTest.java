package com.jyy.daoTest;

import com.jyy.dao.ProductDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductDaoTest extends BaseTest{
    @Autowired
    ProductDao productDao;

    @Test
    public void productCate(){
       int effectedNum =  productDao.updateProductCategoryToNull(1);
        System.out.println(effectedNum);
    }
}
