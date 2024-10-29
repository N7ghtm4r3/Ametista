package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametista.services.DefaultAmetistaController;
import com.tecknobit.ametista.services.applications.ApplicationsHelper.ApplicationPayload;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.tecknobit.ametista.services.applications.ApplicationsHelper.DEFAULT_PLATFORMS_FILTER;
import static com.tecknobit.ametistacore.helpers.AmetistaValidator.*;
import static com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.*;
import static com.tecknobit.ametistacore.models.AmetistaApplication.*;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.*;

@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + APPLICATIONS_KEY)
public class ApplicationsController extends DefaultAmetistaController {

    @Autowired
    private ApplicationsHelper applicationsHelper;

    @GetMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    public <T> T getApplications(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(name = PAGE_KEY, defaultValue = DEFAULT_PAGE, required = false) int page,
            @RequestParam(name = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = NAME_KEY, defaultValue = "", required = false) String name,
            @RequestParam(
                    name = PLATFORMS_KEY,
                    defaultValue = DEFAULT_PLATFORMS_FILTER,
                    required = false
            ) List<String> platforms
    ) {
        if (!isMe(userId, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(applicationsHelper.getApplications(page, pageSize, name, platforms));
    }

    @PostMapping(
            headers = {
                    TOKEN_KEY
            }
    )
    public String saveApplication(
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
            applicationsHelper.saveApplication(application);
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    @PostMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
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
            applicationsHelper.editApplication(applicationId, application);
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    @GetMapping(
            path = "{" + APPLICATION_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    public <T> T getApplication(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(APPLICATION_IDENTIFIER_KEY) String applicationId
    ) {
        Optional<AmetistaApplication> application = applicationsHelper.getApplication(applicationId);
        if (!isMe(userId, token) || application.isEmpty())
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(application.get());
    }

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
        applicationsHelper.deleteApplication(applicationId);
        return successResponse();
    }

    private String validateApplicationPayload(ApplicationPayload application) {
        String appName = application.name();
        if (!isAppNameValid(appName))
            return failedResponse(WRONG_APP_NAME_MESSAGE);
        String appDescription = application.description();
        if (!isAppDescriptionValid(appDescription))
            return failedResponse(WRONG_APP_DESCRIPTION_MESSAGE);
        return null;
    }

    private boolean userAllowedAndApplicationExists(String userId, String token, String applicationId) {
        return isAdmin(userId, token) && applicationsHelper.applicationExists(applicationId);
    }

}
