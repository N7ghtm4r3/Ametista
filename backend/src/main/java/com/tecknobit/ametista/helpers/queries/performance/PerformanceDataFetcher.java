package com.tecknobit.ametista.helpers.queries.performance;

import com.tecknobit.ametista.services.applications.repositories.PerformanceRepository;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.apimanager.formatters.JsonHelper;
import kotlin.Pair;
import org.json.JSONObject;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.MAX_VERSION_SAMPLES;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.FINAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.INITIAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.*;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem.MAX_TEMPORAL_RANGE;

public class PerformanceDataFetcher {

    private static final JsonHelper EMPTY_JSON_HELPER = new JsonHelper(new JSONObject());

    private final String applicationId;

    private final String platformName;

    private final JsonHelper hPayload;

    private final PerformanceRepository performanceRepository;

    public PerformanceDataFetcher(String applicationId, String platformName, JsonHelper filters,
                                  PerformanceRepository performanceRepository) {
        this.applicationId = applicationId;
        this.platformName = platformName;
        this.hPayload = filters;
        this.performanceRepository = performanceRepository;
    }

    @Wrapper
    public PerformanceDataItem getLaunchTimeData() {
        return getPerformanceData(LAUNCH_TIME);
    }

    @Wrapper
    public PerformanceDataItem getNetworkRequestsData() {
        return getPerformanceData(NETWORK_REQUESTS);
    }

    @Wrapper
    public PerformanceDataItem getTotalIssuesData() {
        return getPerformanceData(TOTAL_ISSUES);
    }

    @Wrapper
    public PerformanceDataItem getIssuesPerSessionData() {
        return getPerformanceData(ISSUES_PER_SESSION);
    }

    private PerformanceDataItem getPerformanceData(PerformanceAnalyticType type) {
        Pair<Long, Long> dateRange = fetchDateFromPayload(type);
        List<String> versionSamples = fetchVersionsFromPayload(type);
        if (versionSamples == null)
            versionSamples = performanceRepository.getLimitedVersionsTarget(applicationId, platformName, type);
        List<PerformanceAnalytic> analytics = performanceRepository.collectPerformanceData(applicationId, platformName,
                type, dateRange.getFirst(), dateRange.getSecond(), versionSamples);
        if (type == LAUNCH_TIME)
            System.out.println(analytics);
        return new PerformanceDataItem(versionSamples, analytics);
    }

    private Pair<Long, Long> fetchDateFromPayload(PerformanceAnalyticType type) {
        JsonHelper analyticPayload = hPayload.getJsonHelper(type.name(), EMPTY_JSON_HELPER);
        long initialDate = analyticPayload.getLong(INITIAL_DATE_KEY, -1);
        long finalDate = analyticPayload.getLong(FINAL_DATE_KEY, System.currentTimeMillis());
        if (initialDate == -1)
            initialDate = finalDate - MAX_TEMPORAL_RANGE;
        return new Pair<>(initialDate, finalDate);
    }

    private List<String> fetchVersionsFromPayload(PerformanceAnalyticType type) {
        List<String> versions = hPayload.getJsonHelper(type.name(), EMPTY_JSON_HELPER).fetchList(VERSION_FILTERS_KEY);
        if (versions != null && versions.size() > MAX_VERSION_SAMPLES)
            versions.subList(MAX_VERSION_SAMPLES, versions.size()).clear();
        return versions;
    }

}
