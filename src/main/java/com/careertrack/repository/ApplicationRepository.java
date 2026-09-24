package com.careertrack.repository;

import com.careertrack.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<ApplicationEntity, Long> {

    List<ApplicationEntity> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    Optional<ApplicationEntity> findByIdAndUserId(
            Long id,
            Long userId
    );

    List<ApplicationEntity> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            String status
    );

    List<ApplicationEntity> findByUserIdAndCompanyNameContainingIgnoreCaseOrderByCreatedAtDesc(
            Long userId,
            String companyName
    );

    List<ApplicationEntity> findByUserIdAndPositionContainingIgnoreCaseOrderByCreatedAtDesc(
            Long userId,
            String position
    );
}