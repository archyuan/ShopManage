package com.jyy.web.productcategorymanagement;

import com.jyy.dto.ProductCategoryExecution;
import com.jyy.dto.Result;
import com.jyy.entity.ProductCategory;
import com.jyy.entity.Shop;
import com.jyy.enums.ProductCategoryStateEnum;
import com.jyy.enums.ShopStateEnum;
import com.jyy.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/productcategory")
public class ProductCategoryManagementController {

    @Autowired
    ProductCategoryService productCategoryService;

    @GetMapping(value = "/getproductcategorylist")
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
        Shop shop = new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop",shop);
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Queue<Integer> q = new LinkedList<>();
        List<ProductCategory> list = null;
        if(currentShop != null && currentShop.getShopId() > 0){
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true,list);
        }else {
            ShopStateEnum productcategory = ShopStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,productcategory.getState(),productcategory.getStateInfo());
        }

    }

    @PostMapping(value = "addproductcategories")
    @ResponseBody
    public HashMap<String,Object> addProductCategories(
            @RequestBody List<ProductCategory> productCategoryList,
            HttpServletRequest request
    ){
        HashMap<String,Object> modelMap = new HashMap<>();
        Shop currentShop  = (Shop)request.getSession().getAttribute("currentShop");
        for(ProductCategory pc:productCategoryList){
            pc.setShopId(currentShop.getShopId());
        }
        System.out.println(productCategoryList);

        if(productCategoryList != null && productCategoryList.size() > 0){
            try{
                System.out.println(productCategoryList.get(0).toString());
                System.out.println(productCategoryList.size());
                System.out.println(productCategoryList.get(0).getPriority());
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少输入一个商品类别");
        }

        return modelMap;
    }

    @PostMapping(value = "removeproductcategory")
    @ResponseBody
    public HashMap<String,Object> removeProductCategory(
            Long productCategoryId,
            HttpServletRequest request
    ){
        HashMap<String,Object> modelMap = new HashMap<>();
        if(productCategoryId != null && productCategoryId > 0){
            try{
                Shop currentShop  = (Shop)request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少选择一个商品类别");
        }

        return modelMap;
    }

}
