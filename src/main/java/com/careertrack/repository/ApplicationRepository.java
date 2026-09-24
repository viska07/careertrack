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

    List<ApplicationEntity> findByUserIdAndStatus(
            Long userId,
            String status
    );

    List<ApplicationEntity> findByUserIdAndCompanyNameContainingIgnoreCase(
            Long userId,
            String companyName
    );

    List<ApplicationEntity> findByUserIdAndPositionContainingIgnoreCase(
            Long userId,
            String position
    );
}