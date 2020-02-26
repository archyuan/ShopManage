package com.jyy.daoTest;

import com.jyy.dao.HeadLineDao;
import com.jyy.entity.HeadLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class HeadLineDaoTest extends BaseTest {

    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQuery(){
        List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
        assertEquals(1,headLineList.size());
        System.out.println(headLineList.get(0).getLineName());
    }
}
