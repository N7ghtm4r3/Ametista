package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametistacore.models.AmetistaApplication;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.*;
import static com.tecknobit.ametistacore.models.AmetistaItem.CREATION_DATE_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

@Service
@Repository
public interface ApplicationsRepository extends JpaRepository<AmetistaApplication, String> {

    @Query(
            value = "SELECT DISTINCT a.* FROM " + APPLICATIONS_KEY + " AS a" +
                    " LEFT JOIN " + PLATFORMS_KEY + " as p ON" +
                    " p." + APPLICATION_IDENTIFIER_KEY + "=" + IDENTIFIER_KEY +
                    " WHERE " + NAME_KEY + " LIKE %:" + NAME_KEY + "%" +
                    " AND (p." + PLATFORM_KEY + " IN (:" + PLATFORMS_KEY + ") OR p." + PLATFORM_KEY + " IS NULL) " +
                    " ORDER BY " + CREATION_DATE_KEY + " DESC",
            nativeQuery = true
    )
    List<AmetistaApplication> getApplications(
            @Param(NAME_KEY) String name,
            @Param(PLATFORMS_KEY) List<String> platforms,
            Pageable pageable
    );

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

}
