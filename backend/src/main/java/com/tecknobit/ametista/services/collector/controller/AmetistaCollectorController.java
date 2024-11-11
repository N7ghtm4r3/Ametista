package com.tecknobit.ametista.services.collector.controller;


import com.tecknobit.ametista.services.DefaultAmetistaController;
import com.tecknobit.ametista.services.collector.service.CollectorHelper;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;

@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + APPLICATIONS_KEY + "/{" + APPLICATION_IDENTIFIER_KEY + "}")
public class AmetistaCollectorController extends DefaultAmetistaController {

    private static final String PLATFORM_ALREADY_CONNECTED = "platform_already_connected_key";

    @Autowired
    private CollectorHelper collectorHelper;

    @PutMapping(
            headers = {
                    SERVER_SECRET_KEY
            }
    )
    public String connectPlatform(
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @RequestHeader(SERVER_SECRET_KEY) String serverSecret,
            @RequestParam(PLATFORM_KEY) Platform platform
    ) {
        AmetistaApplication application = validateCollectorRequest(applicationId, serverSecret);
        if (application == null)
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        if (application.getPlatforms().contains(platform))
            return failedResponse(PLATFORM_ALREADY_CONNECTED);
        collectorHelper.connectPlatform(applicationId, platform);
        return successResponse();
    }

    private AmetistaApplication validateCollectorRequest(String applicationId, String serverSecret) {
        if (!serverProtector.serverSecretMatches(serverSecret))
            return null;
        return collectorHelper.getApplication(applicationId).orElse(null);
    }

}
