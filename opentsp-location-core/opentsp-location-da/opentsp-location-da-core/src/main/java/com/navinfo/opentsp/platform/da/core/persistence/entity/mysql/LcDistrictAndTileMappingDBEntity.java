package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * DistrictAndTileMapping entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_DISTRICT_AND_TILE_MAPPING")
public class LcDistrictAndTileMappingDBEntity implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	@LCTransient
	@LCPrimaryKey
	private Long  tile_id;//瓦片ID，格式：ZZXXXXXYYYYY，Z为zoom，X为瓦片X轴，Y为瓦片Y轴
	private Integer district_id;//行政区域编码（当前为省级，父级行政区域为0）
	private Integer parent_district_id;//父级行政区域编码
	public LcDistrictAndTileMappingDBEntity() {
	}
	public Long getTile_id() {
		return tile_id;
	}
	public void setTile_id(Long tile_id) {
		this.tile_id = tile_id;
	}
	public Integer getDistrict_id() {
		return district_id;
	}
	public void setDistrict_id(Integer district_id) {
		this.district_id = district_id;
	}
	public Integer getParent_district_id() {
		return parent_district_id;
	}
	public void setParent_district_id(Integer parent_district_id) {
		this.parent_district_id = parent_district_id;
	}
	@Override
	public String toString() {
		return "LcDistrictAndTileMappingDBEntity [tile_id=" + tile_id
				+ ", district_id=" + district_id + ", parent_district_id="
				+ parent_district_id + "]";
	}
}