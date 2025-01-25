package com.tecknobit.ametistacore.helpers

import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet

/**
 * The `AmetistaEndpointsSet` class is a container with all the Ametista's system base endpoints
 *
 * @author N7ghtm4r3 - Tecknobit
 */
object AmetistaEndpointsSet : EquinoxBaseEndpointsSet() {

    /**
     * `CHANGE_PRESET_PASSWORD_ENDPOINT` the endpoint to execute the change of the preset viewer password
     */
    const val CHANGE_PRESET_PASSWORD_ENDPOINT: String = "/changePresetPassword"

}
