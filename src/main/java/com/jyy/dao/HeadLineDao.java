package com.jyy.dao;

import com.jyy.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {

    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);

}
