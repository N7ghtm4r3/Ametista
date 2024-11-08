package com.tecknobit.ametistacore.models.analytics.performance;

import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import org.json.JSONObject;

import java.util.List;

import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.FINAL_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.INITIAL_DATE_KEY;

public class PerformanceDataFilters {

    private PerformanceFilter launchTimeFilter;

    private PerformanceFilter networkRequestsFilter;

    private PerformanceFilter totalIssuesFilter;

    private PerformanceFilter issuesPerSessionFilter;

    public PerformanceDataFilters() {
    }

    public PerformanceDataFilters(PerformanceFilter launchTimeFilter, PerformanceFilter networkRequestsFilter,
                                  PerformanceFilter totalIssuesFilter, PerformanceFilter issuesPerSessionFilter) {
        this.launchTimeFilter = launchTimeFilter;
        this.networkRequestsFilter = networkRequestsFilter;
        this.totalIssuesFilter = totalIssuesFilter;
        this.issuesPerSessionFilter = issuesPerSessionFilter;
    }

    public PerformanceFilter getLaunchTimeFilter() {
        return launchTimeFilter;
    }

    public void setLaunchTimeFilter(PerformanceFilter launchTimeFilter) {
        this.launchTimeFilter = launchTimeFilter;
    }

    public PerformanceFilter getNetworkRequestsFilter() {
        return networkRequestsFilter;
    }

    public void setNetworkRequestsFilter(PerformanceFilter networkRequestsFilter) {
        this.networkRequestsFilter = networkRequestsFilter;
    }

    public PerformanceFilter getTotalIssuesFilter() {
        return totalIssuesFilter;
    }

    public void setTotalIssuesFilter(PerformanceFilter totalIssuesFilter) {
        this.totalIssuesFilter = totalIssuesFilter;
    }

    public PerformanceFilter getIssuesPerSessionFilter() {
        return issuesPerSessionFilter;
    }

    public void setIssuesPerSessionFilter(PerformanceFilter issuesPerSessionFilter) {
        this.issuesPerSessionFilter = issuesPerSessionFilter;
    }

    public void setFilter(PerformanceAnalyticType analyticType, PerformanceFilter filter) {
        switch (analyticType) {
            case LAUNCH_TIME -> setLaunchTimeFilter(filter);
            case NETWORK_REQUESTS -> setNetworkRequestsFilter(filter);
            case TOTAL_ISSUES -> setTotalIssuesFilter(filter);
            case ISSUES_PER_SESSION -> setIssuesPerSessionFilter(filter);
        }
    }

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

    public JSONObject toPayload() {
        JSONObject payload = new JSONObject();
        for (PerformanceAnalyticType analyticType : PerformanceAnalyticType.values()) {
            PerformanceFilter filter = getFilter(analyticType);
            if (filter != null) {
                JSONObject jAnalytic = new JSONObject();
                jAnalytic.put(INITIAL_DATE_KEY, filter.getInitialDate());
                jAnalytic.put(FINAL_DATE_KEY, filter.getFinalDate());
                jAnalytic.put(VERSION_FILTERS_KEY, filter.getVersions());
                payload.put(analyticType.name(), jAnalytic);
            }
        }
        return payload;
    }

    public static class PerformanceFilter {

        private long initialDate;

        private long finalDate;

        private List<String> versions;

        public PerformanceFilter(long initialDate, long finalDate, List<String> versions) {
            this.initialDate = initialDate;
            this.finalDate = finalDate;
            this.versions = versions;
        }

        public long getInitialDate() {
            return initialDate;
        }

        public void setInitialDate(long initialDate) {
            this.initialDate = initialDate;
        }

        public long getFinalDate() {
            return finalDate;
        }

        public void setFinalDate(long finalDate) {
            this.finalDate = finalDate;
        }

        public List<String> getVersions() {
            return versions;
        }

        public void setVersions(List<String> versions) {
            this.versions = versions;
        }

    }

}
