package com.tecknobit.ametista.services.applications.repositories;

import com.tecknobit.ametista.services.applications.entities.AmetistaApplication;
import com.tecknobit.ametistacore.enums.Platform;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.ametista.services.applications.entities.AmetistaApplication.*;
import static com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;

/**
 * The {@code ApplicationsRepository} interface is useful to manage the queries for the application operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see JpaRepository
 */
@Repository
public interface ApplicationsRepository extends JpaRepository<AmetistaApplication, String> {

    /**
     * Method to execute the query to get the applications list registered in the system
     *
     * @param name      The application name used as filter
     * @param platforms The list of platforms used as filter
     * @param pageable  The parameters to paginate the query
     */
    @Query(
            value = "SELECT DISTINCT a.* FROM " + APPLICATIONS_KEY + " AS a" +
                    " LEFT JOIN " + PLATFORMS_KEY + " as p ON" +
                    " p." + APPLICATION_IDENTIFIER_KEY + "=" + IDENTIFIER_KEY +
                    " WHERE " + NAME_KEY + " LIKE %:" + NAME_KEY + "%" +
                    " AND (" +
                    "COALESCE(:" + PLATFORMS_KEY + ") IS NULL OR " +
                    "p." + PLATFORM_KEY + " IN (:" + PLATFORMS_KEY + ")" +
                    ") " +
                    " ORDER BY " + CREATION_DATE_KEY + " DESC",
            nativeQuery = true
    )
    List<AmetistaApplication> getApplications(
            @Param(NAME_KEY) String name,
            @Param(PLATFORMS_KEY) List<String> platforms,
            Pageable pageable
    );

    /**
     * Method to save and register a new application in the system
     *
     * @param applicationId The identifier of the application
     * @param creationDate The creation date when the application has been registered
     * @param name The name of the application
     * @param description The description of the application
     * @param icon The icon of the application
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "INSERT INTO " + APPLICATIONS_KEY + "(" +
                    IDENTIFIER_KEY + "," +
                    CREATION_DATE_KEY + "," +
                    NAME_KEY + "," +
                    DESCRIPTION_KEY + "," +
                    APPLICATION_ICON_KEY +
                    ")" + " VALUES " + " ( " +
                    ":" + IDENTIFIER_KEY + "," +
                    ":" + CREATION_DATE_KEY + "," +
                    ":" + NAME_KEY + "," +
                    ":" + DESCRIPTION_KEY + "," +
                    ":" + APPLICATION_ICON_KEY +
                    ")",
            nativeQuery = true
    )
    void saveApplication(
            @Param(IDENTIFIER_KEY) String applicationId,
            @Param(CREATION_DATE_KEY) long creationDate,
            @Param(NAME_KEY) String name,
            @Param(DESCRIPTION_KEY) String description,
            @Param(APPLICATION_ICON_KEY) String icon
    );

    /**
     * Method to edit an existing application
     *
     * @param applicationId The identifier of the application
     * @param name The name of the application
     * @param description The description of the application
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "UPDATE " + APPLICATIONS_KEY +
                    " SET " +
                    NAME_KEY + "=:" + NAME_KEY + "," +
                    DESCRIPTION_KEY + "=:" + DESCRIPTION_KEY +
                    " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void editApplication(
            @Param(IDENTIFIER_KEY) String applicationId,
            @Param(NAME_KEY) String name,
            @Param(DESCRIPTION_KEY) String description
    );

    /**
     * Method to edit an existing application
     *
     * @param applicationId The identifier of the application
     * @param name The name of the application
     * @param description The description of the application
     * @param icon The icon of the application
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "UPDATE " + APPLICATIONS_KEY +
                    " SET " +
                    NAME_KEY + "=:" + NAME_KEY + "," +
                    DESCRIPTION_KEY + "=:" + DESCRIPTION_KEY + "," +
                    APPLICATION_ICON_KEY + "=:" + APPLICATION_ICON_KEY +
                    " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void editApplication(
            @Param(IDENTIFIER_KEY) String applicationId,
            @Param(NAME_KEY) String name,
            @Param(DESCRIPTION_KEY) String description,
            @Param(APPLICATION_ICON_KEY) String icon
    );

    /**
     * Method to delete an existing application
     *
     * @param applicationId The identifier of the application
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "DELETE FROM " + APPLICATIONS_KEY +
                    " WHERE " + IDENTIFIER_KEY + "=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    void deleteApplication(
            @Param(IDENTIFIER_KEY) String applicationId
    );

    /**
     * Method to connect a platform version for an existing application
     *
     * @param applicationId The identifier of the application
     * @param platform The platform to connect
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "INSERT INTO " + PLATFORMS_KEY + "(" +
                    APPLICATION_IDENTIFIER_KEY + "," +
                    PLATFORM_KEY + ")" +
                    " VALUES " + " ( " +
                    ":" + APPLICATION_IDENTIFIER_KEY + "," +
                    ":#{#" + PLATFORM_KEY + ".name()}" +
                    ")",
            nativeQuery = true
    )
    void connectPlatform(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) Platform platform
    );

}
