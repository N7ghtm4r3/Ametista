package com.tecknobit.ametista.services.applications;

import com.tecknobit.ametista.services.DefaultAmetistaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.BASE_EQUINOX_ENDPOINT;
import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.USERS_KEY;

@RestController
@RequestMapping(BASE_EQUINOX_ENDPOINT + USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + APPLICATIONS_KEY)
public class ApplicationsController extends DefaultAmetistaController {

    @Autowired
    private ApplicationsHelper applicationsHelper;

    @GetMapping
    public <T> T getApplications() {
        return null;
    }

}
