package com.jyy.serviceTest;

import com.jyy.daoTest.BaseTest;
import com.jyy.dto.ShopExecution;
import com.jyy.entity.Shop;
import com.jyy.entity.ShopCategory;
import com.jyy.enums.ShopStateEnum;
import com.jyy.service.ShopService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(1L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopExecution = shopService.getShopList(
                shopCondition,1,2
        );
        System.out.println("店铺列表数:"+shopExecution.getShopList().size());
        System.out.println("店铺总数为:"+shopExecution.getCount());
    }

    @Test
    @Ignore
    public void testMOdifyShop() throws RuntimeException,FileNotFoundException{
        Shop shop = new Shop();
        shop.setShopId(1L);
        InputStream is = new FileInputStream(new File("D:\\aa.jpg"));
        ShopExecution shopExecution = shopService.modifyShop(shop,is,"aa.jpg");
        System.out.println("新的图片地址："+ shopExecution.getShop().getShopImg());
    }

    @Test
    @Ignore
    public void addShop(){
        Shop shop1 = new Shop();
        shop1.setShopId(3L);
        shop1.setShopName("更改后的标签");
        shop1.setOwnerId(1L);
        shop1.setLastEditTime(new Date());
        shop1.setShopDesc("test");
        shop1.setAdvice("审核中");
        File shopImg = new File("D:\\Salco.jpg");
        ShopExecution se  = null;
        try {
            se = shopService.addShop(shop1,new FileInputStream(shopImg),shopImg.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }
}
