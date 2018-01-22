package com.navinfo.opentsp.platform.da.core.persistence.entity.extend;

import java.io.Serializable;

public class DictData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1318224875978383293L;

	private Integer dataCode;
	private Integer gbCode;
	private Integer dictType;
	private String name;
	private Integer parentDataCode;
	private String dictValue;

	public Integer getDataCode() {
		return dataCode;
	}

	public void setDataCode(Integer dataCode) {
		this.dataCode = dataCode;
	}

	public Integer getGbCode() {
		return gbCode;
	}

	public void setGbCode(Integer gbCode) {
		this.gbCode = gbCode;
	}

	public Integer getDictType() {
		return dictType;
	}

	public void setDictType(Integer dictType) {
		this.dictType = dictType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentDataCode() {
		return parentDataCode;
	}

	public void setParentDataCode(Integer parentDataCode) {
		this.parentDataCode = parentDataCode;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	@Override
	public String toString() {
		return "DictData [dataCode=" + dataCode + ", gbCode=" + gbCode
				+ ", dictType=" + dictType + ", name=" + name
				+ ", parentDataCode=" + parentDataCode + ", dictValue="
				+ dictValue + "]";
	}

}
