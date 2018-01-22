package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.send;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.location.kit.servicemonitor.ServiceMonitor;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.LCHeartBeatToMM.HeartBeatToMM;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCCpuInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMemoryInfo;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMonitorCacheSize;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMonitorCacheSize.MonitorCacheSize;
import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCMonitorThreadStatus.MonitorThreadStatus;
import  com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import  com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.servicemonitor.mode.CpuInfo;
import com.navinfo.opentsp.platform.location.kit.servicemonitor.mode.Memory;

public class MM_1603_HeartBeatToMM extends MMCommand {

	@Override
	public int processor(Packet packet) {
		HeartBeatToMM.Builder builder = HeartBeatToMM.newBuilder();
		builder.setNodeCode(NodeHelper.getNodeCode());
		//cpu信息
		CpuInfo cpu = ServiceMonitor.getInstance().getCpu();
		LCCpuInfo.CpuInfo.Builder cpuBuilder = LCCpuInfo.CpuInfo.newBuilder();
		cpuBuilder.setServerCpu(this.retainPrecision(cpu.getTotalCpu().getUserPerc(), 2));
		cpuBuilder.setSystemCpu(this.retainPrecision(cpu.getTotalCpu().getSysPerc(), 2));
		cpuBuilder.setTotalCpu(this.retainPrecision(cpu.getTotalCpu().getCpuRatio(), 2));
		cpuBuilder.setIdleCpu(this.retainPrecision(cpu.getTotalCpu().getIdlePerc(), 2));
		builder.setCpuInfos(cpuBuilder);
		//内存信息
		Memory memory = ServiceMonitor.getInstance().getMemory();
		LCMemoryInfo.MemoryInfo.Builder memoryBuilder = LCMemoryInfo.MemoryInfo.newBuilder();
		memoryBuilder.setServerMemory(this.retainPrecision(memory.getJvmUsedMemory(), 2));
		memoryBuilder.setTotalMemory(this.retainPrecision(memory.getPhysicalTotalMemory(), 2));
		memoryBuilder.setIdleMemory(this.retainPrecision(memory.getPhysicalFreePhysicalMemory(), 2));
		builder.setMemoryInfos(memoryBuilder);
//		//缓存状态
//		builder.addAllCacheSize(this.getMonitorCacheSize());
//		//工作线程状态
//		builder.addAllThreadStatus(this.getMonitorThreadStatus());

		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setCommand(AllCommands.NodeCluster.HeartBeatToMM_VALUE);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setFrom(NodeHelper.getNodeCode());
		_out_packet.setContent(builder.build().toByteArray());
		super.broadcast(_out_packet);
		return 0;
	}
	//格式化
	private float retainPrecision(double d, int b) {
//		NumberFormat nf = NumberFormat.getNumberInstance();
//		nf.setMinimumFractionDigits(2);
//		nf.setMaximumFractionDigits(b);
//		return Float.parseFloat(nf.format(d));
		return Float.parseFloat(String.valueOf(d));
	}
	//缓存状态
	private List<MonitorCacheSize> getMonitorCacheSize(){
		List<MonitorCacheSize> result = new ArrayList<LCMonitorCacheSize.MonitorCacheSize>();
		return result;
	}
	private List<MonitorThreadStatus> getMonitorThreadStatus(){
		List<MonitorThreadStatus> result = new ArrayList<MonitorThreadStatus>();
		//Gps位置数据存储线程状态
//		MonitorThreadStatus.Builder ta_responseMechanism = MonitorThreadStatus.newBuilder();
//		ta_responseMechanism.setTypes(ThreadType.ta_responseMechanism);
//		TaskStatus ta_responseMechanism_status = ScheduleController.instance().getTaskStatus(TaskType.ResponseMechanismTask);
//		ta_responseMechanism.setStatus(ta_responseMechanism_status.ordinal() == TaskStatus.Normal.ordinal() ? ThreadStatus.threadNormal : ThreadStatus.threadException);
//		result.add(ta_responseMechanism.build());
		return result;
	}
	

}
