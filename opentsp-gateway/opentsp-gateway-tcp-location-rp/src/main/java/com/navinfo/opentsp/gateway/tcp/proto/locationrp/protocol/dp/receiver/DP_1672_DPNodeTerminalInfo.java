package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * DP节点推送终端信息 kafka
 */
public class DP_1672_DPNodeTerminalInfo extends DPCommand {

    public Packet processor(Packet packet) {
        /*try {
            DPNodeTerminalInfo nodeTerminalInfo = DPNodeTerminalInfo.parseFrom(packet.getContent());
			List<TerminalRPHashInfo> hashInfos = nodeTerminalInfo.getRpHashInfoList();
			if(hashInfos != null && hashInfos.size() > 0){
				for (TerminalRPHashInfo terminalRPHashInfo : hashInfos) {
					long terminalId = terminalRPHashInfo.getInfo().getTerminalId();
					//检测当前终端的hash是否在当前RP节点
					//是?查询终端列表是否有此终端.存在?更新其DP节点;不存在?加入终端列表.
					//不是?查询终端列表是否存在终端.存在?从列表中删除,并更新订阅列表/终端所在DP节点列表;不存在?不处理.
					if(terminalRPHashInfo.getRpNodeCode() == NodeHelper.getNodeCode()){
						//此处需要关注一个问题：如果在TA、DP节点收到MM节点通知，RP节点还未收到新增通知的情况下，终端上传数据，则可能会被抛弃
						TerminalInfo terminal = TerminalManage.getInstance().getTerminal(terminalId);
						TerminalManage.getInstance().updateTerminalDpNode(terminalId, packet.getFrom());
					}else{
						TerminalInfo terminal = TerminalManage.getInstance().getTerminal(terminalId);
						if(terminal != null){
							TerminalManage.getInstance().deleteTerminal(terminalId , packet.getFrom());
						}
						logger.error("终端列表没有此终端");
					}
				}
			}else{
				logger.error("1672:终端哈希信息集合List<TerminalRPHashInfo>  is null");
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}*/
        return null;
    }

    @Override
    public void handle(KafkaCommand kafkaCommand) {

    }
}
