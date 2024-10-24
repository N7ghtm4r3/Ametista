package com.tecknobit.ametistacore.models;

import com.tecknobit.equinox.environment.records.EquinoxUser;
import org.json.JSONObject;

import java.util.Random;

public class AmetistaUser extends EquinoxUser {

    public enum Role {

        VIEWER,

        ADMIN

    }

    private final Role role;

    // TODO: 24/10/2024 TO REMOVE
    public AmetistaUser(String email) {
        this(new Random().nextLong() + "", null, "Don", "Joe", email, null, "https://t3.ftcdn.net/jpg/06/48/40/06/360_F_648400633_xGkZpiwO8Dna5j0egnXVXPmdzMYup4K2.jpg",
                null, null, Role.values()[new Random().nextInt(2)]);
    }

    public AmetistaUser() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    public AmetistaUser(String id, String token, String name, String surname, String email, String password, String language,
                        Role role) {
        this(id, token, name, surname, email, password, null, language, null, role);
    }

    public AmetistaUser(String id, String token, String name, String surname, String email, String password,
                        String profilePic, String language, ApplicationTheme theme, Role role) {
        super(id, token, name, surname, email, password, profilePic, language, theme);
        this.role = role;
    }

    public AmetistaUser(JSONObject jUser) {
        super(jUser);
        // TODO: 24/10/2024 TO INIT CORRECTLY
        role = null;
    }

    public Role getRole() {
        return role;
    }

}
