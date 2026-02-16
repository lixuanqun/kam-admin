package io.kamailio.admin.modules.permissions.repository;

import io.kamailio.admin.modules.permissions.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Page<Address> findByIpAddrContaining(String keyword, Pageable pageable);
    @Query("SELECT a.grp, COUNT(a) FROM Address a GROUP BY a.grp ORDER BY a.grp")
    List<Object[]> countByGrp();
}
