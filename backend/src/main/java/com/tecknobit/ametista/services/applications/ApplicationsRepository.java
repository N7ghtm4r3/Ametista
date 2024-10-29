package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametistacore.models.AmetistaApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.ametistacore.models.AmetistaItem.CREATION_DATE_KEY;

@Service
@Repository
public interface ApplicationsRepository extends JpaRepository<AmetistaApplication, String> {

    @Query(
            value = "SELECT * FROM " + APPLICATIONS_KEY + " ORDER BY " + CREATION_DATE_KEY + " DESC",
            nativeQuery = true
    )
    List<AmetistaApplication> getApplications(Pageable pageable);

}
