package com.tecknobit.ametista.services.applications.repositories;

import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.AmetistaDevice.*;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.*;

@Repository
public interface AnalyticIssuesRepository extends JpaRepository<IssueAnalytic, String> {

    @Query(
            value = "SELECT i.* FROM " + ISSUES_KEY + " AS i" +
                    " INNER JOIN " + DEVICES_KEY + " d ON" +
                    " i." + DEVICE_IDENTIFIER_KEY + "= d." + DEVICE_IDENTIFIER_KEY +
                    " WHERE i." + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND i." + PLATFORM_KEY + "=:" + PLATFORM_KEY +
                    " AND (" +
                    ":" + DATE_FILTERS_KEY + " IS NULL OR " +
                    "DATE_FORMAT(FROM_UNIXTIME(i.creation_date / 1000), '%d/%m/%Y') IN (:" + DATE_FILTERS_KEY + ")" +
                    ")" +
                    " AND (" +
                    "(:" + VERSION_FILTERS_KEY + " IS NULL OR" +
                    " i." + APP_VERSION_KEY + " IN (:" + VERSION_FILTERS_KEY + ")) OR " +
                    "(:" + VERSION_FILTERS_KEY + " IS NULL OR" +
                    " d." + OS_VERSION_KEY + " IN (:" + VERSION_FILTERS_KEY + "))" +
                    ")" +
                    " AND (" +
                    ":" + BRAND_FILTERS_KEY + " IS NULL OR" +
                    " d." + BRAND_KEY + " IN (:" + BRAND_FILTERS_KEY + ")" +
                    ")" +
                    " AND (" +
                    ":" + MODEL_FILTERS_KEY + " IS NULL OR" +
                    " d." + MODEL_KEY + " IN (:" + MODEL_FILTERS_KEY + ")" +
                    ")",
            nativeQuery = true
    )
    List<IssueAnalytic> getIssues(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform,
            @Param(DATE_FILTERS_KEY) Set<String> dates,
            @Param(VERSION_FILTERS_KEY) Set<String> versions,
            @Param(BRAND_FILTERS_KEY) Set<String> brands,
            @Param(MODEL_FILTERS_KEY) Set<String> models,
            Pageable pageable
    );

    @Query(
            value = "SELECT i.* FROM " + ISSUES_KEY + " AS i" +
                    " INNER JOIN " + DEVICES_KEY + " d ON" +
                    " i." + DEVICE_IDENTIFIER_KEY + "= d." + DEVICE_IDENTIFIER_KEY +
                    " WHERE i." + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND i." + PLATFORM_KEY + "=:" + PLATFORM_KEY +
                    " AND (" +
                    "(:" + VERSION_FILTERS_KEY + " IS NULL OR" +
                    " i." + APP_VERSION_KEY + " IN (:" + VERSION_FILTERS_KEY + ")) OR " +
                    "(:" + VERSION_FILTERS_KEY + " IS NULL OR" +
                    " d." + OS_VERSION_KEY + " IN (:" + VERSION_FILTERS_KEY + "))" +
                    ")" +
                    " AND (" +
                    ":" + BRAND_FILTERS_KEY + " IS NULL OR" +
                    " d." + BRAND_KEY + " IN (:" + BRAND_FILTERS_KEY + ")" +
                    ")" +
                    " AND (" +
                    ":" + MODEL_FILTERS_KEY + " IS NULL OR" +
                    " d." + MODEL_KEY + " IN (:" + MODEL_FILTERS_KEY + ")" +
                    ")",
            nativeQuery = true
    )
    List<IssueAnalytic> getWebIssues(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform,
            @Param(VERSION_FILTERS_KEY) Set<String> versions,
            @Param(BRAND_FILTERS_KEY) Set<String> brands,
            @Param(MODEL_FILTERS_KEY) Set<String> models,
            Pageable pageable
    );

    @Query(
            value = "SELECT COUNT(*) FROM " + ISSUES_KEY +
                    " WHERE " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND " + PLATFORM_KEY + "=:" + PLATFORM_KEY,
            nativeQuery = true
    )
    long countIssuesPerPlatform(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform
    );

}
