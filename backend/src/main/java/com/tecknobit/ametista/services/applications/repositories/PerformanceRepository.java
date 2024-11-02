package com.tecknobit.ametista.services.applications.repositories;

import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.AmetistaApplication.MAX_VERSION_SAMPLES;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.*;

@Repository
public interface PerformanceRepository extends JpaRepository<PerformanceAnalytic, String> {

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

}
