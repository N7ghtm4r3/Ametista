package com.tecknobit.ametistacore.helpers

import com.tecknobit.equinoxcore.annotations.Validator
import com.tecknobit.equinoxcore.helpers.InputsValidator
import kotlin.jvm.JvmStatic

/**
 * The `InputValidator` class is useful to validate the inputs
 *
 * @author N7ghtm4r3 - Tecknobit
 */
object AmetistaValidator : InputsValidator() {

    /**
     * `DEFAULT_VIEWER_PASSWORD` the default password of a viewer just inserted
     */
    const val DEFAULT_VIEWER_PASSWORD: String = "changeme"

    /**
     * `APP_NAME_MAX_LENGTH` the max valid length for the application name
     */
    const val APP_NAME_MAX_LENGTH: Int = 30

    /**
     * `APP_DESCRIPTION_MAX_LENGTH` the max valid length for the application description
     */
    const val APP_DESCRIPTION_MAX_LENGTH: Int = 65535

    /**
     * Method to validate a new password
     *
     * @param password The password value to check the validity
     * @return whether the password is valid or not as `boolean`
     */
    @Validator
    fun isNewPasswordValid(password: String): Boolean {
        return isPasswordValid(password) && password != DEFAULT_VIEWER_PASSWORD
    }

    /**
     * Method to validate an application name
     *
     * @param appName The application name value to check the validity
     * @return whether the application name is valid or not as `boolean`
     */
    @Validator
    @JvmStatic
    fun isAppNameValid(appName: String): Boolean {
        return isInputValid(appName) && appName.length <= APP_NAME_MAX_LENGTH
    }

    /**
     * Method to validate an application description
     *
     * @param appDescription The application description value to check the validity
     * @return whether the application description is valid or not as `boolean`
     */
    @Validator
    @JvmStatic
    fun isAppDescriptionValid(appDescription: String): Boolean {
        return isInputValid(appDescription) && appDescription.length <= APP_DESCRIPTION_MAX_LENGTH
    }

}
