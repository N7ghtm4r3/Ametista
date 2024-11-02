package com.tecknobit.ametistacore.models.analytics.performance;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.Random;

import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.PERFORMANCE;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCE_ANALYTICS_KEY;

@Entity
@Table(name = PERFORMANCE_ANALYTICS_KEY)
public class PerformanceAnalytic extends AmetistaAnalytic {

    public enum PerformanceAnalyticType {

        LAUNCH_TIME,

        NETWORK_REQUESTS,

        TOTAL_ISSUES,

        ISSUES_PER_SESSION

    }

    public static final String PERFORMANCES_KEY = "performance";

    public static final String INITIAL_DATE_KEY = "initial_date";

    public static final String FINAL_DATE_KEY = "final_date";

    public static final String PERFORMANCE_ANALYTICS_KEY = "performance_analytics";

    public static final String PERFORMANCE_VALUE_KEY = "value";

    public static final String PERFORMANCE_ANALYTIC_TYPE_KEY = "performance_analytic_type";

    public static final String DATA_UPDATES_KEY = "data_updates";

    @Column(name = PERFORMANCE_VALUE_KEY)
    private final double value;

    @Enumerated(EnumType.STRING)
    @Column(name = PERFORMANCE_ANALYTIC_TYPE_KEY)
    private final PerformanceAnalyticType performanceAnalyticType;

    @Column(name = DATA_UPDATES_KEY)
    private final int dataUpdates;

    // TODO: 21/10/2024 TO REMOVE
    public PerformanceAnalytic(double value) {
        this(null, System.currentTimeMillis() - (86400000L * new Random().nextInt(90)), null, value, null, null, 0);
    }

    public PerformanceAnalytic() {
        this(null, -1, null, 0, null, null, 0);
    }

    public PerformanceAnalytic(String id, long creationDate, String appVersion, double value, Platform platform,
                               PerformanceAnalyticType performanceAnalyticType, int dataUpdates) {
        super(id, null, creationDate, appVersion, PERFORMANCE, platform);
        this.value = value;
        this.performanceAnalyticType = performanceAnalyticType;
        this.dataUpdates = dataUpdates;
    }

    public PerformanceAnalytic(JSONObject jPerformance) {
        super(jPerformance);
        // TODO: 18/10/2024 TO INIT CORRECTLY
        value = 0;
        performanceAnalyticType = null;
        dataUpdates = 0;
    }

    public double getValue() {
        return value;
    }

    @JsonGetter(PERFORMANCE_ANALYTIC_TYPE_KEY)
    public PerformanceAnalyticType getPerformanceAnalyticType() {
        return performanceAnalyticType;
    }

    @JsonIgnore
    public int getDataUpdates() {
        return dataUpdates;
    }

}
