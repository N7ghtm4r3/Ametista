package com.tecknobit.ametista.services.applications.repositories;

import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUES_KEY;

@Repository
public interface AnalyticIssuesRepository extends JpaRepository<IssueAnalytic, String> {

    @Query(
            value = "SELECT * FROM " + ISSUES_KEY +
                    " WHERE " + APPLICATION_IDENTIFIER_KEY + "=:" + APPLICATION_IDENTIFIER_KEY +
                    " AND " + PLATFORM_KEY + "=:" + PLATFORM_KEY,
            nativeQuery = true
    )
    List<IssueAnalytic> getIssues(
            @Param(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @Param(PLATFORM_KEY) String platform,
            Pageable pageable
    );

}
