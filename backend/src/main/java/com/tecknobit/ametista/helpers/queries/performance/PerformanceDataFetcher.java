package com.tecknobit.ametista.helpers.queries.performance;

import com.tecknobit.ametista.services.applications.repositories.PerformanceRepository;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.apimanager.formatters.JsonHelper;
import kotlin.Pair;
import kotlin.Triple;
import org.json.JSONObject;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.MAX_VERSION_SAMPLES;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.FINAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.INITIAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.*;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem.MAX_TEMPORAL_RANGE;

/**
 * The {@code PerformanceDataFetcher} class is useful to fetch the performance data and format them to transfer to the
 * clients
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class PerformanceDataFetcher {

    /**
     * {@code EMPTY_JSON_HELPER} constant value of an empty json helper
     */
    private static final JsonHelper EMPTY_JSON_HELPER = new JsonHelper(new JSONObject());

    /**
     * {@code applicationId} the identifier of the application
     */
    private final String applicationId;

    /**
     * {@code platformName} the name of the platform
     */
    private final String platformName;

    /**
     * {@code hPayload} helper to manage the json data
     */
    private final JsonHelper hPayload;

    /**
     * {@code performanceRepository} the repository used to retrieve the performance data from the database
     */
    private final PerformanceRepository performanceRepository;

    /**
     * Constructor to init the {@link PerformanceDataFetcher}
     *
     * @param applicationId         The identifier of the application
     * @param platformName          The name of the platform
     * @param filters               Helper to manage the json data
     * @param performanceRepository The repository used to retrieve the performance data from the database
     */
    public PerformanceDataFetcher(String applicationId, String platformName, JsonHelper filters,
                                  PerformanceRepository performanceRepository) {
        this.applicationId = applicationId;
        this.platformName = platformName;
        this.hPayload = filters;
        this.performanceRepository = performanceRepository;
    }

    /**
     * Method to get the LAUNCH_TIME performance data
     *
     * @return the LAUNCH_TIME performance data as {@link PerformanceDataItem}
     */
    @Wrapper
    public PerformanceDataItem getLaunchTimeData() {
        return getPerformanceData(LAUNCH_TIME);
    }

    /**
     * Method to get the NETWORK_REQUESTS performance data
     *
     * @return the NETWORK_REQUESTS performance data as {@link PerformanceDataItem}
     */
    @Wrapper
    public PerformanceDataItem getNetworkRequestsData() {
        return getPerformanceData(NETWORK_REQUESTS);
    }

    /**
     * Method to get the TOTAL_ISSUES performance data
     *
     * @return the TOTAL_ISSUES performance data as {@link PerformanceDataItem}
     */
    @Wrapper
    public PerformanceDataItem getTotalIssuesData() {
        return getPerformanceData(TOTAL_ISSUES);
    }

    /**
     * Method to get the ISSUES_PER_SESSION performance data
     *
     * @return the ISSUES_PER_SESSION performance data as {@link PerformanceDataItem}
     */
    @Wrapper
    public PerformanceDataItem getIssuesPerSessionData() {
        return getPerformanceData(ISSUES_PER_SESSION);
    }

    /**
     * Method to get the specific performance data based on the type
     *
     * @param type The analytic type to fetch the related performance data
     *
     * @return the specific performance data based on the type as {@link PerformanceDataItem}
     */
    private PerformanceDataItem getPerformanceData(PerformanceAnalyticType type) {
        Triple<Long, Long, Boolean> dateRange = fetchDateFromPayload(type);
        Pair<List<String>, Boolean> versionSamplesResult = fetchVersionsFromPayload(type);
        List<String> versionSamples = versionSamplesResult.getFirst();
        if (versionSamples == null)
            versionSamples = performanceRepository.getLimitedVersionsTarget(applicationId, platformName, type);
        List<PerformanceAnalytic> analytics = performanceRepository.collectPerformanceData(applicationId, platformName,
                type, dateRange.getFirst(), dateRange.getSecond(), versionSamples);
        return new PerformanceDataItem(versionSamples, analytics, type,
                dateRange.getThird() || versionSamplesResult.getSecond());
    }

    /**
     * Method to fetch the specific performance data from json payload
     *
     * @param type The analytic type to fetch the related performance data
     *
     * @return the specific performance data based on the type
     */
    private Triple<Long, Long, Boolean> fetchDateFromPayload(PerformanceAnalyticType type) {
        JsonHelper analyticPayload = hPayload.getJsonHelper(type.name(), EMPTY_JSON_HELPER);
        long initialDate = analyticPayload.getLong(INITIAL_DATE_KEY, -1);
        long finalDate = analyticPayload.getLong(FINAL_DATE_KEY, -1);
        boolean customFiltered = initialDate != -1 || finalDate != -1;
        if (finalDate == -1)
            finalDate = System.currentTimeMillis();
        if (initialDate == -1)
            initialDate = finalDate - MAX_TEMPORAL_RANGE;
        return new Triple<>(initialDate, finalDate, customFiltered);
    }

    /**
     * Method to fetch the specific versions filter from json payload
     *
     * @param type The analytic type to fetch the related versions filter
     *
     * @return the specific versions filter based on the type
     */
    private Pair<List<String>, Boolean> fetchVersionsFromPayload(PerformanceAnalyticType type) {
        List<String> versions = hPayload.getJsonHelper(type.name(), EMPTY_JSON_HELPER).fetchList(VERSION_FILTERS_KEY);
        boolean customFiltered = false;
        if (versions != null && versions.size() > MAX_VERSION_SAMPLES) {
            versions.subList(MAX_VERSION_SAMPLES, versions.size()).clear();
            customFiltered = true;
        }
        return new Pair<>(versions, customFiltered);
    }

}
