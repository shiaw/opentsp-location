package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.DSATerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalRuleDao2;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LcTerminalRuleDaoImpl2 extends BaseDaoImpl<LcTerminalRuleDBEntity>
		implements LcTerminalRuleDao2 {
	private Logger logger = LoggerFactory.getLogger(LcTerminalRuleDaoImpl2.class);


	@Override
	public int saveRegularData(LCRegularData.RegularData regularData)  throws Exception {
		try {
			int trId = -1;
			// 先查找该区域无关的规则是否存在，如果存在则删除，如果不存在直接插入
			String insertSql = "insert into LC_TERMINAL_RULE (TERMINAL_ID,BUSINESS_TYPE,RULE_CONTENT,LAST_UPDATE_TIME,ORIGINAL_AREA_ID,ORIGINAL_LINE_ID,RULE_TYPE) values (?,?,?,?,?,?,?)";
			String querySql = "select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID from LC_TERMINAL_RULE rule where rule.TERMINAL_ID = ? and rule.BUSINESS_TYPE = ? and rule.ORIGINAL_AREA_ID = ?";
			String delete_sql = "delete from LC_TERMINAL_RULE where ORIGINAL_AREA_ID is null and TERMINAL_ID=? and BUSINESS_TYPE in ("+ RegularCode.driverNotCard_VALUE+","+ RegularCode.drivingBan_VALUE+")";
			String updateSql="update LC_TERMINAL_RULE set RULE_CONTENT=? , LAST_UPDATE_TIME=? where TERMINAL_ID = ? and BUSINESS_TYPE = ? and ORIGINAL_AREA_ID = ?";
			String sqlPlus=" and rule.ORIGINAL_LINE_ID = ?";
			//Object[][] para = new Object[regularDatas.size()][];
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();

			boolean routeFlag = false;
			//0:TerminalId,
			//1:RegularCode 规则编码(170090超速)对应BUSINESS_TYPE
			//2:regularData.getSpeeding().toByteArray 对应RULE_CONTENT
			//3:LastModifyDate,
			//4:AreaId
			//5:RouteId ,
			//6:RULE_TYPE  规则类型
			Object[] regular = new Object[7];
			regular[0] = regularData.getTerminalId();
			regular[1] = regularData.getRegularCode().getNumber();

			regular[3] = regularData.getLastModifyDate();
			switch (regularData.getRegularCode().getNumber()) {
				case RegularCode.speeding_VALUE:// 区域超速报警
					regular[2] = regularData.getSpeeding().toByteArray();
					switch (regularData.getSpeeding().getTypes()
							.getNumber()) {
						case LCAreaType.AreaType.route_VALUE:
							regular[4] = regularData.getSpeeding().getAreaId();// 这里要找出区域标识
							regular[5] = regularData.getSpeeding().getRouteId();// 这里要找出区域标识
							routeFlag=true;
							break;
						case LCAreaType.AreaType.segment_VALUE:
							regular[4] = regularData.getSpeeding().getAreaId();// 这里要找出区域标识
							regular[5] = regularData.getSpeeding().getRouteId();// 这里要找出区域标识
							routeFlag=true;
							break;
						default:
							regular[4] = regularData.getSpeeding().getAreaId();// 这里要找出区域标识
					}
					break;
				case RegularCode.inOutArea_VALUE:// 进出区域/路线报警
					regular[2] = regularData.getInOutArea().toByteArray();
					regular[4] = regularData.getInOutArea().getAreaId();// 这里要找出区域标识
					break;
				case RegularCode.routeDriverTime_VALUE:// 路线行驶时间不足或者过长
					regular[2] = regularData.getDriverTime().toByteArray();
					regular[4] = regularData.getDriverTime()
							.getRouteId();// 这里要找出区域标识
					regular[5] = regularData.getDriverTime().getLineId();// 这里要找出区域标识
					routeFlag=true;
					break;
				case RegularCode.driverNotCard_VALUE:// 驾驶员未刷卡报警
					super.executeUpdate(delete_sql, regularData
							.getTerminalId());
					regular[2] = regularData.getDriverNotCard().toByteArray();
					regular[4] = null;
					break;
				case RegularCode.doorOpenOutArea_VALUE:// 区域外开门报警
					regular[2] = regularData.getDoorOpenOutArea().toByteArray();
					regular[4] = regularData.getDoorOpenOutArea()
							.getAreaId();// 这里要找出区域标识
					break;
				case RegularCode.drivingBan_VALUE:// 禁驾报警
					regular[2] = regularData.getDriverNotCard().toByteArray();
					super.executeUpdate(delete_sql, regularData
							.getTerminalId());
					regular[4] = regularData.getSpeeding().getAreaId();;
					break;
				case RegularCode.keyPointFence_VALUE:// 禁驾报警
					regular[2] = regularData.getKeyPointFence().toByteArray();
					regular[4] = regularData.getKeyPointFence().getAreaId();
					routeFlag=true;
					break;
				case RegularCode.messageBroadcast_VALUE:// 信息播报规则
					regular[2] = regularData.getMessageBroadcast().toByteArray();
					regular[4] = regularData.getMessageBroadcast().getAreaId();
					break;
				case RegularCode.overtimePark_VALUE:// 服务站内超时
					regular[2] = regularData.getOvertimePark().toByteArray();
					regular[4] = regularData.getOvertimePark().getAreaId();
					break;
				case RegularCode.outregionToLSpeed_VALUE:// 出区域限速
					regular[2] = regularData.getOutregionToLSpeed().toByteArray();
					regular[4] = regularData.getOutregionToLSpeed().getAreaId();
					break;
				case RegularCode.terminalBroadcastSwitch_VALUE://170130 TerminalMessageSwitch 终端信息广播开关
					regular[2] = regularData.getTerminalMessage().toByteArray();
					regular[4] = 0;
					break;
				case RegularCode.delayOvertimePark_VALUE:
					regular[2] = regularData.getDelayPark().toByteArray();
					regular[4] = 0;
					break;
				case RegularCode.vehiclePassStatistic_VALUE://车辆经过次数统计
					regular[2] = regularData.getPassTimes().toByteArray();
					regular[4] = regularData.getPassTimes().getAreaId();
					break;
				case RegularCode.inAreaTriggerActivationOrLockNotify_VALUE: //进区域激活/锁车设置通知
					regular[2] = regularData.getInAreaTriggerActivationOrLock().toByteArray();
					regular[4] = regularData.getInAreaTriggerActivationOrLock().getAreaId();
					break;
				case RegularCode.inOrOutAreaNotifySwitchPara_VALUE:
					byte[] bt = {0};
					regular[2] = bt;
					regular[4] = 0;
					break;
				case RegularCode.inOrOutAreaNotifySetPara_VALUE: //4.5.8.7	进出区域通知设置
					regular[2] = regularData.getSetPara().toByteArray();
					regular[4] = regularData.getSetPara().getAreaIdentify();
					break;
			}

			regular[6] = regularData.getType().getNumber();

			List<LcTerminalRuleDBEntity> lcTerminalRule=null;
			/**
			 //0:TerminalId,
			 //1:RegularCode 规则编码(170090超速)对应BUSINESS_TYPE
			 //2:regularData.getSpeeding().toByteArray 对应RULE_CONTENT
			 //3:LastModifyDate,
			 //4:AreaId
			 * 根据0,1,4
			 */
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
				logger.error("已存在，更新操作，TR_ID="+trId+",RegularCode="+lcTerminalRule.get(0).getBusiness_type()+",areaId="+lcTerminalRule.get(0).getOriginal_area_id());


				trId = lcTerminalRule.get(0).getTr_id();
				if(!routeFlag){
					super.executeUpdate(updateSql, new Object[]{regular[2],regular[3],regular[0],regular[1],regular[4]});
				}else{
					super.executeUpdate(updateSql+" and ORIGINAL_LINE_ID = ?", new Object[]{regular[2],regular[3],regular[0],regular[1],regular[4],regular[5]});
				}
			}else{
				logger.error("新增操作：Rule:RegularCode="+regular[1]+",areaId="+regular[4]);
				trId  = super.executeUpdate2(insertSql, regular);
			}
			return trId;
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> queryRegularData(Integer districtCode,
			int... node_code)throws Exception {
		QueryRunner qryRun = new QueryRunner();
		Connection conn = MySqlConnPoolUtil.getContainer().get();
		StringBuilder builder = new StringBuilder();
		builder.append("select a.TR_ID,a.TERMINAL_ID,a.BUSINESS_TYPE,a.RULE_CONTENT,a.LAST_UPDATE_TIME,a.ORIGINAL_AREA_ID,a.RULE_TYPE from 	LC_TERMINAL_RULE a join LC_TERMINAL_INFO c on a.TERMINAL_ID = c.TERMINAL_ID and DATA_STATUS=1 and c.DISTRICT=?");
		Object[] paramters = new Object[1];
		paramters[0] = districtCode;
		/*builder.append(" AND (");
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
		builder.append(")");*/
		//通用规则
		String comSql = "SELECT * FROM LC_TERMINAL_RULE R WHERE R.RULE_TYPE = 0";
		try {
			List<LcTerminalRuleDBEntity> lcTerminalRules = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),paramters );
			List<LcTerminalRuleDBEntity> lcTerminalRules2 = qryRun.query(conn, comSql
					.toUpperCase(), new BeanListHandler<LcTerminalRuleDBEntity>(
					LcTerminalRuleDBEntity.class));
			lcTerminalRules.addAll(lcTerminalRules2);
			return lcTerminalRules;
		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
			throw e;
		}
	}




	@Override
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int currentPage,int pageSize)throws Exception {
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
//			e.printStackTrace();
//			return null;
			throw e;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData1(long terminalId, int ruleCode)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int areaId) throws Exception {
		try {
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

			lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),params);

			return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularData2(long terminalId,int regularCode,int areaId) throws Exception {
		try {
			Connection conn=MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			Object [] params=null;

			StringBuilder builder = new StringBuilder();
			builder.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_RULE rule where rule.TERMINAL_ID = ? and rule.BUSINESS_TYPE =? ");
			List<LcTerminalRuleDBEntity> lcTerminalRule = null;
			if(areaId>0){
				params=new Object[]{terminalId,regularCode,areaId};
				builder.append(" and rule.ORIGINAL_AREA_ID=?");
			}else{
				params=new Object[]{terminalId,regularCode};
			}

			lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),params);

			return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
		} catch (SQLException e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaId(long terminalId,
															   int areaId)  throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteByTermianlIdAndAreaId(long terminalId, int... areaIdentify)  throws Exception {
		/*try {
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
			}*/
		try {
			StringBuilder str2 = new StringBuilder();
			Object[] obj = new Object[areaIdentify.length + 1];
			obj[0] = terminalId;
			for (int i = 0; i < areaIdentify.length; i++) {
				if (i == 0) {
					str2.append(" and b.ORIGINAL_AREA_ID=?");
				} else {
					str2.append(" OR b.ORIGINAL_AREA_ID=?");
				}
				obj[i+1] = areaIdentify[i];
			}
			StringBuilder builder = new StringBuilder();
			builder.append(" DELETE from LC_TERMINAL_RULE where TR_ID in( select b.TR_ID FROM LC_TERMINAL_AREA b  WHERE b.TERMINAL_ID=?  ").append(str2).append(")");
			super.executeUpdate(builder.toString().toUpperCase(), obj);
			return true;
		}catch(Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteRegularData(long terminalId, int regularCode,long areaId) throws Exception {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("delete from LC_TERMINAL_RULE  where TERMINAL_ID=? and BUSINESS_TYPE =?  ") ;
			if(0l != areaId){
				builder.append("and ORIGINAL_AREA_ID=?");
				Object [] values=new Object[3];
				values[0]=terminalId;
				values[1]=regularCode;
				values[2]=areaId;
				super.executeUpdate(builder.toString().toUpperCase(), values);
			}else{
				super.executeUpdate(builder.toString().toUpperCase(), terminalId, regularCode);
			}

			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 根据终端列表查询规则和区域类型
	 */
	@Override
	public List<DSATerminalRuleDBEntity> getRegularDataForDsa(long[] terminalIds) throws Exception {
		try {
			MySqlConnPoolUtil.startTransaction();
			if(terminalIds!=null) {
				String sql = "insert into LC_TERMINALID_TEMP (TERMINAL_ID) values (?)";
				String delete_sql = "delete from LC_TERMINALID_TEMP";
				super.executeUpdate(delete_sql);
				Object[][] para = new Object[terminalIds.length][];
				for(int i=0;i<terminalIds.length;i++){
					para[i]=new Object[]{terminalIds[i]};
				}
				QueryRunner qryRun = new QueryRunner();
				Connection conn = MySqlConnPoolUtil.getContainer().get();
				qryRun.batch(conn, sql, para);
			}

			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.ORIGINAL_LINE_ID,rule.RULE_TYPE, area.AREA_TYPE from LC_TERMINALID_TEMP info ,LC_TERMINAL_RULE rule ,LC_TERMINAL_AREA area where info.TERMINAL_ID=rule.TERMINAL_ID and info.TERMINAL_ID = area.TERMINAL_ID and rule.ORIGINAL_AREA_ID =area.ORIGINAL_AREA_ID";
			List<DSATerminalRuleDBEntity> lcTerminalRule = qryRun.query(conn, sql.toUpperCase(),
					new BeanListHandler<DSATerminalRuleDBEntity>(
							DSATerminalRuleDBEntity.class));
			MySqlConnPoolUtil.commit();
			return lcTerminalRule;
		} catch (SQLException e) {
			MySqlConnPoolUtil.rollback();
			throw e;
		/*	logger.error(ExceptionUtils.getFullStackTrace(e));
			return null;*/
		}finally {
			MySqlConnPoolUtil.close();
		}
	}


	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			long terminalId, int[] areaIds) throws Exception {
		try {
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

			lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),params);

			return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}


	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			List<Long> areaIds) throws Exception {
		try {
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

				lcTerminalRule = qryRun.query(conn, builder.toString().toUpperCase(),
						new BeanListHandler<LcTerminalRuleDBEntity>(
								LcTerminalRuleDBEntity.class),params);

				return (lcTerminalRule==null||lcTerminalRule.size()==0)?null:lcTerminalRule;
			}
			return null;
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}


	@Override
	public List<LcTerminalRuleDBEntity> getRegularDataByTerminalIds(
			List<Long> terminalIds) throws Exception {
		try {
			if(terminalIds!=null) {
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
			}
			return null;
		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
			throw e;
		}
	}


	@Override
	public int ruleCount(long terminalId, long areaId)throws Exception {
		try{
			Long count = 0l;
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			String sql = "SELECT COUNT(1) FROM `LC_TERMINAL_RULE` rule WHERE rule.TERMINAL_ID = ? AND rule.ORIGINAL_AREA_ID = ?";
			count = qryRun.query(conn, sql.toUpperCase(),new ScalarHandler<Long>(),terminalId,areaId);
			return count.intValue();
		} catch (SQLException e) {
//			e.printStackTrace();
//			return 0;
			throw e;
		}
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRuleByType(int type) {
		try {
			MySqlConnPoolUtil.startTransaction();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			StringBuilder sql =new StringBuilder();
			sql.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_RULE rule where rule.RULE_TYPE = ?");
			List<LcTerminalRuleDBEntity> lcTerminalRule = qryRun.query(conn, sql.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),type);
			return lcTerminalRule;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			MySqlConnPoolUtil.close();
		}
		return null;
	}

	@Override
	public List<LcTerminalRuleDBEntity> getRuleByCode(int type) {
		try {
			MySqlConnPoolUtil.startTransaction();
			Connection conn = MySqlConnPoolUtil.getContainer().get();
			QueryRunner qryRun = new QueryRunner();
			StringBuilder sql =new StringBuilder();
			sql.append("select rule.TR_ID,rule.TERMINAL_ID,rule.BUSINESS_TYPE,rule.RULE_CONTENT,rule.LAST_UPDATE_TIME,rule.ORIGINAL_AREA_ID,rule.RULE_TYPE from LC_TERMINAL_RULE rule where rule.BUSINESS_TYPE = ?");
			List<LcTerminalRuleDBEntity> lcTerminalRule = qryRun.query(conn, sql.toString().toUpperCase(),
					new BeanListHandler<LcTerminalRuleDBEntity>(
							LcTerminalRuleDBEntity.class),type);
			return lcTerminalRule;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally{
			MySqlConnPoolUtil.close();
		}
		return null;
	}
}
