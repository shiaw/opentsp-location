package com.navinfo.opentsp.platform.da.core.persistence.common;


/**
 * 获取entity的注解
 *
 * @author ss
 *
 */
public class EntityAnnotation {

	private static EntityAnnotation entityAnnotation=null;

	private EntityAnnotation(){

	}
	public static EntityAnnotation getInstance(){
		if(entityAnnotation==null){
			entityAnnotation=new EntityAnnotation();
		}
		return entityAnnotation;
	}
	/**
	 * @param o
	 *            {@link:T 实体类}
	 * @return Table {@link:Table 注解类}
	 */
	public LCTable getClassAnnotation(Object o){
		LCTable lcTable = o.getClass().getAnnotation(LCTable.class);
		return lcTable;
	}
}
