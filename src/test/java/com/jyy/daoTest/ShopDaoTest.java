package com.jyy.daoTest;

import com.jyy.dao.ShopDao;
import com.jyy.entity.Area;
import com.jyy.entity.PersonInfo;
import com.jyy.entity.Shop;
import com.jyy.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;


    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo() ;
        Area area = new Area();
        area.setAreaId(3L);
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setUserId(1L);
        shopCategory.setShopCategoryId(1L);
        shop.setOwnerId(1L);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);
        shop.setShopName("test");
        shop.setArea(area);
        shop.setShopName("测试的店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1,effectedNum);
    }

    @Test
    @Ignore
    public void updateShop(){
        Shop shop1 = new Shop();
        shop1.setShopId(1L);
        shop1.setShopName("更改后的标签");
        shop1.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop1);
        assertEquals(1,effectedNum);
    }

    @Test
    @Ignore
    public void testQueryShopByShopId(){
        long shopId = 10;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("area "+shop.getArea().getAreaName());
        System.out.println("shopName "+shop.getShopName());
    }

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        shopCondition.setOwnerId(1L);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,1,5);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表大小: "+shopList.size());
        System.out.println("店铺总数:"+count);
    }
}
