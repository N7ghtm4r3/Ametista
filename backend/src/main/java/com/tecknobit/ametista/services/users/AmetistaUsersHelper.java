package com.tecknobit.ametista.services.users;

import com.tecknobit.ametistacore.models.AmetistaUser;
import com.tecknobit.equinox.annotations.CustomParametersOrder;
import com.tecknobit.equinox.environment.helpers.services.EquinoxUsersHelper;
import org.springframework.context.annotation.Primary;
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

}
