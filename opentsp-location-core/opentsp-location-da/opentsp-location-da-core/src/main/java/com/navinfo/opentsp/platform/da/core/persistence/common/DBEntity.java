package com.navinfo.opentsp.platform.da.core.persistence.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DBEntity implements Serializable {
	private String primaryKeyName;
	private int primaryKeyValue;
	private String tableName;
	private String[] fieldNames;
	private Object[] fieldValues;
	private int _field_number;
	private int _position;

	public DBEntity(int fieldNumber) {
		this._field_number = fieldNumber;
		this.fieldNames = new String[this._field_number];
		this.fieldValues = new Object[this._field_number];
	}

	public String fieldNamesToString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this._position; i++) {
			if (i == 0)
				builder.append(fieldNames[i]);
			else
				builder.append("," + fieldNames[i]);
		}
		return builder.toString();
	}
	public String[] fieldNames() {
		String[] values=new String[this._position];
		for (int i = 0; i < this._position; i++) {
			values[i]=this.fieldNames[i];
		}
		return values;
	}
	public Object[] fieldValues(){
		Object[] values = new Object[this._position];
		for (int i = 0; i < this._position; i++) {
			values[i]=this.fieldValues[i];
		}
		return values;
	}

	public void addField(String fieldName, Object fieldValue) {
		this.fieldNames[this._position] = fieldName;
		this.fieldValues[this._position] = fieldValue;
		this._position++;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public int getPrimaryKeyValue() {
		return primaryKeyValue;
	}

	public void setPrimaryKeyValue(int primaryKeyValue) {
		this.primaryKeyValue = primaryKeyValue;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public Object[] getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Object[] fieldValues) {
		this.fieldValues = fieldValues;
	}

}
