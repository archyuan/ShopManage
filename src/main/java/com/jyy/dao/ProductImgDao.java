package com.jyy.dao;

import com.jyy.entity.ProductImg;

import java.util.List;



public interface ProductImgDao {

	List<ProductImg> queryProductImgList(long productId);

	int batchInsertProductImg(List<ProductImg> productImgList);

	int deleteProductImgByProductId(long productId);

}
