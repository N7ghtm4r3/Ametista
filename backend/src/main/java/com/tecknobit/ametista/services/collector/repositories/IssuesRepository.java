package com.tecknobit.ametista.services.collector.repositories;

import com.tecknobit.ametista.services.collector.entities.issues.IssueAnalytic;
import com.tecknobit.ametistacore.enums.Platform;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.IDENTIFIER_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;

/**
 * The {@code IssuesRepository} interface is useful to manage the queries for the issues operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 */
@Repository
public interface IssuesRepository extends JpaRepository<IssueAnalytic, String> {

    /**
     * Method to store a new issue
     *
     * @param id            The identifier of the issue
     * @param creationDate  When the issue occurred
     * @param name          The name of the issue
     * @param appVersion    The version of the application where the issue occurred
     * @param platform      The platform of the application where the issue occurred
     * @param issue         The issue cause message
     * @param applicationId The identifier of the application where the issue occurred
     * @param deviceId      The identifier of the device where the issue occurred
     */
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

    /**
     * Method to store a new issue occurred on a browser
     *
     * @param id The identifier of the issue
     * @param creationDate When the issue occurred
     * @param name The name of the issue
     * @param appVersion The version of the application where the issue occurred
     * @param platform The platform of the application where the issue occurred
     * @param issue The issue cause message
     * @param browser The browser where the issue occurred
     * @param browserVersion The version of the browser where the issue occurred
     * @param applicationId The identifier of the application where the issue occurred
     * @param deviceId The identifier of the device where the issue occurred
     */
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
