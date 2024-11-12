package com.tecknobit.ametista.services.collector.repositories;

import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.AmetistaDevice.DEVICE_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.AmetistaItem.CREATION_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.APP_VERSION_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUES_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUE_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic.*;
import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

@Repository
public interface IssuesRepository extends JpaRepository<IssueAnalytic, String> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "INSERT INTO " + ISSUES_KEY + " (" +
                    "dtype" + "," +
                    IDENTIFIER_KEY + "," +
                    CREATION_DATE_KEY + "," +
                    NAME_KEY + "," +
                    APP_VERSION_KEY + "," +
                    PLATFORM_KEY + "," +
                    ISSUE_KEY + "," +
                    APPLICATION_IDENTIFIER_KEY + "," +
                    DEVICE_IDENTIFIER_KEY +
                    ") VALUES ('" +
                    ISSUE_KEY + "'," +
                    ":" + IDENTIFIER_KEY + "," +
                    ":" + CREATION_DATE_KEY + "," +
                    ":" + NAME_KEY + "," +
                    ":" + APP_VERSION_KEY + "," +
                    ":#{#" + PLATFORM_KEY + ".name()}" + "," +
                    ":" + ISSUE_KEY + "," +
                    ":" + APPLICATION_IDENTIFIER_KEY + "," +
                    ":" + DEVICE_IDENTIFIER_KEY +
                    ")",
            nativeQuery = true
    )
    void storeIssue(
            @Param(IDENTIFIER_KEY) String id,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(NAME_KEY) String name,
            @Param(APP_VERSION_KEY) String appVersion,
            @Param(PLATFORM_KEY) Platform platform,
            @Param(ISSUE_KEY) String issue,
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(DEVICE_IDENTIFIER_KEY) String deviceId
    );

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "INSERT INTO " + ISSUES_KEY + " (" +
                    "dtype" + "," +
                    IDENTIFIER_KEY + "," +
                    CREATION_DATE_KEY + "," +
                    NAME_KEY + "," +
                    APP_VERSION_KEY + "," +
                    PLATFORM_KEY + "," +
                    ISSUE_KEY + "," +
                    BROWSER_KEY + "," +
                    BROWSER_VERSION_KEY + "," +
                    APPLICATION_IDENTIFIER_KEY + "," +
                    DEVICE_IDENTIFIER_KEY +
                    ") VALUES ('" +
                    WEB_ISSUE_KEY + "'," +
                    ":" + IDENTIFIER_KEY + "," +
                    ":" + CREATION_DATE_KEY + "," +
                    ":" + NAME_KEY + "," +
                    ":" + APP_VERSION_KEY + "," +
                    ":#{#" + PLATFORM_KEY + ".name()}" + "," +
                    ":" + ISSUE_KEY + "," +
                    ":" + BROWSER_KEY + "," +
                    ":" + BROWSER_VERSION_KEY + "," +
                    ":" + APPLICATION_IDENTIFIER_KEY + "," +
                    ":" + DEVICE_IDENTIFIER_KEY +
                    ")",
            nativeQuery = true
    )
    void storeWebIssue(
            @Param(IDENTIFIER_KEY) String id,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(NAME_KEY) String name,
            @Param(APP_VERSION_KEY) String appVersion,
            @Param(PLATFORM_KEY) Platform platform,
            @Param(ISSUE_KEY) String issue,
            @Param(BROWSER_KEY) String browser,
            @Param(BROWSER_VERSION_KEY) String browserVersion,
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(DEVICE_IDENTIFIER_KEY) String deviceId
    );

}
