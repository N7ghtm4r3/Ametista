package com.tecknobit.ametistacore.models.analytics.performance;

import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.FINAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.INITIAL_DATE_KEY;

/**
 * The {@code PerformanceDataFilters} class is used to filter the retrieving of the {@link PerformanceData} from the
 * database
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class PerformanceDataFilters {

    /**
     * {@code launchTime} used to filter the launch time collected
     */
    private PerformanceFilter launchTimeFilter;

    /**
     * {@code networkRequestsFilter} used to filter the network requests collected
     */
    private PerformanceFilter networkRequestsFilter;

    /**
     * {@code totalIssuesFilter} used to filter the total issues collected
     */
    private PerformanceFilter totalIssuesFilter;

    /**
     * {@code issuesPerSessionFilter} used to filter the issues per session collected
     */
    private PerformanceFilter issuesPerSessionFilter;

    /**
     * Constructor to init the {@link PerformanceDataFilters} class
     *
     * @apiNote empty constructor required
     */
    public PerformanceDataFilters() {
    }

    /**
     * Constructor to init the {@link PerformanceDataFilters} class
     *
     * @param launchTimeFilter       Used to filter the launch time collected
     * @param networkRequestsFilter  used to filter the network requests collected
     * @param totalIssuesFilter      Used to filter the total issues collected
     * @param issuesPerSessionFilter Used to filter the issues per session collected
     */
    public PerformanceDataFilters(PerformanceFilter launchTimeFilter, PerformanceFilter networkRequestsFilter,
                                  PerformanceFilter totalIssuesFilter, PerformanceFilter issuesPerSessionFilter) {
        this.launchTimeFilter = launchTimeFilter;
        this.networkRequestsFilter = networkRequestsFilter;
        this.totalIssuesFilter = totalIssuesFilter;
        this.issuesPerSessionFilter = issuesPerSessionFilter;
    }

    /**
     * Method to get {@link #launchTimeFilter} instance
     *
     * @return {@link #launchTimeFilter} instance as {@link PerformanceFilter}
     */
    public PerformanceFilter getLaunchTimeFilter() {
        return launchTimeFilter;
    }

    /**
     * Method to set {@link #launchTimeFilter} instance
     *
     * @param launchTimeFilter The new filter to use
     */
    public void setLaunchTimeFilter(PerformanceFilter launchTimeFilter) {
        this.launchTimeFilter = launchTimeFilter;
    }

    /**
     * Method to get {@link #networkRequestsFilter} instance
     *
     * @return {@link #networkRequestsFilter} instance as {@link PerformanceFilter}
     */
    public PerformanceFilter getNetworkRequestsFilter() {
        return networkRequestsFilter;
    }

    /**
     * Method to set {@link #networkRequestsFilter} instance
     *
     * @param networkRequestsFilter The new filter to use
     */
    public void setNetworkRequestsFilter(PerformanceFilter networkRequestsFilter) {
        this.networkRequestsFilter = networkRequestsFilter;
    }

    /**
     * Method to get {@link #totalIssuesFilter} instance
     *
     * @return {@link #totalIssuesFilter} instance as {@link PerformanceFilter}
     */
    public PerformanceFilter getTotalIssuesFilter() {
        return totalIssuesFilter;
    }

    /**
     * Method to set {@link #totalIssuesFilter} instance
     *
     * @param totalIssuesFilter The new filter to use
     */
    public void setTotalIssuesFilter(PerformanceFilter totalIssuesFilter) {
        this.totalIssuesFilter = totalIssuesFilter;
    }

    /**
     * Method to get {@link #issuesPerSessionFilter} instance
     *
     * @return {@link #issuesPerSessionFilter} instance as {@link PerformanceFilter}
     */
    public PerformanceFilter getIssuesPerSessionFilter() {
        return issuesPerSessionFilter;
    }

    /**
     * Method to set {@link #issuesPerSessionFilter} instance
     *
     * @param issuesPerSessionFilter The new filter to use
     */
    public void setIssuesPerSessionFilter(PerformanceFilter issuesPerSessionFilter) {
        this.issuesPerSessionFilter = issuesPerSessionFilter;
    }

    /**
     * Method to set dynamically the filter value based on the analytic type
     *
     * @param analyticType The type of the analytic to set the filter
     * @param filter The new filter to use
     */
    public void setFilter(PerformanceAnalyticType analyticType, PerformanceFilter filter) {
        switch (analyticType) {
            case LAUNCH_TIME -> setLaunchTimeFilter(filter);
            case NETWORK_REQUESTS -> setNetworkRequestsFilter(filter);
            case TOTAL_ISSUES -> setTotalIssuesFilter(filter);
            case ISSUES_PER_SESSION -> setIssuesPerSessionFilter(filter);
        }
    }

    /**
     * Method to get dynamically the filter value based on the analytic type
     *
     * @param analyticType The type of the analytic to set the filter
     *
     * @return the specific filter as {@link PerformanceFilter}
     */
    public PerformanceFilter getFilter(PerformanceAnalyticType analyticType) {
        switch (analyticType) {
            case LAUNCH_TIME -> {
                return getLaunchTimeFilter();
            }
            case NETWORK_REQUESTS -> {
                return getNetworkRequestsFilter();
            }
            case TOTAL_ISSUES -> {
                return getTotalIssuesFilter();
            }
            case ISSUES_PER_SESSION -> {
                return getIssuesPerSessionFilter();
            }
        }
        return null;
    }

    /**
     * Method to use the class as json payload
     *
     * @return the data of the class formatted as {@link JSONObject}
     */
    public JSONObject toPayload() {
        JSONObject payload = new JSONObject();
        for (PerformanceAnalyticType analyticType : PerformanceAnalyticType.values()) {
            PerformanceFilter filter = getFilter(analyticType);
            if (filter != null) {
                JSONObject jAnalytic = new JSONObject();
                payload.put(INITIAL_DATE_KEY, filter.getInitialDate());
                jAnalytic.put(FINAL_DATE_KEY, filter.getFinalDate());
                jAnalytic.put(VERSION_FILTERS_KEY, new JSONArray(filter.getVersions()));
                payload.put(analyticType.name(), jAnalytic);
            }
        }
        return payload;
    }

    /**
     * The {@code PerformanceFilter} class is used to contain the filter details to use during the retrieving of the
     * related data
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     */
    public static class PerformanceFilter {

        /**
         * {@code initialDate} the initial date from retrieve the data
         */
        private long initialDate;

        /**
         * {@code finalDate} the final date to retrieve the data
         */
        private long finalDate;

        /**
         * {@code versions} the versions of the data to retrieve
         */
        private List<String> versions;

        /**
         * Constructor to init the {@link PerformanceFilter} class
         *
         * @param initialDate The initial date from retrieve the data
         * @param finalDate The final date to retrieve the data
         * @param versions The versions of the data to retrieve
         */
        public PerformanceFilter(long initialDate, long finalDate, List<String> versions) {
            this.initialDate = initialDate;
            this.finalDate = finalDate;
            this.versions = versions;
        }

        /**
         * Method to get {@link #initialDate} instance
         *
         * @return {@link #initialDate} instance as {@code long}
         */
        public long getInitialDate() {
            return initialDate;
        }

        /**
         * Method to set {@link #initialDate} instance
         *
         * @param initialDate The initial date value to use
         */
        public void setInitialDate(long initialDate) {
            this.initialDate = initialDate;
        }

        /**
         * Method to get {@link #finalDate} instance
         *
         * @return {@link #finalDate} instance as {@code long}
         */
        public long getFinalDate() {
            return finalDate;
        }

        /**
         * Method to set {@link #finalDate} instance
         *
         * @param finalDate The final date value to use
         */
        public void setFinalDate(long finalDate) {
            this.finalDate = finalDate;
        }

        /**
         * Method to get {@link #versions} instance
         *
         * @return {@link #versions} instance as {@link List} of {@link String}
         */
        public List<String> getVersions() {
            return versions;
        }

        /**
         * Method to set {@link #versions} instance
         *
         * @param versions The versions value to use
         */
        public void setVersions(List<String> versions) {
            this.versions = versions;
        }

    }

}
