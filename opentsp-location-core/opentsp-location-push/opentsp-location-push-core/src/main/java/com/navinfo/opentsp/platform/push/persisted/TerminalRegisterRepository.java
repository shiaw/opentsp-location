package com.navinfo.opentsp.platform.push.persisted;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository which store tasks with persisted flag
 */
@Repository
public interface TerminalRegisterRepository extends JpaRepository<TerminalRegisterEntry, Long> {

    @Query("select t from TerminalRegisterEntry t where terminalId = ?1")
    TerminalRegisterEntry findByTerminalId(String terminalId);
}
