package com.tecknobit.ametista.services;

import com.tecknobit.ametista.services.users.AmetistaUsersHelper;
import com.tecknobit.ametista.services.users.AmetistaUsersRepository;
import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.controllers.EquinoxController;

@Structure
public abstract class DefaultAmetistaController extends EquinoxController<AmetistaUser, AmetistaUsersRepository,
        AmetistaUsersHelper> {

    protected boolean isViewer(String id, String token) {
        return isMe(id, token) && me.isViewer();
    }

    protected boolean isAdmin(String id, String token) {
        return isMe(id, token) && me.isAdmin();
    }

}
