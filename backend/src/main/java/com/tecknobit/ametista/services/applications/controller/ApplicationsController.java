package com.tecknobit.ametista.services.applications.controller;

import com.tecknobit.ametista.services.DefaultAmetistaController;
import com.tecknobit.ametista.services.applications.entities.AmetistaApplication;
import com.tecknobit.ametista.services.applications.service.ApplicationsService;
import com.tecknobit.ametista.services.applications.service.ApplicationsService.ApplicationPayload;
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.ametistacore.enums.Role;
import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.tecknobit.ametista.services.applications.service.ApplicationsService.DEFAULT_PLATFORMS_FILTER;
import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.ametistacore.helpers.AmetistaValidator.*;
import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.GET;
import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.POST;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;

/**
 * The {@code ApplicationsController} class is useful to manage all the applications operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see DefaultAmetistaController
 * @see EquinoxController
 */
@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + APPLICATIONS_KEY)
public class ApplicationsController extends DefaultAmetistaController {

    /**
     * {@code applicationsService} helper to manage the applications database operations
     */
    @Autowired
    private ApplicationsService applicationsService;

    /**
     * Method to get the applications list registered in the system
     *
     * @param userId    The identifier of the user
     * @param token     The token of the user
     * @param page      The page requested
     * @param pageSize  The size of the items to insert in the page
     * @param name      The application name used as filter
     * @param platforms The list of platforms used as filter
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/applications", method = GET)
    public <T> T getApplications(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = NAME_KEY, defaultValue = "", required = false) String name,
            @RequestParam(
                    name = PLATFORMS_KEY,
                    defaultValue = DEFAULT_PLATFORMS_FILTER,
                    required = false
            ) List<String> platforms
    ) {
        if (!isMe(userId, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(applicationsService.getApplications(page, pageSize, name, platforms));
    }

    /**
     * Method to save and add in the system a new application
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param application The application payload to use
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/applications", method = POST)
    public String addApplication(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @ModelAttribute ApplicationPayload application
    ) {
        if (!isAdmin(userId, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        String payloadValidation = validateApplicationPayload(application);
        if (payloadValidation != null)
            return payloadValidation;
        MultipartFile icon = application.icon();
        if (icon == null || icon.isEmpty())
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        try {
            applicationsService.saveApplication(application);
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to edit an existing application
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The identifier of the application to edit
     * @param application The application payload to use
     *
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = POST)
    public String editApplication(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @ModelAttribute ApplicationPayload application
    ) {
        if (!userAllowedAndApplicationExists(userId, token, applicationId))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        String payloadValidation = validateApplicationPayload(application);
        if (payloadValidation != null)
            return payloadValidation;
        try {
            applicationsService.editApplication(applicationId, application);
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to get an existing application
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The identifier of the application to get
     *
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = GET)
    public <T> T getApplication(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId
    ) {
        AmetistaApplication application = validateUserAndFetchApplication(userId, token, applicationId);
        if (application == null)
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(application);
    }

    /**
     * Method to get the issues related to an application
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The application of retrieve the related issues
     * @param platform The platform of retrieve the related issues
     * @param page The page requested
     * @param pageSize The size of the items to insert in the page
     * @param filters The filters to use for the issue selection
     *
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}/" + ISSUES_KEY,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{user_id}/applications/{application_id}/issues",
            query_parameters = "?platform={platform}",
            method = GET
    )
    public <T> T getIssues(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @RequestParam(name = PLATFORM_KEY) Platform platform,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize,
            @RequestParam(name = FILTERS_KEY, defaultValue = "", required = false) Set<String> filters
    ) {
        AmetistaApplication application = validateUserAndFetchApplication(userId, token, applicationId);
        if (application == null)
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(applicationsService.getIssues(application, page, pageSize, platform, filters));
    }

    /**
     * Method to get the performance data of an application
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The application identifier of retrieve the related performance data
     * @param platform The platform of retrieve the related performance data
     * @param payload The filters payload to use for the performance data selection
     *
     * @return the result of the request as {@link T}
     *
     */
    @PostMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}/" + PERFORMANCES_KEY,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{user_id}/applications/{application_id}/performance",
            query_parameters = "?platform={platform}",
            method = POST
    )
    public <T> T getPerformanceData(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @RequestParam(name = PLATFORM_KEY) Platform platform,
            @RequestBody Map<String, Object> payload
    ) {
        AmetistaApplication application = validateUserAndFetchApplication(userId, token, applicationId);
        if (application == null)
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        loadJsonHelper(payload);
        return (T) successResponse(applicationsService.getPerformanceData(applicationId, platform, jsonHelper));
    }

    /**
     * Method to get all the available versions target for a specific analytic
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The application identifier related to the performance data collected
     * @param platform The platform related to the performance data collected
     * @param analyticType The specific performance data to retrieve
     *
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}/" + VERSION_FILTERS_KEY,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(
            path = "/api/v1/users/{user_id}/applications/{application_id}/versions",
            query_parameters = "?platform={platform}&performance_analytic_type={performance_analytic_type}",
            method = GET
    )
    public <T> T getVersionSamples(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId,
            @RequestParam(name = PLATFORM_KEY) Platform platform,
            @RequestParam(name = PERFORMANCE_ANALYTIC_TYPE_KEY) PerformanceAnalyticType analyticType
    ) {
        AmetistaApplication application = validateUserAndFetchApplication(userId, token, applicationId);
        if (application == null)
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(applicationsService.getVersionSamples(applicationId, platform, analyticType));
    }

    /**
     * Method to delete an existing application
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The identifier of the application
     */
    @DeleteMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    public String deleteApplication(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId
    ) {
        if (!userAllowedAndApplicationExists(userId, token, applicationId))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        applicationsService.deleteApplication(applicationId);
        return successResponse();
    }

    /**
     * Method to validate the payload used in the {@link #addApplication(String, String, ApplicationPayload)} or {@link #editApplication(String, String, String, ApplicationPayload)}
     * operations
     *
     * @param application The payload used
     *
     * @return null if the payload is valid or the error message if not valid as {@link String}
     */
    private String validateApplicationPayload(ApplicationPayload application) {
        String appName = application.name();
        if (!isAppNameValid(appName))
            return failedResponse(WRONG_APP_NAME_MESSAGE);
        String appDescription = application.description();
        if (!isAppDescriptionValid(appDescription))
            return failedResponse(WRONG_APP_DESCRIPTION_MESSAGE);
        return null;
    }

    /**
     * Method to check if the user is allowed to operate (him/she is an {@link Role#ADMIN}) and the application exists
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The identifier of the application
     *
     * @return whether the user is allowed to operate and the application exists as {@code boolean}
     */
    private boolean userAllowedAndApplicationExists(String userId, String token, String applicationId) {
        return isAdmin(userId, token) && applicationsService.applicationExists(applicationId);
    }

    /**
     * Method to check if the user is allowed to operate (him/she is an {@link Role#ADMIN}) and the application exists
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param applicationId The identifier of the application
     *
     * @return whether the user is allowed to operate and the application exists as {@link AmetistaApplication}
     */
    private AmetistaApplication validateUserAndFetchApplication(String userId, String token, String applicationId) {
        Optional<AmetistaApplication> application = applicationsService.getApplication(applicationId);
        if (!isMe(userId, token) || application.isEmpty())
            return null;
        return application.get();
    }

}
