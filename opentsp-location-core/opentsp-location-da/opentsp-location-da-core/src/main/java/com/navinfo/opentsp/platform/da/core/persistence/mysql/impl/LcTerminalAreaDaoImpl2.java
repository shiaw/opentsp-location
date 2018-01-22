package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaAndDataDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalAreaDataDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalRuleDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LcTerminalAreaDaoImpl2 extends BaseDaoImpl<LcTerminalAreaDBEntity>
		implements LcTerminalAreaDao2 {
	private Logger logger = LoggerFactory.getLogger(LcTerminalAreaDaoImpl2.class);

	@Override
	public int saveAreaInfo(AreaInfo areaInfo,int trId)  throws Exception {
		try {

			// 查找区域数据是否超过了16条，如果超过了就把最旧的删除，然后再新增新的区域数据
			String query_num = "select * from LC_TERMINAL_AREA where AREA_TYPE=? and TERMINAL_ID=? order by CREATE_TIME";
			// 判断是否超过16条
			List<LcTerminalAreaDBEntity> list = super.queryForList(query_num, LcTerminalAreaDBEntity.class, areaInfo.getTypes(),
					areaInfo.getTerminalId());
			if (CollectionUtils.isNotEmpty(list) && list.size() > 15) {
				// 得到最小的那条数据 删除
				LcTerminalAreaDBEntity lcTerminalArea = list.get(0);
				LcTerminalAreaDataDao2 areaDataDao=new LcTerminalAreaDataDaoImpl2();
				LcTerminalRuleDao2 ruleDao=new LcTerminalRuleDaoImpl2();

				//通过 Area 的TA_ID  删除区域数据
				areaDataDao.deleteByTaId( lcTerminalArea.getTa_id());
				//通过Area的 TR_ID  删除规则
				ruleDao.delete(LcTerminalRuleDBEntity.class, new int[]{lcTerminalArea.getTr_id()});
				//删除区域 area
				super.delete(LcTerminalAreaDBEntity.class,new int[]{lcTerminalArea.getTa_id()});
			}
			// 下面是新增逻辑,新增区域
			return addNewAreaInfo(areaInfo,trId);
		} catch (SQLException e) {
//			e.printStackTrace();
			throw e;
		}
	}



	/**
	 * 根据获得区域信息
	 * @param districtCode
	 * @param node_code
	 * @return
	 */
	@Override
	public List<LcTerminalAreaAndDataDBEntity> getAreaInfoByTerminalId(long[] terminalIds
	)throws Exception {
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
//			e.printStackTrace();
			throw e;
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
													int... node_code) throws Exception {
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
			String sql = "select a.TA_ID,a.ORIGINAL_AREA_ID,a.AREA_TYPE,a.CREATE_TIME,a.TERMINAL_ID,a.TR_ID from LC_TERMINAL_INFO c ,LC_TERMINAL_AREA a where c.TERMINAL_ID=a.TERMINAL_ID  and c.DISTRICT=? ";
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
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteAreaInfo(long terminalId, int... areaIdentify)throws Exception{
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
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}


	private int addNewAreaInfo(AreaInfo areaInfo,int trId) throws Exception {
		try {
			String querySql = "select * from LC_TERMINAL_AREA where TR_ID=?";
			List<LcTerminalAreaDBEntity> areaList = super.queryForList(querySql, LcTerminalAreaDBEntity.class, trId);
			if(areaList != null && areaList.size()>0) {
				LcTerminalAreaDBEntity entity = areaList.get(0);
				int taId = entity.getTa_id();
				String updateSql = "update LC_TERMINAL_AREA set original_area_id=?,area_type=?,create_time=?,terminal_id=?,tr_id=? where ta_id=?";
				executeUpdate(updateSql,areaInfo.getAreaIdentify(), areaInfo.getTypes().getNumber(),areaInfo.getCreateDate(), areaInfo.getTerminalId(), trId,taId);
				return taId;
			} else {

				// 新增区域信息
				String sql = "insert into LC_TERMINAL_AREA (original_area_id,area_type,create_time,terminal_id,tr_id) values (?,?,?,?,?)";
				executeUpdate(sql, areaInfo.getAreaIdentify(), areaInfo.getTypes().getNumber(),areaInfo.getCreateDate(), areaInfo.getTerminalId(), trId);
				QueryRunner qryRun = new QueryRunner();
				Connection conn = MySqlConnPoolUtil.getContainer().get();
				/* 获取主键的语句 */
				Long area_id = new Long(qryRun.query(conn, "SELECT LAST_INSERT_ID()",new ScalarHandler(1)).toString());
			/*	// 新增区域数据
				String sql_data = "insert into LC_TERMINAL_AREA_DATA (TA_ID,DATA_SN,DATA_STATUS,RADIUS_LEN,LATITUDE,LONGITUDE) values (?,?,?,?,?,?)";
				List<AreaData> areaDatas = areaInfo.getDatasList();
				Object[][] batchParams = new Object[areaDatas.size()][];
				for (int i = 0; i < areaDatas.size(); i++) {
					AreaData areaData = areaDatas.get(i);
					Object[] b = new Object[6];
					b[0] = area_id;
					b[1] = areaData.getDataSN();
					b[2] = areaData.getDataStatus();
					b[3] = areaData.getRadiusLength();
					b[4] = areaData.getLatitude();
					b[5] = areaData.getLongitude();
					batchParams[i] = b;
				}
				batchUpdate(sql_data, batchParams);*/
				return area_id.intValue();
			}
		}catch(Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<LcTerminalAreaDBEntity> getAreaInfo(long terminalId) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LcTerminalAreaDBEntity getAreaInfo(long terminalId, int areaId) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcTerminalAreaDBEntity> getAreaInfoByType(long terminalId, int areaType)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAreaInfo(long terminalId, int areaType)throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) throws Exception{
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
//			e.printStackTrace();
//			return null;
			throw e;
		}
	}


	@Override
	public LcTerminalAreaDBEntity getAreaInfoByRuleId(int ruleId) throws Exception {
		try {
			QueryRunner qryRun = new QueryRunner();
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT * FROM LC_TERMINAL_AREA  WHERE TR_ID =?");
			LcTerminalAreaDBEntity result = qryRun.query(MySqlConnPoolUtil.getContainer().get(), builder.toString().toUpperCase(), new BeanHandler<LcTerminalAreaDBEntity>(LcTerminalAreaDBEntity.class),ruleId);
			return result;
//			super.executeUpdate(builder.toString().toUpperCase(), ruleId);
		} catch(Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * TODO:此处使用in，mysql限制sql长度不能超过1M，需要改造，使用联表或者使用临时表方案处理——hk
	 */
	@Override
	public List<LcTerminalAreaAndDataDBEntity> getAreaInfosByRuleIds(int[] ruleIds) {
		try {
			MySqlConnPoolUtil.startTransaction();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String codes = "(";
			for (int i = 0; i < ruleIds.length; i++) {
				if(i==0){
					codes +=ruleIds[i];
				}else{
					codes += "," + ruleIds[i];
				}
			}
			codes += ")";
			String sql = "select a.TA_ID,a.ORIGINAL_AREA_ID,a.AREA_TYPE,a.CREATE_TIME,a.TERMINAL_ID ,c.DATA_SN,c.DATA_STATUS,c.LATITUDE,c.LONGITUDE,c.RADIUS_LEN from LC_TERMINAL_AREA_DATA c ,LC_TERMINAL_AREA a where c.TA_ID=a.TA_ID  and a.TR_ID in "+codes;
			List<LcTerminalAreaAndDataDBEntity> lcTerminalAreas = qryRun.query(conn, sql
					.toUpperCase(), new BeanListHandler<LcTerminalAreaAndDataDBEntity>(
					LcTerminalAreaAndDataDBEntity.class));
			return lcTerminalAreas;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}

}
