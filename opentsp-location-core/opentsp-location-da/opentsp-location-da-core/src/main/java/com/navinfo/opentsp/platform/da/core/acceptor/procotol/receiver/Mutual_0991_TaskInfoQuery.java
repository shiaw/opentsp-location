package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskInfo.TaskInfo;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQueryRes.TaskInfoQueryRes;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TaskInfoManagerImpl;
@DaRmiNo(id = "0991")
public class Mutual_0991_TaskInfoQuery extends Dacommand {

	@Override
	public Packet processor(Packet packet) {
		TaskInfoManagerImpl service=new TaskInfoManagerImpl();
		try {
			TaskInfoQuery query = TaskInfoQuery.parseFrom(packet.getContent());
			TaskType types = query.getTypes();
			List<Long> nodeCodeList = query.getNodeCodeList();
			long[] nodes=new long[nodeCodeList.size()];
			for(int i=0;i<nodeCodeList.size();i++){
				nodes[i]=nodeCodeList.get(i);
			}
			//查询节点编号对应的终端ID
			Map<String,List<Long>> queryTaskInfo = service.queryTaskInfo(types.getNumber(), nodes);
			//构建返回对象
			TaskInfoQueryRes.Builder res = TaskInfoQueryRes.newBuilder();
			res.setSerialNumber(packet.getSerialNumber());
			res.setResults(PlatformResponseResult.success);
			Set<Entry<String, List<Long>>> entrySet = queryTaskInfo.entrySet();
			//遍历节点-终端s
			for(Entry<String, List<Long>> entry : entrySet){
				TaskInfo.Builder taskInfo = TaskInfo.newBuilder();
				taskInfo.setNodeCode(Long.parseLong(entry.getKey()));
				List<Long> tids = entry.getValue();
				for(Long tid : tids){
					taskInfo.addTerminalId(tid);
				}
				res.addInfos(taskInfo);
			}
			Packet _out_packet = new Packet(true);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.TaskInfoQueryRes_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setTo(packet.getFrom());
			_out_packet.setFrom(NodeHelper.getNodeCode());
			_out_packet.setContent(res.build().toByteArray());
		} catch (InvalidProtocolBufferException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

}
