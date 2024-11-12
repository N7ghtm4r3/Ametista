package com.tecknobit.ametista.services.collector.service;

import com.tecknobit.ametista.services.applications.service.ApplicationsHelper;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import org.springframework.stereotype.Service;

import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.LAUNCH_TIME_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.LAUNCH_TIME;
import static com.tecknobit.equinox.environment.controllers.EquinoxController.generateIdentifier;

@Service
public class CollectorHelper extends ApplicationsHelper {

    private static final TimeFormatter timeFormatter = TimeFormatter.getInstance("dd/MM/yyyy");

    public void connectPlatform(String applicationId, Platform platform) {
        applicationsRepository.connectPlatform(applicationId, platform);
    }

    public void collectAnalytic(String applicationId, String appVersion, Platform platform, PerformanceAnalyticType type,
                                JsonHelper hPayload) {
        if (type == LAUNCH_TIME)
            storeLaunchTime(applicationId, appVersion, platform, hPayload);
        else
            storeAnalytic(applicationId, appVersion, platform, type, 1);
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
        long currentDate = timeFormatter.formatAsTimestamp(timeFormatter.formatNowAsString());
        PerformanceAnalytic analytic = performanceRepository.getPerformanceAnalyticByDate(applicationId, appVersion,
                platform, type, currentDate);
        if (analytic == null) {
            performanceRepository.storeAnalytic(generateIdentifier(), currentDate, appVersion, platform, 1, type,
                    value, applicationId);
        } else {
            switch (type) {
                case LAUNCH_TIME, ISSUES_PER_SESSION -> computeAnalyticValue(analytic, value, currentDate);
                default -> incrementAnalyticValue(analytic, (int) value, currentDate);
            }
        }
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
