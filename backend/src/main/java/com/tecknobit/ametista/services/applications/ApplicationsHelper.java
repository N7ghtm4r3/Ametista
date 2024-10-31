package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametista.helpers.queries.issues.IssuesQuery;
import com.tecknobit.ametista.helpers.queries.issues.WebIssuesQuery;
import com.tecknobit.ametista.helpers.resources.AmetistaResourcesManager;
import com.tecknobit.ametista.services.applications.repositories.AnalyticIssuesRepository;
import com.tecknobit.ametista.services.applications.repositories.ApplicationsRepository;
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import com.tecknobit.equinox.environment.helpers.services.EquinoxItemsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.tecknobit.equinox.environment.controllers.EquinoxController.generateIdentifier;

@Service
public class ApplicationsHelper extends EquinoxItemsHelper<IssueAnalytic> implements AmetistaResourcesManager {

    public static final String DEFAULT_PLATFORMS_FILTER = "ANDROID,IOS,DESKTOP,WEB";

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @Autowired
    private AnalyticIssuesRepository issuesRepository;

    public PaginatedResponse<AmetistaApplication> getApplications(int page, int pageSize, String name,
                                                                  List<String> platforms) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AmetistaApplication> applications = applicationsRepository.getApplications(name, platforms, pageable);
        return new PaginatedResponse<>(applications, page, pageSize, applicationsRepository);
    }

    public boolean applicationExists(String applicationId) {
        return applicationsRepository.findById(applicationId).isPresent();
    }

    public void saveApplication(ApplicationPayload payload) throws IOException {
        String applicationId = generateIdentifier();
        MultipartFile icon = payload.icon;
        String iconPath = createAppIcon(icon, applicationId);
        applicationsRepository.saveApplication(
                applicationId,
                System.currentTimeMillis(),
                payload.name,
                payload.description,
                iconPath
        );
        saveResource(icon, iconPath);
    }

    public void editApplication(String applicationId, ApplicationPayload payload) throws IOException {
        applicationsRepository.editApplication(
                applicationId,
                payload.name,
                payload.description
        );
        MultipartFile newIcon = payload.icon;
        if (newIcon != null && !newIcon.isEmpty()) {
            String iconPath = createAppIcon(newIcon, applicationId);
            saveResource(newIcon, iconPath);
        }
    }

    public Optional<AmetistaApplication> getApplication(String applicationId) {
        return applicationsRepository.findById(applicationId);
    }

    public <T extends IssueAnalytic> PaginatedResponse<T> getIssues(AmetistaApplication application, int page,
                                                                    int pageSize, Platform platform, Set<String> filters) {
        String applicationId = application.getId();
        Pageable pageable = PageRequest.of(page, pageSize);
        List<T> issues;
        if (platform == Platform.WEB) {
            WebIssuesQuery webIssuesQuery = new WebIssuesQuery(entityManager, applicationId, filters);
            issues = (List<T>) webIssuesQuery.getIssues(pageable);
        } else {
            IssuesQuery<IssueAnalytic> issuesQuery = new IssuesQuery<>(IssueAnalytic.class, entityManager, platform,
                    applicationId, filters);
            issues = (List<T>) issuesQuery.getIssues(pageable);
        }
        long totalIssues = issuesRepository.countIssuesPerPlatform(applicationId, platform.name()) - issues.size();
        return new PaginatedResponse<>(issues, page, pageSize, totalIssues);
    }

    public void deleteApplication(String applicationId) {
        applicationsRepository.deleteApplication(applicationId);
        deleteAppIcon(applicationId);
    }

    public record ApplicationPayload(MultipartFile icon, String name, String description) {

    }

}
