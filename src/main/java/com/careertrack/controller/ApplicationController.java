package com.careertrack.controller;

import com.careertrack.dto.ApplicationResponse;
import com.careertrack.entity.ApplicationEntity;
import com.careertrack.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
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
            @RequestBody ApplicationRequest request,
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
            @RequestBody ApplicationRequest request,
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

    private Long getUserId(HttpServletRequest request) {

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
            String companyName,
            String position,
            String status,
            LocalDate applicationDate,
            String jobUrl,
            String location,
            String notes
    ) {
    }
}