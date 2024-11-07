package com.tecknobit.ametistacore.models.analytics.performance;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.apimanager.formatters.JsonHelper;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCE_ANALYTIC_TYPE_KEY;

public class PerformanceData {

    public static final String LAUNCH_TIMES_KEY = "launch_times";

    public static final String NETWORK_REQUESTS_KEY = "network_requests";

    public static final String TOTAL_ISSUES_KEY = "total_issues";

    public static final String ISSUES_PER_SESSION_KEY = "issues_per_session";

    private final PerformanceDataItem launchTimes;

    private final PerformanceDataItem networkRequests;

    private final PerformanceDataItem totalIssues;

    private final PerformanceDataItem issuesPerSession;

    public PerformanceData() {
        this(null, null, null, null);
    }

    public PerformanceData(PerformanceDataItem launchTimes, PerformanceDataItem networkRequests,
                           PerformanceDataItem totalIssues, PerformanceDataItem issuesPerSession) {
        this.launchTimes = launchTimes;
        this.networkRequests = networkRequests;
        this.totalIssues = totalIssues;
        this.issuesPerSession = issuesPerSession;
    }

    public PerformanceData(JSONObject jPerformanceData) {
        JsonHelper hItem = new JsonHelper(jPerformanceData);
        /* TODO: 02/11/2024 TO INIT CORRECTLY */
        launchTimes = null;
        networkRequests = null;
        totalIssues = null;
        issuesPerSession = null;
    }

    @JsonGetter(LAUNCH_TIMES_KEY)
    public PerformanceDataItem getLaunchTimes() {
        return launchTimes;
    }

    @JsonGetter(NETWORK_REQUESTS_KEY)
    public PerformanceDataItem getNetworkRequests() {
        return networkRequests;
    }

    @JsonGetter(TOTAL_ISSUES_KEY)
    public PerformanceDataItem getTotalIssues() {
        return totalIssues;
    }

    @JsonGetter(ISSUES_PER_SESSION_KEY)
    public PerformanceDataItem getIssuesPerSession() {
        return issuesPerSession;
    }

    public static class PerformanceDataItem {

        public static final long MAX_TEMPORAL_RANGE = 86400000L * 90;

        private final Map<String, List<PerformanceAnalytic>> data;

        private final PerformanceAnalyticType analyticType;

        public PerformanceDataItem() {
            this(null, null);
        }

        public PerformanceDataItem(List<String> versions, List<PerformanceAnalytic> analytics,
                                   PerformanceAnalyticType analyticType) {
            this.analyticType = analyticType;
            HashMap<String, List<PerformanceAnalytic>> data = new HashMap<>();
            CopyOnWriteArrayList<PerformanceAnalytic> containerList = new CopyOnWriteArrayList<>(analytics);
            for (String version : versions) {
                List<PerformanceAnalytic> analyticByVersion = getAnalyticByVersion(version, containerList);
                data.put(version, analyticByVersion);
            }
            this.data = data;
        }

        public PerformanceDataItem(Map<String, List<PerformanceAnalytic>> data, PerformanceAnalyticType analyticType) {
            this.data = data;
            this.analyticType = analyticType;
        }

        public PerformanceDataItem(JSONObject jItem) {
            JsonHelper hItem = new JsonHelper(jItem);
            // TODO: 21/10/2024 TO INIT CORRECTLY
            data = null;
            analyticType = null;
        }

        // TODO: 02/11/2024 WARN IN THE DOCU ABOUT THE BREAK EXIT BEFORE SCAN ALL THE LIST BEAUSE FROM DATABASE THE LIST IS ORDERED BY APP VERSION
        private List<PerformanceAnalytic> getAnalyticByVersion(String appVersion, CopyOnWriteArrayList<PerformanceAnalytic> analytics) {
            List<PerformanceAnalytic> analyticsByVersion = new ArrayList<>();
            for (int j = 0; j < analytics.size(); j++) {
                PerformanceAnalytic analytic = analytics.get(j);
                if (analytic.getAppVersion().equals(appVersion)) {
                    analyticsByVersion.add(analytic);
                    analytics.remove(analytic);
                } else
                    break;
            }
            return analyticsByVersion;
        }

        public Map<String, List<PerformanceAnalytic>> getData() {
            return data;
        }

        public Set<String> sampleVersions() {
            return data.keySet();
        }

        public boolean noDataAvailable() {
            return data.isEmpty();
        }

        @JsonGetter(PERFORMANCE_ANALYTIC_TYPE_KEY)
        public PerformanceAnalyticType getAnalyticType() {
            return analyticType;
        }

        // TODO: 22/10/2024 WARN ABOUT THE ALGORITHM NOT NEEDS SORT BECAUSE DATE FETCHED ALREADY ORDERED
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

        // TODO: 22/10/2024 WARN ABOUT THE ALGORITHM NOT NEEDS SORT BECAUSE DATE FETCHED ALREADY ORDERED
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

        @Override
        public String toString() {
            return new JSONObject(this).toString();
        }

    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
