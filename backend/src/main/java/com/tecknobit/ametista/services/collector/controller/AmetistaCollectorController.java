package com.tecknobit.ametista.services.collector.controller;


import com.tecknobit.ametista.services.DefaultAmetistaController;
import com.tecknobit.ametista.services.collector.service.CollectorHelper;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType;
import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.equinox.environment.controllers.EquinoxController;
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
import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.PUT;
import static com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY;
import static com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL;
import static com.tecknobit.equinox.Requester.RESPONSE_DATA_KEY;
import static com.tecknobit.equinox.Requester.RESPONSE_STATUS_KEY;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;

/**
 * The {@code AmetistaCollectorController} class is useful to manage all the collection operations requested by the
 * Ametista-Engine
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see DefaultAmetistaController
 * @see EquinoxController
 */
@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + APPLICATIONS_KEY + "/{" + APPLICATION_IDENTIFIER_KEY + "}")
public class AmetistaCollectorController extends DefaultAmetistaController {

    /**
     * {@code IS_DEBUG_MODE} whether the analytics sent by the Ametista-Engine must be counted
     */
    private static final String IS_DEBUG_MODE = "is_debug_mode";

    /**
     * {@code PLATFORM_ALREADY_CONNECTED} error message used when the platform requested to connect is already connected
     */
    private static final String PLATFORM_ALREADY_CONNECTED = "platform_already_connected_key";

    /**
     * {@code DEBUG_OPERATION_EXECUTED_SUCCESSFULLY} message sent when the collection request has been completed successfully
     */
    private static final String DEBUG_OPERATION_EXECUTED_SUCCESSFULLY = "debug_operation_executed_successfully_key";

    /**
     * {@code collectorHelper} helper to manage the collection operations
     */
    @Autowired
    private CollectorHelper collectorHelper;

    /**
     * Method to connect a new platform for the application
     *
     * @param applicationId The identifier of the application
     * @param serverSecret  The token of the user
     * @param platform      The platform to connect
     * @return the result of the request as {@link String}
     */
    @PutMapping(
            params = {
                    PLATFORM_KEY
            },
            headers = {
                    SERVER_SECRET_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/applications/{application_id}",
            query_parameters = "?platform={platform}",
            method = PUT
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
    
    /**
     * Method to collect a new issue for the application
     *
     * @param applicationId Identifier of the application
     * @param serverSecret User token for authentication
     * @param appVersion Version of the application where the issue occurred
     * @param platform Platform to connect
     * @param isDebugMode Specifies whether analytics sent by the Ametista-Engine should be counted
     * @param payload Request payload containing the device, browser, and issue details:
     *                <pre>
     *                      {
     *                         "device": {
     *                             "device_id": " the device identifier -> [String]",
     *                             "brand": "the device brand -> [String]",
     *                             "model": "the device model -> [String]",
     *                             "os": "the operating system of the device -> [String]",
     *                             "os_version": "the version of the operating system of the device -> [String]"
     *                         },
     *                         "browser": "(WEB) the browser where the issue occurred -> [String]",
     *                         "browser_version": "(WEB) the version of the browser where the issue occurred -> [String]",
     *                         "issue": "the issue cause message -> [String]"
     *                      }
     *                </pre>
     * @return result of the request as {@link String}.
     */
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
    @RequestPath(
            path = "/api/v1/applications/{application_id}/issues",
            query_parameters = "?app_version={app_version}&platform={platform}&is_debug_mode={is_debug_mode}",
            method = PUT
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

    /**
     * Method to collect an analytics for the application
     *
     * @param applicationId Identifier of the application
     * @param serverSecret User token for authentication
     * @param appVersion Version of the application where the issue occurred
     * @param platform Platform to connect
     * @param isDebugMode Specifies whether analytics sent by the Ametista-Engine should be counted
     * @param payload The request payload with the different analytic stats to store
     * @return result of the request as {@link String}.
     */
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
    @RequestPath(
            path = "/api/v1/applications/{application_id}/performance_analytics",
            query_parameters = "?app_version={app_version}&platform={platform}&is_debug_mode={is_debug_mode}",
            method = PUT
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

    /**
     * Method to send the debug request response
     *
     * @return the request response as {@link String}
     */
    private String sendDebugRequestResponse() {
        return new JSONObject()
                .put(RESPONSE_STATUS_KEY, SUCCESSFUL)
                .put(RESPONSE_DATA_KEY, mantis.getResource(DEBUG_OPERATION_EXECUTED_SUCCESSFULLY)).toString();
    }

    /**
     * Method to validate the collector request checking the server secret and the identifier of the application sent
     *
     * @param applicationId Identifier of the application
     * @param serverSecret User token for authentication
     *
     * @return result of the validation as {@link AmetistaApplication}
     */
    private AmetistaApplication validateCollectorRequest(String applicationId, String serverSecret) {
        if (!serverProtector.serverSecretMatches(serverSecret))
            return null;
        return collectorHelper.getApplication(applicationId).orElse(null);
    }

}
