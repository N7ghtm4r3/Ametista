package com.tecknobit.ametista.services.collector.controller;


import com.tecknobit.ametista.services.DefaultAmetistaController;
import com.tecknobit.ametista.services.collector.service.CollectorHelper;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.APP_VERSION_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUES_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCE_ANALYTICS_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCE_ANALYTIC_TYPE_KEY;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.ISSUES_PER_SESSION;
import static com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType.TOTAL_ISSUES;
import static com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;
import static com.tecknobit.equinox.Requester.RESPONSE_DATA_KEY;
import static com.tecknobit.equinox.Requester.RESPONSE_STATUS_KEY;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;

@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + APPLICATIONS_KEY + "/{" + APPLICATION_IDENTIFIER_KEY + "}")
public class AmetistaCollectorController extends DefaultAmetistaController {

    private static final String IS_DEBUG_MODE = "is_debug_mode";

    private static final String PLATFORM_ALREADY_CONNECTED = "platform_already_connected_key";

    private static final String DEBUG_OPERATION_EXECUTED_SUCCESSFULLY = "debug_operation_executed_successfully_key";

    @Autowired
    private CollectorHelper collectorHelper;

    @PutMapping(
            params = {
                    PLATFORM_KEY
            },
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

    @PutMapping(
            path = "/" + ISSUES_KEY,
            params = {
                    APP_VERSION_KEY,
                    PLATFORM_KEY
            },
            headers = {
                    SERVER_SECRET_KEY
            }
    )
    public String collectIssue(
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @RequestHeader(SERVER_SECRET_KEY) String serverSecret,
            @RequestParam(APP_VERSION_KEY) String appVersion,
            @RequestParam(PLATFORM_KEY) Platform platform,
            @RequestParam(value = IS_DEBUG_MODE, defaultValue = "false", required = false) boolean isDebugMode,
            @RequestBody Map<String, Object> payload
    ) {
        if (isDebugMode)
            return sendDebugRequestResponse();
        AmetistaApplication application = validateCollectorRequest(applicationId, serverSecret);
        if (application == null || !application.getPlatforms().contains(platform))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        loadJsonHelper(payload);
        try {
            collectorHelper.collectIssue(applicationId, appVersion, platform, jsonHelper);
            return successResponse();
        } catch (IllegalArgumentException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
    }

    @PutMapping(
            path = "/" + PERFORMANCE_ANALYTICS_KEY,
            params = {
                    APP_VERSION_KEY,
                    PLATFORM_KEY,
                    PERFORMANCE_ANALYTIC_TYPE_KEY
            },
            headers = {
                    SERVER_SECRET_KEY
            }
    )
    public String collectAnalytic(
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @RequestHeader(SERVER_SECRET_KEY) String serverSecret,
            @RequestParam(APP_VERSION_KEY) String appVersion,
            @RequestParam(PLATFORM_KEY) Platform platform,
            @RequestParam(PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType type,
            @RequestParam(value = IS_DEBUG_MODE, defaultValue = "false", required = false) boolean isDebugMode,
            @RequestBody(required = false) Map<String, String> payload
    ) {
        if (isDebugMode)
            return sendDebugRequestResponse();
        AmetistaApplication application = validateCollectorRequest(applicationId, serverSecret);
        if (application == null || type == TOTAL_ISSUES || type == ISSUES_PER_SESSION ||
                !application.getPlatforms().contains(platform)) {
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        }
        loadJsonHelper(payload);
        try {
            collectorHelper.collectAnalytic(applicationId, appVersion, platform, type, jsonHelper);
            return successResponse();
        } catch (IllegalArgumentException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
    }

    private String sendDebugRequestResponse() {
        return new JSONObject()
                .put(RESPONSE_STATUS_KEY, SUCCESSFUL)
                .put(RESPONSE_DATA_KEY, mantis.getResource(DEBUG_OPERATION_EXECUTED_SUCCESSFULLY)).toString();
    }

    private AmetistaApplication validateCollectorRequest(String applicationId, String serverSecret) {
        if (!serverProtector.serverSecretMatches(serverSecret))
            return null;
        return collectorHelper.getApplication(applicationId).orElse(null);
    }

}
