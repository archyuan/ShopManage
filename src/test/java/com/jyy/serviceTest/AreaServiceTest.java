package com.jyy.serviceTest;

import com.jyy.daoTest.BaseTest;
import com.jyy.entity.Area;
import com.jyy.service.AreaService;
import com.jyy.service.Impl.AreaServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAREAlIST(){
        List<Area> areaList = areaService.getAreaList();
        assertEquals("東街",areaList.get(0).getAreaName());
    }
}
