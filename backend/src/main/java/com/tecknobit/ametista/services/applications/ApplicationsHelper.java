package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametista.helpers.resources.AmetistaResourcesManager;
import com.tecknobit.ametista.services.applications.repositories.AnalyticIssuesRepository;
import com.tecknobit.ametista.services.applications.repositories.ApplicationsRepository;
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import com.tecknobit.apimanager.annotations.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tecknobit.equinox.environment.controllers.EquinoxController.generateIdentifier;

@Service
public class ApplicationsHelper implements AmetistaResourcesManager {

    public static final String DEFAULT_PLATFORMS_FILTER = "ANDROID,IOS,DESKTOP,WEB";

    private static final String DATE_REGEX = "\\b(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/\\d{4}(\\s+([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])?\\b";

    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);

    private static final String VERSION_REGEX = "\\b(v?)(\\d+(\\.\\d+)*)([-_]?([a-zA-Z]+[-_]?\\d*)?)?\\b";

    private static final Pattern VERSION_PATTERN = Pattern.compile(VERSION_REGEX);

    private static final String BRAND_REGEX = "^(Pixel|Apple|Samsung|Google|OnePlus|Sony|Motorola|Oppo|Vivo|Nokia|HTC|Asus|Realme|Honor|ZTE|Dell|HP|Lenovo|Acer|Razer|Microsoft|MSI|Toshiba|LG|Huawei|Amazon|Xiaomi|RCA|iMac|Alienware|ASUS ROG|HP Omen|Gigabyte AORUS|Corsair|Lenovo Legion|Dell G Series)$";

    private static final Pattern BRAND_PATTERN = Pattern.compile(BRAND_REGEX);

    private static final String MODEL_REGEX = "^(\\d{1,2}[A-Za-z]?[- ]?\\d?[A-Za-z]?|[A-Za-z]+[ -]?\\d{1,2}([A-Za-z]?|\\d*)|[A-Za-z0-9]+[- ]?\\d{1,2}|\\d{1,2}[- ]?[A-Za-z0-9]+|[A-Za-z0-9]+[ -]?[A-Za-z0-9]+|\\d{1,2}[A-Za-z]? ?[A-Za-z]+|\\w+ ?[A-Za-z0-9]+)$";

    private static final Pattern MODEL_PATTERN = Pattern.compile(MODEL_REGEX);

    private static final String BROWSER_REGEX = "^(Google Chrome|Safari|Microsoft Edge|Mozilla Firefox|Opera|Samsung Internet|Brave|Vivaldi|Tor Browser|Epic Privacy Browser|Maxthon)$";

    private static final Pattern BROWSER_PATTERN = Pattern.compile(BROWSER_REGEX);

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

    public PaginatedResponse<IssueAnalytic> getIssues(AmetistaApplication application, int page, int pageSize,
                                                      Platform platform, Set<String> filters) {
        CopyOnWriteArraySet<String> concurrentFilters = new CopyOnWriteArraySet<>(filters);
        String applicationId = application.getId();
        String platformName = platform.name();
        Pageable pageable = PageRequest.of(page, pageSize);
        HashSet<String> dates = getDateFilters(concurrentFilters);
        HashSet<String> versions = getVersionFilters(concurrentFilters);
        HashSet<String> brands = getBrandFilters(concurrentFilters);
        HashSet<String> models = getModelFilters(concurrentFilters);
        List<IssueAnalytic> issues = List.of();
        if (platform == Platform.WEB) {
            HashSet<String> browsers = getBrowserFilters(concurrentFilters);
            //issues = issuesRepository.getIssues(applicationId, platformName, pageable);
        } else
            issues = issuesRepository.getIssues(applicationId, platformName, dates, versions, brands, models, pageable);
        long totalIssues = issuesRepository.countIssuesPerPlatform(applicationId, platformName);
        return new PaginatedResponse<>(issues, page, pageSize, totalIssues);
    }

    @Wrapper
    private HashSet<String> getDateFilters(CopyOnWriteArraySet<String> filters) {
        return getFiltersList(filters, DATE_PATTERN);
    }

    @Wrapper
    private HashSet<String> getVersionFilters(CopyOnWriteArraySet<String> filters) {
        return getFiltersList(filters, VERSION_PATTERN);
    }

    @Wrapper
    private HashSet<String> getBrandFilters(CopyOnWriteArraySet<String> filters) {
        return getFiltersList(filters, BRAND_PATTERN);
    }

    @Wrapper
    private HashSet<String> getModelFilters(CopyOnWriteArraySet<String> filters) {
        return getFiltersList(filters, MODEL_PATTERN);
    }

    @Wrapper
    private HashSet<String> getBrowserFilters(CopyOnWriteArraySet<String> filters) {
        return getFiltersList(filters, BROWSER_PATTERN);
    }

    private HashSet<String> getFiltersList(CopyOnWriteArraySet<String> filters, Pattern pattern) {
        HashSet<String> filtersList = new HashSet<>();
        for (String filter : filters) {
            Matcher matcher = pattern.matcher(filter);
            if (matcher.matches()) {
                filtersList.add(filter.trim());
                filters.remove(filter);
            }
        }
        return filtersList;
    }

    public void deleteApplication(String applicationId) {
        applicationsRepository.deleteApplication(applicationId);
        deleteAppIcon(applicationId);
    }

    public record ApplicationPayload(MultipartFile icon, String name, String description) {

    }

}
