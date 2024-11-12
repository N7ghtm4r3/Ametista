package com.tecknobit.ametista.services.collector.repositories;

import com.tecknobit.ametistacore.models.AmetistaDevice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static com.tecknobit.ametistacore.models.AmetistaDevice.*;

@Repository
public interface DevicesRepository extends JpaRepository<AmetistaDevice, String> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = "INSERT INTO " + DEVICES_KEY + " (" +
                    DEVICE_IDENTIFIER_KEY + "," +
                    BRAND_KEY + "," +
                    MODEL_KEY + "," +
                    OS_KEY + "," +
                    OS_VERSION_KEY +
                    ") VALUES (" +
                    ":" + DEVICE_IDENTIFIER_KEY + "," +
                    ":" + BRAND_KEY + "," +
                    ":" + MODEL_KEY + "," +
                    ":" + OS_KEY + "," +
                    ":" + OS_VERSION_KEY +
                    ")",
            nativeQuery = true
    )
    void saveDevice(
            @Param(DEVICE_IDENTIFIER_KEY) String deviceId,
            @Param(BRAND_KEY) String brand,
            @Param(MODEL_KEY) String model,
            @Param(OS_KEY) String os,
            @Param(OS_VERSION_KEY) String osVersion
    );

}
