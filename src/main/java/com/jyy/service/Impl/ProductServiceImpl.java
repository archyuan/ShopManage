package com.jyy.service.Impl;

import com.jyy.dao.ProductDao;
import com.jyy.dao.ProductImgDao;
import com.jyy.dto.ImageHolder;
import com.jyy.dto.ProductExecution;
import com.jyy.entity.Product;
import com.jyy.entity.ProductImg;
import com.jyy.enums.ProductStateEnum;
import com.jyy.service.ProductService;
import com.jyy.util.ImageUtil;
import com.jyy.util.PageCompute;
import com.jyy.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    @Override
    @Transactional
    //1处理缩略图，获取缩略图相对路径并赋值给product
    //2往tb_product写入商品信息，获取productId
    //3 结合productId批量处理商品详情图
    //4将商品详情图列表批量插入tb_product_img中
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImageList) throws RuntimeException {
        if(product != null && product.getShop() != null && product.getShop().getShopId()!=null){
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            if(thumbnail != null){
                addThumbnail(product,thumbnail);
            }
            try{
                int effectedNum = productDao.insertProduct(product);
                if(effectedNum <=0){
                    throw new RuntimeException("创建商品失败");
                }
            }catch (Exception e){
                throw new RuntimeException("创建商品失败"+e.toString());
            }
            //详情图添加
            if(productImageList != null && productImageList.size()>0){
                addProductImgList(product,productImageList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS,product);
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }

    }

    private void addThumbnail(
            Product product,
            ImageHolder thumbnail
    ){
        String dest = PathUtil.getShopImagePath(
                product.getShop().getShopId()
        );
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }

    private void addProductImgList(
            Product product,
            List<ImageHolder> productImageList
    ){
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        for(ImageHolder productImageHolder:productImageList){
            String imgAddr = ImageUtil.generateNormalImg(
                    productImageHolder,
                    dest
            );
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        if(productImgList.size() >0){
            try{
                int effectedNum = productImgDao
                        .batchInsertProductImg(productImgList);
                if(effectedNum <=0){
                    throw new RuntimeException("创建商品详情图片失败");
                }
            }catch (Exception e){
                throw new RuntimeException("创建商品详情图片失败"+e.toString());
            }
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    @Override
    @Transactional
    //若缩略图有值，先处理缩略图
    //原先存在就先删除，在添加新的，之后获取缩略图相对路径并赋值给product
    //详情图也是一样
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImageList) throws RuntimeException {
        if(product != null &&
                product.getShop() != null &&
                product.getShop().getShopId()!=null){
            product.setLastEditTime(new Date());
            if(thumbnail != null){
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                if(tempProduct.getImgAddr() !=null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product,thumbnail);
            }
            if(productImageList != null && productImageList.size() >0){
                deleteProductImageList(product.getProductId());
                addProductImgList(product,productImageList);
            }
            try{
                int effectedNum = productDao.updateProduct(product);
                if(effectedNum<=0){
                    throw new RuntimeException("更新商品信息失败");
                }
            }catch (Exception e){
                throw new RuntimeException("更新商品信息失败:"+e.toString());
            }
            return new ProductExecution(ProductStateEnum.SUCCESS);
        }else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }


    }

    private void deleteProductImageList(long productId){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        for(ProductImg productImg:productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        productImgDao.deleteProductImgByProductId(productId);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCompute.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(
                productCondition,
                rowIndex,
                pageSize
        );
        int count = productDao.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }
}
