package com.tecknobit.ametista.services.users.controller;

import com.tecknobit.ametista.services.users.entity.AmetistaUser;
import com.tecknobit.ametista.services.users.repository.AmetistaUsersRepository;
import com.tecknobit.ametista.services.users.service.AmetistaUsersService;
import com.tecknobit.ametistacore.enums.Role;
import com.tecknobit.apimanager.annotations.RequestPath;
import com.tecknobit.apimanager.apis.ServerProtector;
import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.equinoxbackend.environment.services.users.controller.EquinoxUsersController;
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.ametistacore.enums.Role.ADMIN;
import static com.tecknobit.ametistacore.helpers.AmetistaEndpointsSet.CHANGE_PRESET_PASSWORD_ENDPOINT;
import static com.tecknobit.apimanager.apis.APIRequest.RequestMethod.*;
import static com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;
import static com.tecknobit.equinoxcore.helpers.InputsValidator.Companion;
import static com.tecknobit.equinoxcore.helpers.InputsValidator.DEFAULT_LANGUAGE;
import static com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.SIGN_UP_ENDPOINT;
import static com.tecknobit.equinoxcore.pagination.PaginatedResponse.*;

/**
 * The {@code AmetistaUsersController} class is useful to manage all the user operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxController
 */
@RestController
public class AmetistaUsersController extends EquinoxUsersController<AmetistaUser, AmetistaUsersRepository, AmetistaUsersService> {

    /**
     * {@code adminCodeProvider} helper to provide the admin code and manage the admin accesses
     */
    public static final ServerProtector adminCodeProvider = new ServerProtector(
            "tecknobit/ametista/admin",
            ""
    );

    /**
     * {@code INVALID_ADMIN_CODE_MESSAGE} error message used when the admin code inserted is not valid
     */
    private static final String INVALID_ADMIN_CODE_MESSAGE = "invalid_admin_code";

    /**
     * Method to sign up in the <b>Equinox's system</b>
     *
     * @param payload The payload of the request
     *                 <pre>
     *                                      {@code
     *                                              {
     *                                                  "name" : "the name of the user" -> [String],
     *                                                  "surname": "the surname of the user" -> [String],
     *                                                  "email": "the email of the user" -> [String],
     *                                                  "password": "the password of the user" -> [String]
     *                                                  "language": "the language of the user" -> [String]
     *                                              }
     *                                      }
     *                                 </pre>
     * @return the result of the request as {@link String}
     */
    @Override
    @PostMapping(path = SIGN_UP_ENDPOINT)
    @RequestPath(path = "/api/v1/users/signUp", method = POST)
    public String signUp(
            @RequestBody Map<String, Object> payload
    ) {
        loadJsonHelper(payload);
        setSessionLocale(jsonHelper.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE));
        String name = jsonHelper.getString(NAME_KEY);
        String surname = jsonHelper.getString(SURNAME_KEY);
        String email = jsonHelper.getString(EMAIL_KEY);
        String password = jsonHelper.getString(PASSWORD_KEY);
        String language = jsonHelper.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE);
        setSessionLocale(language);
        Object[] custom = getSignUpCustomParams();
        String signUpValidation = validateSignUp(name, surname, email, password, language, custom);
        if (signUpValidation != null)
            return failedResponse(signUpValidation);
        try {
            JSONObject response = new JSONObject();
            String id = generateIdentifier();
            String token = generateIdentifier();
            usersService.signUpUser(
                    id,
                    token,
                    name,
                    surname,
                    email.toLowerCase(),
                    password,
                    language,
                    custom
            );
            setSessionLocale(DEFAULT_LANGUAGE);
            return successResponse(response
                    .put(IDENTIFIER_KEY, id)
                    .put(TOKEN_KEY, token)
                    .put(PROFILE_PIC_KEY, DEFAULT_PROFILE_PIC)
            );
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, ROLE_KEY})
    protected Object[] getSignUpCustomParams() {
        return new Object[]{
                jsonHelper.getString(ADMIN_CODE_KEY),
                ADMIN
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, ROLE_KEY})
    protected String validateSignUp(String name, String surname, String email, String password, String language, Object... custom) {
        String validation = super.validateSignUp(name, surname, email, password, language, custom);
        if (!adminCodeProvider.serverSecretMatches((String) custom[0]))
            return INVALID_ADMIN_CODE_MESSAGE;
        return validation;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomParametersOrder(order = {IS_ADMIN_KEY, ADMIN_CODE_KEY, SERVER_SECRET_KEY})
    protected String validateSignIn(String email, String password, String language, Object... custom) {
        String validation = super.validateSignIn(email, password, language, custom);
        if ((boolean) custom[0]) {
            if (!adminCodeProvider.serverSecretMatches((String) custom[1]))
                return INVALID_ADMIN_CODE_MESSAGE;
        } else {
            if (!serverProtector.serverSecretMatches((String) custom[2]))
                return NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE;
        }
        return validation;
    }

    /**
     * Method to change the preset password of a {@link Role#VIEWER}
     *
     * @param userId  The identifier of the user
     * @param token   The token of the user
     * @param payload The payload of the request
     *                <pre>
     *                                                     {@code
     *                                                             {
     *                                                                 "password": "the password of the viewer" -> [String]
     *                                                             }
     *                                                     }
     *                                                </pre>
     * @return the result of the request as {@link String}
     */
    @PatchMapping(
            path = USERS_KEY + "/{" + IDENTIFIER_KEY + "}" + CHANGE_PRESET_PASSWORD_ENDPOINT,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{userId}/changePresetPassword", method = PATCH)
    public String changePresetPassword(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody Map<String, String> payload
    ) {
        if (!isMe(userId, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        if (!me.isViewer())
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        loadJsonHelper(payload);
        String password = jsonHelper.getString(PASSWORD_KEY);
        if (!Companion.isPasswordValid(password))
            return failedResponse(WRONG_PASSWORD_MESSAGE);
        try {
            usersService.changePassword(password, userId);
            return successResponse();
        } catch (Exception e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
    }

    /**
     * Method to get the current members registered in the system
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param page The page requested
     * @param pageSize The size of the items to insert in the page
     *
     * @return the result of the request as {@link T}
     */
    @GetMapping(
            path = USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + SESSION_KEY + "/" + MEMBERS_KEY,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/session/members", method = GET)
    public <T> T getSessionMembers(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestParam(value = PAGE_KEY, defaultValue = DEFAULT_PAGE_HEADER_VALUE, required = false) int page,
            @RequestParam(value = PAGE_SIZE_KEY, defaultValue = DEFAULT_PAGE_SIZE_HEADER_VALUE, required = false) int pageSize
    ) {
        if (!isMe(userId, token))
            return (T) failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        return (T) successResponse(usersService.getSessionMembers(page, pageSize, userId));
    }

    /**
     * Method to add a new {@link Role#VIEWER} in the system
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param payload The payload of the request
     *                 <pre>
     *                                      {@code
     *                                              {
     *                                                  "name" : "the name of the viewer" -> [String],
     *                                                  "surname": "the surname of the viewer" -> [String],
     *                                                  "email": "the email of the viewer" -> [String]
     *                                              }
     *                                      }
     *                                 </pre>
     * @return the result of the request as {@link String}
     */
    @PostMapping(
            path = USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + SESSION_KEY + "/" + MEMBERS_KEY,
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/session/members", method = POST)
    public String addViewer(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @RequestBody Map<String, String> payload
    ) {
        if (!isAdmin(userId, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        loadJsonHelper(payload);
        String name = jsonHelper.getString(NAME_KEY);
        if (!Companion.isNameValid(name))
            return failedResponse(WRONG_NAME_MESSAGE);
        String surname = jsonHelper.getString(SURNAME_KEY);
        if (!Companion.isSurnameValid(surname))
            return failedResponse(WRONG_SURNAME_MESSAGE);
        String email = jsonHelper.getString(EMAIL_KEY);
        if (!Companion.isEmailValid(email))
            return failedResponse(WRONG_EMAIL_MESSAGE);
        try {
            usersService.addViewer(name, surname, email);
        } catch (NoSuchAlgorithmException e) {
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        }
        return successResponse();
    }

    /**
     * Method to remove a member from the system
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     * @param memberId The identifier of the member to remove
     *
     * @return the result of the request as {@link String}
     */
    @DeleteMapping(
            path = USERS_KEY + "/{" + IDENTIFIER_KEY + "}/" + SESSION_KEY + "/" + MEMBERS_KEY
                    + "/{" + MEMBER_IDENTIFIER_KEY + "}",
            headers = {
                    TOKEN_KEY
            }
    )
    @RequestPath(path = "/api/v1/users/{user_id}/session/members/{member_id}", method = DELETE)
    public String removeMember(
            @PathVariable(IDENTIFIER_KEY) String userId,
            @RequestHeader(TOKEN_KEY) String token,
            @PathVariable(MEMBER_IDENTIFIER_KEY) String memberId
    ) {
        if (!isAdmin(userId, token))
            return failedResponse(NOT_AUTHORIZED_OR_WRONG_DETAILS_MESSAGE);
        if (!usersService.userExists(memberId))
            return failedResponse(WRONG_PROCEDURE_MESSAGE);
        usersService.deleteUser(memberId);
        return successResponse();
    }

    /**
     * Method to get whether the user who request to execute an action is a {@link Role#ADMIN}
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     *
     * @return whether the user is a {@link Role#ADMIN} {@code boolean}
     */
    private boolean isAdmin(String userId, String token) {
        return isMe(userId, token) && me.isAdmin();
    }

}
