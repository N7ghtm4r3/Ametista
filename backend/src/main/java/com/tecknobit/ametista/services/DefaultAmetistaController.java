package com.tecknobit.ametista.services;

import com.tecknobit.ametista.services.users.repository.AmetistaUsersRepository;
import com.tecknobit.ametista.services.users.service.AmetistaUsersHelper;
import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.ametistacore.models.AmetistaUser.Role;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.controllers.EquinoxController;

/**
 * The {@code DefaultAmetistaController} class is useful to give the base behavior of the <b>Ametista's controllers</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Structure
public abstract class DefaultAmetistaController extends EquinoxController<AmetistaUser, AmetistaUsersRepository,
        AmetistaUsersHelper> {

    /**
     * Method to get whether the user who request to execute an action is a {@link Role#VIEWER}
     *
     * @param userId The identifier of the user
     * @param token  The token of the user
     * @return whether the user is a {@link Role#VIEWER} {@code boolean}
     */
    protected boolean isViewer(String userId, String token) {
        return isMe(userId, token) && me.isViewer();
    }

    /**
     * Method to get whether the user who request to execute an action is a {@link Role#ADMIN}
     *
     * @param userId The identifier of the user
     * @param token The token of the user
     *
     * @return whether the user is a {@link Role#ADMIN} {@code boolean}
     */
    protected boolean isAdmin(String userId, String token) {
        return isMe(userId, token) && me.isAdmin();
    }

}
