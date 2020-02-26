package com.jyy.dto;

import com.jyy.entity.ProductCategory;
import com.jyy.enums.ProductCategoryStateEnum;

import java.util.List;

public class ProductCategoryExecution {
    private int state;

    private String stateInfo;

    private List<ProductCategory> productCategoryList;

    public ProductCategoryExecution(){}
    public ProductCategoryExecution(ProductCategoryStateEnum stateEnum){
        this.state = stateEnum.getState();
        stateInfo = stateEnum.getStateInfo();
    }
    public ProductCategoryExecution(ProductCategoryStateEnum state,
                                    List<ProductCategory> productCategoryList){
        this.state = state.getState();
        this.stateInfo = state.getStateInfo();
        this.productCategoryList = productCategoryList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
