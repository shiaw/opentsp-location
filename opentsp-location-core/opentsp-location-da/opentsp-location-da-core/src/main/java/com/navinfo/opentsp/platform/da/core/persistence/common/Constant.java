package com.navinfo.opentsp.platform.da.core.persistence.common;

/**
 * @author ss
 * 
 */
public class Constant {
	/**
	 * 新增
	 */
	public static final int FLAG_INT_INSERT = 0x0001;
	/**
	 * 删除
	 */
	public static final int FLAG_INT_DELETE = 0x0002;
	/**
	 * 修改
	 */
	public static final int FLAG_INT_UPDATE = 0x0003;
	/**
	 * 查询
	 */
	public static final int FLAG_INT_SELECT = 0x0004;
	/**
	 * 成功
	 */
	public static final int FLAG_INT_SUCCESS = 0x0005;
	/**
	 * 失败
	 */
	public static final int FLAG_INT_FAIL = 0x0006;
	/**
	 * 账号停用
	 */
	public static final int FLAG_INT_DISABLE = 0x0007;
	/**
	 * 成功返回值
	 */
	public static final String CONTENT_STRING_SUCCESS = "成功";
	/**
	 * 失败返回值
	 */
	public static final String CONTENT_STRING_FAIL = "失败";
	/**
	 * 参数为空
	 */
	public static final String PARM_STRING_NULL = "参数为空";
	/**
	 * redis字典值
	 */
	public static final String REDIS_PARM_DICT = "dict";
	public static final String REDIS_PARM_DICTTYPE = "dictType";
	public static final String REDIS_PARM_TERMINAL = "terminalInfo";
	public static final String REDIS_PARM_LOCATION = "location";
	public static final String REDIS_PARM_SERVICEMARK = "serviceMark";
	public static final String REDIS_PARM_LOCATIONCACHE = "locationCache";
	
	/**
	 * 
	 */
	public static final long TASK_REDISTOMONGDB_DELAY = 24 * 60 * 60 * 1000;
	public static final long TASK_SERVICEMARK_DELAY = 5 * 60 * 1000;
	
}
