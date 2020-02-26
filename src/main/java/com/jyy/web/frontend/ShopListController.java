package com.jyy.web.frontend;

import com.jyy.dto.ShopExecution;
import com.jyy.entity.Area;
import com.jyy.entity.Shop;
import com.jyy.entity.ShopCategory;
import com.jyy.service.AreaService;
import com.jyy.service.ShopCategoryService;
import com.jyy.service.ShopService;
import com.jyy.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/frontend")
public class ShopListController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private ShopService shopService;

    //返回商店列表页里的Shopcategory列表，
    //以及区域信息
    @GetMapping("/listshopspageinfo")
    @ResponseBody
    public Map<String,Object> listShopPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();

        Long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList = null;
        if(parentId != -1){
            try{
                ShopCategory shopCategory = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategory.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategories(shopCategory);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else {
            try{
                shopCategoryList = shopCategoryService.getShopCategories(null);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try{
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
            return modelMap;
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }

        return modelMap;
    }


    @GetMapping("/listshops")
    @ResponseBody
    public Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");

        if((pageIndex > -1)&&(pageSize > -1)){
            long parentId= HttpServletRequestUtil.getLong(request,"parentId");
            long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            Shop shopCondition = compactShopCondition4Search(parentId,
                    shopCategoryId,
                    areaId,
                    shopName);
            ShopExecution se = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);

        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex");
        }
        return modelMap;
    }

    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if(parentId != -1L){
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if(shopCategoryId != -1L){
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId != -1L){
            Area area = new Area();
            area.setAreaId(Long.valueOf(areaId));
            shopCondition.setArea(area);
        }
        if(shopName != null){
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }


}
