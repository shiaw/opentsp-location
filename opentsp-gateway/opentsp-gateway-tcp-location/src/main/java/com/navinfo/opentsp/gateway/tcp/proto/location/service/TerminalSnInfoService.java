package com.navinfo.opentsp.gateway.tcp.proto.location.service;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalSnInfo;

import java.util.List;

/**
 * Created by 修伟 on 2017/9/25 0025.
 */
public interface TerminalSnInfoService {

    List<TerminalSnInfo> getAll();

    int insert(TerminalSnInfo record);

    int updateByPrimaryKey(TerminalSnInfo record);
}
