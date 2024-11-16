package com.tecknobit.ametistacore.helpers;

import com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet;

/**
 * The {@code AmetistaEndpointsSet} class is a container with all the Ametista's system base endpoints
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class AmetistaEndpointsSet extends EquinoxBaseEndpointsSet {

    /**
     * {@code CHANGE_PRESET_PASSWORD_ENDPOINT} the endpoint to execute the change of the preset viewer password
     */
    public static final String CHANGE_PRESET_PASSWORD_ENDPOINT = "/changePresetPassword";

    /**
     * Constructor to init the {@link AmetistaEndpointsSet} class <br>
     */
    private AmetistaEndpointsSet() {

    }

}
