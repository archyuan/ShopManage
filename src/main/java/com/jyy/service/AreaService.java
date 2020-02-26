package com.jyy.service;

import com.jyy.entity.Area;

import java.util.List;

public interface AreaService {

    public static final String AREALISTKEY = "arealist";

    List<Area> getAreaList();
}
