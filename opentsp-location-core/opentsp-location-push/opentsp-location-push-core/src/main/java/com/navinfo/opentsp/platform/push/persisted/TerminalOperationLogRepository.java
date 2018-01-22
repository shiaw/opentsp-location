package com.navinfo.opentsp.platform.push.persisted;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository which store tasks with persisted flag
 */
@Repository
public interface TerminalOperationLogRepository extends JpaRepository<TerminalOperationLogEntry, Long> {

    @Query("select t from TerminalOperationLogEntry t where terminalId = ?1")
    TerminalOperationLogEntry findByTerminalId(String terminalId);

    @Query("select t from TerminalOperationLogEntry t where operationId = ?1")
    TerminalOperationLogEntry findByOperationId(String operationId);

    @Modifying
    @Query("update TerminalOperationLogEntry t set t.operatorResult = ?2 where t.operationId = ?1")
    void updateOperatorResult(String operationId, String operatorResult);
}
