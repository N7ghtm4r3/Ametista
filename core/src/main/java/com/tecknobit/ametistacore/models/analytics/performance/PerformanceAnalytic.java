package com.tecknobit.ametistacore.models.analytics.performance;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.models.AmetistaItem;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import jakarta.persistence.*;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.PERFORMANCE;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCE_ANALYTICS_KEY;

/**
 * The {@code PerformanceAnalytic} class is useful to represent an Ametista's performance analytic
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see AmetistaAnalytic
 * @see AmetistaItem
 * @see EquinoxItem
 */
@Entity
@Table(name = PERFORMANCE_ANALYTICS_KEY)
public class PerformanceAnalytic extends AmetistaAnalytic {

    /**
     * The performance analytics available
     */
    public enum PerformanceAnalyticType {

        /**
         * **LAUNCH_TIME** -> Inherent measurement of application startup time
         */
        LAUNCH_TIME,

        /**
         * **NETWORK_REQUESTS** -> Inherent measure of the number of HTTP requests executed by each application daily
         */
        NETWORK_REQUESTS,

        /**
         * **TOTAL_ISSUES** -> Inherent measurement of the number of crashes or problems encountered while using the application
         */
        TOTAL_ISSUES,

        /**
         * **ISSUES_PER_SESSION** -> Inherent measurement of the average number of crashes or issues encountered while using the application for a single session
         */
        ISSUES_PER_SESSION

    }

    /**
     * {@code PERFORMANCES_KEY} the key for the <b>"performance"</b> field
     */
    public static final String PERFORMANCES_KEY = "performance";

    /**
     * {@code LAUNCH_TIME_KEY} the key for the <b>"launch_time"</b> field
     */
    public static final String LAUNCH_TIME_KEY = "launch_time";

    /**
     * {@code INITIAL_DATE_KEY} the key for the <b>"initial_date"</b> field
     */
    public static final String INITIAL_DATE_KEY = "initial_date";

    /**
     * {@code FINAL_DATE_KEY} the key for the <b>"final_date"</b> field
     */
    public static final String FINAL_DATE_KEY = "final_date";

    /**
     * {@code PERFORMANCE_ANALYTICS_KEY} the key for the <b>"performance_analytics"</b> field
     */
    public static final String PERFORMANCE_ANALYTICS_KEY = "performance_analytics";

    /**
     * {@code PERFORMANCE_VALUE_KEY} the key for the <b>"value"</b> field
     */
    public static final String PERFORMANCE_VALUE_KEY = "value";

    /**
     * {@code PERFORMANCE_ANALYTIC_TYPE_KEY} the key for the <b>"performance_analytic_type"</b> field
     */
    public static final String PERFORMANCE_ANALYTIC_TYPE_KEY = "performance_analytic_type";

    /**
     * {@code DATA_UPDATES_KEY} the key for the <b>"data_updates"</b> field
     */
    public static final String DATA_UPDATES_KEY = "data_updates";

    /**
     * {@code value} the value related to the analytic
     */
    @Column(name = PERFORMANCE_VALUE_KEY)
    private final double value;

    /**
     * {@code performanceAnalyticType} the type of the performance analytic
     */
    @Enumerated(EnumType.STRING)
    @Column(name = PERFORMANCE_ANALYTIC_TYPE_KEY)
    private final PerformanceAnalyticType performanceAnalyticType;

    /**
     * {@code dataUpdates} the number of the updates computed for the analytic
     */
    @Column(name = DATA_UPDATES_KEY)
    private final int dataUpdates;

    /**
     * Constructor to init the {@link PerformanceAnalytic} class
     *
     * @apiNote empty constructor required
     */
    public PerformanceAnalytic() {
        this(null, -1, null, 0, null, null, 0);
    }

    /**
     * Constructor to init the {@link PerformanceAnalytic} class
     *
     * @param id                      The identifier of the analytic
     * @param creationDate            The date when the analytic has been inserted in the system
     * @param appVersion              The application version where the analytic is attached
     * @param value                   The type of the analytic
     * @param platform                The platform target of the application where the analytic is attached
     * @param performanceAnalyticType The type of the performance analytic
     * @param dataUpdates             The number of the updates computed for the analytic
     */
    public PerformanceAnalytic(String id, long creationDate, String appVersion, double value, Platform platform,
                               PerformanceAnalyticType performanceAnalyticType, int dataUpdates) {
        super(id, null, creationDate, appVersion, PERFORMANCE, platform);
        this.value = value;
        this.performanceAnalyticType = performanceAnalyticType;
        this.dataUpdates = dataUpdates;
    }

    /**
     * Constructor to init the {@link PerformanceAnalytic} class
     *
     * @param jPerformance Performance analytic details formatted as JSON
     */
    public PerformanceAnalytic(JSONObject jPerformance) {
        super(jPerformance, PERFORMANCE);
        value = hItem.getDouble(PERFORMANCE_VALUE_KEY);
        performanceAnalyticType = PerformanceAnalyticType.valueOf(hItem.getString(PERFORMANCE_ANALYTIC_TYPE_KEY));
        dataUpdates = 0;
    }

    /**
     * Method to get {@link #value} instance
     *
     * @return {@link #value} instance as {@code double}
     */
    public double getValue() {
        return value;
    }

    /**
     * Method to get {@link #performanceAnalyticType} instance
     *
     * @return {@link #performanceAnalyticType} instance as {@link PerformanceAnalyticType}
     */
    @JsonGetter(PERFORMANCE_ANALYTIC_TYPE_KEY)
    public PerformanceAnalyticType getPerformanceAnalyticType() {
        return performanceAnalyticType;
    }

    /**
     * Method to get {@link #dataUpdates} instance
     *
     * @return {@link #dataUpdates} instance as {@code int}
     */
    @JsonIgnore
    public int getDataUpdates() {
        return dataUpdates;
    }

}
