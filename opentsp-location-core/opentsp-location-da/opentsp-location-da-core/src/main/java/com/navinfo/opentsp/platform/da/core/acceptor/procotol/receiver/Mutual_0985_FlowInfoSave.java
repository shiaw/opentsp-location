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
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCFlowInfoSave.FlowInfoSave;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCFlowInfoSave.FlowInfoSave.TerminalDataFlow;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.StatisticsStoreService;
import  com.navinfo.opentsp.platform.da.core.persistence.application.StatisticsStoreServiceimpl;

/**
 * 终端流量存储
 * 消息回复平台通用应答（0x1100）。
 * @author admin
 *
 */
@DaRmiNo(id = "0985")
public class Mutual_0985_FlowInfoSave extends Dacommand {
    final static StatisticsStoreService statisticsService = new StatisticsStoreServiceimpl();
    @Override
    public Packet processor(Packet packet) {
        try {
            //终端流量
            FlowInfoSave flowInfo = FlowInfoSave.parseFrom(packet.getContent());
            List<TerminalDataFlow> flowsList = flowInfo.getFlowsList();
            for(TerminalDataFlow flow:flowsList){
                //添加数据到缓存
                statisticsService.saveTerminalWFlowInfo(flow.getTerminalId(), flow.getFlowDate(),(float) flow.getUpFlow(), (float) flow.getDownFlow());
            }
            //消息回复平台通用应答
            super.commonResponsesForPlatform(packet.getFrom(),
                    packet.getSerialNumber(), packet.getCommand(),
                    PlatformResponseResult.success);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 测试
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        String time="2014-10-25 02:01:00";
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=sf.parse(time);
        System.out.println(date);
        System.out.println(date.getTime()/1000);
        FlowInfoSave.Builder flowInfo =FlowInfoSave.newBuilder();
        TerminalDataFlow.Builder terminalDataFlow=TerminalDataFlow.newBuilder();
        terminalDataFlow.setDownFlow(231);
        terminalDataFlow.setUpFlow(342);
        terminalDataFlow.setTerminalId(16800102841l);
        terminalDataFlow.setFlowDate(date.getTime()/1000);
        flowInfo.addFlows(terminalDataFlow);
        List<TerminalDataFlow> flowsList = flowInfo.getFlowsList();
        System.out.println(date.getTime()/1000);
        for(TerminalDataFlow flow:flowsList){
            statisticsService.saveTerminalWFlowInfo(flow.getTerminalId(), (int)flow.getFlowDate(),(float) flow.getUpFlow(), (float) flow.getDownFlow());
        }


    }
}
