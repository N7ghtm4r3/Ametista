package com.tecknobit.ametista.services.collector.service;

import com.tecknobit.ametista.services.applications.service.ApplicationsHelper;
import com.tecknobit.ametistacore.models.Platform;
import org.springframework.stereotype.Service;

@Service
public class CollectorHelper extends ApplicationsHelper {

    public void connectPlatform(String applicationId, Platform platform) {
        applicationsRepository.connectPlatform(applicationId, platform);
    }

}
