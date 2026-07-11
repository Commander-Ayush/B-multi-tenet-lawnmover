package com.growthmul.app.lawnmover_fs.repository;

import com.growthmul.app.lawnmover_fs.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findByCompanyIdAndApprovedTrueOrderBySubmittedAtDesc(Long companyId);

    List<Review> findByCompanyIdOrderBySubmittedAtDesc(Long companyId);

    long countByCompanyIdAndApprovedFalse(Long companyId);
}
