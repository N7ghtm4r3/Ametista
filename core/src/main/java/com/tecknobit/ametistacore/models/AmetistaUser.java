package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.ametistacore.helpers.dtoutils.DTOConvertible;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import com.tecknobit.equinox.environment.records.EquinoxUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.json.JSONObject;

import static com.tecknobit.equinox.environment.records.EquinoxUser.PASSWORD_KEY;

/**
 * The {@code AmetistaUser} class is useful to represent a base Ametista's system user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 * @see DTOConvertible
 * 
 */
@Entity
@JsonIgnoreProperties({PASSWORD_KEY})
public class AmetistaUser extends EquinoxUser implements DTOConvertible<AmetistaMember> {

    /**
     * {@code DEFAULT_VIEWER_PASSWORD} the default password of a {@link Role#VIEWER} just inserted
     */
    public static final String DEFAULT_VIEWER_PASSWORD = "changeme";

    /**
     * {@code ADMIN_CODE_KEY} the key for the <b>"admin_code"</b> field
     */
    public static final String ADMIN_CODE_KEY = "admin_code";

    /**
     * {@code IS_ADMIN_KEY} the key for the <b>"is_admin"</b> field
     */
    public static final String IS_ADMIN_KEY = "is_admin";

    /**
     * {@code ROLE_KEY} the key for the <b>"role"</b> field
     */
    public static final String ROLE_KEY = "role";

    /**
     * {@code SESSION_KEY} the key for the <b>"session"</b> field
     */
    public static final String SESSION_KEY = "session";

    /**
     * {@code MEMBERS_KEY} the key for the <b>"members"</b> field
     */
    public static final String MEMBERS_KEY = "members";

    /**
     * List of the available roles
     */
    public enum Role {

        /**
         * {@code VIEWER} this role allows the users to only see the data without the possibility to add/edit/remove them
         */
        VIEWER,

        /**
         * {@code ADMIN} this role allows the users to execute the basics action and add new {@link #VIEWER} in the system
         */
        ADMIN

    }

    /**
     * {@code role} the role of the user
     */
    @Enumerated(EnumType.STRING)
    @Column(name = ROLE_KEY)
    private final Role role;

    /**
     * Constructor to init the {@link AmetistaUser} class
     *
     * @apiNote empty constructor required
     */
    public AmetistaUser() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Constructor to init the {@link EquinoxUser} class
     *
     * @param id The identifier of the user
     * @param token The token which the user is allowed to operate on server
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param language The language of the user
     * @param role The role of the user
     */
    public AmetistaUser(String id, String token, String name, String surname, String email, String password, String language,
                        Role role) {
        this(id, token, name, surname, email, password, null, language, null, role);
    }

    /**
     * Constructor to init the {@link EquinoxUser} class
     *
     * @param id The identifier of the user
     * @param token The token which the user is allowed to operate on server
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param profilePic The profile picture of the user
     * @param language The language of the user
     * @param theme The theme selected by the user
     * @param role The role of the user
     */
    public AmetistaUser(String id, String token, String name, String surname, String email, String password,
                        String profilePic, String language, ApplicationTheme theme, Role role) {
        super(id, token, name, surname, email, password, profilePic, language, theme);
        this.role = role;
    }

    /**
     * Constructor to init the {@link AmetistaUser} class
     *
     * @param jUser User details formatted as JSON
     */
    public AmetistaUser(JSONObject jUser) {
        super(jUser);
        role = Role.valueOf(hItem.getString(ROLE_KEY));
    }

    /**
     * Method to get {@link #role} instance
     *
     * @return {@link #role} instance as {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Method to get whether the user is a {@link Role#VIEWER}
     *
     * @return whether the user is a {@link Role#VIEWER} as {@code boolean}
     */
    @JsonIgnore
    public boolean isViewer() {
        return role == Role.VIEWER;
    }

    /**
     * Method to get whether the user is a {@link Role#ADMIN}
     *
     * @return whether the user is a {@link Role#ADMIN} as {@code boolean}
     */
    @JsonIgnore
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    /**
     * {@inheritDoc}
     */
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
