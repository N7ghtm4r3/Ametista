package com.tecknobit.ametista.services.collector.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.ametista.services.collector.entities.performance.PerformanceAnalytic;
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType;
import com.tecknobit.equinoxcore.annotations.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecknobit.ametistacore.ConstantsKt.*;

/**
 * The {@code PerformanceData} class is a container to simplify the transfer and the parsing of the performance data by
 * the server and the clients
 *
 * @param launchTime       The container of the launch time collected
 * @param networkRequests  The container of the network requests collected
 * @param totalIssues      The container of the total issues collected
 * @param issuesPerSession The container of the issues per session collected
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@DTO
public record PerformanceData(
        @JsonGetter(LAUNCH_TIME_KEY) PerformanceDataItem launchTime,
        @JsonGetter(NETWORK_REQUESTS_KEY) PerformanceDataItem networkRequests,
        @JsonGetter(TOTAL_ISSUES_KEY) PerformanceDataItem totalIssues,
        @JsonGetter(ISSUES_PER_SESSION_KEY) PerformanceDataItem issuesPerSession
) {

    /**
     * The {@code PerformanceDataItem} class is a container of single performance data collected
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    public static class PerformanceDataItem {

        /**
         * {@code data} the collected data
         */
        private final Map<String, List<PerformanceAnalytic>> data;

        /**
         * {@code analyticType} the type of the analytic collected
         */
        private final PerformanceAnalyticType analyticType;

        /**
         * {@code customFiltered} whether the collected data have been filtered with custom filters
         */
        private final boolean customFiltered;

        /**
         * Constructor to init the {@link PerformanceDataItem} class
         *
         * @param versions       The version samples used to retrieve the collected data
         * @param analytics      The list of the retrieved analytics
         * @param analyticType   The type of the collected analytic
         * @param customFiltered Whether the collected data have been filtered with custom filters
         */
        public PerformanceDataItem(List<String> versions, List<PerformanceAnalytic> analytics,
                                   PerformanceAnalyticType analyticType, boolean customFiltered) {
            this.analyticType = analyticType;
            HashMap<String, List<PerformanceAnalytic>> data = new HashMap<>();
            ArrayList<PerformanceAnalytic> containerList = new ArrayList<>(analytics);
            for (String version : versions) {
                List<PerformanceAnalytic> analyticByVersion = getAnalyticByVersion(version, containerList);
                data.put(version, analyticByVersion);
            }
            this.data = data;
            this.customFiltered = customFiltered;
        }

        /**
         * Method to group the analytics by their version
         *
         * @param appVersion The application version used to group the analytics
         * @param analytics  The complete list of the analytics
         * @return the analytics grouped by version as {@link List} of {@link PerformanceAnalytic}
         */
        private List<PerformanceAnalytic> getAnalyticByVersion(String appVersion, ArrayList<PerformanceAnalytic> analytics) {
            List<PerformanceAnalytic> analyticsByVersion = new ArrayList<>();
            for (PerformanceAnalytic analytic : analytics) {
                if (analytic.getAppVersion().equals(appVersion))
                    analyticsByVersion.add(analytic);
            }
            analytics.removeAll(analyticsByVersion);
            return analyticsByVersion;
        }

        /**
         * Method to get {@link #data} instance
         *
         * @return {@link #data} instance as {@link Map} of {@link String} and {@link List} of {@link PerformanceAnalytic}
         */
        public Map<String, List<PerformanceAnalytic>> getData() {
            return data;
        }

        /**
         * Method to get {@link #customFiltered} instance
         *
         * @return {@link #customFiltered} instance as {@code boolean}
         */
        @JsonGetter(IS_CUSTOM_FILTERED_KEY)
        public boolean isCustomFiltered() {
            return customFiltered;
        }

        /**
         * Method to get {@link #analyticType} instance
         *
         * @return {@link #analyticType} instance as {@link  PerformanceAnalyticType}
         */
        @JsonGetter(PERFORMANCE_ANALYTIC_TYPE_KEY)
        public PerformanceAnalyticType getAnalyticType() {
            return analyticType;
        }

    }

}
