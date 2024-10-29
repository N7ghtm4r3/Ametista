package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametistacore.models.AmetistaApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
                    " INNER JOIN " + PLATFORMS_KEY + " as p ON " +
                    " p." + APPLICATION_IDENTIFIER_KEY + "=" + IDENTIFIER_KEY +
                    " WHERE " + NAME_KEY + " LIKE %:" + NAME_KEY + "%" +
                    " AND p." + PLATFORM_KEY + " IN (:" + PLATFORMS_KEY + ")" +
                    " ORDER BY " + CREATION_DATE_KEY + " DESC",
            nativeQuery = true
    )
    List<AmetistaApplication> getApplications(
            @Param(NAME_KEY) String name,
            @Param(PLATFORMS_KEY) List<String> platforms,
            Pageable pageable
    );

}
