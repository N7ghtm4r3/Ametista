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

    @Autowired
    private ApplicationsRepository applicationsRepository;

    public PaginatedResponse<AmetistaApplication> getApplications(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AmetistaApplication> applications = applicationsRepository.getApplications(pageable);
        return new PaginatedResponse<>(applications, page, pageSize, applicationsRepository);
    }

}
