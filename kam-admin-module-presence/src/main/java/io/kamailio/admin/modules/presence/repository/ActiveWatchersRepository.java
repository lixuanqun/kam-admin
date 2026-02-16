package io.kamailio.admin.modules.presence.repository;

import io.kamailio.admin.modules.presence.entity.ActiveWatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveWatchersRepository extends JpaRepository<ActiveWatchers, Integer> {
    Page<ActiveWatchers> findByWatcherUsernameContaining(String keyword, Pageable pageable);
}
