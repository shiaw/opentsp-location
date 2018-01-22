package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.DSATerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalRuleDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LcTerminalRuleDaoImpl extends BaseDaoImpl<LcTerminalRuleDBEntity>
		implements LcTerminalRuleDao {
	private Logger logger = LoggerFactory.getLogger(LcTerminalRuleDaoImpl.class);

	@Override
	public List<LcTerminalRuleDBEntity> queryRegularData(Integer districtCode,
														 int... node_code) {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select a.TR_ID,a.TERMINAL_ID,a.BUSINESS_TYPE,a.RULE_CONTENT,a.LAST_UPDATE_TIME,a.ORIGINAL_AREA_ID,a.RULE_TYPE from 	LC_TERMINAL_RULE a join LC_TERMINAL_INFO c on a.TERMINAL_ID = c.TERMINAL_ID and DATA_STATUS=1 and c.DISTRICT=?");
		builder.append(" AND (");
		Object[] paramters = new Object[node_code.length + 1];
		paramters[0] = districtCode;
		for(int i = 0 ; i < node_code.length ; i++){
			paramters[ i + 1 ] = node_code[i];
			if(i == 0){
				builder.append(" c.NODE_CODE=? ");
			}else{
				builder.append(" OR c.NODE_CODE=? ");
			}
		}
		builder.append(")");
		//通用规则
		String comSql = "SELECT * FROM LC_TERMINAL_RULE R WHERE R.RULE_TYPE = 0";
		try {
			List<LcTerminalRuleDBEntity> lcTerminalRules = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class) , paramters);
			List<LcTerminalRuleDBEntity> lcTerminalRules2 = qryRun.query(conn, comSql
					.toUpperCase(), new BeanListHandler<LcTerminalRuleDBEntity>(
					LcTerminalRuleDBEntity.class));
			lcTerminalRules.addAll(lcTerminalRules2);
			return lcTerminalRules;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}


	@Override
	public boolean batchSave(List<LCRegularData.RegularData> regularDatas) {
		try{
			if (CollectionUtils.isNotEmpty(regularDatas)) {
				// 先查找该区域无关的规则是否存在，如果存在则删除，如果不存在直接插入
				String sql = "insert into LC_TERMINAL_RULE (TERMINAL_ID,BUSINESS_TYPE,RULE_CONTENT,LAST_UPDATE_TIME,ORIGINAL_AREA_ID,ORIGINAL_LINE_ID,RULE_TYPE) values (?,?,?,?,?,?,?)";
				String querySql = "select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID from LC_TERMINAL_RULE rule where rule.TERMINAL_ID = ? and rule.BUSINESS_TYPE = ? and rule.ORIGINAL_AREA_ID = ?";
				String delete_sql = "delete from LC_TERMINAL_RULE where ORIGINAL_AREA_ID is null and TERMINAL_ID=? and BUSINESS_TYPE in ("+ LCRegularCode.RegularCode.driverNotCard_VALUE+","+ LCRegularCode.RegularCode.drivingBan_VALUE+")";
				String updateSql="update LC_TERMINAL_RULE set RULE_CONTENT=? , LAST_UPDATE_TIME=? where TERMINAL_ID = ? and BUSINESS_TYPE = ? and ORIGINAL_AREA_ID = ?";
				String sqlPlus=" and rule.ORIGINAL_LINE_ID = ?";
				//Object[][] para = new Object[regularDatas.size()][];
				Connection conn = MySqlConnPoolUtil.getContainer().get();
				QueryRunner qryRun = new QueryRunner();

				for (int i = 0; i < regularDatas.size(); i++) {
					boolean routeFlag=false;
					Object[] regular = new Object[7];
					regular[0] = regularDatas.get(i).getTerminalId();
					regular[1] = regularDatas.get(i).getRegularCode().getNumber();

					regular[3] = regularDatas.get(i).getLastModifyDate();
					switch (regularDatas.get(i).getRegularCode().getNumber()) {
						case LCRegularCode.RegularCode.speeding_VALUE:// 区域超速报警
							regular[2] = regularDatas.get(i).getSpeeding().toByteArray();
							switch (regularDatas.get(i).getSpeeding().getTypes()
									.getNumber()) {
								case LCAreaType.AreaType.route_VALUE:
									regular[4] = regularDatas.get(i).getSpeeding().getAreaId();// 这里要找出区域标识
									regular[5] = regularDatas.get(i).getSpeeding().getRouteId();// 这里要找出区域标识
									routeFlag=true;
									break;
								case LCAreaType.AreaType.segment_VALUE:
									regular[4] = regularDatas.get(i).getSpeeding().getAreaId();// 这里要找出区域标识
									regular[5] = regularDatas.get(i).getSpeeding().getRouteId();// 这里要找出区域标识
									routeFlag=true;
									break;
								default:
									regular[4] = regularDatas.get(i).getSpeeding().getAreaId();// 这里要找出区域标识
							}
							break;
						case LCRegularCode.RegularCode.inOutArea_VALUE:// 进出区域/路线报警
							regular[2] = regularDatas.get(i).getInOutArea().toByteArray();
							regular[4] = regularDatas.get(i).getInOutArea().getAreaId();// 这里要找出区域标识
							break;
						case LCRegularCode.RegularCode.routeDriverTime_VALUE:// 路线行驶时间不足或者过长
							regular[2] = regularDatas.get(i).getDriverTime().toByteArray();
							regular[4] = regularDatas.get(i).getDriverTime()
									.getRouteId();// 这里要找出区域标识
							regular[5] = regularDatas.get(i).getDriverTime().getLineId();// 这里要找出区域标识
							routeFlag=true;
							break;
						case LCRegularCode.RegularCode.driverNotCard_VALUE:// 驾驶员未刷卡报警
							super.executeUpdate(delete_sql, regularDatas.get(i)
									.getTerminalId());
							regular[2] = regularDatas.get(i).getDriverNotCard().toByteArray();
							regular[4] = null;
							break;
						case LCRegularCode.RegularCode.doorOpenOutArea_VALUE:// 区域外开门报警
							regular[2] = regularDatas.get(i).getDoorOpenOutArea().toByteArray();
							regular[4] = regularDatas.get(i).getDoorOpenOutArea()
									.getAreaId();// 这里要找出区域标识
							break;
						case LCRegularCode.RegularCode.drivingBan_VALUE:// 禁驾报警
							regular[2] = regularDatas.get(i).getDriverNotCard().toByteArray();
							super.executeUpdate(delete_sql, regularDatas.get(i)
									.getTerminalId());
							regular[4] = regularDatas.get(i).getSpeeding().getAreaId();;
							break;
						case LCRegularCode.RegularCode.keyPointFence_VALUE:// 禁驾报警
							regular[2] = regularDatas.get(i).getKeyPointFence().toByteArray();
							regular[4] = regularDatas.get(i).getKeyPointFence().getAreaId();
							routeFlag=true;
							break;
						case LCRegularCode.RegularCode.messageBroadcast_VALUE:// 信息播报规则
							regular[2] = regularDatas.get(i).getMessageBroadcast().toByteArray();
							regular[4] = regularDatas.get(i).getMessageBroadcast().getAreaId();
							break;
						case LCRegularCode.RegularCode.overtimePark_VALUE:// 服务站内超时
							regular[2] = regularDatas.get(i).getOvertimePark().toByteArray();
							regular[4] = regularDatas.get(i).getOvertimePark().getAreaId();
							break;
						case LCRegularCode.RegularCode.outregionToLSpeed_VALUE:// 出区域限速
							regular[2] = regularDatas.get(i).getOutregionToLSpeed().toByteArray();
							regular[4] = regularDatas.get(i).getOutregionToLSpeed().getAreaId();
							break;
					}

				/*boolean isGeneral = regularDatas.get(i).getIsGeneral();
				if(isGeneral){
					regular[6] = 0;
				}else {
					regular[6] = 1;
				}*/

					List<LcTerminalRuleDBEntity> lcTerminalRule=null;
					if(!routeFlag){
						lcTerminalRule = qryRun.query(conn, querySql,
								new BeanListHandler<LcTerminalRuleDBEntity>(
										LcTerminalRuleDBEntity.class),new Object[]{regular[0],regular[1],regular[4]});
					}else{
						lcTerminalRule = qryRun.query(conn, querySql+sqlPlus,
								new BeanListHandler<LcTerminalRuleDBEntity>(
										LcTerminalRuleDBEntity.class),new Object[]{regular[0],regular[1],regular[4],regular[5]});
					}

					if(lcTerminalRule!=null&&lcTerminalRule.size()>0){
						if(!routeFlag){
							super.executeUpdate(updateSql, new Object[]{regular[2],regular[3],regular[0],regular[1],regular[4]});
						}else{
							super.executeUpdate(updateSql+" and ORIGINAL_LINE_ID = ?", new Object[]{regular[2],regular[3],regular[0],regular[1],regular[4],regular[5]});
						}

					}else{
						super.executeUpdate(sql, regular);
					}
				}
				return true;
			} else {
				return false;
			}}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int currentPage,int pageSize) {
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			Object [] params=null;
			String sql = "select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_INFO info ,LC_TERMINAL_RULE rule where info.TERMINAL_ID=rule.TERMINAL_ID and info.TERMINAL_ID = ?";
			if(currentPage>0){
				int beginRecord = (currentPage-1)*pageSize;//开始记录
				int endRecord = pageSize;//从开始到结束的记录数
				sql+="  limit ?,?";
				params=new Object[]{terminalId,beginRecord,endRecord};
			}else{
				params=new Object[]{terminalId};
			}
			List<LcTerminalRuleDBEntity> lcTerminalRule = qryRun.query(conn, sql.toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),params);
			return lcTerminalRule;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData1(long terminalId, int ruleCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId, int areaId) {
		Connection conn=MySqlConnPoolUtil.getContainer().get();
		QueryRunner qryRun = new QueryRunner();
		Object [] params=null;

		StringBuilder builder = new StringBuilder();
		builder.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_INFO info ,LC_TERMINAL_RULE rule where info.TERMINAL_ID=rule.TERMINAL_ID and info.TERMINAL_ID = ?");
		List<LcTerminalRuleDBEntity> lcTerminalRule = null;
		if(areaId>0){
			params=new Object[]{terminalId,areaId};
			builder.append(" and rule.ORIGINAL_AREA_ID=?");
		}else{
			params=new Object[]{terminalId};
		}
		try {
			lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaId(long terminalId,
															   int areaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteByTermianlIdAndAreaId(long terminalId, int... areaIdentify) {
		try {
			Object[] obj = new Object[areaIdentify.length + 1];
			StringBuilder builder = new StringBuilder();
			builder.append("DELETE FROM LC_TERMINAL_RULE  WHERE TERMINAL_ID=?");
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
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteRegularData(long terminalId, int regularCode,long areaId) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("delete from LC_TERMINAL_RULE  where TERMINAL_ID=? and BUSINESS_TYPE =?  ") ;
			if(0l != areaId){
				Object [] values=new Object[3];
				values[0]=terminalId;
				values[1]=regularCode;
				builder.append("and ORIGINAL_AREA_ID=?");
				values[2]=areaId;

				super.executeUpdate(builder.toString().toUpperCase(), values);
			}else{
				super.executeUpdate(builder.toString().toUpperCase(), terminalId, regularCode);
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<DSATerminalRuleDBEntity> getRegularDataForDsa(long[] terminalIds) {
		if(terminalIds!=null) {
			try {
				MySqlConnPoolUtil.startTransaction();
				String sql = "insert into LC_TERMINALID_TEMP (TERMINAL_ID) values (?)";
				String delete_sql = "delete from LC_TERMINALID_TEMP";

				super.executeUpdate(delete_sql);


				Object[][] para = new Object[terminalIds.length][];
				for(int i=0;i<terminalIds.length;i++){
					para[i]=new Object[]{terminalIds[i]};
				}
				QueryRunner qryRun = new QueryRunner();
				Connection conn = MySqlConnPoolUtil.getContainer().get();
				int[] results= qryRun.batch(conn, sql, para);
				conn.commit();
				if (results != null && results.length == terminalIds.length) {
					//	return true;
				} else {
					//return false;
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				MySqlConnPoolUtil.rollback();
			} finally {
				MySqlConnPoolUtil.close();
			}
		}
		MySqlConnPoolUtil.startTransaction();
		try {
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.ORIGINAL_LINE_ID,rule.RULE_TYPE, area.AREA_TYPE from LC_TERMINALID_TEMP info ,LC_TERMINAL_RULE rule ,LC_TERMINAL_AREA area where info.TERMINAL_ID=rule.TERMINAL_ID and info.TERMINAL_ID = area.TERMINAL_ID and rule.ORIGINAL_AREA_ID =area.ORIGINAL_AREA_ID";
			List<DSATerminalRuleDBEntity> lcTerminalRule = qryRun.query(conn, sql.toUpperCase(),
					new BeanListHandler<DSATerminalRuleDBEntity>(
							DSATerminalRuleDBEntity.class));
			return lcTerminalRule;
		} catch (SQLException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			return null;
		}finally {
			MySqlConnPoolUtil.close();
		}
	}


	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			long terminalId, int[] areaIds) {
		Connection conn=MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		QueryRunner qryRun = new QueryRunner();
		Object [] params;
		builder.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_INFO info ,LC_TERMINAL_RULE rule where info.TERMINAL_ID=rule.TERMINAL_ID and info.TERMINAL_ID = ?");
		if(areaIds!=null&&areaIds.length!=0){
			params=new Object[1+areaIds.length];
			params[0]=terminalId;
			for(int i=0;i<areaIds.length;i++){
				if(i==0){
					builder.append("and (rule.ORIGINAL_AREA_ID=? ");
				}else{
					builder.append(" or rule.ORIGINAL_AREA_ID=?");
				}
				params[i+1]=areaIds[i];
			}
			builder.append(");");
		}else{
			params=new Object[]{terminalId};
		}
		List<LcTerminalRuleDBEntity> lcTerminalRule = null;
		try {
			lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
	}


	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			List<Long> areaIds) {
		Connection conn=MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		QueryRunner qryRun = new QueryRunner();
		Object [] params;
		builder.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_INFO info ,LC_TERMINAL_RULE rule where info.TERMINAL_ID=rule.TERMINAL_ID  ");
		if(areaIds!=null&&areaIds.size()!=0){
			params=new Object[areaIds.size()];
			for(int i=0;i<areaIds.size();i++){
				if(i==0){
					builder.append("and (rule.ORIGINAL_AREA_ID=? ");
				}else{
					builder.append(" or rule.ORIGINAL_AREA_ID=?");
				}
				params[i]=areaIds.get(i);
			}
			builder.append(");");
			List<LcTerminalRuleDBEntity> lcTerminalRule = null;
			try {
				lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcTerminalRuleDBEntity>(
								LcTerminalRuleDBEntity.class),params);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
		}
		return null;
	}


	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByTerminalIds(
			List<Long> terminalIds) {
		if(terminalIds!=null) {

			MySqlConnPoolUtil.startTransaction();
			try {
				Connection conn = MySqlConnPoolUtil.getContainer().get();
				QueryRunner qryRun = new QueryRunner();
				StringBuilder sql =new StringBuilder();
				sql.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_INFO info ,LC_TERMINAL_RULE rule where info.TERMINAL_ID=rule.TERMINAL_ID  ");
				Object [] params=new Object [terminalIds.size()] ;
				sql.append("and rule.TERMINAL_ID in (");
				for(int i=0;i<terminalIds.size();i++){
					params[i]=terminalIds.get(i);
					if(i==0){
						sql.append("?");
					}else{
						sql.append(",?");
					}

				}
				sql.append(");");
				List<LcTerminalRuleDBEntity> lcTerminalRule = qryRun.query(conn, sql.toString().toUpperCase(),
						new BeanListHandler<LcTerminalRuleDBEntity>(
								LcTerminalRuleDBEntity.class),params);
				return lcTerminalRule;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}finally {
				MySqlConnPoolUtil.close();
			}
		}
		return null;
	}


	@Override
	public int ruleCount(long terminalId, long areaId) {
		try{
			int count = 0;
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "SELECT COUNT(1) FROM `LC_TERMINAL_RULE` rule WHERE rule.TERMINAL_ID = ? AND rule.ORIGINAL_AREA_ID = ?";
			count = qryRun.query(conn, sql.toUpperCase(),new ScalarHandler<Integer>(),terminalId,areaId);
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
