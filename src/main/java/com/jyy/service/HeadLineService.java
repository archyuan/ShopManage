package com.jyy.service;

import com.jyy.entity.HeadLine;

import java.io.IOException;
import java.util.List;

public interface HeadLineService {

    public static final String HLLISTKEY = "headlinelist";

    List<HeadLine> getHeadLineList(
            HeadLine headLineCondition
    )throws IOException;

}
