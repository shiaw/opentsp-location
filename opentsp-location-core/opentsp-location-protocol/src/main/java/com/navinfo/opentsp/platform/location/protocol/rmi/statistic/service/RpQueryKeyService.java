package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCCrossGridCounts.CrossGridCounts;

import java.util.List;
import java.util.Map;


public interface RpQueryKeyService  {

  Object getQueryKeyData(String queryKey);
  void saveQueryKeyData(String queryKey, Object value) ;
  void removeQueryKeyData(String queryKey) ;
  Map<String, Object> findQueryKeyDatas() ;
  public CrossGridCounts getGridCrossCounts(List<Long> terminalIds, List<Long> tileId,
                                                              long startDate, long endDate, long accessTocken) ;
}
