package com.tecknobit.ametista.services.users.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametista.services.users.entity.AmetistaUser.Role;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.equinoxcore.annotations.DTO;
import org.json.JSONObject;

import static com.tecknobit.ametista.services.users.entity.AmetistaUser.ROLE_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.*;

/**
 * The {@code AmetistaUser} class is useful to represent a base Ametista's system user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 */
@DTO
public class AmetistaMember extends EquinoxItem {

    /**
     * {@code MEMBER_IDENTIFIER_KEY} the key for the <b>"member_id"</b> field
     */
    public static final String MEMBER_IDENTIFIER_KEY = "member_id";

    /**
     * {@code profilePic} the profile picture of the user
     */
    private final String profilePic;

    /**
     * {@code name} the name of the user
     */
    private final String name;

    /**
     * {@code surname} the surname of the user
     */
    private final String surname;

    /**
     * {@code email} the email of the user
     */
    private final String email;

    /**
     * {@code role} the role of the user
     */
    private final Role role;

    /**
     * Constructor to init the {@link AmetistaMember} class
     *
     * @param id         The identifier of the user
     * @param name       The name of the user
     * @param surname    The surname of the user
     * @param email      The email of the user
     * @param profilePic The profile picture of the user
     * @param role       The role of the user
     */
    public AmetistaMember(String id, String profilePic, String name, String surname, String email, Role role) {
        super(id);
        this.profilePic = profilePic;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }

    /**
     * Constructor to init the {@link AmetistaMember} class
     *
     * @param jMember: user details formatted as JSON
     */
    public AmetistaMember(JSONObject jMember) {
        super(jMember);
        profilePic = hItem.getString(PROFILE_PIC_KEY);
        name = hItem.getString(NAME_KEY);
        surname = hItem.getString(SURNAME_KEY);
        email = hItem.getString(EMAIL_KEY);
        role = Role.valueOf(hItem.getString(ROLE_KEY));
    }

    /**
     * Method to get {@link #profilePic} instance <br>
     * No-any params required
     *
     * @return {@link #profilePic} instance as {@link String}
     */
    @JsonGetter(PROFILE_PIC_KEY)
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Method to get {@link #name} instance <br>
     * No-any params required
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get {@link #surname} instance <br>
     * No-any params required
     *
     * @return {@link #surname} instance as {@link String}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Method to get the complete name of the user <br>
     * No-any params required
     *
     * @return the complete name of the user as {@link String}
     */
    @JsonIgnore
    public String getCompleteName() {
        return name + " " + surname;
    }

    /**
     * Method to get {@link #email} instance <br>
     * No-any params required
     *
     * @return {@link #email} instance as {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get {@link #role} instance
     *
     * @return {@link #role} instance as {@link Role}
     */
    public Role getRole() {
        return role;
    }

}
