package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.ametistacore.helpers.dtoutils.DTOConvertible;
import com.tecknobit.equinox.environment.records.EquinoxUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.json.JSONObject;

import static com.tecknobit.equinox.environment.records.EquinoxUser.PASSWORD_KEY;

@Entity
@JsonIgnoreProperties({PASSWORD_KEY})
public class AmetistaUser extends EquinoxUser implements DTOConvertible<AmetistaMember> {

    public static final String DEFAULT_VIEWER_PASSWORD = "changeme";

    public static final String ADMIN_CODE_KEY = "admin_code";

    public static final String IS_ADMIN_KEY = "is_admin";

    public static final String ROLE_KEY = "role";

    public static final String SESSION_KEY = "session";

    public static final String MEMBERS_KEY = "members";

    public enum Role {

        VIEWER,

        ADMIN

    }

    @Enumerated(EnumType.STRING)
    @Column(name = ROLE_KEY)
    private final Role role;

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
        role = Role.valueOf(hItem.getString(ROLE_KEY));
    }

    public Role getRole() {
        return role;
    }

    @JsonIgnore
    public boolean isViewer() {
        return role == Role.VIEWER;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public AmetistaMember convertToRelatedDTO() {
        return new AmetistaMember(
                id,
                profilePic,
                name,
                surname,
                email,
                role
        );
    }

}
