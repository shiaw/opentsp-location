package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/*************************
 * 报警查询的通用参数对象定义. 
 * 
 * @author claus
 *
 */
public class CommonParameter implements Serializable{
	
	//
	private static final long serialVersionUID = 7740493224319398539L;
	
    //客户端请求鉴权码
	long accessTocken;
	//是否分页显示
	boolean isMultipage;
	//分页大小
	int pageSize;
	//开始
	int pageIndex;
	/**
	 * 查询唯一标识（位置云内部使用）
	 */
	String	queryKey;
	/**
	 * 动态分配节点编码（位置云内部使用）
	 */
	long code;
	
	boolean sort; //时间排序：true：升序；false：降序
	
	boolean sortTerminal;  //是否按查询的终端序，true：是；false：否；

	//扩展参数
	Map<String, Object> extParameters;
	
	public CommonParameter(){}
	
	/**********************
	 * Default constructor 
	 * 
	 * @param _tocken
	 * @param multiPage
	 * @param psize
	 * @param pindex
	 */
	public CommonParameter(long _tocken, boolean multiPage, int psize, int pindex){
		accessTocken = _tocken;
		isMultipage = multiPage;
		pageSize = psize;
		pageIndex = pindex;
	}


	public Map<String, Object> getExtParameters() {
		if(extParameters == null) extParameters = new HashMap<String, Object>();
		return extParameters;
	}

	public void setExtParameters(Map<String, Object> extParameters) {
		this.extParameters = extParameters;
	}

	public long getAccessTocken() {
		return accessTocken;
	}


	public void setAccessTocken(long accessTocken) {
		this.accessTocken = accessTocken;
	}


	public boolean isMultipage() {
		return isMultipage;
	}


	public void setMultipage(boolean isMultipage) {
		this.isMultipage = isMultipage;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getPageIndex() {
		return pageIndex;
	}


	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	public boolean isSortTerminal() {
		return sortTerminal;
	}

	public void setSortTerminal(boolean sortTerminal) {
		this.sortTerminal = sortTerminal;
	}
	
}
