// OrderRepository.java
package com.growthmul.app.lawnmover_fs.repository;

import com.growthmul.app.lawnmover_fs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCompanyIdOrderBySubmittedAtDesc(Long companyId);
    long countByCompanyId(Long companyId);
}