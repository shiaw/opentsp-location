package com.navinfo.opentsp.platform.dp.core.cache.entity;

import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType.DictType;

/**
 * 字典实体
 * 
 * @author lgw
 * 
 */
public class DictEntity extends Entity {
	private static final long serialVersionUID = 1L;
	private int dataCode;
	private int gbCode;
	private DictType dictType;
	private String name;
	private int parentDataCode;
	private String dictValue;

	public DictEntity() {
		super();
	}

	public int getDataCode() {
		return dataCode;
	}

	public void setDataCode(int dataCode) {
		this.dataCode = dataCode;
	}

	public int getGbCode() {
		return gbCode;
	}

	public void setGbCode(int gbCode) {
		this.gbCode = gbCode;
	}

	public DictType getDictType() {
		return dictType;
	}

	public void setDictType(DictType dictType) {
		this.dictType = dictType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentDataCode() {
		return parentDataCode;
	}

	public void setParentDataCode(int parentDataCode) {
		this.parentDataCode = parentDataCode;
	}
	
	public String getDictValue() {
		return dictValue;
	}
	
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

}
