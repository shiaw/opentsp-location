package com.navinfo.opentsp.platform.da.core.webService.service.impl.center;

import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.webService.manage.WebServiceTools;
import com.navinfo.opentsp.platform.da.core.webService.service.LogWebService;
import com.navinfo.opentsp.platform.da.core.webService.service.TerminalWebService;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "com.navinfo.opentsp.platform.da.core.webService.service.LogWebService")
public class CenterLogWebServiceImpl implements LogWebService {

    private TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();
    @Override
    public List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLogList(
            long terminalId, int switchType, long start, long end) {
        LcTerminalInfoDBEntity terminal = terminalInfoManage
                .queryTerminalInfo(terminalId);
        if (terminal != null) {
            try {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                List<LcTerminalSwitchLogDBEntity> switchLogs = new ArrayList<LcTerminalSwitchLogDBEntity>();
                switchLogs = logWebService.queryTerminalSwitchLogList(
                        terminalId, switchType, start, end);
                return switchLogs;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(
            long terminalId, int ruleCode, long start, long end) {
        try {
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                List<LcTerminalRuleLogDBEntity> terminalRuleLogs = new ArrayList<LcTerminalRuleLogDBEntity>();
                terminalRuleLogs = logWebService.queryTerminalRuleLogList(
                        terminalId, ruleCode, start, end);
                return terminalRuleLogs;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            MySqlConnPoolUtil.close();
        }
    }

    @Override
    public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(
            long terminalId, int originalAreaId, long start, long end) {
        try {
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                List<LcTerminalAreaLogDBEntity> terminalAreaLogs = new ArrayList<LcTerminalAreaLogDBEntity>();
                terminalAreaLogs = logWebService.queryTerminalAreaLogList(
                        terminalId, originalAreaId, start, end);
                return terminalAreaLogs;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(
            long terminalId, int taId) {
        try {
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                List<LcTerminalAreaDataLogDBEntity> terminalAreaDataLogs = new ArrayList<LcTerminalAreaDataLogDBEntity>();
                terminalAreaDataLogs = logWebService.queryTerminalAreaDataList(
                        terminalId, taId);
                return terminalAreaDataLogs;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int queryServiceLogCount(long start, long end,
                                    int logDistrict, String logIp, int typeCode) {
        try {
            MySqlConnPoolUtil.startTransaction();
            //调用分区的ws
            LogWebService logWebService = (LogWebService) WebServiceTools
                    .getOperationClass(String.valueOf(logDistrict),
                            LogWebService.class);
            return logWebService.queryServiceLogCount(start, end,
                    logDistrict, logIp, typeCode);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public List<LcServiceLogDBEntity> queryServiceLogList(long start, long end,
                                                          int logDistrict, String logIp, int typeCode,int currentPage,int pageSize) {
        try {
            MySqlConnPoolUtil.startTransaction();
            //调用分区的ws
            LogWebService logWebService = (LogWebService) WebServiceTools
                    .getOperationClass(String.valueOf(logDistrict),
                            LogWebService.class);
            return logWebService.queryServiceLogList(start, end,
                    logDistrict, logIp, typeCode,currentPage,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int queryNodeLogCount(int nodeCode, long start,
                                 long end, int logDistrict) {
        try {
            MySqlConnPoolUtil.startTransaction();
            LogWebService logWebService = (LogWebService) WebServiceTools
                    .getOperationClass(String.valueOf(logDistrict),
                            LogWebService.class);
            return logWebService.queryNodeLogCount(nodeCode, start, end,
                    logDistrict);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public List<LcNodeLogDBEntity> queryNodeLogList(int nodeCode, long start,
                                                    long end, int logDistrict,int currentPage,int pageSize) {
        try {
            MySqlConnPoolUtil.startTransaction();
            LogWebService logWebService = (LogWebService) WebServiceTools
                    .getOperationClass(String.valueOf(logDistrict),
                            LogWebService.class);
            return logWebService.queryNodeLogList(nodeCode, start, end,
                    logDistrict,currentPage,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(
            long terminalId, int operatinoType, long start, long end,int currentPage,int pageSize) {
        try {
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                List<LcTerminalOperationLogDBEntity> operationLogs = new ArrayList<LcTerminalOperationLogDBEntity>();
                operationLogs = logWebService.queryTerminalOperationLogList(terminalId, operatinoType, start, end,currentPage,pageSize);
                return operationLogs;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LcTerminalRuleLogDBEntity queryTerminalRuleLogById(int ruleId,
                                                              long terminalId) {
        try {
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                TerminalWebService.class);
                return logWebService.queryTerminalRuleLogById(ruleId, terminalId);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LcTerminalOperationLogDBEntity queryTerminalOperationLogById(
            int tol_id,long terminalId) {
        LcTerminalInfoDBEntity terminal = terminalInfoManage
                .queryTerminalInfo(terminalId);
        if (terminal != null) {
            LogWebService logWebService = (LogWebService) WebServiceTools
                    .getOperationClass(terminal.getDistrict().toString(),
                            TerminalWebService.class);
            return logWebService.queryTerminalOperationLogById(tol_id, terminalId);
        } else {
            return null;
        }

    }

    @Override
    public List<LcMessageBroadcastLogDBEntity> queryMessageBroadcastLogList(
            long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
        try {
            MySqlConnPoolUtil.startTransaction();
            //调用分区的ws
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                return logWebService.queryMessageBroadcastLogList(terminalId, areaId,
                        start, end,currentPage,pageSize);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LcOutRegionLimitSpeedLogDBEntity> queryOutRegionLimitSpeedLogList(
            long terminalId, int areaId,long start, long end,int currentPage,int pageSize) {
        try {
            MySqlConnPoolUtil.startTransaction();
            //调用分区的ws
            LcTerminalInfoDBEntity terminal = terminalInfoManage
                    .queryTerminalInfo(terminalId);
            if (terminal != null) {
                LogWebService logWebService = (LogWebService) WebServiceTools
                        .getOperationClass(terminal.getDistrict().toString(),
                                LogWebService.class);
                return logWebService.queryOutRegionLimitSpeedLogList(terminalId, areaId,
                        start, end,currentPage,pageSize);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
