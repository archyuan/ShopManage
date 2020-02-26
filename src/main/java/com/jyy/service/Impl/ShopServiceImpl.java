package com.jyy.service.Impl;

import ch.qos.logback.core.util.FileUtil;
import com.jyy.dao.ShopDao;
import com.jyy.dto.ImageHolder;
import com.jyy.dto.ShopExecution;
import com.jyy.entity.Shop;
import com.jyy.enums.ShopStateEnum;
import com.jyy.service.ShopService;
import com.jyy.util.ImageUtil;
import com.jyy.util.PageCompute;
import com.jyy.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopDao shopDao;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
        //空值判断
        if(shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            //店铺信息的初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if(effectedNum<=0){
                throw new RuntimeException("店铺创建失败");
            }else {
                if(shopImgInputStream != null){
                    try{
                        addShopImg(shop,shopImgInputStream,fileName);
                    }catch (Exception e){
                        throw new RuntimeException("addShopImg error:"+e.getMessage());
                    }
                    effectedNum = shopDao.updateShop(shop);
                    if(effectedNum<=0) {
                        throw new RuntimeException("店铺创建失败");
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("addShop error"+e.getMessage());
        }

        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop,InputStream shopImg,String filname){
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        ImageHolder imageHolder = new ImageHolder(filname,shopImg);
        String shopImgAddr = ImageUtil.generateThumbnail(imageHolder,dest);
        shop.setShopImg(shopImgAddr);
    }

    @Override
    public Shop getShopByShopId(Long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws RuntimeException {
        //1 handle image
        if(shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else {
            try{
                if(shopImgInputStream != null&&fileName != null
                        &&!"".equals(fileName)){
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if(tempShop.getShopImg() != null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop,shopImgInputStream,fileName);
                }

                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if(effectedNum <=0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else {
                    return new ShopExecution(ShopStateEnum.SUCCESS,shop);
                }
            }catch (Exception e){
                throw new RuntimeException("modifyShop error "+e.getMessage());
            }
        }
        //2 update shop


    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
       int rowIndex = PageCompute.calculateRowIndex(pageIndex,pageSize);
       List<Shop> shopList = shopDao.queryShopList(shopCondition,rowIndex,pageSize);
       int count = shopDao.queryShopCount(shopCondition);
       ShopExecution shopExecution = new ShopExecution();
        if(shopList != null){
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        }else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
       return shopExecution;
    }
}
