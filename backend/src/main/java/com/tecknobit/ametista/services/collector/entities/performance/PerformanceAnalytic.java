package com.tecknobit.ametista.services.collector.entities.performance;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic;
import com.tecknobit.ametista.shared.data.AmetistaItem;
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.*;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.ametistacore.enums.AnalyticType.PERFORMANCE;


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
