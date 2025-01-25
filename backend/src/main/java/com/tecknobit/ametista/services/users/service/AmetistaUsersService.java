package com.tecknobit.ametista.services.users.service;

import com.tecknobit.ametista.services.users.dtos.AmetistaMember;
import com.tecknobit.ametista.services.users.entity.AmetistaUser;
import com.tecknobit.ametista.services.users.repository.AmetistaUsersRepository;
import com.tecknobit.equinoxbackend.environment.services.builtin.service.EquinoxItemsHelper;
import com.tecknobit.equinoxbackend.environment.services.users.entity.EquinoxUser;
import com.tecknobit.equinoxbackend.environment.services.users.service.EquinoxUsersHelper;
import com.tecknobit.equinoxbackend.resourcesutils.ResourcesManager;
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder;
import com.tecknobit.equinoxcore.pagination.PaginatedResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.ametista.services.users.entity.AmetistaUser.ADMIN_CODE_KEY;
import static com.tecknobit.ametista.services.users.entity.AmetistaUser.ROLE_KEY;
import static com.tecknobit.ametista.services.users.entity.AmetistaUser.Role.VIEWER;
import static com.tecknobit.ametistacore.helpers.AmetistaValidator.DEFAULT_VIEWER_PASSWORD;
import static com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController.generateIdentifier;
import static com.tecknobit.equinoxcore.helpers.InputsValidator.DEFAULT_LANGUAGE;

/**
 * The {@code EquinoxUsersHelper} class is useful to manage all the user database operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxUsersHelper
 * @see EquinoxItemsHelper
 * @see ResourcesManager
 */
@Primary
@Service
public class AmetistaUsersService extends EquinoxUsersHelper<AmetistaUser, AmetistaUsersRepository> {

    /**
     * Method to sign up a new user in the system
     *
     * @param id       The identifier of the user
     * @param token    The token of the user
     * @param name     The name of the user
     * @param surname  The surname of the user
     * @param email    The email of the user
     * @param password The password of the user
     * @param language The language of the user
     * @param custom   The custom parameters to add in the default query
     * @apiNote the order of the custom parameters must be the same of that specified in the {@link #getQueryValuesKeys()}
     */
    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, ROLE_KEY})
    public void signUpUser(String id, String token, String name, String surname, String email, String password,
                           String language, Object... custom) throws NoSuchAlgorithmException {
        super.signUpUser(id, token, name, surname, email, password, language, custom[1]);
    }

    /**
     * Method to get the list of keys to use in the {@link #BASE_SIGN_UP_QUERY} <br>
     * No-any params required
     *
     * @return a list of keys as {@link List} of {@link String}
     * @apiNote This method allows a customizable sign-up with custom parameters added in a customization of the {@link EquinoxUser}
     */
    @Override
    @CustomParametersOrder(order = ROLE_KEY)
    protected List<String> getQueryValuesKeys() {
        ArrayList<String> keys = new ArrayList<>(DEFAULT_USER_VALUES_KEYS);
        keys.add(ROLE_KEY);
        return keys;
    }

    /**
     * Method to get whether the user is present in the database
     *
     * @param userId The identifier of the user to check
     *
     * @return whether the user exists as {@code boolean}
     */
    public boolean userExists(String userId) {
        return usersRepository.findById(userId).isPresent();
    }

    /**
     * Method to get the current session members
     *
     * @param page The page requested
     * @param pageSize The size of the items to insert in the page
     * @param userId The identifier of the user to exclude from the list
     *
     * @return the paginated response as {@link PaginatedResponse} of {@link AmetistaMember}
     */
    public PaginatedResponse<AmetistaMember> getSessionMembers(int page, int pageSize, String userId) {
        ArrayList<AmetistaMember> members = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AmetistaUser> users = usersRepository.getSessionMembers(userId, pageable);
        for (AmetistaUser user : users)
            members.add(user.convertToRelatedDTO());
        long totalUsers = usersRepository.count() - 1;
        return new PaginatedResponse<>(members, page, pageSize, totalUsers);
    }

    /**
     * Method to add a new viewer in the system
     *
     * @param name The name of the viewer
     * @param surname The surname of the viewer
     * @param email The email of the viewer
     */
    public void addViewer(String name, String surname, String email) throws NoSuchAlgorithmException {
        String userId = generateIdentifier();
        String token = generateIdentifier();
        signUpUser(userId, token, name, surname, email, DEFAULT_VIEWER_PASSWORD, DEFAULT_LANGUAGE, null, VIEWER);
    }

}
