package com.navinfo.opentsp.platform.push.persisted;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link com.navinfo.opentsp.platform.push.persisted.DeliveringEntry }
 */
public interface DeliveringsRepository extends JpaRepository<DeliveringEntry, Long> {
}
