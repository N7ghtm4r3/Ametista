package com.tecknobit.ametista.services.users;

import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.apimanager.apis.ServerProtector;
import com.tecknobit.equinox.annotations.CustomParametersOrder;
import com.tecknobit.equinox.environment.controllers.EquinoxUsersController;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.tecknobit.ametistacore.helpers.AmetistaValidator.INVALID_ADMIN_CODE;
import static com.tecknobit.ametistacore.models.AmetistaUser.*;
import static com.tecknobit.ametistacore.models.AmetistaUser.Role.ADMIN;
import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.POST;
import static com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY;
import static com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.SIGN_UP_ENDPOINT;
import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.DEFAULT_PROFILE_PIC;
import static com.tecknobit.equinox.inputs.InputValidator.DEFAULT_LANGUAGE;

@RestController
public class AmetistaUsersController extends EquinoxUsersController<AmetistaUser, AmetistaUsersRepository, AmetistaUsersHelper> {

    public static final ServerProtector adminCodeProvider = new ServerProtector(
            "tecknobit/nova/admin",
            ""
    );

    /**
     * Method to sign up in the <b>Equinox's system</b>
     *
     * @param payload: payload of the request
     *                 <pre>
     *                                      {@code
     *                                              {
     *                                                  "name" : "the name of the user" -> [String],
     *                                                  "surname": "the surname of the user" -> [String],
     *                                                  "email": "the email of the user" -> [String],
     *                                                  "password": "the password of the user" -> [String]
     *                                              }
     *                                      }
     *                                 </pre>
     * @return the result of the request as {@link String}
     */
    @Override
    @PostMapping(path = SIGN_UP_ENDPOINT)
    @RequestPath(path = "/api/v1/users/signUp", method = POST)
    public String signUp(@RequestBody Map<String, String> payload) {
        loadJsonHelper(payload);
        mantis.changeCurrentLocale(jsonHelper.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE));
        String name = jsonHelper.getString(NAME_KEY);
        String surname = jsonHelper.getString(SURNAME_KEY);
        String email = jsonHelper.getString(EMAIL_KEY);
        String password = jsonHelper.getString(PASSWORD_KEY);
        String language = jsonHelper.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE);
        mantis.changeCurrentLocale(language);
        Object[] custom = getSignUpCustomParams();
        String signUpValidation = validateSignUp(name, surname, email, password, language, custom);
        if (signUpValidation != null)
            return failedResponse(signUpValidation);
        try {
            JSONObject response = new JSONObject();
            String id = generateIdentifier();
            String token = generateIdentifier();
            usersHelper.signUpUser(
                    id,
                    token,
                    name,
                    surname,
                    email.toLowerCase(),
                    password,
                    language,
                    custom
            );
            mantis.changeCurrentLocale(DEFAULT_LANGUAGE);
            return successResponse(response
                    .put(IDENTIFIER_KEY, id)
                    .put(TOKEN_KEY, token)
                    .put(PROFILE_PIC_KEY, DEFAULT_PROFILE_PIC)
            );
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
    }

    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, ROLE_KEY})
    protected Object[] getSignUpCustomParams() {
        return new Object[]{
                jsonHelper.getString(ADMIN_CODE_KEY),
                ADMIN
        };
    }

    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, ROLE_KEY})
    protected String validateSignUp(String name, String surname, String email, String password, String language, Object... custom) {
        String validation = super.validateSignUp(name, surname, email, password, language, custom);
        if (!adminCodeProvider.serverSecretMatches((String) custom[0]))
            return INVALID_ADMIN_CODE;
        return validation;
    }

    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, /* OR*/ SERVER_SECRET_KEY})
    protected Object[] getSignInCustomParams() {
        String adminCode = jsonHelper.getString(ADMIN_CODE_KEY);
        String serverSecret = jsonHelper.getString(SERVER_SECRET_KEY);
        boolean isAdmin = adminCode != null;
        return new Object[]{
                isAdmin,
                adminCode,
                serverSecret
        };
    }

    @Override
    @CustomParametersOrder(order = {IS_ADMIN_KEY, ADMIN_CODE_KEY, SERVER_SECRET_KEY})
    protected String validateSignIn(String email, String password, String language, Object... custom) {
        String validation = super.validateSignIn(email, password, language, custom);
        if ((boolean) custom[0]) {
            if (!adminCodeProvider.serverSecretMatches((String) custom[1]))
                return INVALID_ADMIN_CODE;
        } else {
            if (!serverProtector.serverSecretMatches((String) custom[2]))
                return NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE;
        }
        return validation;
    }


}