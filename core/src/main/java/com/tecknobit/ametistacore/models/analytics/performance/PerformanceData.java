package com.tecknobit.ametistacore.models.analytics.performance;

import com.tecknobit.apimanager.formatters.JsonHelper;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerformanceData extends EquinoxItem {

    private final PerformanceDataItem launchTimes;

    private final PerformanceDataItem networkRequests;

    private final PerformanceDataItem totalIssues;

    private final PerformanceDataItem issuesPerSession;

    public PerformanceData() {
        this(null, null, null, null, null);
    }

    public PerformanceData(String id, PerformanceDataItem launchTimes, PerformanceDataItem networkRequests, PerformanceDataItem totalIssues, PerformanceDataItem issuesPerSession) {
        super(id);
        this.launchTimes = launchTimes;
        this.networkRequests = networkRequests;
        this.totalIssues = totalIssues;
        this.issuesPerSession = issuesPerSession;
    }

    public PerformanceData(JSONObject jPerformanceData) {
        super(jPerformanceData);
        launchTimes = null;
        networkRequests = null;
        totalIssues = null;
        issuesPerSession = null;
    }

    public PerformanceDataItem getLaunchTimes() {
        return launchTimes;
    }

    public PerformanceDataItem getNetworkRequests() {
        return networkRequests;
    }

    public PerformanceDataItem getTotalIssues() {
        return totalIssues;
    }

    public PerformanceDataItem getIssuesPerSession() {
        return issuesPerSession;
    }

    public static class PerformanceDataItem {

        public static final long MAX_TEMPORAL_RANGE = 86400000L * 90;

        private final Map<String, List<PerformanceAnalytic>> data;

        public PerformanceDataItem() {
            this((Map<String, List<PerformanceAnalytic>>) null);
        }

        public PerformanceDataItem(Map<String, List<PerformanceAnalytic>> data) {
            this.data = data;
        }

        public PerformanceDataItem(JSONObject jItem) {
            JsonHelper hItem = new JsonHelper(jItem);
            // TODO: 21/10/2024 TO INIT CORRECTLY
            data = null;
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

        // TODO: 22/10/2024 WARN ABOUT THE ALGORITHM NOT NEEDS SORT BECAUSE DATE FETCHED ALREADY ORDERED
        public long getStartTemporalRangeDate() {
            long startDate = Integer.MAX_VALUE;
            for (List<PerformanceAnalytic> analytics : data.values()) {
                long checkTimestamp = analytics.get(0).getCreationTimestamp();
                if (checkTimestamp < startDate)
                    startDate = checkTimestamp;
            }

            // TODO: 22/10/2024 TO REMOVE
            if (getEndTemporalRangeDate() - startDate >= 86400000L * 90)
                startDate = getEndTemporalRangeDate() - 86400000L * 90;

            return startDate;
        }

        // TODO: 22/10/2024 WARN ABOUT THE ALGORITHM NOT NEEDS SORT BECAUSE DATE FETCHED ALREADY ORDERED
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

    }

}
