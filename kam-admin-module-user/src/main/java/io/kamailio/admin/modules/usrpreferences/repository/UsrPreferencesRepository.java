package io.kamailio.admin.modules.usrpreferences.repository;

import io.kamailio.admin.modules.usrpreferences.entity.UsrPreferences;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsrPreferencesRepository extends JpaRepository<UsrPreferences, Integer> {
    Page<UsrPreferences> findByUsernameContaining(String keyword, Pageable pageable);
    List<UsrPreferences> findByUsernameAndDomain(String username, String domain);
    void deleteByUsernameAndDomain(String username, String domain);
}
