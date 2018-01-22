package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * Created by 修伟 on 2017/7/28 0028.
 */
@LCTable(name="LC_TERMINAL_MILEAGE_OIL_TYPE")
public class LcTerMilOilTypeDBEntity implements java.io.Serializable {
    @LCTransient
    @LCPrimaryKey
    private long terminal_id;
    private int mileage_type;
    private int oil_type;
    private long create_time;

    public long getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(long terminal_id) {
        this.terminal_id = terminal_id;
    }

    public int getMileage_type() {
        return mileage_type;
    }

    public void setMileage_type(int mileage_type) {
        this.mileage_type = mileage_type;
    }

    public int getOil_type() {
        return oil_type;
    }

    public void setOil_type(int oil_type) {
        this.oil_type = oil_type;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
