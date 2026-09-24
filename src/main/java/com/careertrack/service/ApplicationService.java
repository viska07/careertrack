package com.careertrack.service;

import com.careertrack.entity.ApplicationEntity;
import com.careertrack.entity.User;
import com.careertrack.repository.ApplicationRepository;
import com.careertrack.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    private static final Set<String> VALID_STATUSES = Set.of(
            "WISHLIST",
            "APPLIED",
            "INTERVIEW",
            "OFFER",
            "REJECTED"
    );

    public ApplicationService(
            ApplicationRepository applicationRepository,
            UserRepository userRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public List<ApplicationEntity> getApplicationsByUserId(
            Long userId,
            String search,
            String status
    ) {

        if (status != null && !status.isBlank()) {
            validateStatus(status);
        }

        List<ApplicationEntity> applications;

        if (search != null && !search.isBlank()) {

            String keyword = search.trim();

            List<ApplicationEntity> byCompany =
                    applicationRepository
                            .findByUserIdAndCompanyNameContainingIgnoreCaseOrderByCreatedAtDesc(
                                    userId,
                                    keyword
                            );

            List<ApplicationEntity> byPosition =
                    applicationRepository
                            .findByUserIdAndPositionContainingIgnoreCaseOrderByCreatedAtDesc(
                                    userId,
                                    keyword
                            );

            applications = new ArrayList<>(byCompany);

            for (ApplicationEntity application : byPosition) {

                if (!applications.contains(application)) {
                    applications.add(application);
                }
            }

            applications.sort(
                    (first, second) ->
                            second.getCreatedAt()
                                    .compareTo(first.getCreatedAt())
            );

        } else if (status != null && !status.isBlank()) {

            applications =
                    applicationRepository
                            .findByUserIdAndStatusOrderByCreatedAtDesc(
                                    userId,
                                    status
                            );

        } else {

            applications =
                    applicationRepository
                            .findByUserIdOrderByCreatedAtDesc(
                                    userId
                            );
        }

        if (status != null && !status.isBlank()
                && search != null && !search.isBlank()) {

            applications = applications.stream()
                    .filter(application ->
                            application.getStatus()
                                    .equals(status)
                    )
                    .toList();
        }

        return applications;
    }

    public ApplicationEntity getApplicationById(
            Long applicationId,
            Long userId
    ) {

        return applicationRepository
                .findByIdAndUserId(
                        applicationId,
                        userId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Application tidak ditemukan"
                        )
                );
    }

    public ApplicationEntity createApplication(
            Long userId,
            String companyName,
            String position,
            String status,
            LocalDate applicationDate,
            String jobUrl,
            String location,
            String notes
    ) {

        validateStatus(status);

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User tidak ditemukan"
                                )
                        );

        ApplicationEntity application =
                new ApplicationEntity();

        application.setUser(user);
        application.setCompanyName(
                companyName.trim()
        );
        application.setPosition(
                position.trim()
        );
        application.setStatus(
                status.trim()
        );
        application.setApplicationDate(
                applicationDate
        );
        application.setJobUrl(
                jobUrl
        );
        application.setLocation(
                location
        );
        application.setNotes(
                notes
        );

        LocalDateTime now =
                LocalDateTime.now();

        application.setCreatedAt(now);
        application.setUpdatedAt(now);

        return applicationRepository.save(
                application
        );
    }

    public ApplicationEntity updateApplication(
            Long applicationId,
            Long userId,
            String companyName,
            String position,
            String status,
            LocalDate applicationDate,
            String jobUrl,
            String location,
            String notes
    ) {

        validateStatus(status);

        ApplicationEntity application =
                applicationRepository
                        .findByIdAndUserId(
                                applicationId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application tidak ditemukan"
                                )
                        );

        application.setCompanyName(
                companyName.trim()
        );
        application.setPosition(
                position.trim()
        );
        application.setStatus(
                status.trim()
        );
        application.setApplicationDate(
                applicationDate
        );
        application.setJobUrl(
                jobUrl
        );
        application.setLocation(
                location
        );
        application.setNotes(
                notes
        );
        application.setUpdatedAt(
                LocalDateTime.now()
        );

        return applicationRepository.save(
                application
        );
    }

    public void deleteApplication(
            Long applicationId,
            Long userId
    ) {

        ApplicationEntity application =
                applicationRepository
                        .findByIdAndUserId(
                                applicationId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application tidak ditemukan"
                                )
                        );

        applicationRepository.delete(
                application
        );
    }

    private void validateStatus(
            String status
    ) {

        if (status == null ||
                !VALID_STATUSES.contains(
                        status.trim()
                )) {

            throw new IllegalArgumentException(
                    "Status tidak valid. Gunakan: " +
                    "WISHLIST, APPLIED, INTERVIEW, " +
                    "OFFER, atau REJECTED"
            );
        }
    }
}