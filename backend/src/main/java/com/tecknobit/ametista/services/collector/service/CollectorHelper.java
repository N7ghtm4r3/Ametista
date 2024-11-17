package com.tecknobit.ametista.services.collector.service;

import com.tecknobit.ametista.services.applications.service.ApplicationsHelper;
import com.tecknobit.ametista.services.collector.repositories.DevicesRepository;
import com.tecknobit.ametista.services.collector.repositories.IssuesRepository;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.equinox.environment.helpers.services.EquinoxItemsHelper;
import com.tecknobit.equinox.resourcesutils.ResourcesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tecknobit.ametistacore.models.AmetistaDevice.*;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUE_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic.BROWSER_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic.BROWSER_VERSION_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.LAUNCH_TIME_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.*;
import static com.tecknobit.equinox.environment.controllers.EquinoxController.generateIdentifier;

/**
 * The {@code CollectorHelper} class is useful to collect all the issues and analytics data sent by the Ametista Engine
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ApplicationsHelper
 * @see EquinoxItemsHelper
 * @see ResourcesManager
 */
@Service
public class CollectorHelper extends ApplicationsHelper {

    /**
     * {@code timeFormatter} helps to format the temporal value with the specific "dd/MM/yyyy" pattern
     */
    private static final TimeFormatter timeFormatter = TimeFormatter.getInstance("dd/MM/yyyy");

    /**
     * {@code EXCEPTION_NAME_REGEX} regex to validate the exception name
     */
    private static final String EXCEPTION_NAME_REGEX = "\\b(\\w+Exception)\\b";

    /**
     * {@code EXCEPTION_NAME_PATTERN} the pattern to validate the exception names values
     */
    private static final Pattern EXCEPTION_NAME_PATTERN = Pattern.compile(EXCEPTION_NAME_REGEX);

    /**
     * {@code issuesRepository} instance for the issues repository
     */
    @Autowired
    private IssuesRepository issuesRepository;

    /**
     * {@code devicesRepository} instance for the device repository
     */
    @Autowired
    private DevicesRepository devicesRepository;

    /**
     * Method to connect a new platform for the application
     *
     * @param applicationId The identifier of the application
     * @param platform      The platform to connect
     */
    public void connectPlatform(String applicationId, Platform platform) {
        applicationsRepository.connectPlatform(applicationId, platform);
    }

    /**
     * Method to collect a new issue occurred in the application
     *
     * @param applicationId The identifier of the application
     * @param appVersion The application version where the issue occurred
     * @param platform The platform to connect
     * @param hPayload The json helper to manage the json data
     */
    public void collectIssue(String applicationId, String appVersion, Platform platform, JsonHelper hPayload) {
        String issue = hPayload.getString(ISSUE_KEY);
        if (issue == null)
            throw new IllegalArgumentException("Invalid issue");
        String deviceId = saveDevice(hPayload);
        String name = getIssueName(issue);
        String issueId = generateIdentifier();
        long currentDate = timeFormatter.formatNowAsTimestamp();
        if (platform == Platform.WEB) {
            String browser = hPayload.getString(BROWSER_KEY);
            String browserVersion = hPayload.getString(BROWSER_VERSION_KEY);
            if (browser == null || browserVersion == null)
                throw new IllegalArgumentException("Invalid browser details");
            issuesRepository.storeWebIssue(issueId, currentDate, name, appVersion, platform, issue, browserVersion,
                    browserVersion, applicationId, deviceId);
        } else
            issuesRepository.storeIssue(issueId, currentDate, name, appVersion, platform, issue, applicationId, deviceId);
        collectAnalytic(applicationId, appVersion, platform, TOTAL_ISSUES, null);
    }

    /**
     * Method to save a new device in the system if not already exists
     *
     * @param hDevice The json helper to manage the device data
     *
     * @return the device identifier as {@link String}
     */
    private String saveDevice(JsonHelper hDevice) {
        String deviceId = hDevice.getString(DEVICE_IDENTIFIER_KEY);
        if (deviceId == null)
            throw new IllegalArgumentException("Invalid device");
        if (devicesRepository.findById(deviceId).isEmpty()) {
            String brand = hDevice.getString(BRAND_KEY);
            String model = hDevice.getString(MODEL_KEY);
            String os = hDevice.getString(OS_KEY);
            String osVersion = hDevice.getString(OS_VERSION_KEY);
            devicesRepository.saveDevice(deviceId, brand, model, os, osVersion);
        }
        return deviceId;
    }

    /**
     * Method to get with the {@link #EXCEPTION_NAME_PATTERN} the name of the issue
     *
     * @param issue The issue case from extract its name
     *
     * @return the name of the issue as {@link String}
     */
    private String getIssueName(String issue) {
        Matcher matcher = EXCEPTION_NAME_PATTERN.matcher(issue);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    /**
     * Method to collect an analytic for an application and its platform version
     *
     * @param applicationId The identifier of the application
     * @param appVersion The application version
     * @param platform The platform to connect
     * @param type The analytic type to collect
     * @param hPayload The json helper to manage the analytic payload data
     */
    public void collectAnalytic(String applicationId, String appVersion, Platform platform, PerformanceAnalyticType type,
                                JsonHelper hPayload) {
        switch (type) {
            case LAUNCH_TIME -> storeLaunchTime(applicationId, appVersion, platform, hPayload);
            case NETWORK_REQUESTS -> storeAnalytic(applicationId, appVersion, platform, type, 1);
            case TOTAL_ISSUES -> {
                storeAnalytic(applicationId, appVersion, platform, type, 1);
                manageIssuesPerSessionAnalytic(applicationId, appVersion, platform);
            }
        }
    }

    /**
     * Method to collect a {@link PerformanceAnalytic.PerformanceAnalyticType#LAUNCH_TIME} analytic
     *
     * @param applicationId The identifier of the application
     * @param appVersion The application version
     * @param platform The platform to connect
     * @param hPayload The json helper to manage the analytic payload data
     */
    @Wrapper
    private void storeLaunchTime(String applicationId, String appVersion, Platform platform, JsonHelper hPayload) {
        long launchTime = hPayload.getLong(LAUNCH_TIME_KEY, -1);
        storeAnalytic(applicationId, appVersion, platform, LAUNCH_TIME, launchTime);
    }

    /**
     * Method to store an analytic for an application and its platform version
     *
     * @param applicationId The identifier of the application
     * @param appVersion The application version
     * @param platform The platform to connect
     * @param type The analytic type to collect
     * @param value The analytic related value
     */
    private void storeAnalytic(String applicationId, String appVersion, Platform platform,
                               PerformanceAnalyticType type, double value) {
        if (value < 0)
            throw new IllegalArgumentException("Invalid analytic value");
        long currentDate = getCurrentDate();
        PerformanceAnalytic analytic = performanceRepository.getPerformanceAnalyticByDate(applicationId, appVersion,
                platform, type, currentDate);
        if (analytic == null) {
            performanceRepository.storeAnalytic(generateIdentifier(), currentDate, appVersion, platform, 1, type,
                    value, applicationId);
        } else {
            if (type == LAUNCH_TIME) {
                calculateAnalyticValue(analytic, value, currentDate);
                manageIssuesPerSessionAnalytic(applicationId, appVersion, platform);
            } else
                incrementAnalyticValue(analytic, (int) value, currentDate);
        }
    }

    /**
     * Method to calculate the {@link PerformanceAnalytic.PerformanceAnalyticType#ISSUES_PER_SESSION} rate and save it
     *
     * @param applicationId The identifier of the application
     * @param appVersion The application version
     * @param platform The platform to connect
     */
    private void manageIssuesPerSessionAnalytic(String applicationId, String appVersion, Platform platform) {
        long currentDate = getCurrentDate();
        PerformanceAnalytic totalIssues = performanceRepository.getPerformanceAnalyticByDate(applicationId,
                appVersion, platform, TOTAL_ISSUES, currentDate);
        if (totalIssues == null)
            return;
        PerformanceAnalytic sessions = performanceRepository.getPerformanceAnalyticByDate(applicationId,
                appVersion, platform, LAUNCH_TIME, currentDate);
        PerformanceAnalytic issuesPerSession = performanceRepository.getPerformanceAnalyticByDate(applicationId, appVersion,
                platform, ISSUES_PER_SESSION, currentDate);
        double value = totalIssues.getValue() / sessions.getDataUpdates();
        if (issuesPerSession == null) {
            performanceRepository.storeAnalytic(generateIdentifier(), currentDate, appVersion, platform, 1,
                    ISSUES_PER_SESSION, value, applicationId);
        } else {
            int updatesRefreshed = issuesPerSession.getDataUpdates() + 1;
            updateAnalytic(updatesRefreshed, value, issuesPerSession, currentDate);
        }
    }

    /**
     * Method to get the current date formatted with the {@link #timeFormatter}
     *
     * @return the date formatted as {@code long}
     */
    private long getCurrentDate() {
        return timeFormatter.formatAsTimestamp(timeFormatter.formatNowAsString());
    }

    /**
     * Method to calculate and store the value of an analytic
     *
     * @param analytic    The analytic type to calculate
     * @param value       The initial value from calculate the new one
     * @param currentDate The current date where store the analytic
     */
    private void calculateAnalyticValue(PerformanceAnalytic analytic, double value, long currentDate) {
        int currentUpdates = analytic.getDataUpdates();
        int updatesRefreshed = currentUpdates + 1;
        double updatedValue = (((analytic.getValue() * currentUpdates) + value) / updatesRefreshed);
        updateAnalytic(updatesRefreshed, updatedValue, analytic, currentDate);
    }

    /**
     * Method to increment and store the value of an analytic
     *
     * @param analytic The analytic type to calculate
     * @param incrementValue The value to use as increment
     * @param currentDate The current date where store the analytic
     */
    private void incrementAnalyticValue(PerformanceAnalytic analytic, int incrementValue, long currentDate) {
        int currentUpdates = analytic.getDataUpdates();
        int updatesRefreshed = currentUpdates + incrementValue;
        double updatedValue = analytic.getValue() + incrementValue;
        updateAnalytic(updatesRefreshed, updatedValue, analytic, currentDate);
    }

    /**
     * Method to update and then store the updated analytic
     *
     * @param updatesRefreshed The number of the updates refreshed executed
     * @param updatedValue The updated value
     * @param analytic The analytic to store
     * @param currentDate The current date where store the analytic
     */
    private void updateAnalytic(int updatesRefreshed, double updatedValue, PerformanceAnalytic analytic,
                                long currentDate) {
        performanceRepository.updateAnalytic(updatesRefreshed, updatedValue, analytic.getId(), currentDate,
                analytic.getAppVersion(), analytic.getPlatform(), analytic.getPerformanceAnalyticType(),
                analytic.getApplication().getId());
    }

}
