package com.tecknobit.ametistacore.helpers;

import com.tecknobit.equinox.inputs.InputValidator;

import static com.tecknobit.ametistacore.models.AmetistaUser.DEFAULT_VIEWER_PASSWORD;

/**
 * The {@code InputValidator} class is useful to validate the inputs
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class AmetistaValidator extends InputValidator {

    /**
     * {@code INVALID_ADMIN_CODE} error message used when the admin code inserted is not valid
     */
    public static final String INVALID_ADMIN_CODE = "invalid_admin_code_key";

    /**
     * {@code WRONG_APP_NAME_MESSAGE} error message used when the application name inserted is not valid
     */
    public static final String WRONG_APP_NAME_MESSAGE = "wrong_app_name_key";

    /**
     * {@code APP_NAME_MAX_LENGTH} the max valid length for the application name
     */
    public static final int APP_NAME_MAX_LENGTH = 30;

    /**
     * {@code WRONG_APP_DESCRIPTION_MESSAGE} error message used when the application description inserted is not valid
     */
    public static final String WRONG_APP_DESCRIPTION_MESSAGE = "wrong_app_description_key";

    /**
     * {@code APP_DESCRIPTION_MAX_LENGTH} the max valid length for the application description
     */
    public static final int APP_DESCRIPTION_MAX_LENGTH = 65535;

    /**
     * Method to validate a new password
     *
     * @param password The password value to check the validity
     * @return whether the password is valid or not as {@code boolean}
     */
    public static boolean isNewPasswordValid(String password) {
        return isPasswordValid(password) && !password.equals(DEFAULT_VIEWER_PASSWORD);
    }

    /**
     * Method to validate an application name
     *
     * @param appName The application name value to check the validity
     * @return whether the application name is valid or not as {@code boolean}
     */
    public static boolean isAppNameValid(String appName) {
        return isInputValid(appName) && appName.length() <= APP_NAME_MAX_LENGTH;
    }

    /**
     * Method to validate an application description
     *
     * @param appDescription The application description value to check the validity
     * @return whether the application description is valid or not as {@code boolean}
     */
    public static boolean isAppDescriptionValid(String appDescription) {
        return isInputValid(appDescription) && appDescription.length() <= APP_DESCRIPTION_MAX_LENGTH;
    }

}
