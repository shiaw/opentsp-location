package com.navinfo.tasktracker.rprest.entity;// default package

/**
 * ImDialog entity. @author MyEclipse Persistence Tools
 */

public class AreaVehiclesEntity implements java.io.Serializable
{
	// Fields

	private Long tile_Id;
	private int district_Id;
	private int parent_District_Id;

	public AreaVehiclesEntity()
	{
	}
	public Long getTile_Id() {
		return tile_Id;
	}

	public void setTile_Id(Long tile_Id) {
		this.tile_Id = tile_Id;
	}
	public int getDistrict_Id() {
		return district_Id;
	}

	public void setDistrict_Id(int district_Id) {
		this.district_Id = district_Id;
	}
	public int getParent_District_Id() {
		return parent_District_Id;
	}

	public void setParent_District_Id(int parent_District_Id) {
		this.parent_District_Id = parent_District_Id;
	}
}