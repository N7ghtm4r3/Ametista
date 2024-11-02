package com.tecknobit.ametista.services.users.service;

import com.tecknobit.ametista.services.users.repository.AmetistaUsersRepository;
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse;
import com.tecknobit.ametistacore.models.AmetistaMember;
import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.equinox.annotations.CustomParametersOrder;
import com.tecknobit.equinox.environment.helpers.services.EquinoxUsersHelper;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.tecknobit.ametistacore.models.AmetistaUser.ADMIN_CODE_KEY;
import static com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY;

@Primary
@Service
public class AmetistaUsersHelper extends EquinoxUsersHelper<AmetistaUser, AmetistaUsersRepository> {

    @Override
    @CustomParametersOrder(order = {ADMIN_CODE_KEY, ROLE_KEY})
    public void signUpUser(String id, String token, String name, String surname, String email, String password, String language, Object... custom) throws NoSuchAlgorithmException {
        super.signUpUser(id, token, name, surname, email, password, language, custom[1]);
    }

    @Override
    @CustomParametersOrder(order = ROLE_KEY)
    protected List<String> getQueryValuesKeys() {
        ArrayList<String> keys = new ArrayList<>(DEFAULT_USER_VALUES_KEYS);
        keys.add(ROLE_KEY);
        return keys;
    }

    public PaginatedResponse<AmetistaMember> getSessionMembers(int page, int pageSize, String userId) {
        ArrayList<AmetistaMember> members = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize);
        List<AmetistaUser> users = usersRepository.getSessionMembers(userId, pageable);
        for (AmetistaUser user : users)
            members.add(user.convertToRelatedDTO());
        long totalUsers = usersRepository.count() - 1;
        return new PaginatedResponse<>(members, page, pageSize, totalUsers);
    }

}
