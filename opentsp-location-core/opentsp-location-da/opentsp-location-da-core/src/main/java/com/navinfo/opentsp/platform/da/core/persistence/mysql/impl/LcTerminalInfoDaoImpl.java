package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.common.DBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMap;
import com.navinfo.opentsp.platform.da.core.persistence.common.ObjectForMapException;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBInfo;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalInfoDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LcTerminalInfoDaoImpl extends BaseDaoImpl<LcTerminalInfoDBEntity>
        implements LcTerminalInfoDao {
    private Logger logger = LoggerFactory.getLogger(LcTerminalInfoDaoImpl.class);

    @Override
    public LcTerminalInfoDBEntity getByTerminalId(long terminalId) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT TI_ID,TERMINAL_ID,PROTO_CODE,NODE_CODE,REGULAR_IN_TERMINAL,DISTRICT,CREATE_TIME,DATA_STATUS,DEVICE_ID,BUSINESS_TYPE FROM LC_TERMINAL_INFO WHERE ");
            builder.append("DATA_STATUS=1 AND TERMINAL_ID=? ");
            return super.queryForObject(builder.toString().toUpperCase(), LcTerminalInfoDBEntity.class, terminalId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LcTerminalInfoDBEntity getInfoByTerminalId(long terminalId) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT TI_ID,TERMINAL_ID,PROTO_CODE,NODE_CODE,REGULAR_IN_TERMINAL,DISTRICT,CREATE_TIME,DATA_STATUS,DEVICE_ID,BUSINESS_TYPE FROM LC_TERMINAL_INFO WHERE ");
            builder.append(" TERMINAL_ID=? ");//唯一 不需要data_status == 1
            return super.queryForObject(builder.toString().toUpperCase(), LcTerminalInfoDBEntity.class, terminalId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LcTerminalInfoDBEntity> getByIpAndDistrict(long node_code,
                                                           int district) {
        try {
            QueryRunner qryRun = new QueryRunner();
            Connection conn = MySqlConnPoolUtil.getContainer().get();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT TI_ID,TERMINAL_ID,PROTO_CODE,NODE_CODE,REGULAR_IN_TERMINAL,DISTRICT,CREATE_TIME,DATA_STATUS FROM LC_TERMINAL_INFO WHERE ");
            builder.append("DATA_STATUS=1 AND DISTRICT=? ");
            List<LcTerminalInfoDBEntity> list = new ArrayList<LcTerminalInfoDBEntity>();
            if (node_code != 0) {
                builder.append(" AND NODE_CODE=?");
                logger.info(builder.toString() + " " + district + " " + node_code);
                list = qryRun.query(conn, builder.toString(),
                        new BeanListHandler<LcTerminalInfoDBEntity>(
                                LcTerminalInfoDBEntity.class), district,
                        node_code);
            } else {
                list = qryRun.query(conn, builder.toString(),
                        new BeanListHandler<LcTerminalInfoDBEntity>(
                                LcTerminalInfoDBEntity.class), district);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询终端IDS，
     * @return
     */
    @Override
    public List<Long> getTerminalIds() {
        try {
            QueryRunner qryRun = new QueryRunner();
            Connection conn = MySqlConnPoolUtil.getContainer().get();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT TERMINAL_ID FROM LC_TERMINAL_INFO  ");
            List<Long> list = new ArrayList<Long>();
            List<LcTerminalInfoDBInfo> result = (List<LcTerminalInfoDBInfo>) qryRun.query(conn, builder.toString(),
                        new BeanListHandler(LcTerminalInfoDBInfo.class));
            if(result != null && result.size() > 0) {
                for(LcTerminalInfoDBInfo temp :result) {
                    list.add(temp.getTerminal_id());
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LcTerminalInfoDBInfo> getByNodeCodeAndDistrict(int district) {
        try {
            QueryRunner qryRun = new QueryRunner();
            Connection conn = MySqlConnPoolUtil.getContainer().get();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT TERMINAL.TI_ID,TERMINAL.TERMINAL_ID,TERMINAL.PROTO_CODE,TERMINAL.REGULAR_IN_TERMINAL,TERMINAL.DISTRICT,TERMINAL.CREATE_TIME,TERMINAL.DATA_STATUS,REGISTER.AUTH_CODE, "
                    + " TERMINAL.DEVICE_ID,TERMINAL.CHANGE_TID,TERMINAL.BUSINESS_TYPE "
                    + " FROM LC_TERMINAL_INFO TERMINAL left join LC_TERMINAL_REGISTER REGISTER on TERMINAL.TERMINAL_ID=REGISTER.TERMINAL_ID WHERE ");
            builder.append("TERMINAL.DATA_STATUS=1 AND TERMINAL.DISTRICT=? ");
            List<LcTerminalInfoDBInfo> list = new ArrayList<LcTerminalInfoDBInfo>();
//			if (node_code != 0) {
//				builder.append(" AND TERMINAL.NODE_CODE=?");
////				logger.info(builder.toString()+" "+district+" "+node_code);
//				list = qryRun.query(conn, builder.toString(),
//						new BeanListHandler<LcTerminalInfoDBInfo>(
//								LcTerminalInfoDBInfo.class), district,
//						node_code);
//			} else {
////				logger.info(builder.toString()+" "+district+" "+node_code);
//				list = qryRun.query(conn, builder.toString(),
//						new BeanListHandler<LcTerminalInfoDBInfo>(
//								LcTerminalInfoDBInfo.class), district);
//			}
            list = qryRun.query(conn, builder.toString(),
                    new BeanListHandler<LcTerminalInfoDBInfo>(
                            LcTerminalInfoDBInfo.class), district);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LcTerminalInfoDBEntity> getByProto(int proto_code, int district) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LcTerminalInfoDBEntity> getByDistrict(int district) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LcTerminalInfoDBEntity> getByStatus(short data_status,
                                                    int district) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 更新终端所在TA节点信息
     *
     * @param terminalId
     * @param districtCode
     * @return
     */
    @Override
    public int updateTerminalInfo(long terminalId, int districtCode,
                                  long node_code, long changeTId) {
        try {
            //String sql = "update LC_TERMINAL_INFO info  set info.NODE_CODE=?,CHANGE_TID=? where  info.TERMINAL_ID=? and info.DISTRICT=? and info.DATA_STATUS=?";
            String sql = "update LC_TERMINAL_INFO info  set CHANGE_TID=? where  info.TERMINAL_ID=? and info.DISTRICT=? and info.DATA_STATUS=?";
            super.executeUpdate(sql.toUpperCase(), node_code, changeTId, terminalId,
                    districtCode, 1);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<LcTerminalInfoDBEntity> queryTerminalParaRes(long terminalId,
                                                             int protoType, String deviceId, long changeId, long start, long end, int flag, int currentPage, int pageSize) {
        try {
            // 拼接sql
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT info.terminal_id,info.proto_code,info.district,info.node_code,info.regular_in_terminal,info.create_time,info.data_status,info.ti_id,info.device_id,info.change_tid,info.business_type ");
            builder.append(" FROM LC_TERMINAL_INFO info ");
            if (flag == 1) {
                builder.append(" where 1=1 ");
            } else {
                builder.append(" where info.DATA_STATUS=1 ");
            }
            if (protoType != 0) {
                builder.append(" and info.PROTO_CODE =" + protoType);
            }
            if (flag == 1) {
                if (terminalId != 0) {
                    builder.append(" and info.TERMINAL_ID = '" + terminalId + "' ");
                }
            } else {
                if (terminalId != 0) {
                    builder.append(" and info.TERMINAL_ID like '%" + terminalId + "%' ");
                }
            }
            if (!"".equals(deviceId)) {
                builder.append(" and info.DEVICE_ID =" + deviceId);
            }
            if (changeId != 1) {
                builder.append(" and info.CHANGE_TID =" + changeId);
            }
            if (start != 0) {
                builder.append(" and info.CREATE_TIME >=" + start + " ");
            }
            if (end != 0) {
                builder.append(" and info.CREATE_TIME <=" + end + " ");
            }
            builder.append(" order by info.CREATE_TIME desc ");
            if (currentPage > 0) {
                int beginRecord = (currentPage - 1) * pageSize;//开始记录
                int endRecord = pageSize;//从开始到结束的记录数
                builder.append("  limit " + beginRecord + "," + endRecord + "");
            }
            QueryRunner qryRun = new QueryRunner();
            Connection conn = MySqlConnPoolUtil.getContainer().get();
            List<LcTerminalInfoDBEntity> list = qryRun.query(conn, builder
                    .toString(), new BeanListHandler<LcTerminalInfoDBEntity>(
                    LcTerminalInfoDBEntity.class));
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int deleteByTerminalId(long terminalId) {
        try {
            String sql = "update LC_TERMINAL_INFO info  set info.DATA_STATUS=? where  info.TERMINAL_ID=?";
            super.executeUpdate(sql.toUpperCase(), 0, terminalId);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateByTerminalId(LcTerminalInfoDBEntity terminalInfo) {
        try {
            // 获得实体类属性
            DBEntity entity = ObjectForMap.dbObjectForMap(terminalInfo);
            // 得到类注解
            String tableName = entity.getTableName();
            // 得到所有有效属性
            String[] fields = entity.fieldNames();
            // 得到有效属性的值
            Object[] values = entity.fieldValues();
            // 拼接sql
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE ");
            builder.append(tableName.toUpperCase());
            builder.append(" SET ");
            for (int i = 0; i < fields.length; i++) {
                if (i == 0) {
                    builder.append(fields[i].toUpperCase() + "=?");
                } else {
                    builder.append("," + fields[i].toUpperCase() + "=?");
                }
            }
            builder.append(" WHERE TERMINAL_ID=? ");
            // 执行sql
            Object[] objValues = new Object[values.length + 1];
            for (int i = 0; i < objValues.length; i++) {
                if (i == objValues.length - 1) {
                    objValues[i] = terminalInfo.getTerminal_id();
                } else {
                    objValues[i] = values[i];
                }
            }
            executeUpdate(builder.toString(), objValues);
            return PlatformResponseResult.success_VALUE;
        } catch (ObjectForMapException | SQLException e) {
            logger.error(e.getMessage(), e);
            return PlatformResponseResult.failure_VALUE;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int delete(Class clazz, int[] id) throws SQLException {
        try {
            LcTerminalInfoDBEntity obj = (LcTerminalInfoDBEntity) clazz.newInstance();
            DBEntity entity = ObjectForMap.dbObjectForMap(obj);
            // 得到类注解
            String tableName = entity.getTableName();
            // 得到主键
            String primaryKey = entity.getPrimaryKeyName();
            // 拼接sql
            StringBuilder builder = new StringBuilder();
            builder.append("update ");
            builder.append(tableName.toUpperCase());
            builder.append(" set DATA_STATUS=0 ");
            builder.append(" WHERE ");
            builder.append(primaryKey.toUpperCase());
            builder.append(" IN (");
            for (int i = 0; i < id.length; i++) {
                if (i == 0) {
                    builder.append(id[i]);
                } else {
                    builder.append("," + id[i]);
                }
            }
            builder.append(")");
            // 执行sql
            executeUpdate(builder.toString().toUpperCase());
            return PlatformResponseResult.success_VALUE;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ObjectForMapException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return PlatformResponseResult.failure_VALUE;
    }

    @Override
    public int batchAddTerminalInfo(List<LcTerminalInfoDBEntity> list) {
        try {
            Object[][] para = new Object[list.size()][];
            String sql = "INSERT INTO LC_TERMINAL_INFO(TERMINAL_ID,PROTO_CODE,DISTRICT,REGULAR_IN_TERMINAL,CREATE_TIME,DATA_STATUS) VALUES(?,?,?,?,?,?)";
            int i = 0;
            for (LcTerminalInfoDBEntity lcTerminalInfoDBEntity : list) {
                Object[] regular = new Object[6];
                regular[0] = lcTerminalInfoDBEntity.getTerminal_id();
                regular[1] = lcTerminalInfoDBEntity.getProto_code();
                regular[2] = lcTerminalInfoDBEntity.getDistrict();
                regular[3] = lcTerminalInfoDBEntity.getRegular_in_terminal();
                regular[4] = lcTerminalInfoDBEntity.getCreate_time();
                regular[5] = lcTerminalInfoDBEntity.getData_status();
                para[i] = regular;
                i++;
            }
            int[] result = super.batchUpdate(sql, para);
            if (result != null && result.length == list.size()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据dbutil通用类，查询结果数量
     */
    @SuppressWarnings({"rawtypes"})
    private ScalarHandler scalarHandler = new ScalarHandler() {
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            Object obj = super.handle(rs);
            if (obj instanceof BigInteger)
                return ((BigInteger) obj).longValue();
            return obj;
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    public int queryTerminalInfoResCount(long terminalId,
                                         int protoType, String deviceId, long changeId, long start, long end) {
        try {
            // 拼接sql
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT count(*) ");
            builder.append(" FROM LC_TERMINAL_INFO info ");
            builder.append(" where info.DATA_STATUS=1 ");
            if (protoType != 0) {
                builder.append(" and info.PROTO_CODE =" + protoType);
            }
            if (terminalId != 0) {
                builder.append(" and info.TERMINAL_ID like '%" + terminalId + "%' ");
            }
            if (!"".equals(deviceId)) {
                builder.append(" and info.DEVICE_ID =" + deviceId);
            }
            if (changeId != 1) {
                builder.append(" and info.CHANGE_TID =" + changeId);
            }
            if (start != 0) {
                builder.append(" and info.CREATE_TIME >=" + start + " ");
            }
            if (end != 0) {
                builder.append(" and info.CREATE_TIME <=" + end + " ");
            }
            QueryRunner qryRun = new QueryRunner();
            Connection conn = MySqlConnPoolUtil.getContainer().get();
            int count = 0;
            Number infoCount = 0;
            infoCount = (Number) qryRun.query(conn, builder.toString(), scalarHandler);
            count = (int) infoCount.longValue();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public LcTerminalInfoDBEntity getInfoByDeviceId(String deviceId) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT TI_ID,TERMINAL_ID,PROTO_CODE,NODE_CODE,REGULAR_IN_TERMINAL,DISTRICT,CREATE_TIME,DATA_STATUS FROM LC_TERMINAL_INFO WHERE ");
            builder.append(" DEVICE_ID=?  AND DATA_STATUS = 1 ");// 需要data_status == 1
            return super.queryForObject(builder.toString().toUpperCase(), LcTerminalInfoDBEntity.class, deviceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateByDeviceId(LcTerminalInfoDBEntity terminalInfo) {
        try {

            // 获得实体类属性
            DBEntity entity = ObjectForMap.dbObjectForMap(terminalInfo);
            // 得到类注解
            String tableName = entity.getTableName();
            // 得到所有有效属性
            String[] fields = entity.fieldNames();
            // 得到有效属性的值
            Object[] values = entity.fieldValues();
            // 拼接sql
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE ");
            builder.append(tableName.toUpperCase());
            builder.append(" SET ");
            for (int i = 0; i < fields.length; i++) {
                if (i == 0) {
                    builder.append(fields[i].toUpperCase() + "=?");
                } else {
                    builder.append("," + fields[i].toUpperCase() + "=?");
                }
            }
            builder.append(" WHERE DATA_STATUS=1 AND DEVICE_ID=?");
            // 执行sql
            Object[] objValues = new Object[values.length + 1];
            for (int i = 0; i < objValues.length; i++) {
                if (i == objValues.length - 1) {
                    objValues[i] = terminalInfo.getDevice_id();
                } else {
                    objValues[i] = values[i];
                }
            }
            executeUpdate(builder.toString(), objValues);
            return PlatformResponseResult.success_VALUE;
        } catch (ObjectForMapException | SQLException e) {
            e.printStackTrace();
            return PlatformResponseResult.failure_VALUE;
        }
    }

    @Override
    public List<LcTerminalInfoDBEntity> getAllTerminal() {
        try {
            MySqlConnPoolUtil.startTransaction();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT C.TI_ID,C.TERMINAL_ID,C.PROTO_CODE,C.NODE_CODE,C.DISTRICT,C.CREATE_TIME,C.DATA_STATUS,C.REGULAR_IN_TERMINAL,C.DEVICE_ID,C.CHANGE_TID,C.BUSINESS_TYPE FROM LC_TERMINAL_INFO C WHERE C.DATA_STATUS=1");
            List<LcTerminalInfoDBEntity> list = new ArrayList<LcTerminalInfoDBEntity>();
            QueryRunner qryRun = new QueryRunner();
            Connection conn = MySqlConnPoolUtil.getContainer().get();
            list = qryRun.query(conn, builder
                    .toString(), new BeanListHandler<LcTerminalInfoDBEntity>(
                    LcTerminalInfoDBEntity.class));
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySqlConnPoolUtil.close();
        }
        return null;
    }

    @Override
    public int deletePhysiscByTerminalId(long terminalId) {
        // TODO Auto-generated method stub
        try {
            String sql = "delete from LC_TERMINAL_INFO   where  TERMINAL_ID=?";
            super.executeUpdate(sql.toUpperCase(), terminalId);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}