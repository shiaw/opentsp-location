
package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCDriverInfoReportSave.DriverInfoReportSave;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCFlowInfoSave.FlowInfoSave;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCFlowInfoSave.FlowInfoSave.TerminalDataFlow;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.DriverLogin;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDriverInfoReport.DriverInfoReport;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.StatisticsStoreService;
import  com.navinfo.opentsp.platform.da.core.persistence.application.StatisticsStoreServiceimpl;
@DaRmiNo(id = "0986")
public class Mutual_0986_DriverInfoReportSave extends Dacommand {
    final static StatisticsStoreService statisticsService = new StatisticsStoreServiceimpl();
    @Override
    public Packet processor(Packet packet) {
        try {
            DriverInfoReportSave driverInfoReportSave = DriverInfoReportSave.parseFrom(packet.getContent());
            long terminalId = driverInfoReportSave.getTerminalId();
            DriverInfoReport info = driverInfoReportSave.getInfo();
            DriverLogin driverLogin=new DriverLogin();
            driverLogin.setDriverName(info.getName());
            driverLogin.setAgencyName(info.getOrganizationName());
            if(info.getCardStatus()){
                driverLogin.setCreditDate(info.getCardSwitchDate());
            }else{
                driverLogin.setStubbsCard(info.getCardSwitchDate());
            }
            driverLogin.setDriverIdCode(info.getCertificateCode());
            driverLogin.setTerminalId(terminalId);
            driverLogin.setCertificateValid(info.getLicenseValidDate()+"");
            driverLogin.setDriverTime(info.getCardSwitchDate());
            info.getResults();
            statisticsService.saveDriverLoginInfo(terminalId, driverLogin);
//		   _id:objectId,   //MongoDB自增ID（索引）
//		   terminalId:long,//终端标识号（索引）
//		   driverName:String,  //驾驶员姓名
//		   driverIdCode:String,//驾驶证编号
//		   driverCertificate:String,//行车资格证编号
//		   agencyName:String, //发证机构名称
//		   certificateValid:String,//证件有效期
//		   creditDate:long,    //刷卡时间（索引）
//		   stubbsCard:long,    //拔卡时间（索引）
//		   driverTime:long,    //登录时间

//		   cardStatus	true：插入；false：拔出；
//		   cardSwitchDate	插卡/拔卡时间
//		   results	IC卡读取结果
//		   name	驾驶员姓名
//		   certificateCode	从业资格证编码
//		   organizationName	从业资格发证机构名称
//		   licenseValidDate	证件有效期，精确到天

            super.commonResponsesForPlatform(packet.getFrom(),
                    packet.getSerialNumber(), packet.getCommand(),
                    PlatformResponseResult.success);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws ParseException {
        String time="2014-09-05 08:01:00";
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=sf.parse(time);
        System.out.println(date);
        System.out.println(date.getTime()/1000);
        FlowInfoSave.Builder flowInfo =FlowInfoSave.newBuilder();
        TerminalDataFlow.Builder terminalDataFlow=TerminalDataFlow.newBuilder();
        terminalDataFlow.setDownFlow(231);
        terminalDataFlow.setUpFlow(342);
        terminalDataFlow.setTerminalId(2014002);
        terminalDataFlow.setFlowDate(date.getTime()/1000);
        flowInfo.addFlows(terminalDataFlow);
        List<TerminalDataFlow> flowsList = flowInfo.getFlowsList();
        System.out.println(date.getTime()/1000);
        for(TerminalDataFlow flow:flowsList){
            statisticsService.saveTerminalWFlowInfo(flow.getTerminalId(), (int)flow.getFlowDate(),(float) flow.getUpFlow(), (float) flow.getDownFlow());
        }


    }
}
