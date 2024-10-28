package com.tecknobit.ametista.services.users;

import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.equinox.environment.helpers.services.repositories.EquinoxUsersRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Primary
@Repository
public interface AmetistaUsersRepository extends EquinoxUsersRepository<AmetistaUser> {


}
