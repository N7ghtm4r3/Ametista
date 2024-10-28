package com.tecknobit.ametista.services;

import com.tecknobit.ametista.services.users.AmetistaUsersHelper;
import com.tecknobit.ametista.services.users.AmetistaUsersRepository;
import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.controllers.EquinoxController;

@Structure
public abstract class DefaultAmetistaController extends EquinoxController<AmetistaUser, AmetistaUsersRepository,
        AmetistaUsersHelper> {
}
