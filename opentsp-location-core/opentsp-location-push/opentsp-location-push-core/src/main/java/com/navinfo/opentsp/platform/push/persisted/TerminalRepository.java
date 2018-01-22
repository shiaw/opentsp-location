package com.navinfo.opentsp.platform.push.persisted;

import org.hibernate.mapping.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Repository which store tasks with persisted flag
 */
public interface TerminalRepository extends JpaRepository<TerminalEntry, Long> {

    /**
     * 查询终端信息列表
     *
     *
     * @return 查询终端信息列表
     */
//    @Query("select  new com.navinfo.opentsp.platform.push.persisted.TerminalEntry(i.terminalId, i.protoCode, i.nodeCode, i.district, i.createTime, i.dataStatus, i.regularInTerminal, i.deviceId, i.changeTid, i.businessType,r.authCode) from TerminalEntry  as i left join TerminalRegisterEntry as r on i.terminalId=r.terminalId")
    @Query(nativeQuery = true, value ="select terminalen0_.terminal_id as col_0_0_, terminalen0_.proto_code as col_1_0_, terminalen0_.node_code as col_2_0_, terminalen0_.district as col_3_0_, terminalen0_.create_time as col_4_0_, terminalen0_.data_status as col_5_0_, terminalen0_.regular_in_terminal as col_6_0_, terminalen0_.device_id as col_7_0_, terminalen0_.change_tid as col_8_0_, terminalen0_.business_type as col_9_0_, terminalre1_.auth_code as col_10_0_ from lc_terminal_info terminalen0_ left join lc_terminal_register terminalre1_ on terminalen0_.terminal_id=terminalre1_.terminal_id")
    public List<Object[]> findTerminalInfos();

    /**
     * 查询终端信息列表
     *
     *
     * @return 查询终端信息列表
     */
    @Query("select  new com.navinfo.opentsp.platform.push.persisted.TerminalEntry(i.terminalId, i.protoCode, i.nodeCode, i.district, i.createTime, i.dataStatus, i.regularInTerminal, i.deviceId, i.changeTid, i.businessType,'') from TerminalEntry as i where  i.terminalId=?1  ")
    public TerminalEntry findTerminalInfoById(String terminalId);

}
