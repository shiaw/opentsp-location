package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/******************
 * 
 * 
 * @author claus
 *
 */
public class OptResult implements Serializable{
	//
	private int status;
	//
	private Map<String, Object> map;
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	
	/**
	 * 添加一个自定义参数
	 * @param pKey {@link String} Key
	 * @param pValue {@link Object} Value
	 * @return {@link Packet}
	 */
	public Map<String, Object> addParameter(String pKey , Object pValue){
		if(this.map == null)
			this.map = new ConcurrentHashMap<String, Object>();
		this.map.put(pKey, pValue);
		return this.map;
	}
	
	/**
	 * 返回{@link Object}类型
	 * @param pKey
	 * @return
	 */
	public Object getParamter(String pKey){
		if(this.map != null)
			return this.map.get(pKey);
		return null;
	}
	/**
	 * 返回{@link String}类型
	 * @param pKey
	 * @return
	 */
	public String getParamterForString(String pKey){
		if(this.map != null){
			Object value = this.map.get(pKey);
			return String.valueOf(value);
		}
		return null;
	}
	/**
	 * 返回{@link Integer}类型
	 * @param pKey
	 * @return
	 */
	public int getParamterForInt(String pKey){
		if(this.map != null){
			Object value = this.map.get(pKey);
			return Integer.parseInt(String.valueOf(value));
		}
		return 0;
	}
	
	/**
	 * 返回{@link Long}类型
	 * @param pKey
	 * @return
	 */
	public long getParamterForLong(String pKey){
		if(this.map != null){
			Object value = this.map.get(pKey);
			return Long.parseLong(String.valueOf(value));
		}
		return 0;
	}
}
