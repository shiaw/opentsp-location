package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcMultimediaParaDao;

import java.sql.SQLException;
import java.util.List;



public class LcMultimediaParaDaoImpl extends BaseDaoImpl<LcMultimediaParaDBEntity>
		implements LcMultimediaParaDao {

	@Override
	public List<LcMultimediaParaDBEntity> getMultimediaPara(long terminalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcMultimediaParaDBEntity> getMultimedia(long[] terminalIds,
														int multimediaType, long beginTime, long endTime,int currentPage,int pageSize) {
		try {
			LcMultimediaParaDBEntity lcMultimediaPara = new LcMultimediaParaDBEntity();
			// 获得实体类属性
			DBEntity entity = ObjectForMap.dbObjectForMap(lcMultimediaPara);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			builder.append(fields);
			builder.append(" from ");
			builder.append(tableName);
			Object[] obj=null;
			int flag=0;
			if(beginTime!=0&&endTime!=0){
				if(currentPage>0){
					obj=new Object[terminalIds.length+4];
				}else{
					obj=new Object[terminalIds.length+2];
				}
				builder.append(" where MEDIA_DATE BETWEEN ? and ? ");
				obj[0]=beginTime;
				obj[1]=endTime;
				flag=3;
			}
			else if(beginTime==0&&endTime!=0){
				if(currentPage>0){
					obj=new Object[terminalIds.length+3];
				}else{
					obj=new Object[terminalIds.length+1];
				}
				builder.append(" where MEDIA_DATE <= ? ");
				obj[0]=endTime;
				flag=2;
			}
			else if(beginTime!=0&&endTime==0){
				if(currentPage>0){
					obj=new Object[terminalIds.length+3];
				}else{
					obj=new Object[terminalIds.length+1];
				}
				builder.append(" where MEDIA_DATE >= ? ");
				obj[0]=beginTime;
				flag=1;
			}else{
				if(currentPage>0){
					obj=new Object[terminalIds.length+2];
				}else{
					obj=new Object[terminalIds.length];
				}
				builder.append(" where 1= 1 ");
			}
			for(int i=0;i<terminalIds.length;i++){
				if(flag==3){
					obj[i+2]=terminalIds[i];
				}else if(flag==2||flag==1){
					obj[i+1]=terminalIds[i];
				}else{
					obj[i]=terminalIds[i];
				}
				if(i==0){
					builder.append(" and TERMINAL_ID=?");
				}else{
					builder.append(" or TERMINAL_ID=?");
				}
			}
			builder.append(" ORDER BY TERMINAL_ID,MEDIA_DATE");
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				if(flag==3){
					obj[terminalIds.length+2] = beginRecord;
					obj[terminalIds.length+3] = endRecord;
				}else if(flag==2||flag==1){
					obj[terminalIds.length+1] = beginRecord;
					obj[terminalIds.length+2] = endRecord;
				}else{
					obj[terminalIds.length] = beginRecord;
					obj[terminalIds.length+1] = endRecord;
				}
				builder.append(" limit ?,?");
			}
			return super.queryForList(builder.toString().toUpperCase(), LcMultimediaParaDBEntity.class, obj);
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
