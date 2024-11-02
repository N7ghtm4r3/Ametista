package com.tecknobit.ametista.services.users.repository;

import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.equinox.environment.helpers.services.repositories.EquinoxUsersRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinox.environment.records.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.USERS_KEY;

@Primary
@Repository
public interface AmetistaUsersRepository extends EquinoxUsersRepository<AmetistaUser> {

    @Query(
            value = "SELECT * FROM " + USERS_KEY +
                    " WHERE " + IDENTIFIER_KEY + "!=:" + IDENTIFIER_KEY,
            nativeQuery = true
    )
    List<AmetistaUser> getSessionMembers(
            @Param(IDENTIFIER_KEY) String userId,
            Pageable pageable
    );

}
