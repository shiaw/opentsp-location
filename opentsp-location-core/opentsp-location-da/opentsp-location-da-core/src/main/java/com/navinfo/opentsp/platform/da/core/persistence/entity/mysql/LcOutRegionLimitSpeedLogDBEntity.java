package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * 信息播报日志实体
 */
@LCTable(name="LC_OUTREGION_LIMIT_SPEED_LOG")
public class LcOutRegionLimitSpeedLogDBEntity implements java.io.Serializable {

	@LCTransient
	@LCPrimaryKey
	private int id;
	/**
	 * 终端标识
	 */
	private long terminal_id;
	/**
	 * 区域标识（上层业务系统提供唯一标识）
	 */
	private long original_area_id;
	/**
	 * 限速时间
	 */
	private int limit_date;
	/**
	 * 限速值
	 */
	private int limit_speed;
	/**
	 * 标识，1：限速；0：解除限速
	 */
	private int sign;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(long terminal_id) {
		this.terminal_id = terminal_id;
	}
	public long getOriginal_area_id() {
		return original_area_id;
	}
	public void setOriginal_area_id(long original_area_id) {
		this.original_area_id = original_area_id;
	}
	public int getLimit_date() {
		return limit_date;
	}
	public void setLimit_date(int limit_date) {
		this.limit_date = limit_date;
	}
	public int getLimit_speed() {
		return limit_speed;
	}
	public void setLimit_speed(int limit_speed) {
		this.limit_speed = limit_speed;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "LcOutRegionLimitSpeedLogDBEntity [id=" + id + ", terminal_id="
				+ terminal_id + ", original_area_id=" + original_area_id
				+ ", limit_date=" + limit_date + ", limit_speed=" + limit_speed
				+ ", sign=" + sign + "]";
	}

}