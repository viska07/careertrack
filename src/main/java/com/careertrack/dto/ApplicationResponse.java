package com.careertrack.dto;

import com.careertrack.entity.ApplicationEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long userId,
        String companyName,
        String position,
        String status,
        LocalDate applicationDate,
        String jobUrl,
        String location,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ApplicationResponse from(
            ApplicationEntity application
    ) {
        return new ApplicationResponse(
                application.getId(),
                application.getUser().getId(),
                application.getCompanyName(),
                application.getPosition(),
                application.getStatus(),
                application.getApplicationDate(),
                application.getJobUrl(),
                application.getLocation(),
                application.getNotes(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}