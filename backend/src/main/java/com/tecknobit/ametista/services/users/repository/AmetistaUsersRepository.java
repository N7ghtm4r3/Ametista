package com.tecknobit.ametista.services.users.repository;

import com.tecknobit.ametista.services.users.entity.AmetistaUser;
import com.tecknobit.equinoxbackend.environment.services.users.repository.EquinoxUsersRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem.IDENTIFIER_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.USERS_KEY;

/**
 * The {@code AmetistaUsersRepository} interface is useful to manage the queries for the users operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxUsersRepository
 * @see JpaRepository
 */
@Primary
@Repository
public interface AmetistaUsersRepository extends EquinoxUsersRepository<AmetistaUser> {

    /**
     * Method to execute the query to get the current members registered in the system
     *
     * @param userId   The user to exclude from the list
     * @param pageable The parameters to paginate the query
     */
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
