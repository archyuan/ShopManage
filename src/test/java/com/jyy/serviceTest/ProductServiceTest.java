package com.jyy.serviceTest;

import com.jyy.daoTest.BaseTest;
import com.jyy.dto.ImageHolder;
import com.jyy.dto.ProductExecution;
import com.jyy.entity.Product;
import com.jyy.entity.ProductCategory;
import com.jyy.entity.Shop;
import com.jyy.enums.ProductStateEnum;
import com.jyy.service.ProductService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends BaseTest {
    @Autowired
    ProductService productService;

    @Test
    @Ignore
    public void testAddProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("测试商品");
        product.setProductDesc("测试商品1");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        File thumbnailFile = new File("" +
                "D:\\aa.jpg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getName(),
                is);
        File pri = new File("D:\\aa.jpg");
        InputStream is1 = new FileInputStream(pri);
        List<ImageHolder> list = new ArrayList<>();
        list.add(new ImageHolder(thumbnailFile.getName(),is));
        list.add(new ImageHolder(thumbnailFile.getName(),is));
        ProductExecution pe = productService.addProduct(
                product,
                imageHolder,
                list
        );
        assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
    }


    @Test
    public void testModifyProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("修改商品");
        product.setProductDesc("修改商品11");
        product.setPriority(10);
        product.setCreateTime(new Date());
        product.setProductId(1L);
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        File thumbnailFile = new File("" +
                "D:\\aa.jpg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder imageHolder = new ImageHolder(thumbnailFile.getName(),
                is);
        File pri = new File("D:\\aa.jpg");
        InputStream is1 = new FileInputStream(pri);
        List<ImageHolder> list = new ArrayList<>();
        list.add(new ImageHolder(thumbnailFile.getName(),is));
        list.add(new ImageHolder(thumbnailFile.getName(),is));
        ProductExecution pe =   productService.modifyProduct(
                product,
                imageHolder,
                list
        );

        System.out.println(
                pe.getStateInfo()
        );
    }
}
