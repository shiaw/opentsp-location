package com.navinfo.opentsp.platform.da.core.common;


/*************************
 * 报警查询的通用参数对象定义.
 *
 * @author claus
 *
 */
public class CommonParameter {

    //客户端请求鉴权码
	long accessTocken;
	//是否分页显示
	boolean isMultipage;
	//分页大小
	int pageSize;
	//开始
	int pageIndex;
	
	
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
}
