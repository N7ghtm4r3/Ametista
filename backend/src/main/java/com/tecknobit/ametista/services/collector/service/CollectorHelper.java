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

@Service
public class CollectorHelper extends ApplicationsHelper {

    private static final TimeFormatter timeFormatter = TimeFormatter.getInstance("dd/MM/yyyy");

    private static final String EXCEPTION_NAME_REGEX = "\\b(\\w+Exception)\\b";

    private static final Pattern EXCEPTION_NAME_PATTERN = Pattern.compile(EXCEPTION_NAME_REGEX);

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    public void connectPlatform(String applicationId, Platform platform) {
        applicationsRepository.connectPlatform(applicationId, platform);
    }

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

    private String getIssueName(String issue) {
        Matcher matcher = EXCEPTION_NAME_PATTERN.matcher(issue);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

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

    @Wrapper
    private void storeLaunchTime(String applicationId, String appVersion, Platform platform, JsonHelper hPayload) {
        long launchTime = hPayload.getLong(LAUNCH_TIME_KEY, -1);
        storeAnalytic(applicationId, appVersion, platform, LAUNCH_TIME, launchTime);
    }

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
                computeAnalyticValue(analytic, value, currentDate);
                manageIssuesPerSessionAnalytic(applicationId, appVersion, platform);
            } else
                incrementAnalyticValue(analytic, (int) value, currentDate);
        }
    }

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

    private long getCurrentDate() {
        return timeFormatter.formatAsTimestamp(timeFormatter.formatNowAsString());
    }

    private void computeAnalyticValue(PerformanceAnalytic analytic, double value, long currentDate) {
        int currentUpdates = analytic.getDataUpdates();
        int updatesRefreshed = currentUpdates + 1;
        double updatedValue = (((analytic.getValue() * currentUpdates) + value) / updatesRefreshed);
        updateAnalytic(updatesRefreshed, updatedValue, analytic, currentDate);
    }

    private void incrementAnalyticValue(PerformanceAnalytic analytic, int incrementValue, long currentDate) {
        int currentUpdates = analytic.getDataUpdates();
        int updatesRefreshed = currentUpdates + incrementValue;
        double updatedValue = analytic.getValue() + incrementValue;
        updateAnalytic(updatesRefreshed, updatedValue, analytic, currentDate);
    }

    private void updateAnalytic(int updatesRefreshed, double updatedValue, PerformanceAnalytic analytic,
                                long currentDate) {
        performanceRepository.updateAnalytic(updatesRefreshed, updatedValue, analytic.getId(), currentDate,
                analytic.getAppVersion(), analytic.getPlatform(), analytic.getPerformanceAnalyticType(),
                analytic.getApplication().getId());
    }

}
