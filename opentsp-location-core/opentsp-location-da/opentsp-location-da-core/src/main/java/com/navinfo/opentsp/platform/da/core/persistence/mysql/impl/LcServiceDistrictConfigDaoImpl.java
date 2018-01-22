package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcServiceDistrictConfigDao;
import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;

import java.sql.SQLException;
import java.util.List;



public class LcServiceDistrictConfigDaoImpl extends
		BaseDaoImpl<LcServiceDistrictConfigDBEntity> implements
		LcServiceDistrictConfigDao {

	@Override
	public List<LcServiceDistrictConfigDBEntity> getByScId(int sc_id) {
		try {
			LcServiceDistrictConfigDBEntity obj = new LcServiceDistrictConfigDBEntity();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到类注解
			String tableName = entity.getTableName();
			// 得到有效属性
			String fields = entity.fieldNamesToString();
			// 拼接主键
			fields += "," + entity.getPrimaryKeyName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("select ");
			builder.append(fields.toUpperCase());
			builder.append(" from ");
			builder.append(tableName);
			builder.append(" WHERE SC_ID=?");
			// 执行sql
			return super.queryForList(builder.toString().toUpperCase(), LcServiceDistrictConfigDBEntity.class, sc_id);
		} catch (ObjectForMapException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LcServiceDistrictConfigDBEntity getByServiceDistrict(
			int service_district) {
		return null;
	}

	@Override
	public void deleteByService(int sc_id) {
		try {
			LcServiceDistrictConfigDBEntity obj = new LcServiceDistrictConfigDBEntity();
			DBEntity entity = ObjectForMap.dbObjectForMap(obj);
			// 得到类注解
			String tableName = entity.getTableName();
			// 拼接sql
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM ");
			builder.append(tableName.toUpperCase());
			builder.append(" WHERE SC_ID=?");
			// 执行sql
			executeUpdate(builder.toString(), sc_id);
		} catch (ObjectForMapException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
