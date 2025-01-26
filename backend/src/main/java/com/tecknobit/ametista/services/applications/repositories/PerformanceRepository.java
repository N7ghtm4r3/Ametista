package com.tecknobit.ametista.services.applications.repositories;

import com.tecknobit.ametista.services.collector.entities.performance.PerformanceAnalytic;
import com.tecknobit.ametistacore.enums.Platform;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.ametista.services.applications.entities.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametista.services.applications.entities.AmetistaApplication.MAX_VERSION_SAMPLES;
import static com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic.APP_VERSION_KEY;
import static com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.ametista.services.collector.entities.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametista.services.collector.entities.performance.PerformanceAnalytic.*;
import static com.tecknobit.ametista.shared.data.AmetistaItem.CREATION_DATE_KEY;
import static com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem.IDENTIFIER_KEY;

/**
 * The {@code PerformanceRepository} interface is useful to manage the queries for the performance data operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 */
@Repository
public interface PerformanceRepository extends JpaRepository<PerformanceAnalytic, String> {

    /**
     * Method to retrieve the collected performance data from the database
     *
     * @param applicationId The application identifier related to the performance data collected
     * @param platform      The platform related to the performance data collected
     * @param type          The specific performance data to retrieve
     * @param initialDate   The initial date to retrieve the data
     * @param finalDate     The final date allowed to retrieve the data
     * @param versions      The versions sample of the collected data to retrieve
     * @return the performance data collected as {@link List} of {@link PerformanceAnalytic}
     */
    @Query(
            value = "SELECT * FROM " + PERFORMANCE_ANALYTICS_KEY +
                    " WHERE " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND " + PLATFORM_KEY + "=:" + PLATFORM_KEY +
                    " AND " + PERFORMANCE_ANALYTIC_TYPE_KEY + "=:#{#" + PERFORMANCE_ANALYTIC_TYPE_KEY + ".name()}" +
                    " AND " + CREATION_DATE_KEY + " BETWEEN :" + INITIAL_DATE_KEY + " AND :" + FINAL_DATE_KEY +
                    " AND (" +
                    "COALESCE(:" + VERSION_FILTERS_KEY + ") IS NULL OR " +
                    APP_VERSION_KEY + " IN (:" + VERSION_FILTERS_KEY + ")" +
                    ")" +
                    " ORDER BY " + APP_VERSION_KEY,
            nativeQuery = true
    )
    List<PerformanceAnalytic> collectPerformanceData(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform,
            @Param(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type,
            @Param(INITIAL_DATE_KEY) long initialDate,
            @Param(FINAL_DATE_KEY) long finalDate,
            @Param(VERSION_FILTERS_KEY) List<String> versions
    );

    /**
     * Method to get all the available versions target for a specific analytic
     *
     * @param applicationId The application identifier related to the performance data collected
     * @param platform The platform related to the performance data collected
     * @param type The specific performance data to retrieve
     *
     * @return all the available versions target as {@link List} of {@link String}
     */
    @Query(
            value = "SELECT DISTINCT " + APP_VERSION_KEY + " FROM " + PERFORMANCE_ANALYTICS_KEY +
                    " WHERE " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND " + PLATFORM_KEY + "=:" + PLATFORM_KEY +
                    " AND " + PERFORMANCE_ANALYTIC_TYPE_KEY + "=:#{#" + PERFORMANCE_ANALYTIC_TYPE_KEY + ".name()}",
            nativeQuery = true
    )
    List<String> getAllVersionsTarget(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform,
            @Param(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type
    );

    /**
     * Method to get all the available versions target for a specific analytic limited for the chart data presentation
     *
     * @param applicationId The application identifier related to the performance data collected
     * @param platform The platform related to the performance data collected
     * @param type The specific performance data to retrieve
     *
     * @return all the available versions target as {@link List} of {@link String}
     */
    @Query(
            value = "SELECT DISTINCT " + APP_VERSION_KEY + " FROM " + PERFORMANCE_ANALYTICS_KEY +
                    " WHERE " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND " + PLATFORM_KEY + "=:" + PLATFORM_KEY +
                    " AND " + PERFORMANCE_ANALYTIC_TYPE_KEY + "=:#{#" + PERFORMANCE_ANALYTIC_TYPE_KEY + ".name()}" +
                    " LIMIT " + MAX_VERSION_SAMPLES,
            nativeQuery = true
    )
    List<String> getLimitedVersionsTarget(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform,
            @Param(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type
    );

    /**
     * Method to retrieve an analytic by date
     *
     * @param applicationId The application identifier related to the performance data collected
     * @param appVersion The application version related to the performance data collected
     * @param platform The platform related to the performance data collected
     * @param type The specific performance data to retrieve
     * @param creationDate The filter date to retrieve the analytic
     *
     * @return analytic as {@link PerformanceAnalytic}
     */
    @Query(
            value = "SELECT * FROM " + PERFORMANCE_ANALYTICS_KEY +
                    " WHERE " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND " + APP_VERSION_KEY + "=:" + APP_VERSION_KEY +
                    " AND " + PLATFORM_KEY + "=:#{#" + PLATFORM_KEY + ".name()}" +
                    " AND " + PERFORMANCE_ANALYTIC_TYPE_KEY + "=:#{#" + PERFORMANCE_ANALYTIC_TYPE_KEY + ".name()}" +
                    " AND " + CREATION_DATE_KEY + "=:" + CREATION_DATE_KEY,
            nativeQuery = true
    )
    PerformanceAnalytic getPerformanceAnalyticByDate(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(APP_VERSION_KEY) String appVersion,
            @Param(PLATFORM_KEY) Platform platform,
            @Param(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type,
            @Param(CREATION_DATE_KEY) long creationDate
    );

    /**
     * Method to store a new analytic in the system
     *
     * @param id The identifier of the analytic
     * @param creationDate The date when the analytic has been inserted in the system
     * @param appVersion The application version related to the analytic
     * @param platform The platform version related to the analytic
     * @param updates The updates number executed on the analytic
     * @param type The type of the analytic
     * @param value The representative value of the analytic
     * @param applicationId The application identifier related to the analytic
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "INSERT INTO " + PERFORMANCE_ANALYTICS_KEY + " (" +
                    IDENTIFIER_KEY + "," +
                    CREATION_DATE_KEY + "," +
                    APP_VERSION_KEY + "," +
                    PLATFORM_KEY + "," +
                    DATA_UPDATES_KEY + "," +
                    PERFORMANCE_ANALYTIC_TYPE_KEY + "," +
                    PERFORMANCE_VALUE_KEY + "," +
                    APPLICATION_IDENTIFIER_KEY +
                    ") VALUES (" +
                    ":" + IDENTIFIER_KEY + "," +
                    ":" + CREATION_DATE_KEY + "," +
                    ":" + APP_VERSION_KEY + "," +
                    ":#{#" + PLATFORM_KEY + ".name()}" + "," +
                    ":" + DATA_UPDATES_KEY + "," +
                    ":#{#" + PERFORMANCE_ANALYTIC_TYPE_KEY + ".name()}" + "," +
                    ":" + PERFORMANCE_VALUE_KEY + "," +
                    ":" + APPLICATION_IDENTIFIER_KEY +
                    ")",
            nativeQuery = true
    )
    void storeAnalytic(
            @Param(IDENTIFIER_KEY) String id,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(APP_VERSION_KEY) String appVersion,
            @Param(PLATFORM_KEY) Platform platform,
            @Param(DATA_UPDATES_KEY) int updates,
            @Param(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type,
            @Param(PERFORMANCE_VALUE_KEY) double value,
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId
    );

    /**
     * Method to update an existing analytic in the system
     *
     * @param updates The updates number executed on the analytic
     * @param value The representative value of the analytic
     * @param id The identifier of the analytic
     * @param creationDate The date when the analytic has been inserted in the system
     * @param appVersion The application version related to the analytic
     * @param platform The platform version related to the analytic
     * @param type The type of the analytic
     * @param applicationId The application identifier related to the analytic
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "UPDATE " + PERFORMANCE_ANALYTICS_KEY + " SET " +
                    DATA_UPDATES_KEY + "=:" + DATA_UPDATES_KEY + "," +
                    PERFORMANCE_VALUE_KEY + "=:" + PERFORMANCE_VALUE_KEY +
                    " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY +
                    " AND " + CREATION_DATE_KEY + "=:" + CREATION_DATE_KEY +
                    " AND " + APP_VERSION_KEY + "=:" + APP_VERSION_KEY +
                    " AND " + PLATFORM_KEY + "=:#{#" + PLATFORM_KEY + ".name()}" +
                    " AND " + PERFORMANCE_ANALYTIC_TYPE_KEY + "=:#{#" + PERFORMANCE_ANALYTIC_TYPE_KEY + ".name()}" +
                    " AND " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY,
            nativeQuery = true
    )
    void updateAnalytic(
            @Param(DATA_UPDATES_KEY) int updates,
            @Param(PERFORMANCE_VALUE_KEY) double value,
            @Param(IDENTIFIER_KEY) String id,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(APP_VERSION_KEY) String appVersion,
            @Param(PLATFORM_KEY) Platform platform,
            @Param(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type,
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId
    );

}
