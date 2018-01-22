package com.navinfo.opentsp.platform.location.kit;

import java.io.Serializable;

import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;

public class Block implements Serializable {
	private static final long serialVersionUID = 1L;
	private int commandId;
	private int total;
	private Packet[] packets;
	private long createTime;

	public Block(int total) {
		this.packets = new Packet[total];
		this.total = total;
		this.createTime = System.currentTimeMillis() / 1000;
	}

	public void addBlock(int serial, Packet packet) {
		this.packets[serial - 1] = packet;
	}

	/**
	 * 包是否接收完成
	 * 
	 * @return
	 */
	public boolean isComplete() {
		for (Packet p : packets) {
			if (p == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 转换成完整包
	 * 
	 * @return
	 */
	public Packet toCompletePacket() {
		Packet _out_packet = this.packets[0];
		byte[] bytes = null;
		for (Packet p : this.packets) {
			bytes = ArraysUtils.arraycopy(bytes, p.getContent());
		}
		_out_packet.setContent(bytes);
		return _out_packet;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Packet[] getPackets() {
		return packets;
	}

	public void setPackets(Packet[] packets) {
		this.packets = packets;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
