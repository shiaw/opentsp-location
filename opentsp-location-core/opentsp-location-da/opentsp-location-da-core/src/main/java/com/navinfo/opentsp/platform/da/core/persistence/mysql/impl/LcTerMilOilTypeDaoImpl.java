package com.navinfo.opentsp.platform.da.core.persistence.mysql.impl;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerMilOilTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.BaseDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerMilOilTypeDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 修伟 on 2017/7/28 0028.
 */
public class LcTerMilOilTypeDaoImpl extends BaseDaoImpl<LcTerMilOilTypeDBEntity> implements LcTerMilOilTypeDao{

    @Override
    public List<LcTerMilOilTypeDBEntity> queryData() {
        MySqlConnPoolUtil.startTransaction();
        QueryRunner qryRun = new QueryRunner();
        Connection conn = MySqlConnPoolUtil.getContainer().get();
        String sql = "select t.TERMINAL_ID, t.MILEAGE_TYPE,t.OIL_TYPE,t.CREATE_TIME from lc_terminal_mileage_oil_type t ";
        List<LcTerMilOilTypeDBEntity> list = null;
        try {
            list = qryRun.query(conn, sql, new BeanListHandler<LcTerMilOilTypeDBEntity>(
                    LcTerMilOilTypeDBEntity.class));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySqlConnPoolUtil.close();
        }
        return list;
    }
}
