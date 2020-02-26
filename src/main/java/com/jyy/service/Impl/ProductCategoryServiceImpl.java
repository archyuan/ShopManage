package com.jyy.service.Impl;

import com.jyy.dao.ProductCategoryDao;
import com.jyy.dao.ProductDao;
import com.jyy.dto.ProductCategoryExecution;
import com.jyy.entity.ProductCategory;
import com.jyy.enums.ProductCategoryStateEnum;
import com.jyy.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements
        ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryLIst(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws RuntimeException {
       if(productCategoryList != null && productCategoryList.size() >0){
           try{
               int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
               if(effectedNum <= 0){
                   throw new RuntimeException("店铺类别创建失败");
               }else {
                   return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
               }
           }catch(Exception e){
               throw new RuntimeException("batchAddProductCategory error:"+e.getMessage());
           }
       }else {
           return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
       }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws RuntimeException {
        //将此商品类别下的商品的类别Id值为空
        try{
            int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
            if(effectedNum < 0){
                throw new RuntimeException("商品类别更新失败");
            }
        }catch (Exception e){
            throw new RuntimeException("商品类别更新失败");
        }
        try{
            int effectedNum = productCategoryDao.deleteProductCategory(
                    productCategoryId,shopId
            );
            if(effectedNum <= 0){
                throw new RuntimeException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch (Exception e){
            throw new RuntimeException("deleteProductCategory error:"+e.getMessage());
        }


    }
}
