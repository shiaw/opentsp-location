package com.navinfo.opentsp.platform.push.persisted;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * Repository which store tasks with persisted flag
 */
public interface TasksRepository extends JpaRepository<TaskEntry, Long> {
    @Query("select t from TaskEntry t where t.end != true and t.originalId = ?1")
    TaskEntry findByOriginalId(String originalId);

    @Query("select t.originalId from TaskEntry t where (t.end is null or t.end = 0) and exists (" +
              "select d.id from t.deliverings d where (d.status is null or d.status != com.navinfo.opentsp.platform.push.api.DeliveringStatus.OK and d.status != com.navinfo.opentsp.platform.push.api.DeliveringStatus.FAIL)" +
           ")")
    Collection<String> findAllOriginalIds();

    @Modifying
    @Query("update TaskEntry t set t.end = ?2 where t.originalId = ?1")
    void updateEnd(String id, boolean end);
}
