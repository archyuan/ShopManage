package com.jyy.web.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyy.dto.ImageHolder;
import com.jyy.dto.ProductExecution;
import com.jyy.entity.Product;
import com.jyy.entity.ProductCategory;
import com.jyy.entity.Shop;
import com.jyy.enums.ProductStateEnum;
import com.jyy.service.ProductCategoryService;
import com.jyy.service.ProductService;
import com.jyy.util.CodeUtil;
import com.jyy.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private static final int  IMAGEMAXCOUNT = 6;

    @PostMapping("/addproduct")
    @ResponseBody
    private HashMap<String,Object> addProduct(HttpServletRequest request){
        HashMap<String,Object> modelMap = new HashMap<>();

        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil
                .getString(request,"productStr");
        MultipartHttpServletRequest muiltirequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver(
                        request.getSession().getServletContext()
        );
        try{
            if(multipartResolver.isMultipart(request)){
                muiltirequest = (MultipartHttpServletRequest) request;
                CommonsMultipartFile thumbnailFile =(CommonsMultipartFile)
                        muiltirequest.getFile("thumbnail");
                thumbnail = new ImageHolder(
                        thumbnailFile.getOriginalFilename(),
                        thumbnailFile.getInputStream()
                );
                for(int i = 0;i<IMAGEMAXCOUNT;i++){
                    CommonsMultipartFile productImgFile
                            = (CommonsMultipartFile)muiltirequest.getFile(
                                    "productImg"+i
                    );
                    if(productImgFile != null){
                        ImageHolder productImg =
                                new ImageHolder(
                                        productImgFile.getOriginalFilename(),
                                        productImgFile.getInputStream()
                                );
                        productImgList.add(productImg);
                    }else{
                        break;
                    }
                }
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
    try{
        product = mapper.readValue(productStr,Product.class);
    }catch (Exception e){
        modelMap.put("success",false);
        modelMap.put("errMsg",e.toString());
        return modelMap;
    }
    if(product != null && thumbnail != null &&productImgList.size()>0){
        try{
            Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
            product.setShop(currentShop);
            ProductExecution pe = productService.addProduct(
                    product,
                    thumbnail,
                    productImgList
            );
            if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg",pe.getStateInfo());
            }
        }catch (RuntimeException e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
    }else{
        modelMap.put("success",false);
        modelMap.put("errMsg","请输入商品信息");
    }
    return modelMap;
    }

    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductById(@RequestParam Long productId){
        Map<String,Object> modelMap = new HashMap<>();
        if(productId > -1){
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(
                    product.getShop().getShopId()
            );
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }



    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //是商品编辑时候调用还是上下架的时候调用
        //若为前者则进行验证码判断，后者则跳过验证码判断

        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        if(!statusChange&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        try{
            if(multipartResolver.isMultipart(request)){
              MultipartHttpServletRequest  muiltirequest = (MultipartHttpServletRequest) request;
                CommonsMultipartFile thumbnailFile =(CommonsMultipartFile)
                        muiltirequest.getFile("thumbnail");
                if(thumbnailFile != null) {
                    thumbnail = new ImageHolder(
                            thumbnailFile.getOriginalFilename(),
                            thumbnailFile.getInputStream()
                    );
                }
                for(int i = 0;i<IMAGEMAXCOUNT;i++){
                    CommonsMultipartFile productImgFile
                            = (CommonsMultipartFile)muiltirequest.getFile(
                            "productImg"+i
                    );
                    if(productImgFile != null){
                        ImageHolder productImg =
                                new ImageHolder(
                                        productImgFile.getOriginalFilename(),
                                        productImgFile.getInputStream()
                                );
                        productImgList.add(productImg);
                    }else{
                        break;
                    }
                }
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }

        try{
            String productStr = HttpServletRequestUtil.getString(request,"productStr");
            product = mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if(product != null){
            try{
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution pe = productService.modifyProduct(
                        product,
                        thumbnail,
                        productImgList
                );
                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
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
            modelMap.put("errMsg","请输入商品信息");
        }
    return modelMap;
    }
    @RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductListByShop(HttpServletRequest request){
     Map<String,Object> modelMap = new HashMap<>();
     int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
     int pageSize = HttpServletRequestUtil.getInt(
             request,
             "pageSize"
     );

     Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
     if((pageIndex>-1)&&(pageSize>-1)&&(currentShop !=null)&&
             (currentShop.getShopId()!=null)){
         long productCategoryId =
                 HttpServletRequestUtil.getLong(
                         request,
                         "productCategoryId"
                 );
         String productName = HttpServletRequestUtil.getString(
                 request,
                 "productName"
         );
         Product productCondition = compactProductCondition(
                 currentShop.getShopId(),
                 productCategoryId,
                 productName
         );
         ProductExecution pe = productService.getProductList(
                 productCondition,
                 pageIndex,
                 pageSize
         );
         modelMap.put("productList",pe.getProductList());
        modelMap.put("count",pe.getCount());
        modelMap.put("success",true);
     }else {
         modelMap.put("success",false);
         modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
     }
     return modelMap;
    }

    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if(productCategoryId != -1l){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }

        if(productName != null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }
}
