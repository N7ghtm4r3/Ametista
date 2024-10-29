package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationsHelper {

    public static final String DEFAULT_PLATFORMS_FILTER = "ANDROID, IOS, DESKTOP, WEB";

    @Autowired
    private ApplicationsRepository applicationsRepository;

    public PaginatedResponse<AmetistaApplication> getApplications(int page, int pageSize, String name, String platforms) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<String> queryPlatforms = formatPlatformAsQuery(platforms);
        List<AmetistaApplication> applications = applicationsRepository.getApplications(name, queryPlatforms, pageable);
        return new PaginatedResponse<>(applications, page, pageSize, applicationsRepository);
    }

    private List<String> formatPlatformAsQuery(String platforms) {
        return List.of(platforms.replaceAll(" ", "").split(","));
    }

}
