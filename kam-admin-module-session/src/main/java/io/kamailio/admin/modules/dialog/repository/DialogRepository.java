package io.kamailio.admin.modules.dialog.repository;

import io.kamailio.admin.modules.dialog.entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogRepository extends JpaRepository<Dialog, Integer> {}
