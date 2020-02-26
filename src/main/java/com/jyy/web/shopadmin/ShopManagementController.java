package com.jyy.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyy.dto.ShopExecution;
import com.jyy.entity.Area;
import com.jyy.entity.PersonInfo;
import com.jyy.entity.Shop;
import com.jyy.entity.ShopCategory;
import com.jyy.enums.ShopStateEnum;
import com.jyy.service.AreaService;
import com.jyy.service.ShopCategoryService;
import com.jyy.service.ShopService;
import com.jyy.util.CodeUtil;
import com.jyy.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("shopmanagement")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @GetMapping(value = "/getShopmanagementinfo")
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(
                request,"shopId"
        );

        if(shopId <= 0){
            Object currentShopObj = request.getSession()
                    .getAttribute("currentShop");
            if(currentShopObj == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/background_war/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute(
                    "currentShop",currentShop
            );
            modelMap.put("redirect",false);
        }

        return modelMap;
    }

    @GetMapping(value = "/getshoplist")
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
       // PersonInfo user = (PersonInfo) request.getSession()
         //       .getAttribute("user");
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setName("test");
        user.setEnableStatus(1);
        request.getSession().setAttribute("user",user);
        long employeeId = user.getUserId();
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition,0,100);
            modelMap.put("success",true);
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("user",user);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopByShopId(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        System.out.println("shopId "+shopId);
        if(shopId > -1){
            try{
                Shop shop = shopService.getShopByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/shopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategories = new ArrayList<>();
        List<Area> areas = new ArrayList<>();
        try{
            shopCategories = shopCategoryService.getShopCategories(new ShopCategory());
            areas = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategories);
            modelMap.put("areaList",areas);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("suxxess",false);
            modelMap.put("errMsg",e.getMessage());
        }
        System.out.println(modelMap.get("shopCategoryList"));
        return modelMap;
    }

    @PostMapping(value = "/registershop")
    @ResponseBody
    private Map<String,Object> registerShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }
        //1 接收并转化相应参数
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);

        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }

        //2 注册店铺
        if(shop != null&&shopImg != null){
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            //Session todo
            owner.setUserId(1L);
            shop.setOwnerId(owner.getUserId());
            shop.setOwner(owner);


            ShopExecution shopExecution = null;
            try {
                shopExecution = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                if(shopExecution.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if(shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",shopExecution.getStateInfo());
                }
                return modelMap;
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",shopExecution.getStateInfo());
                return modelMap;
            }

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺的信息");
            return modelMap;
        }

        //3返回结果

    }

    @PostMapping(value = "/modifyshop")
    @ResponseBody
    private Map<String,Object> modifyShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }
        //1 接收并转化相应参数
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try{
            shop = mapper.readValue(shopStr,Shop.class);

        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //2 修改店铺
        if(shop != null&&shop.getShopId() != null){
            ShopExecution shopExecution = null;
            try {
                if(shopImg == null){
                    shopExecution = shopService.modifyShop(shop,null,
                            null);
                }else {
                    shopExecution = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }
                if(shopExecution.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",shopExecution.getStateInfo());
                }
                return modelMap;
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺的信息");
            return modelMap;
        }

        //3返回结果

    }




}
