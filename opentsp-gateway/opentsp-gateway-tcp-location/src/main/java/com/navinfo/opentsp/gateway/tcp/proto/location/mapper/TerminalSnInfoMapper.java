package com.navinfo.opentsp.gateway.tcp.proto.location.mapper;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalSnInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TerminalSnInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TerminalSnInfo record);

    int insertSelective(TerminalSnInfo record);

    TerminalSnInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TerminalSnInfo record);

    int updateByPrimaryKey(TerminalSnInfo record);

    List<TerminalSnInfo> getAll();
}