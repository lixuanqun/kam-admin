package io.kamailio.admin.modules.dispatcher.repository;

import io.kamailio.admin.modules.dispatcher.entity.Dispatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DispatcherRepository extends JpaRepository<Dispatcher, Integer> {

    Page<Dispatcher> findByDestinationContaining(String keyword, Pageable pageable);

    List<Dispatcher> findBySetidOrderByPriorityDescIdAsc(Integer setid);

    @Query("SELECT DISTINCT d.setid FROM Dispatcher d ORDER BY d.setid")
    List<Integer> findDistinctSetIds();

    @Query("SELECT d.setid, COUNT(d) FROM Dispatcher d GROUP BY d.setid ORDER BY d.setid")
    List<Object[]> countBySetId();
}
