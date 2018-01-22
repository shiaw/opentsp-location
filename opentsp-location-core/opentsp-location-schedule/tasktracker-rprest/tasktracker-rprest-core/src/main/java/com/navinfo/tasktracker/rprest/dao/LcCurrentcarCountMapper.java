package com.navinfo.tasktracker.rprest.dao;

import com.navinfo.tasktracker.rprest.domain.LcCurrentcarCount;

public interface LcCurrentcarCountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LcCurrentcarCount record);

    int insertSelective(LcCurrentcarCount record);

    LcCurrentcarCount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LcCurrentcarCount record);

    int updateByPrimaryKey(LcCurrentcarCount record);
}