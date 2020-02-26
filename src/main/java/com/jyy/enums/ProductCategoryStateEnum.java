package com.jyy.enums;

import com.jyy.dto.ProductCategoryExecution;

public enum ProductCategoryStateEnum {
    SUCCESS(1,"创建成功"),
    INNER_ERROR(-1001,"操作失败"),
    EMPTY_LIST(-1002,"添加资源为空");

    private int state;

    private String stateInfo;

    private ProductCategoryStateEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getState(){
        return state;
    }

    public static ProductCategoryStateEnum stateOf(int index){
        for(ProductCategoryStateEnum state:values()){
            if(state.getState() == index){
                return  state;
            }
        }
        return null;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
