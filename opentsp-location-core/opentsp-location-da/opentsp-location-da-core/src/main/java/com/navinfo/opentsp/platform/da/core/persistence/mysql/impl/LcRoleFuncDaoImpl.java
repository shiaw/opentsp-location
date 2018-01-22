package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcRoleFuncDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcRoleFuncDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;


public class LcRoleFuncDaoImpl extends BaseDaoImpl<LcRoleFuncDBEntity> implements LcRoleFuncDao {

	@Override
	public List<LcRoleFuncDBEntity> getRoleFuncByRoleId(int role_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcRoleFuncDBEntity> getRoleFuncByFuncId(int func_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteRoleFuncByRoleId(int roleId) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("delete from LC_ROLE_FUNC  where LC_ROLE_FUNC.ROLE_ID=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			return qryRun.update(conn, builder.toString().toUpperCase(), roleId);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int deleteRoleFuncByFuncId(int funcId) {
		StringBuilder builder = null;
		try {
			builder=new StringBuilder("delete from LC_ROLE_FUNC  where LC_ROLE_FUNC.FUNC_ID=?");
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			return qryRun.update(conn, builder.toString().toUpperCase(), funcId);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}

	}


}
