package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.online.TerminalInfo;
import com.navinfo.opentsp.platform.push.persisted.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/10
 * @modify
 * @copyright opentsp
 */
@Component
public class TerminalServiceImpl {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalServiceImpl.class);

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TerminalRegisterRepository terminalRegisterRepository;

    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    @Autowired
    private TerminalOperationLogRepository operationLogRepository;

    @Autowired
    private TerminalMileageOilTypeRepository terminalMileageOilTypeRepository;

    public void loadTerminal() {

        List<Object[]> terminalEntries = terminalRepository.findTerminalInfos();
        for (Object[] terminalEntry : terminalEntries) {
            TerminalInfo terminalInfo = new TerminalInfo();
            if(StringUtils.isEmpty(terminalEntry[10])){
                terminalInfo.setAuth(false);
            }else{
                terminalInfo.setAuth(true);
            }
            terminalInfo.setDeviceId(terminalEntry[7]+"");
            terminalInfo.setTerminalId("0"+terminalEntry[0]);
            //  terminalInfo.setChangeTid(terminalEntry.getChangeTid());
            terminalInfo.setAuthCode(terminalEntry[10]+"");
            terminalInfo.setLastTime(Long.valueOf(terminalEntry[4]+""));
            terminalInfo.setProtocolType((int)terminalEntry[1]);
            redisTerminalInfoStorage.register(terminalInfo);
        }
        LOG.info("终端数据加载完成！，共加载{}条终端数据", terminalEntries.size());
    }

    public void refreshTerminalById(String terminalId) {

        TerminalEntry terminalEntry = terminalRepository.findTerminalInfoById(terminalId);
         if(terminalEntry==null){
             LOG.info("未找到相应的终端信息！");
             return;
         }
            TerminalInfo terminalInfo = new TerminalInfo();
            if(StringUtils.isEmpty(terminalEntry.getAuthCode())){
                terminalInfo.setAuth(false);
            }else{
                terminalInfo.setAuth(true);
            }
            terminalInfo.setDeviceId(terminalEntry.getDeviceId());
            terminalInfo.setTerminalId("0"+terminalEntry.getTerminalId());
            //  terminalInfo.setChangeTid(terminalEntry.getChangeTid());
            terminalInfo.setAuthCode(terminalEntry.getAuthCode());
            terminalInfo.setLastTime(terminalEntry.getCreateTime());
            terminalInfo.setProtocolType(terminalEntry.getProtoCode());
            redisTerminalInfoStorage.register(terminalInfo);
        LOG.info("终端{}，缓存刷新成功！", terminalId);
    }
    /**
     * 终端注册
     * 0：成功；
     * 1：车辆已被注册；
     * 2：数据库中无该车辆；
     * 3：终端已被注册；
     * 4：数据库中无该终端
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public String register(int province,
                         int city,
                         String produce,
                         String terminalModel,
                         String deviceId,
                         String license,
                         String terminalId,
                         int licenseColor) {
        String authCode=this.authCode(terminalId);
        TerminalInfo  terminalInfo=null;
       TerminalEntry terminalEntry= terminalRepository.findTerminalInfoById(terminalId);
       if(terminalEntry==null){
           LOG.info("未查询到终端信息，请先添加终端信息");
           return null;
       }

        TerminalRegisterEntry  terminalRegisterEntry= terminalRegisterRepository.findByTerminalId(terminalId);
        if(terminalRegisterEntry!=null){
            LOG.info("{}终端已经注册", terminalId);
            terminalInfo=redisTerminalInfoStorage.get(terminalId);
            if(terminalInfo!=null) {
                terminalInfo.setAuthCode(terminalRegisterEntry.getAuthCode());
                redisTerminalInfoStorage.register(terminalInfo);
            }
            return terminalRegisterEntry.getAuthCode();
        }
        terminalInfo=new TerminalInfo();
        terminalInfo.setTerminalId(terminalId);
        terminalInfo.setDeviceId(deviceId);
        terminalInfo.setAuthCode(authCode);
        terminalInfo.setProtocolType(terminalEntry.getProtoCode());
        terminalInfo.setAuth(false);
        redisTerminalInfoStorage.register(terminalInfo);
        terminalRegisterEntry=new TerminalRegisterEntry();
        terminalRegisterEntry.setTerminalId(terminalId);
        terminalRegisterEntry.setAuthCode(authCode);
        terminalRegisterEntry.setCity(city);
        terminalRegisterEntry.setLicense(license);
        terminalRegisterEntry.setLicenseColor(licenseColor);
        terminalRegisterEntry.setProduct(produce);
        terminalRegisterEntry.setProvince(province);
        terminalRegisterEntry.setTerminalType(terminalModel);
        terminalRegisterRepository.save(terminalRegisterEntry);
        return authCode;
    }

    static String authCode(String terminalId) {
        String time = DateFormatUtils.format(new Date(), "yyyyMMdd");
        return time + ""+ terminalId;//测试先注释掉
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void saveOperationLog( TerminalOperationLogEntry operationLogEntry){
        TerminalOperationLogEntry entry= operationLogRepository.findByOperationId(operationLogEntry.getOperationId());
        if(entry==null){
            operationLogRepository.save(operationLogEntry);
        }else {
            operationLogRepository.updateOperatorResult(operationLogEntry.getOperationId(),operationLogEntry.getOperatorResult());
        }
    }

    /**
     * 查询终端里程油耗类型
     * @param terminalId
     * @return
     */
    public TerminalMileageOilTypeEntry  getTerminalMileageOilType(long terminalId){
        return terminalMileageOilTypeRepository.findTerminalMileageOilById(terminalId);
    }
}
