package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerMilOilTypeDBEntity;

import java.util.List;

/**
 * Created by 修伟 on 2017/7/28 0028.
 */
public interface LcTerMilOilTypeDao extends BaseDao<LcTerMilOilTypeDBEntity>{
    public List<LcTerMilOilTypeDBEntity> queryData();
}
