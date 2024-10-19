package com.tecknobit.ametistacore.helpers;

import com.tecknobit.equinox.inputs.InputValidator;

public class AmetistaValidator extends InputValidator {

    // TODO: 15/10/2024 TO TRANSLATE
    public static final String WRONG_APP_NAME_MESSAGE = "wrong_app_name_key";

    public static final int APP_NAME_MAX_LENGTH = 30;

    // TODO: 15/10/2024 TO TRANSLATE
    public static final String WRONG_APP_DESCRIPTION_MESSAGE = "wrong_app_description_key";

    public static final int APP_DESCRIPTION_MAX_LENGTH = 65535;

    public static boolean isAppNameValid(String appName) {
        return isInputValid(appName) && appName.length() <= APP_NAME_MAX_LENGTH;
    }

    public static boolean isAppDescriptionValid(String appDescription) {
        return isInputValid(appDescription) && appDescription.length() <= APP_DESCRIPTION_MAX_LENGTH;
    }

}