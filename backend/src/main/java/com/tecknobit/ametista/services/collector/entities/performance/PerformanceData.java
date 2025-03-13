package com.tecknobit.ametista.services.collector.entities.performance;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametista.services.collector.entities.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.apimanager.formatters.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.DATA_KEY;

/**
 * The {@code PerformanceData} class is a container to simplify the transfer and the parsing of the performance data by
 * the server and the clients
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class PerformanceData {

    /**
     * {@code launchTime} the container of the launch time collected
     */
    private final PerformanceDataItem launchTime;

    /**
     * {@code networkRequests} the container of the network requests collected
     */
    private final PerformanceDataItem networkRequests;

    /**
     * {@code totalIssues} the container of the total issues collected
     */
    private final PerformanceDataItem totalIssues;

    /**
     * {@code issuesPerSession} the container of the issues per session collected
     */
    private final PerformanceDataItem issuesPerSession;

    /**
     * Constructor to init the {@link PerformanceData} class
     *
     * @apiNote empty constructor required
     */
    public PerformanceData() {
        this(null, null, null, null);
    }

    /**
     * Constructor to init the {@link PerformanceAnalytic} class
     *
     * @param launchTime       The container of the launch time collected
     * @param networkRequests  The container of the network requests collected
     * @param totalIssues      The container of the total issues collected
     * @param issuesPerSession The container of the issues per session collected
     */
    public PerformanceData(PerformanceDataItem launchTime, PerformanceDataItem networkRequests,
                           PerformanceDataItem totalIssues, PerformanceDataItem issuesPerSession) {
        this.launchTime = launchTime;
        this.networkRequests = networkRequests;
        this.totalIssues = totalIssues;
        this.issuesPerSession = issuesPerSession;
    }

    /**
     * Constructor to init the {@link PerformanceData} class
     *
     * @param jPerformanceData Performance data details formatted as JSON
     */
    // TODO: 13/03/2025 CHECK TO REMOVE
    public PerformanceData(JSONObject jPerformanceData) {
        JsonHelper hItem = new JsonHelper(jPerformanceData);
        launchTime = new PerformanceDataItem(hItem.getJSONObject(LAUNCH_TIME_KEY, new JSONObject()));
        networkRequests = new PerformanceDataItem(hItem.getJSONObject(NETWORK_REQUESTS_KEY, new JSONObject()));
        totalIssues = new PerformanceDataItem(hItem.getJSONObject(TOTAL_ISSUES_KEY, new JSONObject()));
        issuesPerSession = new PerformanceDataItem(hItem.getJSONObject(ISSUES_PER_SESSION_KEY, new JSONObject()));
    }

    /**
     * Method to get {@link #launchTime} instance
     *
     * @return {@link #launchTime} instance as {@link PerformanceDataItem}
     */
    @JsonGetter(LAUNCH_TIME_KEY)
    public PerformanceDataItem getLaunchTime() {
        return launchTime;
    }

    /**
     * Method to get {@link #networkRequests} instance
     *
     * @return {@link #networkRequests} instance as {@link PerformanceDataItem}
     */
    @JsonGetter(NETWORK_REQUESTS_KEY)
    public PerformanceDataItem getNetworkRequests() {
        return networkRequests;
    }

    /**
     * Method to get {@link #totalIssues} instance
     *
     * @return {@link #totalIssues} instance as {@link PerformanceDataItem}
     */
    @JsonGetter(TOTAL_ISSUES_KEY)
    public PerformanceDataItem getTotalIssues() {
        return totalIssues;
    }

    /**
     * Method to get {@link #issuesPerSession} instance
     *
     * @return {@link #issuesPerSession} instance as {@link PerformanceDataItem}
     */
    @JsonGetter(ISSUES_PER_SESSION_KEY)
    public PerformanceDataItem getIssuesPerSession() {
        return issuesPerSession;
    }

    /**
     * The {@code PerformanceDataItem} class is a container of single performance data collected
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     */
    public static class PerformanceDataItem {

        /**
         * {@code MAX_TEMPORAL_RANGE} the maximum temporal range allowed for the data retrieving (about 3 months)
         */
        public static final long MAX_TEMPORAL_RANGE = 86400000L * 90;

        /**
         * {@code IS_CUSTOM_FILTERED_KEY} the key for the <b>"is_custom_filtered"</b> field
         */
        public static final String IS_CUSTOM_FILTERED_KEY = "is_custom_filtered";

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
         * @apiNote empty constructor required
         */
        public PerformanceDataItem() {
            this(null, null, false);
        }

        /**
         * Constructor to init the {@link PerformanceDataItem} class
         *
         * @param versions The version samples used to retrieve the collected data
         * @param analytics The list of the retrieved analytics
         * @param analyticType The type of the collected analytic
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
         * Constructor to init the {@link PerformanceDataItem} class
         *
         * @param data The collected data
         * @param analyticType The type of the collected analytic
         * @param customFiltered Whether the collected data have been filtered with custom filters
         */
        public PerformanceDataItem(Map<String, List<PerformanceAnalytic>> data, PerformanceAnalyticType analyticType,
                                   boolean customFiltered) {
            this.data = data;
            this.analyticType = analyticType;
            this.customFiltered = customFiltered;
        }

        /**
         * Constructor to init the {@link PerformanceDataItem} class
         *
         * @param jItem Performance data item details formatted as JSON
         */
        public PerformanceDataItem(JSONObject jItem) {
            JsonHelper hItem = new JsonHelper(jItem);
            JSONObject jData = hItem.getJSONObject(DATA_KEY);
            data = loadData(jData);
            analyticType = PerformanceAnalyticType.valueOf(hItem.getString(PERFORMANCE_ANALYTIC_TYPE_KEY));
            customFiltered = hItem.getBoolean(IS_CUSTOM_FILTERED_KEY);
        }

        /**
         * Method to group the analytics by their version
         *
         * @param appVersion The application version used to group the analytics
         * @param analytics The complete list of the analytics
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
         * Method to load from the json the data collected
         *
         * @param jData The json from fetch the data to use
         * @return the data fetched from the json as {@link Map} of {@link String} and {@link List} of {@link PerformanceAnalytic}
         */
        private Map<String, List<PerformanceAnalytic>> loadData(JSONObject jData) {
            Map<String, List<PerformanceAnalytic>> data = new HashMap<>();
            for (String appVersion : jData.keySet()) {
                JSONArray analyticsPerVersion = jData.getJSONArray(appVersion);
                ArrayList<PerformanceAnalytic> analytics = new ArrayList<>();
                for (int j = 0; j < analyticsPerVersion.length(); j++)
                    analytics.add(new PerformanceAnalytic(analyticsPerVersion.getJSONObject(j)));
                if (!analytics.isEmpty())
                    data.put(appVersion, analytics);
            }
            return data;
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
         * Method to get keys of the {@link #data} instance
         *
         * @return the keys of the {@link #data} instance as {@link Set} of {@link String}
         */
        public Set<String> sampleVersions() {
            return data.keySet();
        }

        /**
         * Method to check if there are no data available in the {@link #data} instance
         *
         * @return whether there are no data available as {@code boolean}
         */
        public boolean noDataAvailable() {
            return data == null || data.isEmpty();
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

        /**
         * Method to get the initial date of the temporal range of each list present in the {@link #data}
         *
         * @return initial date timestamp as {@code long}
         *
         * @apiNote this method does not require a sort because is already sorted when retried from the database
         */
        @JsonIgnore
        public long getStartTemporalRangeDate() {
            long startDate = Integer.MAX_VALUE;
            long endDate = getEndTemporalRangeDate();
            for (List<PerformanceAnalytic> analytics : data.values()) {
                long checkTimestamp = analytics.get(0).getCreationTimestamp();
                if (checkTimestamp < startDate)
                    startDate = checkTimestamp;
            }
            if (endDate - startDate >= MAX_TEMPORAL_RANGE)
                startDate = endDate - MAX_TEMPORAL_RANGE;
            return startDate;
        }

        /**
         * Method to get the final date of the temporal range of each list present in the {@link #data}
         *
         * @return final date timestamp as {@code long}
         *
         * @apiNote this method does not require a sort because is already sorted when retried from the database
         */
        @JsonIgnore
        public long getEndTemporalRangeDate() {
            long endDate = 0;
            for (List<PerformanceAnalytic> analytics : data.values()) {
                int lastIndex = analytics.size() - 1;
                long checkTimestamp = analytics.get(lastIndex).getCreationTimestamp();
                if (checkTimestamp > endDate)
                    endDate = checkTimestamp;
            }
            return endDate;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return new JSONObject(this).toString();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
