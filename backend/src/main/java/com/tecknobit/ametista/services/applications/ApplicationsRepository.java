package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametistacore.models.AmetistaApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ApplicationsRepository extends JpaRepository<AmetistaApplication, String> {
}
