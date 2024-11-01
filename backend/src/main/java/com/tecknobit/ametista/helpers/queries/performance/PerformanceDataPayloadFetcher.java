package com.tecknobit.ametista.helpers.queries.performance;

import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.apimanager.formatters.JsonHelper;
import kotlin.Pair;
import org.json.JSONObject;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.MAX_VERSION_SAMPLES;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.FINAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.INITIAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.LAUNCH_TIME;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceData.PerformanceDataItem.MAX_TEMPORAL_RANGE;

/**
 * The {@code PerformanceDataPayloadFetcher} class is useful to
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class PerformanceDataPayloadFetcher {

    /**
     * {@code hPayload}
     */
    private final JsonHelper hPayload;

    /**
     * Constructor to init the {@link PerformanceDataPayloadFetcher} class
     *
     * @param hPayload: {@code hPayload}
     */
    public PerformanceDataPayloadFetcher(JsonHelper hPayload) {
        this.hPayload = hPayload;
    }

    @Wrapper
    public Pair<Long, Long> fetchLaunchTimeDateRange() {
        return fetchDateFromPayload(LAUNCH_TIME);
    }

    private Pair<Long, Long> fetchDateFromPayload(PerformanceAnalyticType type) {
        JSONObject analyticPayload = hPayload.getJSONObject(type.name());
        long initialDate = analyticPayload.getLong(INITIAL_DATE_KEY);
        long finalDate = analyticPayload.getLong(FINAL_DATE_KEY);
        if (initialDate == -1)
            initialDate = finalDate - MAX_TEMPORAL_RANGE;
        return new Pair<>(initialDate, finalDate);
    }

    @Wrapper
    public List<String> getLaunchTimeVersionSamples() {
        return fetchVersionsFromPayload(LAUNCH_TIME);
    }

    private List<String> fetchVersionsFromPayload(PerformanceAnalyticType type) {
        List<String> versions = hPayload.getJsonHelper(type.name()).fetchList(VERSION_FILTERS_KEY);
        if (versions != null && versions.size() > MAX_VERSION_SAMPLES)
            versions.subList(MAX_VERSION_SAMPLES, versions.size()).clear();
        return versions;
    }

}
