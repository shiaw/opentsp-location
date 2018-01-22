package com.navinfo.opentsp.gateway.tcp.proto.location.mapper;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilType;
import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TerminalMileageOilTypeMapper {
    int deleteByPrimaryKey(Long terminalId);

    int insert(TerminalMileageOilType record);

    int insertSelective(TerminalMileageOilType record);

    TerminalMileageOilType selectByPrimaryKey(Long terminalId);

    int updateByPrimaryKeySelective(TerminalMileageOilType record);

    int updateByPrimaryKey(TerminalMileageOilType record);

    List<TerminalMileageOilType> getMileageOil(Long minTime);

}