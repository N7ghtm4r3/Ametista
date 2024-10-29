package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametista.services.DefaultAmetistaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.*;
import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.TOKEN_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.USERS_KEY;

@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + APPLICATIONS_KEY)
public class ApplicationsController extends DefaultAmetistaController {

    @Autowired
    private ApplicationsHelper applicationsHelper;

    @GetMapping
    public <T> T getApplications(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(value = PAGE_KEY, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(value = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE) int pageSize
    ) {
        if (!isMe(userId, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(applicationsHelper.getApplications(page, pageSize));
    }

}
