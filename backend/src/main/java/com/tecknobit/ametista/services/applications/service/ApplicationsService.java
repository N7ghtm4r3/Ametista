package com.tecknobit.ametista.services.applications.service;

import com.tecknobit.ametista.configuration.resources.AmetistaResourcesManager;
import com.tecknobit.ametista.services.applications.entities.AmetistaApplication;
import com.tecknobit.ametista.services.applications.helpers.issues.IssuesQuery;
import com.tecknobit.ametista.services.applications.helpers.issues.WebIssuesQuery;
import com.tecknobit.ametista.services.applications.helpers.performance.PerformanceDataFetcher;
import com.tecknobit.ametista.services.applications.repositories.ApplicationsRepository;
import com.tecknobit.ametista.services.applications.repositories.PerformanceRepository;
import com.tecknobit.ametista.services.collector.dtos.PerformanceData;
import com.tecknobit.ametista.services.collector.entities.issues.IssueAnalytic;
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper;
import com.tecknobit.equinoxbackend.resourcesutils.ResourcesManager;
import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.tecknobit.ametista.services.collector.dtos.PerformanceData.PerformanceDataItem;
import static com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController.generateIdentifier;


/**
 * The {@code ApplicationsService} class is useful to manage all the user application operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItemsHelper
 * @see ResourcesManager
 */
@Service
public class ApplicationsService extends EquinoxItemsHelper implements AmetistaResourcesManager {

    /**
     * {@code DEFAULT_PLATFORMS_FILTER} the default value of the platforms filter
     */
    public static final String DEFAULT_PLATFORMS_FILTER = "";

    /**
     * {@code applicationsRepository} instance for the application repository
     */
    @Autowired
    protected ApplicationsRepository applicationsRepository;

    /**
     * {@code performanceRepository} instance for the performance repository
     */
    @Autowired
    protected PerformanceRepository performanceRepository;

    /**
     * Method to get the applications list registered in the system
     *
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param name      The application name used as filter
     * @param platforms The list of platforms used as filter
     */
    public PaginatedResponse<AmetistaApplication> getApplications(int page, int pageSize, String name,
                                                                  List<String> platforms) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AmetistaApplication> applications = applicationsRepository.getApplications(name, platforms, pageable);
        return new PaginatedResponse<>(applications, page, pageSize, applicationsRepository.count());
    }

    /**
     * Method to get whether the application is present in the database
     *
     * @param applicationId The identifier of the application to check
     *
     * @return whether the application exists as {@code boolean}
     */
    public boolean applicationExists(String applicationId) {
        return applicationsRepository.findById(applicationId).isPresent();
    }

    /**
     * Method to save and add in the system a new application
     *
     * @param payload The payload with the data to use to register the new application
     *
     * @throws IOException when an error occurred during the media operation
     */
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

    /**
     * Method to edit an existing application
     *
     * @param applicationId The identifier of the application to edit
     * @param payload The payload with the data to use to edit an existing application
     *
     * @throws IOException when an error occurred during the media operation
     */
    public void editApplication(String applicationId, ApplicationPayload payload) throws IOException {
        MultipartFile newIcon = payload.icon;
        if (newIcon != null && !newIcon.isEmpty()) {
            String iconPath = createAppIcon(newIcon, applicationId + System.currentTimeMillis());
            applicationsRepository.editApplication(
                    applicationId,
                    payload.name,
                    payload.description,
                    iconPath
            );
            deleteAppIcon(applicationId);
            saveResource(newIcon, iconPath);
        } else {
            applicationsRepository.editApplication(
                    applicationId,
                    payload.name,
                    payload.description
            );
        }
    }

    /**
     * Method to get whether the application is present in the database
     *
     * @param applicationId The identifier of the application to check
     *
     * @return whether the application exists as {@link Optional} of {@link AmetistaApplication}
     */
    public Optional<AmetistaApplication> getApplication(String applicationId) {
        return applicationsRepository.findById(applicationId);
    }

    /**
     * Method to get the issues related to an application
     *
     * @param application The application of retrieve the related issues
     * @param page The page requested
     * @param pageSize The size of the items to insert in the page
     * @param platform The platform of retrieve the related issues
     * @param filters The filters to use for the issue selection
     *
     * @return the list of issues occurred in the specified application as {@link List} of {@link T}
     *
     * @param <T> The type of the issue to retrieve
     */
    public <T extends IssueAnalytic> PaginatedResponse<T> getIssues(AmetistaApplication application, int page,
                                                                    int pageSize, Platform platform, Set<String> filters) {
        String applicationId = application.getId();
        Pageable pageable = PageRequest.of(page, pageSize);
        List<T> issues;
        long totalIssues;
        if (platform == Platform.WEB) {
            WebIssuesQuery webIssuesQuery = new WebIssuesQuery(entityManager, applicationId, filters);
            issues = (List<T>) webIssuesQuery.getEntities(pageable);
            totalIssues = webIssuesQuery.getEntities().size();
        } else {
            IssuesQuery<IssueAnalytic> issuesQuery = new IssuesQuery<>(IssueAnalytic.class, entityManager, platform,
                    applicationId, filters);
            issues = (List<T>) issuesQuery.getEntities(pageable);
            totalIssues = issuesQuery.getEntities().size();
        }
        return new PaginatedResponse<>(issues, page, pageSize, totalIssues);
    }

    /**
     * Method to get the performance data of an application
     *
     * @param applicationId The application identifier of retrieve the related performance data
     * @param platform The platform of retrieve the related performance data
     * @param filters The filters to use for the performance data selection
     *
     * @return the performance data as {@link PerformanceData}
     *
     */
    public PerformanceData getPerformanceData(String applicationId, Platform platform, JsonHelper filters) {
        String platformName = platform.name();
        PerformanceDataFetcher payloadFetcher = new PerformanceDataFetcher(applicationId, platformName, filters,
                performanceRepository);
        PerformanceDataItem launchTimes = payloadFetcher.getLaunchTimeData();
        PerformanceDataItem networkRequests = payloadFetcher.getNetworkRequestsData();
        PerformanceDataItem totalIssues = payloadFetcher.getTotalIssuesData();
        PerformanceDataItem issuesPerSession = payloadFetcher.getIssuesPerSessionData();
        return new PerformanceData(launchTimes, networkRequests, totalIssues, issuesPerSession);
    }

    /**
     * Method to get all the available versions target for a specific analytic
     *
     * @param applicationId The application identifier related to the performance data collected
     * @param platform The platform related to the performance data collected
     * @param analyticType The specific performance data to retrieve
     *
     * @return all the available versions target as {@link List} of {@link String}
     */
    public List<String> getVersionSamples(String applicationId, Platform platform, PerformanceAnalyticType analyticType) {
        return performanceRepository.getAllVersionsTarget(applicationId, platform.name(), analyticType);
    }

    /**
     * Method to delete an existing application
     *
     * @param applicationId The identifier of the application
     */
    public void deleteApplication(String applicationId) {
        applicationsRepository.deleteApplication(applicationId);
        deleteAppIcon(applicationId);
    }

    /**
     * Record class useful to manage the {@link #saveApplication(ApplicationPayload)} or {@link #editApplication(String, ApplicationPayload)}
     * operation
     *
     * @param icon The icon of the application
     * @param name The name of the application
     * @param description The description of the application
     */
    public record ApplicationPayload(MultipartFile icon, String name, String description) {
    }

}
