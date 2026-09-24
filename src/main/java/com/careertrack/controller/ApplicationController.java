package com.careertrack.controller;

import com.careertrack.dto.ApplicationResponse;
import com.careertrack.entity.ApplicationEntity;
import com.careertrack.service.ApplicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService
    ) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<?> getApplications(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            HttpServletRequest request
    ) {

        Long userId = getUserId(request);

        List<ApplicationEntity> applications =
                applicationService.getApplicationsByUserId(
                        userId,
                        search,
                        status
                );

        List<ApplicationResponse> response =
                applications.stream()
                        .map(ApplicationResponse::from)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplication(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        Long userId = getUserId(request);

        ApplicationEntity application =
                applicationService.getApplicationById(
                        id,
                        userId
                );

        return ResponseEntity.ok(
                ApplicationResponse.from(application)
        );
    }

    @PostMapping
    public ResponseEntity<?> createApplication(
            @Valid @RequestBody ApplicationRequest request,
            HttpServletRequest httpRequest
    ) {

        Long userId = getUserId(httpRequest);

        ApplicationEntity application =
                applicationService.createApplication(
                        userId,
                        request.companyName(),
                        request.position(),
                        request.status(),
                        request.applicationDate(),
                        request.jobUrl(),
                        request.location(),
                        request.notes()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApplicationResponse.from(application)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequest request,
            HttpServletRequest httpRequest
    ) {

        Long userId = getUserId(httpRequest);

        ApplicationEntity application =
                applicationService.updateApplication(
                        id,
                        userId,
                        request.companyName(),
                        request.position(),
                        request.status(),
                        request.applicationDate(),
                        request.jobUrl(),
                        request.location(),
                        request.notes()
                );

        return ResponseEntity.ok(
                ApplicationResponse.from(application)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        Long userId = getUserId(request);

        applicationService.deleteApplication(
                id,
                userId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Application berhasil dihapus"
                )
        );
    }

    private Long getUserId(
            HttpServletRequest request
    ) {

        Object userIdAttribute =
                request.getAttribute("userId");

        if (userIdAttribute == null) {
            throw new RuntimeException(
                    "User belum terautentikasi"
            );
        }

        return (Long) userIdAttribute;
    }

    public record ApplicationRequest(

            @NotBlank(
                    message = "Company name is required"
            )
            @Size(
                    max = 150,
                    message = "Company name must not exceed 150 characters"
            )
            String companyName,

            @NotBlank(
                    message = "Position is required"
            )
            @Size(
                    max = 150,
                    message = "Position must not exceed 150 characters"
            )
            String position,

            @NotBlank(
                    message = "Status is required"
            )
            String status,

            LocalDate applicationDate,

            @URL(
                    message = "Job URL must be a valid URL"
            )
            @Size(
                    max = 500,
                    message = "Job URL must not exceed 500 characters"
            )
            String jobUrl,

            @Size(
                    max = 150,
                    message = "Location must not exceed 150 characters"
            )
            String location,

            String notes

    ) {
    }
}