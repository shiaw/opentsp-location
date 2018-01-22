package com.navinfo.tasktracker.rprest.dao;

import com.navinfo.tasktracker.rprest.domain.LcDistrictAndTileMapping;

import java.util.List;

public interface LcDistrictAndTileMappingMapper {
    int deleteByPrimaryKey(Long tileId);

    int insert(LcDistrictAndTileMapping record);

    int insertSelective(LcDistrictAndTileMapping record);

    LcDistrictAndTileMapping selectByPrimaryKey(Long tileId);

    List<LcDistrictAndTileMapping> selectAll();

    int updateByPrimaryKeySelective(LcDistrictAndTileMapping record);

    int updateByPrimaryKey(LcDistrictAndTileMapping record);
}