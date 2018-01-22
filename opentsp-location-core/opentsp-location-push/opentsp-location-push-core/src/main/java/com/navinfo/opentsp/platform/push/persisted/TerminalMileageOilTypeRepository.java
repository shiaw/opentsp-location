package com.navinfo.opentsp.platform.push.persisted;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository which store tasks with persisted flag
 */
public interface TerminalMileageOilTypeRepository extends JpaRepository<TerminalMileageOilTypeEntry, Long> {


    /**
     * 查询里程油耗类型
     *
     *
     * @return 查询终端信息列表
     */
    @Query("select  new com.navinfo.opentsp.platform.push.persisted.TerminalMileageOilTypeEntry(i.terminalId, i.mileageType, i.oilType) from TerminalMileageOilTypeEntry as i where  i.terminalId=?1  ")
    public TerminalMileageOilTypeEntry findTerminalMileageOilById(long terminalId);

}
