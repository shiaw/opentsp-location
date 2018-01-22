package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaAndDataDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;


public class LcTerminalAreaDaoImpl extends BaseDaoImpl<LcTerminalAreaDBEntity>
		implements LcTerminalAreaDao {
	/**
	 * 根据获得区域信息
	 *
	 * @param terminalIds
	 * @return
	 */
	@Override
	public List<LcTerminalAreaAndDataDBEntity> getAreaInfoByTerminalId(long[] terminalIds
	) {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String codes = "(";
			for (int i = 0; i < terminalIds.length; i++) {
				if(i==0){
					codes +=terminalIds[i];
				}else{
					codes += "," + terminalIds[i];
				}
			}
			codes += ")";
			String sql = "select a.TA_ID,a.ORIGINAL_AREA_ID,a.AREA_TYPE,a.CREATE_TIME,a.TERMINAL_ID ,c.DATA_SN,c.DATA_STATUS,c.LATITUDE,c.LONGITUDE,c.RADIUS_LEN from LC_TERMINAL_AREA_DATA c ,LC_TERMINAL_AREA a where c.TA_ID=a.TA_ID  and a.TERMINAL_ID in "+codes;
			List<LcTerminalAreaAndDataDBEntity> lcTerminalAreas = qryRun.query(conn, sql
					.toUpperCase(), new BeanListHandler<LcTerminalAreaAndDataDBEntity>(
					LcTerminalAreaAndDataDBEntity.class));
			return lcTerminalAreas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据服务区域和taIp获得区域信息
	 *
	 * @param districtCode
	 * @param node_code
	 * @return
	 */
	@Override
	public List<LcTerminalAreaDBEntity> getAreaInfo(int districtCode,
													int... node_code) {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String codes = "(";
			for (int i = 0; i < node_code.length; i++) {
				if(i==0){
					codes +=node_code[i];
				}else{
					codes += "," + node_code[i];
				}
			}
			codes += ")";
			String sql = "select a.TA_ID,a.ORIGINAL_AREA_ID,a.AREA_TYPE,a.CREATE_TIME,a.TERMINAL_ID from LC_TERMINAL_INFO c ,LC_TERMINAL_AREA a where c.TERMINAL_ID=a.TERMINAL_ID  and c.DISTRICT=? and c.NODE_CODE in "+codes;
			List<LcTerminalAreaDBEntity> lcTerminalAreas = qryRun.query(conn, sql
					.toUpperCase(), new BeanListHandler<LcTerminalAreaDBEntity>(
					LcTerminalAreaDBEntity.class), districtCode);

			//通用规则对应的区域
			String comSql = "SELECT * FROM LC_TERMINAL_AREA A WHERE A.TERMINAL_ID = 0";
			List<LcTerminalAreaDBEntity> lcTerminalAreas2 = qryRun.query(conn, comSql
					.toUpperCase(), new BeanListHandler<LcTerminalAreaDBEntity>(
					LcTerminalAreaDBEntity.class));
			lcTerminalAreas.addAll(lcTerminalAreas2);

			return lcTerminalAreas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteAreaInfo(long terminalId, int... areaIdentify) {
		try {
			Object[] obj = new Object[areaIdentify.length + 1];
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM LC_TERMINAL_AREA  WHERE TERMINAL_ID=?");
			for (int i = 0; i < areaIdentify.length; i++) {
				if (i == 0) {
					builder.append(" and ORIGINAL_AREA_ID=?");
				} else {
					builder.append(" OR ORIGINAL_AREA_ID=?");
				}
				obj[i+1] = areaIdentify[i];
			}
			obj[0] = terminalId;
			super.executeUpdate(builder.toString().toUpperCase(), obj);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addAreaInfo(AreaInfo areaInfo) {
		try {/*
			// 区域的存储大于规则，如果区域有变化规则也跟着变化
			// 先判断同终端，同区域标识的，如果有则把老得置为删除状态，然后删除相应规则，最后新增区域
			String same_ter_area_sql = "select * from LC_TERMINAL_AREA where ORIGINAL_AREA_ID=? and TERMINAL_ID=?";
			LcTerminalAreaDBEntity terminalArea = super.queryForObject(same_ter_area_sql, LcTerminalAreaDBEntity.class, areaInfo.getAreaIdentify(),
					areaInfo.getTerminalId());
			if (terminalArea != null) {
				// 置老得区域信息为删除状态
				LcTerminalAreaDataDao areaDataDao=new LcTerminalAreaDataDaoImpl();
				LcTerminalRuleDao ruleDao=new LcTerminalRuleDaoImpl();
				//deleteOldAreaInfo(lcTerminalArea);
				//删除区域数据
				areaDataDao.deleteByAreaId(terminalArea.getTerminal_id(),terminalArea.getOriginal_area_id());
				//删除区域相关的规则
				ruleDao.deleteByTermianlIdAndAreaId(terminalArea.getTerminal_id(),terminalArea.getOriginal_area_id());
				//删除区域
				super.delete(LcTerminalAreaDBEntity.class,new int[]{terminalArea.getTa_id()});
				// 添加新的区域信息
				// 下面是新增逻辑,新增区域
				return addNewAreaInfo(areaInfo);
			} else {
				// 查找区域数据是否超过了16条，如果超过了就把最旧的删除，然后再新增新的区域数据
				String query_num = "select * from LC_TERMINAL_AREA where AREA_TYPE=? and TERMINAL_ID=? order by CREATE_TIME";
				// 判断是否超过16条
				List<LcTerminalAreaDBEntity> list = super.queryForList(query_num, LcTerminalAreaDBEntity.class, areaInfo.getTypes(),
						areaInfo.getTerminalId());
				if (CollectionUtils.isNotEmpty(list) && list.size() > 15) {
					// 得到最小的那条数据
					LcTerminalAreaDBEntity lcTerminalArea = list.get(0);
					LcTerminalAreaDataDao areaDataDao=new LcTerminalAreaDataDaoImpl();
					LcTerminalRuleDao ruleDao=new LcTerminalRuleDaoImpl();
					deleteAreaInfo(lcTerminalArea.getTerminal_id(), lcTerminalArea.getOriginal_area_id(), 0);

					//删除区域数据
					areaDataDao.deleteByAreaId(lcTerminalArea.getTerminal_id(),lcTerminalArea.getOriginal_area_id());
					//删除区域相关的规则
					ruleDao.deleteByTermianlIdAndAreaId(lcTerminalArea.getTerminal_id(),lcTerminalArea.getOriginal_area_id());
					//删除区域
					super.delete(LcTerminalAreaDBEntity.class,new int[]{lcTerminalArea.getTa_id()});
				}
				// 下面是新增逻辑,新增区域
				return addNewAreaInfo(areaInfo);
			}
		*/} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean addNewAreaInfo(AreaInfo areaInfo) throws SQLException {
		return false;
		// 新增区域信息
		/*String sql = "insert into LC_TERMINAL_AREA (original_area_id,area_type,create_time,terminal_id,data_status) values (?,?,?,?,?)";
		executeUpdate(sql, areaInfo.getAreaIdentify(), areaInfo.getTypes().getNumber(),
				areaInfo.getCreateDate(), areaInfo.getTerminalId(), 1);
		Long area_id = null;
		 获取主键的语句
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		area_id = new Long(qryRun.query(conn, "SELECT LAST_INSERT_ID()",
				new ScalarHandler(1)).toString());
		// 新增区域数据
		String sql_data = "insert into LC_TERMINAL_AREA_DATA (TA_ID,DATA_SN,DATA_STATUS,RADIUS_LEN,LATITUDE,LONGITUDE) values (?,?,?,?,?,?)";
		List<AreaData> areaDatas = areaInfo.getDatasList();
		Object[][] a = new Object[areaDatas.size()][];
		for (int i = 0; i < areaDatas.size(); i++) {
			Object[] b = new Object[6];
			b[0] = area_id;
			b[1] = areaDatas.get(i).getDataSN();
			b[2] = areaDatas.get(i).getDataStatus();
			b[3] = areaDatas.get(i).getRadiusLength();
			b[4] = areaDatas.get(i).getLatitude();
			b[5] = areaDatas.get(i).getLongitude();
			a[i] = b;
		}
		int[] ids = batchUpdate(sql_data, a);
		conn.commit();
		if (ids != null && ids.length > 0) {
			return true;
		} else {
			return false;
		}*/
	}

	@Override
	public List<LcTerminalAreaDBEntity> getAreaInfo(long terminalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LcTerminalAreaDBEntity getAreaInfo(long terminalId, int areaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcTerminalAreaDBEntity> getAreaInfoByType(long terminalId, int areaType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAreaInfo(long terminalId, int areaType) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) {
		try {
			StringBuilder builder = new StringBuilder("select  TA_ID,ORIGINAL_AREA_ID,AREA_TYPE,CREATE_TIME,TERMINAL_ID from LC_TERMINAL_AREA where 1=1 ");
			if(terminalId != 0)
				builder.append(" and  TERMINAL_ID="+terminalId);
			if(areaId != 0)
				builder.append(" and  ORIGINAL_AREA_ID="+areaId);
			if(start !=0 )
				builder.append(" and  CREATE_TIME >= "+start);
			if( end != 0)
				builder.append(" and  CREATE_TIME <= "+end);
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				builder.append(" limit "+beginRecord+","+endRecord+"");
			}
			QueryRunner qryRun = new QueryRunner();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			List<LcTerminalAreaDBEntity>  list = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalAreaDBEntity>(LcTerminalAreaDBEntity.class));
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
